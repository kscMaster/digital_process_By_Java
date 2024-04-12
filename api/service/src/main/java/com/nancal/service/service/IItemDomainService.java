package com.nancal.service.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.model.dataset.FileAttrResp;
import com.nancal.api.utils.CoderSetUtil;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.PageHelper;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.*;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.exception.TipServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.common.utils.MessageSourceUtil;
import com.nancal.common.utils.VersionUtil;
import com.nancal.model.entity.*;
import com.nancal.service.bo.*;
import com.nancal.service.factory.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

public interface IItemDomainService extends IWorkspaceObjectDomainService {


    /**
     *  ebom中的创建零组件或者辅材等都是游离状态，因此不需要创建他的左侧关系
     *  1:
     * @param req
     * @author:  郑复明
     * @date:  10:48  2022/4/26
     * @return:  {@link WorkspaceObjectResp}
     */
    @Transactional
    @Override
    default WorkspaceObjectResp save(BusinessObjectReq req) {
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        ItemRevisionReq myReq = (ItemRevisionReq) req;
        String objectType = myReq.getObjectType();
        if (StrUtil.isBlank(myReq.getObjectType())){
             objectType = EntityUtil.getObjectType();
        }
        // 获取entity实体,并实例化
        ItemEntity item = ReflectUtil.newInstance(EntityUtil.getEntityPackage(objectType));
        // 判断itemId属性是否有值，如果没有值，则自动生成一个
        BeanUtil.copyPropertiesIgnoreNull(myReq, item);
        String itemId = SpringUtil.getBean(CoderSetUtil.class).getOneCoderByObjectType(objectType);
//        if (StrUtil.isBlank(itemId)){
//            itemId = SpringUtil.getBean(CoderSetUtil.class).getOneCoderByObjectType(objectType);
//        }

        item.setItemId(itemId);
        item.setObjectType(objectType);
        item.setUid(IdGeneratorUtil.generate());
        item.setOwnerId(userUtils.getCurrentUserId());
        item.setOwnerName(userUtils.getCurrentUserName());
        item.setLifeCycleState(LifeCycleStateEnum.Working.name());
        entityManager.persist(item);
        //创建辅材版本
        String revisionObjectType = EntityUtil.getRevision(objectType);
        ItemRevisionEntity itemRevision = ReflectUtil.newInstance(EntityUtil.getEntityPackage(revisionObjectType));
        BeanUtil.copyPropertiesIgnoreNull(myReq, itemRevision);
        itemRevision.setUid(IdGeneratorUtil.generate());
        itemRevision.setItemId(itemId);
        itemRevision.setObjectType(revisionObjectType);
        itemRevision.setOwnerId(userUtils.getCurrentUserId());
        itemRevision.setOwnerName(userUtils.getCurrentUserName());
        itemRevision.setRevisionId(VersionUtil.createRevisionId(""));
        itemRevision.setSequence(VersionUtil.createSequence(""));
        itemRevision.setLifeCycleState(LifeCycleStateEnum.Working.name());
        itemRevision.setActive(true);
        entityManager.persist(itemRevision);
        // 保存对象索引数据
        SpringUtil.getBean(ObjectIndexFactory.class).create(itemRevision).saveOrUpdate();
        IncludeRL includeRL = SpringUtil.getBean(IncludeRLFactory.class).create();
        // 创建Master关系
        includeRL.saveRelation(item.getUid(),objectType,itemRevision.getUid());
        if (StrUtil.isNotBlank(myReq.getLeftObject())) {
            // 创建关系
            includeRL.saveRelation(myReq.getLeftObject(),myReq.getLeftObjectType(),item.getUid(),objectType);
        }
        //创建响应
        ItemResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(objectType));
        BeanUtil.copyPropertiesIgnoreNull(item,resp);
        resp.setLeftObject(myReq.getLeftObject());
        resp.setLeftObjectType(myReq.getLeftObjectType());
        resp.setRightObject(itemRevision.getUid());
        resp.setRightObjectType(revisionObjectType);
        resp.setRightObjectRevId(itemRevision.getRevisionId());
        return resp;
    }

    @Transactional
    @Override
    default BusinessObjectResp update(BusinessObjectReq req) {
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        ItemReq myReq = (ItemReq) req;
        Object object = entityManager.find(EntityUtil.getEntityClass(EntityUtil.getObjectType()), req.getUid());
        if (ObjectUtil.isEmpty(object)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        ItemEntity itemEntity = (ItemEntity) object;
        // 权限校验
        this.verifyAuthority(itemEntity, OperatorEnum.Write);
        // 检查是否发生过修改
        ItemResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(EntityUtil.getObjectType()));
        if (!myReq.equals(itemEntity)) {
            // 修改零组件主体

            BeanUtil.copyPropertiesIgnoreNull(myReq,itemEntity);
//            itemEntity.setObjectName(myReq.getObjectName());
//            itemEntity.setObjectDesc(myReq.getObjectDesc());
//            itemEntity.setRemark(myReq.getRemark());
//            itemEntity.setSecretLevel(myReq.getSecretLevel());
//            itemEntity.setItemId(myReq.getItemId());
            entityManager.merge(itemEntity);
        }
        // 查找最新的版本
        WorkspaceObjectResp latestRevision = getLatestRevision(new IdRequest(req.getUid()));
        if (Objects.nonNull(latestRevision)) {
            String revisionObjectType = EntityUtil.getRevision(EntityUtil.getObjectType());
            ItemRevisionReq revisionReq = ReflectUtil.newInstance(EntityUtil.getReqPackage(revisionObjectType));
            BeanUtil.copyPropertiesIgnoreNull(latestRevision,revisionReq);
            BeanUtil.copyPropertiesIgnoreNull(myReq,revisionReq);
            revisionReq.setUid(latestRevision.getUid());
            revisionReq.setObjectType(revisionObjectType);
            revisionReq.setLeftObject(req.getUid());
            revisionReq.setLeftObjectType(EntityUtil.getObjectType());
            // 修改最新版本
            String beanName = EntityUtil.getBeanServiceImpl(revisionObjectType);
            SpringUtil.getBean(beanName, IItemRevisionDomainService.class).update(revisionReq);
        }
        BeanUtil.copyPropertiesIgnoreNull(itemEntity,resp);
        resp.setLeftObject(myReq.getLeftObject());
        resp.setLeftObjectType(myReq.getLeftObjectType());
        return resp;
    }

    @Override
    default BusinessObjectResp getObject(IdRequest id) {
        // 获取当前组件数据
        WorkspaceObjectResp objectResp = (WorkspaceObjectResp) IWorkspaceObjectDomainService.super.getObject(id);
        // 获取最新版本数据
        WorkspaceObjectResp latestRevision = getLatestRevision(id);
        if (Objects.isNull(latestRevision)) {
            return objectResp;
        }
        ItemRevisionResp revisionResp = (ItemRevisionResp) latestRevision;
        String itemId = revisionResp.getItemId();
        String rightObject = revisionResp.getUid();
        String rightObjectType = revisionResp.getObjectType();
        // 将最新版本数据的名称描述等修改并返回
        BeanUtil.copyPropertiesIgnoreNull(objectResp,revisionResp);
        revisionResp.setRightObject(rightObject);
        revisionResp.setRightObjectType(rightObjectType);
        revisionResp.setItemId(itemId);
        Item item = SpringUtil.getBean(ItemFactory.class).create();
        // 设置是否有修改图代号的权限
        boolean permission = item.editItemIdPermission(id.getUid(), StrUtil.blankToDefault(id.getObjectType(),EntityUtil.getObjectType()));
        revisionResp.setHasDrawingCode(permission);
        SpringUtil.getBean(DictUtil.class).translate(revisionResp);
        return revisionResp;
    }

//    /***
//     * 获取文件列表
//     *
//     * @param id 请求参数
//     * @author 徐鹏军
//     * @date 2022/4/12 15:26
//     * @return {@link List <? extends BusinessObjectResp >}
//     */
//    default List<? extends BusinessObjectResp> fileList(IdRequest id) {
//        ItemFactory itemFactory = SpringUtil.getBean(ItemFactory.class);
//        // 判断当前class是否可以进行查询文件的
//        List<FileStorage> entities = itemFactory.create().getFileStorage(id.getUid(), EntityUtil.getObjectType());
//        if (CollUtil.isEmpty(entities)) {
//            return Collections.emptyList();
//        }
//        FileStorage fileStorage = CollUtil.getFirst(entities);
//        MasterRL masterRL = SpringUtil.getBean(MasterRLFactory.class).create();
//        String objectType = new DatasetFileRLEntity().getObjectType();
//        // 通过右id找左对象
//        List<WorkspaceObjectEntity> parents = masterRL.getParents(fileStorage.getUid(), objectType, null);
//        if (CollUtil.isEmpty(parents)) {
//            return Collections.emptyList();
//        }
//        WorkspaceObjectEntity leftEntity = CollUtil.getFirst(parents);
//        FileStorageResp resp = new FileStorageResp();
//        BeanUtil.copyPropertiesIgnoreNull(fileStorage, resp);
//        resp.setObjectName(leftEntity.getObjectName());
//        resp.setObjectDesc(leftEntity.getObjectDesc());
//        return Collections.singletonList(resp);
//    }

    /***
     * 获取最新零组件版本
     *
     * @param id 主键id
     * @author 徐鹏军
     * @date 2022/4/18 10:58
     * @return {@link WorkspaceObjectResp}
     */
    default WorkspaceObjectResp getLatestRevision(IdRequest id) {
        WorkspaceObjectEntity entity = SpringUtil.getBean(ItemFactory.class).create().getLastVersion(id.getUid(), StrUtil.blankToDefault(id.getObjectType(),EntityUtil.getObjectType()));
        if (Objects.isNull(entity)) {
            return null;
        }
        String revision = EntityUtil.getRevision(StrUtil.blankToDefault(id.getObjectType(),EntityUtil.getObjectType()));
        WorkspaceObjectResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(revision));
        BeanUtil.copyPropertiesIgnoreNull(entity, resp);
        SpringUtil.getBean(DictUtil.class).translate(resp);
        return resp;
    }

    /***
     * 修改记录查询(获取各个版次的所有最新版本)
     *
     * @param id 主键id
     * @author 徐鹏军
     * @date 2022/4/18 13:11
     * @return {@link List< WorkspaceObjectResp>}
     */
    default List<WorkspaceObjectResp> updateRecordList(IdRequest id) {
        // 通过版本ID获取
        String revisionObjectType =  StrUtil.blankToDefault(id.getObjectType(),EntityUtil.getObjectType());
        String revisionType = EntityUtil.getRevision(revisionObjectType);
        // 查询出当前版本记录
        WorkspaceObjectEntity objectEntity = EntityUtil.getById(revisionObjectType, id.getUid());
        if (Objects.isNull(objectEntity)) {
            return Collections.emptyList();
        }
        //通过ItemId倒序取3条记录
        ItemEntity revision = (ItemEntity) objectEntity;
        // 由于同一个版本的版本号相同，并且同一组件图号也相同。根据此条件查询
        List<Pair<String, Object>> params = Arrays.asList(
                Pair.of(ItemRevisionEntity.ITEM_ID, revision.getItemId())
        );
        OrderSpecifier order = new OrderSpecifier(Order.DESC, ExpressionUtils.path(EntityUtil.getEntityClass(revisionObjectType), ItemRevisionEntity.SEQUENCE));
        List<WorkspaceObjectEntity> dataList = EntityUtil.getDynamicEqQuery(revisionType, params).orderBy(order).limit(3).fetch();
        if (CollUtil.isEmpty(dataList)) {
            return Collections.emptyList();
        }
        return dataList.stream().map(data -> {
            ItemRevisionResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(revisionType));
            BeanUtil.copyPropertiesIgnoreNull(data, resp);
            return resp;
        }).collect(Collectors.toList());

    }

    /***
     * 删除
     * @param id id
     * @author: 王辉
     * @date: 2022/4/20 11:13
     * @return:  {@link BusinessObjectResp}
     */
    @Transactional
    @Override
    default BusinessObjectResp deleteObject(IdRequest id, AppNameEnum appName) {
        WorkspaceObjectResp resp = (WorkspaceObjectResp) IWorkspaceObjectDomainService.super.deleteObject(id, appName);
        // 判断他是否被BOM引用，如果被引用则不能为删除
        QBOMNodeEntity bomNode = QBOMNodeEntity.bOMNodeEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(bomNode.childItem.eq(id.getUid()));
        builder.and(bomNode.delFlag.isFalse());
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        if (jpaQueryFactory.selectFrom(bomNode).where(builder).fetchCount() > 0) {
            throw new ServiceException(ErrorCode.FORBIDDEN, "当前" + resp.getObjectName() + "被BOM引用，操作无法完成");
        }
        return resp;
    }


    default TableResponse<WorkspaceObjectResp> pagePlus(TableRequest<? extends ItemRevisionReq> req) {
        String relationType = new IncludeRLEntity().getObjectType();
        String objectType = EntityUtil.getObjectType();
        // 获取树的递归
        Relation relation = SpringUtil.getBean(RelationFactory.class).create();
        List<String> folderUidList = relation.childList(relationType, req.getData().getUid(), objectType).
                stream().map(RelationEntity::getRightObject).collect(Collectors.toList());
        folderUidList.add(req.getData().getUid());
        List<Triple<String, Ops, Object>> params = Arrays.asList(
                Triple.of(RelationEntity.LEFT_OBJECT,Ops.IN,folderUidList),
                Triple.of(RelationEntity.LEFT_OBJECT_TYPE,Ops.EQ,new LibraryFolderEntity().getObjectType())
        );
        JPAQuery<LibraryFolderRLEntity> dynamicQuery = EntityUtil.getDynamicQuery(new LibraryFolderRLEntity().getObjectType(), params);

        PageRequest pageRequest = PageHelper.ofReq(req);
        OrderSpecifier orderSpecifier = new OrderSpecifier(Order.DESC,
                ExpressionUtils.path(EntityUtil.getEntityClass(relationType), WorkspaceObjectEntity.CREATION_DATE));
        QueryResults<LibraryFolderRLEntity> queryResults = dynamicQuery.offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize()).orderBy(orderSpecifier).fetchResults();

        if (queryResults.getTotal() <= 0) {
            return TableResponse.<WorkspaceObjectResp>builder().total(0).data(Collections.emptyList()).build();
        }
        // 根据所有右id集合单表查询分页
        List<String> uidList = queryResults.getResults().stream().map(LibraryFolderRLEntity::getRightObject).collect(Collectors.toList());
        Item itemBo = SpringUtil.getBean(ItemFactory.class).create();
        Collection<WorkspaceObjectEntity> resultList = itemBo.getLastVersion(uidList, objectType).values();

        // 如果当前对象是版本，则把他对应的左对象查询出来
        List<String> rightUids = resultList.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        Map<String, WorkspaceObjectEntity> leftObjectMap = itemRevision.getLeftObjectMap(rightUids, EntityUtil.getRevision(objectType));

        // 转换响应对象
        List<WorkspaceObjectResp> respList = resultList.stream().map(item -> {
            WorkspaceObjectResp instance = ReflectUtil.newInstance(EntityUtil.getRespPackage(EntityUtil.getRevision(objectType)));
            BeanUtil.copyPropertiesIgnoreNull(item, instance);
            if (CollUtil.isEmpty(leftObjectMap)) {
                return instance;
            }
            //获取版本相关的Master对象进行赋值
            WorkspaceObjectEntity leftEntity = leftObjectMap.get(item.getUid());
            if (ObjectUtil.isNotEmpty(leftEntity)){
                instance.setLeftObject(leftEntity.getUid());
                instance.setLeftObjectType(leftEntity.getObjectType());
            }
            return instance;
        }).collect(Collectors.toList());
        // 字段翻译
        ItemRevision itemRevision2= SpringUtil.getBean(ItemRevisionFactory.class).create();
        // 转换响应对象
        List<WorkspaceObjectResp> resps = respList.stream().map(instance -> {
            //处理数据集
            //查询改零组件下的所有未删除版本
            List<FileStorage> fileStorageByIn = itemRevision2.getFileStorage(instance.getUid(), instance.getObjectType());
            List<FileAttrResp> collect = fileStorageByIn.stream().map(file -> {
                FileAttrResp fileAttrResp = new FileAttrResp();
                BeanUtil.copyPropertiesIgnoreNull(file, fileAttrResp);
                fileAttrResp.setFileType(file.getType());
                return fileAttrResp;
            }).sorted(Comparator.comparing(FileAttrResp::getCreationDate).reversed()).collect(Collectors.toList());
            ((ItemRevisionResp) instance).setFiles(collect);
            return instance;
        }).sorted(Comparator.comparing(a -> {
            ItemRevisionResp b = (ItemRevisionResp) a;
            return Long.valueOf(b.getItemId().substring(1));
        }).reversed()).collect(Collectors.toList());

        SpringUtil.getBean(DictUtil.class).translate(resps);
        return TableResponse.<WorkspaceObjectResp>builder().total(queryResults.getTotal()).data(resps).build();
    }



    /**
     *  判断父类零件是否具有编辑权限
     * @param req
     * @author: 拓凯
     * @time: 2022/6/9
     * @return: {@link}
     */
    default void checkParentEidt(BOMNodeReq req) {
        WorkspaceObjectEntity entity = EntityUtil.getById(req.getParentItemType(), req.getParentItem());
        if (Objects.isNull(entity)) {
            throw new ServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("parentItemIsNull",req.getParentItem()));

        }
        this.verifyAuthority(entity, OperatorEnum.Write,DataTypeEnum.PARENT_DATA,"updateError",entity.getObjectName());
    }
    /**
     *  判断父类版本是否具有编辑权限
     * @param req
     * @author: 拓凯
     * @time: 2022/7/21
     * @return: {@link}
     */
    default void checkParentRevEidt(BOMNodeReq req) {
        WorkspaceObjectEntity entity = EntityUtil.getById(EntityUtil.getRevision(req.getParentItemType()), req.getParentItemRev());
        if (Objects.isNull(entity)) {
            throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("parentRevIsNull",req.getParentItemRev()));
        }
        this.verifyAuthority(entity, OperatorEnum.Write, DataTypeEnum.PARENT_DATA,"updateError",entity.getObjectName());
    }


    /**
     * bom行新增
     * @param createBomReq
     * @param bomStructure
     * @param appNameEnum
     * @return
     */
    @Transactional
    default WorkspaceObjectResp saveBomReq(CreateBomReq createBomReq,String bomStructure,AppNameEnum appNameEnum) {
        BOMNodeReq nodeReq = createBomReq.getBomNodeReq();
        nodeReq.setChildItemType(EntityUtil.getObjectType());
        IBOMNodeDomainService ibomNodeDomainService = SpringUtil.getBean(IBOMNodeDomainService.class);
        if(!ibomNodeDomainService.checkBomStructureRelation(nodeReq.getParentItemType(),nodeReq.getChildItemType(),bomStructure)){
            Map<String, String> map = SpringUtil.getBean(DictUtil.class).getCodeValueMap(DictConstant.OBJECT_TYPE_NAME);
            throw new ServiceException(ErrorCode.E_10,MessageSourceUtil.getMessage("createTypeCheck",
                    map.get(nodeReq.getParentItemType()),map.get(nodeReq.getChildItemType())));
        }
        //判断是否可以添加，取决于父级的是否具有编辑权限
        this.checkParentRevEidt(nodeReq);
        WorkspaceObjectResp save = this.save(createBomReq.getReq());
        nodeReq.setChildItem(save.getUid());
        ibomNodeDomainService.createNode(nodeReq, appNameEnum);
        return save;
    }

    /**
     * bom行克隆
     * @param cloneReq
     * @param appNameEnum
     * @return
     */
    @Transactional
    default BusinessObjectResp cloneBomReq(CloneReq cloneReq,AppNameEnum appNameEnum) {
        WorkspaceObjectResp resp = this.save(cloneReq.getReq());
        IBOMNodeDomainService ibomNodeDomainService = SpringUtil.getBean(IBOMNodeDomainService.class);
        ibomNodeDomainService.cloneBomNode(cloneReq.getCloneType(),cloneReq.getCloneId(),resp.getObjectType(),
                resp.getUid(),resp.getRightObject(),appNameEnum,new HashMap<>());
        return resp;
    }

    /**
     *  判断是否是根节点通用方法
     * @return
     */
    default boolean isRoot(String uid,String objectType) {
        boolean isRoot = false;
        String id ;
        //有版本的类型
        if (EntityUtil.checkItem(objectType)) {
            ItemRevisionFactory bean = SpringUtil.getBean(ItemRevisionFactory.class);
            ItemEntity itemEntity = bean.create().getLeftObjectByRightObject(uid, objectType);
            id = itemEntity.getUid();
        //无版本类型如工步
        } else {
            id = uid;
        }
        List<Triple<String, Ops, Object>> paramList = Arrays.asList(
                Triple.of(BOMNodeEntity.DEL_FLAG, Ops.EQ, false),
                Triple.of(BOMNodeEntity.CHILD_ITEM, Ops.EQ,id)
        );
        WorkspaceObjectEntity entity = EntityUtil.getDynamicQuery(new BOMNodeEntity().getObjectType(), paramList).fetchFirst();
        if (Objects.isNull(entity)) {
            isRoot = true;
        }
        return isRoot;
    }

}
