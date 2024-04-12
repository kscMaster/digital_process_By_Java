package com.nancal.service.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.model.dataset.FileAttrReq;
import com.nancal.api.model.dataset.FileAttrResp;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.api.utils.FileItemUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.annotation.TimeLog;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.PageHelper;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.LifeCycleStateEnum;
import com.nancal.common.enums.OperatorEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.exception.TipServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.common.utils.MessageSourceUtil;
import com.nancal.common.utils.VersionUtil;
import com.nancal.model.entity.*;
import com.nancal.remote.service.RemoteLezaoIntegrationService;
import com.nancal.remote.vo.IntegrationFileVo;
import com.nancal.remote.vo.LezaoResult;
import com.nancal.service.bo.FileStorage;
import com.nancal.service.bo.IncludeRL;
import com.nancal.service.bo.Item;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.persistence.EntityManager;
import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public interface IItemRevisionDomainService extends IWorkspaceObjectDomainService {

    @Transactional
    @Override
    default BusinessObjectResp update(BusinessObjectReq req) {
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        ItemRevisionReq myReq = (ItemRevisionReq) req;
        String revision = EntityUtil.getRevision(myReq.getObjectType());
        if (StrUtil.isBlank(myReq.getObjectType())){
            revision = EntityUtil.getRevision(EntityUtil.getObjectType());
        }
        // 根据uid获取数据库记录
        WorkspaceObjectEntity objectEntity = EntityUtil.getById(revision, req.getUid());
        ItemRevisionEntity dbRevision = (ItemRevisionEntity) objectEntity;
        if (ObjectUtil.isEmpty(objectEntity)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        if(!dbRevision.getActive()){
            throw new ServiceException(ErrorCode.E_14);
        }
        // 权限校验
        this.verifyAuthority(dbRevision, OperatorEnum.Write,"updateError",dbRevision.getObjectName());
//        // 检验图代号是否已存在
//        if (!dbRevision.getItemId().equals(myReq.getItemId())) {
//            long count = EntityUtil.getDynamicEqQuery(revisionId, Pair.of(ItemRevisionEntity.ITEM_ID, myReq.getItemId())).fetchCount();
//            if (count>0){
//                throw new ServiceException(ErrorCode.E_10,"牌号或代（图）号已存在");
//            }
//            // 图代号修改的话，则相应的将左对象的图代号进行更新
//            ItemEntity itemEntity = SpringUtil.getBean(ItemRevisionFactory.class).create().getLeftObjectByRightObject(req.getUid(), revisionId);
//            if (Objects.nonNull(itemEntity)) {
//                itemEntity.setItemId(myReq.getItemId());
//                entityManager.merge(itemEntity);
//            }
//        }
        ItemRevisionResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(revision));
        // 比较对象是否存在修改
        if (myReq.equals(dbRevision)) {
            BeanUtil.copyPropertiesIgnoreNull(dbRevision, resp);
            resp.setLeftObject(myReq.getLeftObject());
            resp.setLeftObjectType(myReq.getLeftObjectType());
            return resp;
        }
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        // 针对新请求数据进行升版次，并保存
//        ItemRevisionEntity newRevision = ReflectUtil.newInstance(EntityUtil.getEntityPackage(revision));
//        BeanUtil.copyPropertiesIgnoreNull(dbRevision, newRevision);
//        BeanUtil.copyPropertiesIgnoreNull(myReq, newRevision);
//        newRevision.setUid(IdGeneratorUtil.generate());
//        newRevision.setSequence(VersionUtil.createSequence(dbRevision.getSequence()));
//        newRevision.setOwnerId(userUtils.getCurrentUserId());
//        newRevision.setOwnerName(userUtils.getCurrentUserName());
//        newRevision.setActive(true);
//        newRevision.setCreationDate(dbRevision.getCreationDate());
//        newRevision.setObjectType(revision);
//        entityManager.persist(newRevision);
//        SpringUtil.getBean(ObjectIndexFactory.class).create(newRevision).saveOrUpdate();
//        // 设置数据库对象作为老数据(老版次)
//        dbRevision.setActive(false);
//        entityManager.merge(dbRevision);
//        // 建立新关系
//        IncludeRL includeRL = SpringUtil.getBean(IncludeRLFactory.class).create();
//        includeRL.saveRelation(myReq.getLeftObject(), myReq.getLeftObjectType(), newRevision.getUid(), revision);
//        // 对新版本和数据集做关联
//        QSpecificationRLEntity specificationRL = QSpecificationRLEntity.specificationRLEntity;
//        BooleanBuilder builder = new BooleanBuilder();
//        builder.and(specificationRL.delFlag.isFalse());
//        builder.and(specificationRL.leftObject.eq(dbRevision.getUid()));
//        List<SpecificationRLEntity> datasetRlList = SpringUtil.getBean(JPAQueryFactory.class).selectFrom(specificationRL).where(builder).fetch();
//        for (SpecificationRLEntity specificationRlEntity : datasetRlList) {
//            SpecificationRLEntity newSpecRl = new SpecificationRLEntity();
//            BeanUtil.copyPropertiesIgnoreNull(specificationRlEntity, newSpecRl);
//            newSpecRl.setUid(IdGeneratorUtil.generate());
//            newSpecRl.setLeftObject(newRevision.getUid());
//            entityManager.persist(newSpecRl);
//        }
//        BeanUtil.copyPropertiesIgnoreNull(newRevision, resp);
//        resp.setObjectName(newRevision.getObjectName());
//        resp.setLeftObject(myReq.getLeftObject());
//        resp.setLeftObjectType(myReq.getLeftObjectType());

        ItemRevisionEntity newRevision = ReflectUtil.newInstance(EntityUtil.getEntityPackage(revision));
        BeanUtil.copyPropertiesIgnoreNull(dbRevision, newRevision);
        newRevision.setUid(IdGeneratorUtil.generate());
        newRevision.setActive(false);
        entityManager.persist(newRevision);

        //更新当前数据
        BeanUtil.copyPropertiesIgnoreNull(myReq, dbRevision);
        dbRevision.setSequence(VersionUtil.createSequence(dbRevision.getSequence()));
        dbRevision.setOwnerId(userUtils.getCurrentUserId());
        dbRevision.setOwnerName(userUtils.getCurrentUserName());
        entityManager.merge(dbRevision);

        BeanUtil.copyPropertiesIgnoreNull(dbRevision, resp);
        resp.setObjectName(myReq.getObjectName());
        resp.setLeftObject(myReq.getLeftObject());
        resp.setLeftObjectType(myReq.getLeftObjectType());
        return resp;
    }

    /***
     * 修改记录查询(版次列表)
     *
     * @param id 主键id
     * @author 徐鹏军
     * @date 2022/4/18 13:11
     * @return {@link List<  WorkspaceObjectResp >}
     */
    default List<WorkspaceObjectResp> updateRecordList(IdRequest id) {
        String revisionObjectType = EntityUtil.getRevision( StrUtil.blankToDefault(id.getObjectType(),EntityUtil.getObjectType()));
        // 查询出当前版本记录
        WorkspaceObjectEntity objectEntity = EntityUtil.getById(revisionObjectType, id.getUid());
        if (Objects.isNull(objectEntity)) {
            return Collections.emptyList();
        }
        ItemRevisionEntity revision = (ItemRevisionEntity) objectEntity;
        // 由于同一个版本的版本号相同，并且同一组件图号也相同。根据此条件查询
        List<Pair<String, Object>> params = Arrays.asList(
                Pair.of(ItemRevisionEntity.REVISION_ID, revision.getRevisionId()),
                Pair.of(ItemRevisionEntity.ITEM_ID, revision.getItemId())
        );
        OrderSpecifier order = new OrderSpecifier(Order.DESC, ExpressionUtils.path(EntityUtil.getEntityClass(revisionObjectType), ItemRevisionEntity.SEQUENCE));
        List<WorkspaceObjectEntity> dataList = EntityUtil.getDynamicEqQuery(revisionObjectType, params).orderBy(order).limit(3).fetch();
        if (CollUtil.isEmpty(dataList)) {
            return Collections.emptyList();
        }
        return dataList.stream().map(data -> {
            ItemRevisionResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(revisionObjectType));
            BeanUtil.copyPropertiesIgnoreNull(data, resp);
            return resp;
        }).collect(Collectors.toList());
    }

    /***
     * 删除
     * @param id id
     * @author: 王辉
     * @date: 2022/4/20 11:14
     * @return:  {@link BusinessObjectResp}
     */
    @Transactional
    @Override
    default BusinessObjectResp deleteObject(IdRequest id, AppNameEnum appName) {
        BusinessObjectResp resp = IWorkspaceObjectDomainService.super.deleteObject(id, appName);
        ItemRevisionEntity entity = ReflectUtil.newInstance(EntityUtil.getEntityPackage(EntityUtil.getObjectType()));
        BeanUtil.copyPropertiesIgnoreNull(resp,entity);
        //删除对象对应对象索引
        SpringUtil.getBean(ObjectIndexFactory.class).create(entity).deleteObjectIndex();
        return resp;
    }

    @Override
    default BusinessObjectResp getObject(IdRequest id) {
        BusinessObjectResp resp = IWorkspaceObjectDomainService.super.getObject(id);
        if (Objects.isNull(resp)) {
            return null;
        }

        ItemRevisionResp workspaceObjectResp = (ItemRevisionResp) resp;
        ItemRevision revision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        String objectType =Objects.isNull(id.getObjectType()) ? EntityUtil.getObjectType() : id.getObjectType();
        boolean permission = revision.editItemIdPermission(id.getUid(), objectType);
        workspaceObjectResp.setHasDrawingCode(permission);
        return workspaceObjectResp;
    }

    default BusinessObjectResp getObject(MfgCheckReq req) {
        return null;
    }

    /**
     * 升版
     * @param req
     * @author: 薛锦龙
     * @time: 2022/5/30
     * @return: {@link WorkspaceObjectResp}
     */
    @Transactional
    default WorkspaceObjectResp upgrade(WorkspaceObjectReq req){
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        ItemRevisionEntity entity = EntityUtil.getById(EntityUtil.getObjectType(), req.getUid());
        if(ObjectUtil.isNull(entity)){
            throw new ServiceException(ErrorCode.E_12);
        }
        if(!entity.getActive()){
            throw new ServiceException(ErrorCode.E_14);
        }
        //校验创建人权限
        IWorkspaceObjectDomainService.super.verifyAuthority(entity,OperatorEnum.Upgrade,"upgradeData",entity.getObjectName());
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        List<WorkspaceObjectEntity> allActiveRevision = Optional.ofNullable(itemRevision.getAllActiveRevision(req.getUid(), EntityUtil.getObjectType())).orElse(new ArrayList<>());
        boolean res = allActiveRevision.stream().allMatch(revision -> LifeCycleStateEnum.Released.name().equals(revision.getLifeCycleState()));
        if(!res){
            throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("upgradeData",entity.getObjectName(),"不是已发布状态!"));
        }
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        //对数据进行升版
        ItemRevisionEntity itemRevisionEntity = ReflectUtil.newInstance(EntityUtil.getEntityPackage(EntityUtil.getObjectType()));
        BeanUtil.copyPropertiesIgnoreNull(req,itemRevisionEntity);
        itemRevisionEntity.setUid(IdGeneratorUtil.generate());
        itemRevisionEntity.setActive(true);
        itemRevisionEntity.setObjectType(EntityUtil.getObjectType());
        itemRevisionEntity.setRevisionId(VersionUtil.createRevisionId(entity.getRevisionId()));
        itemRevisionEntity.setLifeCycleState(LifeCycleStateEnum.Working.name());
        itemRevisionEntity.setSequence(null);
        itemRevisionEntity.setSequence(VersionUtil.createSequence(itemRevisionEntity.getSequence()));
        itemRevisionEntity.setOwnerId(userUtils.getCurrentUserId());
        itemRevisionEntity.setOwnerName(userUtils.getCurrentUserName());
        itemRevisionEntity.setItemId(entity.getItemId());
        entityManager.persist(itemRevisionEntity);
        //创建关联关系
        IncludeRL includeRL = SpringUtil.getBean(IncludeRLFactory.class).create();
        includeRL.saveRelation(req.getLeftObject(),EntityUtil.getObjectTypeByRevisionType(EntityUtil.getObjectType()),itemRevisionEntity.getUid());
        //创建返回值
        ItemRevisionResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(EntityUtil.getObjectType()));
        BeanUtil.copyPropertiesIgnoreNull(itemRevisionEntity,resp);
        resp.setLeftObject(req.getLeftObject());
        resp.setLeftObjectType(req.getLeftObjectType());
        return resp;
    }

    /**
     * BOM关系对象升版
     * @param req
     * @author: 拓凯
     * @time: 2022/7/26
     * @return: {@link WorkspaceObjectResp}
     */
    @Transactional
    default WorkspaceObjectResp bomUpgrade(WorkspaceObjectReq req,AppNameEnum appNameEnum){
        ItemRevisionResp resp = (ItemRevisionResp)this.upgrade(req);
        //向bomNode中添加升版后的数据
        IBOMNodeDomainService nodeDomainService = SpringUtil.getBean(IBOMNodeDomainService.class);
        nodeDomainService.insertBomNode(new IdRequest(req.getLeftObject(), req.getLeftObjectType()), resp,appNameEnum);
        //调用维护工时的方法
        nodeDomainService.fullTaskTime(5,resp.getUid(),resp.getObjectType());
        return resp;
    }

    /**
     *
     * 1、升版
     * 2、查询历史数据集
     * 3、处理新增数据集，存在同名，进行清理
     * @param req
     * @return
     */
    @Transactional
    default WorkspaceObjectResp upgradeAndDataSet(WorkspaceObjectReq req) {
        WorkspaceObjectResp resp = this.upgrade(req);
        ItemRevisionReq itemRevisionReq = (ItemRevisionReq)req;

//        String objectType = EntityUtil.getObjectType();
//        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
//        List<FileStorage> storageList = itemRevision.getFileStorage(req.getUid(), objectType);
//        List<FileAttrReq> reqFileList = this.redepositFile(storageList);
//        //升版中的文件是否为新增
//        List<FileAttrReq> newFileList = Optional.ofNullable(itemRevisionReq.getFiles()).orElse(new ArrayList<>()).stream().
//                filter(f -> StrUtil.isEmpty(f.getUid())).collect(Collectors.toList());
//        List<String> fileNameList = newFileList.stream().map(f -> FileNameUtil.getName(f.getFilePath())).collect(Collectors.toList());
//        //将新增的同名进行移除
//        reqFileList = reqFileList.stream().filter(f -> !fileNameList.contains(FileNameUtil.getName(f.getFilePath()))).collect(Collectors.toList());
//        reqFileList.addAll(newFileList);
//        if(CollUtil.isEmpty(reqFileList)){
//            return resp;
//        }
        List<FileAttrReq> reqList = Optional.ofNullable(itemRevisionReq.getFiles()).orElse(Collections.emptyList());
        List<FileAttrReq> reqFileList = reqList.stream().filter(attrReq -> StrUtil.isBlank(attrReq.getUid())).collect(Collectors.toList());
        List<String> uidList = reqList.stream().filter(attrReq -> !StrUtil.isBlank(attrReq.getUid())).map(FileAttrReq::getUid).collect(Collectors.toList());
        List<WorkspaceObjectEntity> storageList = EntityUtil.getByIds(new FileStorageEntity().getObjectType(), uidList);
        reqFileList.addAll(this.redepositFile(storageList));

        //保存附件
        if (CollUtil.isNotEmpty(reqFileList)){
            IWorkspaceObjectDomainService.super.saveAttachment(reqFileList,EntityUtil.getObjectType(),resp.getUid());
        }

        return resp;
    }


    /**
     * req当前版次
     * 转存所有版次的数据集
     * @param storageList
     */
    default List<FileAttrReq> redepositFile(List<WorkspaceObjectEntity> storageList){
        if(CollUtil.isEmpty(storageList)){
            return Collections.emptyList();
        }
        List<FileAttrReq> reqList = new ArrayList<>();
        for (WorkspaceObjectEntity workspaceObject : storageList) {
            FileStorageEntity storage = (FileStorageEntity)workspaceObject;
            if(StrUtil.isEmpty(storage.getFilePath())){
                continue;
            }
            if(!storage.getFilePath().startsWith("//")){
                continue;
            }
            String fileUrl = "http:"+storage.getFilePath();
            fileUrl = fileUrl.substring(0,fileUrl.lastIndexOf(StrPool.SLASH))+StrPool.SLASH+
                    URLEncoder.encode(fileUrl.substring(fileUrl.lastIndexOf(StrPool.SLASH)+1), Charset.forName("utf-8"));
            fileUrl = fileUrl.replace("+","%20");
            String tempPath =  System.getProperty("user.dir")+ StrPool.SLASH+storage.getOriginFileName()+StrPool.DOT+storage.getFileExt();
            try {
                URL httpUrl = new URL(fileUrl);
                File file = new File(tempPath);
                FileUtils.copyURLToFile(httpUrl, new File(tempPath));
                RemoteLezaoIntegrationService integrationService = SpringUtil.getBean(RemoteLezaoIntegrationService.class);
                FileItem fileItem = FileItemUtil.getMultipartFile(file,storage.getOriginFileName());
                MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
                storage.setBucketName(StrUtil.blankToDefault(storage.getBucketName(),"lzdigit"));
                LezaoResult<IntegrationFileVo> upload = integrationService.upload(multipartFile, storage.getBucketName());
                if(ObjectUtil.isNull(upload) || ObjectUtil.isNull(upload.getData() )){
                    throw new ServiceException(ErrorCode.ERROR,"文件转存失败，返回值为空");
                }
                IntegrationFileVo data = upload.getData();
                FileAttrReq fileAttrReq = new FileAttrReq();
                fileAttrReq.setFilePath(data.getUrl());
                fileAttrReq.setFileSize(data.getSize());
                fileAttrReq.setFileType(data.getExt());
                fileAttrReq.setBucketName(data.getGroup());
                reqList.add(fileAttrReq);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(ErrorCode.ERROR,"文件转存失败");
            }finally {
                try{
                    File file = new File(tempPath);
                    file.deleteOnExit();
                }catch (Exception e){
                }
            }
        }
        return reqList;
    }


    @Override
    default BusinessObjectResp deleteObject(IdRequest id) {
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        //校验引用关系
        ItemRevision itemRevision = new ItemRevision();
        itemRevision.deleteRelationshipVerification(id);
        ItemRevisionEntity instance = new ItemRevisionEntity();
        List<Pair<String, Object>> params = Arrays.asList(
                Pair.of(instance.UID, id.getUid()),
                Pair.of(instance.ACTIVE, true)
        );
        WorkspaceObjectEntity entity1 = EntityUtil.getDynamicEqQuery(EntityUtil.getObjectType(), params).fetchFirst();
        if (Objects.isNull(entity1)){
            throw  new ServiceException(ErrorCode.E_10,"数据不存在，请刷新后重试");
        }
        verifyAuthority(entity1,OperatorEnum.Delete);
        WorkspaceObjectResp businessObjectResp =(WorkspaceObjectResp) IWorkspaceObjectDomainService.super.deleteObject(id);
        //删除master关系
        WorkspaceObjectEntity masterRLEntity1 = EntityUtil.getDynamicEqQuery(new MasterRLEntity().getObjectType(), Pair.of(MasterRLEntity.RIGHT_OBJECT, id.getUid())).fetchFirst();
        if (ObjectUtil.isEmpty(masterRLEntity1)){
            throw  new ServiceException(ErrorCode.E_10,"数据不存在，请刷新后重试");
        }
        masterRLEntity1.setDelFlag(true);
        entityManager.merge(masterRLEntity1);
        //删除附件
        List<String> rightObjectRelations = dictUtil.getRightObjectRelations(EntityUtil.getObjectType());
        List<Triple<String, Ops,Object>> query = new ArrayList<>();
        query.add(Triple.of(RelationEntity.LEFT_OBJECT,Ops.EQ,id.getUid()));
        query.add(Triple.of(RelationEntity.LEFT_OBJECT_TYPE,Ops.EQ,EntityUtil.getObjectType()));
        List<WorkspaceObjectEntity> specificationRL = EntityUtil.getDynamicQuery(rightObjectRelations.get(0), query).fetch();
        if (CollectionUtil.isNotEmpty(specificationRL)){
            specificationRL.forEach(data->{
                data.setDelFlag(true);
                entityManager.merge(data);
            });
        }
        businessObjectResp.setLeftObject(masterRLEntity1.getLeftObject());
        businessObjectResp.setLeftObjectType(masterRLEntity1.getLeftObjectType());
        return businessObjectResp;
    }


    @TimeLog
    default TableResponse<WorkspaceObjectResp> multiPageLike(TableRequest<? extends ItemRevisionReq> req) {
        // 获取查询条件
        Pair<List<Triple<String, Ops, Object>>, OrderSpecifier[]> pair = EntityUtil.getAnnotationQueryParam(req.getData());
        if (Objects.isNull(pair)) {
            return TableResponse.<WorkspaceObjectResp>builder().total(0).data(Collections.emptyList()).build();
        }
        String objectType = EntityUtil.getObjectType();
        Class<?> clazz = EntityUtil.getEntityClass(objectType);
        if (ItemRevisionEntity.class.isAssignableFrom(clazz)) {
            pair.getKey().add(Triple.of(ItemRevisionEntity.ACTIVE, Ops.EQ, Boolean.TRUE));
        }
        PageRequest request = PageHelper.ofReq(req);
        // 1：带入分页查询
        JPAQuery<WorkspaceObjectEntity> jpaQuery = EntityUtil.getDynamicQuery(EntityUtil.getObjectType(), pair.getKey())
                .offset(request.getOffset())
                .limit(request.getPageSize());
        if (ArrayUtil.isNotEmpty(pair.getValue())) {
            jpaQuery.orderBy(pair.getValue());
        }
        QueryResults<WorkspaceObjectEntity> queryResults = jpaQuery.fetchResults();
        if (queryResults.getTotal() <= 0) {
            return TableResponse.<WorkspaceObjectResp>builder().total(0).data(Collections.emptyList()).build();
        }
        // 如果当前对象是版本，则吧他对应的左对象查询出来
        List<String> rightUids = queryResults.getResults().stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        Map<String, WorkspaceObjectEntity> leftObjectMap = itemRevision.getLeftObjectMap(rightUids, EntityUtil.getObjectType());
        List<WorkspaceObjectResp> results = fullLeftObjectResult(queryResults.getResults(),leftObjectMap);
        return TableResponse.<WorkspaceObjectResp>builder().total(queryResults.getTotal()).data(results).build();
    }

    @TimeLog
    default TableResponse<WorkspaceObjectResp> pageLike(TableRequest<? extends ItemRevisionReq> req,List<Triple<String, Ops, Object>> of) {
        // 获取查询条件
        Pair<List<Triple<String, Ops, Object>>, OrderSpecifier[]> pair = EntityUtil.getAnnotationQueryParam(req.getData(),of);
        return like(req, pair);
    }

    @TimeLog
    default TableResponse<WorkspaceObjectResp> pageLike(TableRequest<? extends ItemRevisionReq> req) {
        // 获取查询条件
        Pair<List<Triple<String, Ops, Object>>, OrderSpecifier[]> pair = EntityUtil.getAnnotationQueryParam(req.getData());
        return like(req, pair);
    }

    /***
     * 分页查询  (针对只返回一个版本的模糊查询接口)
     * 1：获取请求对象所有标注需要查询的属性，即属性中带有@QueryField注解的
     * 2：将属性值是空的过滤
     * 3：根据@QueryField的value组合查询条件
     * 4：带入分页查询
     *
     * @author 徐鹏军
     * @date 2022/5/12 13:32
     * @return {@link TableResponse < WorkspaceObjectResp>}
     */
    @TimeLog
    default TableResponse<WorkspaceObjectResp> like(TableRequest<? extends ItemRevisionReq> req,Pair<List<Triple<String, Ops, Object>>, OrderSpecifier[]> pair) {
        if (Objects.isNull(pair)) {
            return TableResponse.<WorkspaceObjectResp>builder().total(0).data(Collections.emptyList()).build();
        }
        String objectType = EntityUtil.getObjectType();
        Class<?> clazz = EntityUtil.getEntityClass(objectType);
        if (ItemRevisionEntity.class.isAssignableFrom(clazz)) {
            pair.getKey().add(Triple.of(ItemRevisionEntity.ACTIVE, Ops.EQ, Boolean.TRUE));
        }
        PageRequest request = PageHelper.ofReq(req);
        // 1：带入分页查询
        JPAQuery<WorkspaceObjectEntity> jpaQuery = EntityUtil.getDynamicQuery(EntityUtil.getObjectType(), pair.getKey())
                .offset(request.getOffset())
                .limit(request.getPageSize());
        if (ArrayUtil.isNotEmpty(pair.getValue())) {
            jpaQuery.orderBy(pair.getValue());
        }
        jpaQuery.groupBy(ExpressionUtils.path(clazz, ItemRevisionEntity.ITEM_ID));
        QueryResults<WorkspaceObjectEntity> queryResults = jpaQuery.fetchResults();
        if (queryResults.getTotal() <= 0) {
            return TableResponse.<WorkspaceObjectResp>builder().total(0).data(Collections.emptyList()).build();
        }
        // 如果当前对象是版本，则吧他对应的左对象查询出来
        List<String> rightUids = queryResults.getResults().stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        Map<String, WorkspaceObjectEntity> leftObjectMap = itemRevision.getLeftObjectMap(rightUids, EntityUtil.getObjectType());
        //通过右对象查询最新的版本
        List<String> leftUids = leftObjectMap.values().stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
        Item item = SpringUtil.getBean(ItemFactory.class).create();
        Map<String, WorkspaceObjectEntity> resultList = item.getLastVersion(leftUids, EntityUtil.getObjectTypeByRevisionType(objectType));
        if(CollUtil.isEmpty(resultList)){
            return TableResponse.<WorkspaceObjectResp>builder().total(0).data(Collections.emptyList()).build();
        }
        List<WorkspaceObjectResp> results = fullLeftObjectResult(resultList.values(),leftObjectMap);
        return TableResponse.<WorkspaceObjectResp>builder().total(queryResults.getTotal()).data(results).build();
    }


    /***
     * 分页查询
     * 1：获取请求对象所有标注需要查询的属性，即属性中带有@QueryField注解的
     * 2：将属性值是空的过滤
     * 3：根据@QueryField的value组合查询条件
     * 4：带入分页查询
     *
     * @author 徐鹏军
     * @date 2022/5/12 13:32
     * @return {@link TableResponse < WorkspaceObjectResp>}
     */
    default TableResponse<WorkspaceObjectResp> pageAndFiles(TableRequest<? extends ItemRevisionReq> req) {
        TableResponse<WorkspaceObjectResp> response = this.pageLike(req);
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        // 转换响应对象
        response.getData().stream().map(instance -> {
            //处理数据集
            //查询改零组件下的所有未删除版本
            List<FileStorage> fileStorageByIn = itemRevision.getFileStorage(instance.getUid(), instance.getObjectType());
            List<FileAttrResp> collect = fileStorageByIn.stream().map(file -> {
                FileAttrResp fileAttrResp = new FileAttrResp();
                BeanUtil.copyPropertiesIgnoreNull(file, fileAttrResp);
                return fileAttrResp;
            }).collect(Collectors.toList());
            ((ItemRevisionResp)instance).setFiles(collect);
            return instance;
        }).collect(Collectors.toList());
        // 字段翻译
        return response;
    }

    /**
     * 基础库修改（不需要校验拥有者权限）
     * @param req
     * @author: 薛锦龙
     * @time: 2022/5/18
     * @return: {@link BusinessObjectResp}
     */

    @Transactional
    default BusinessObjectResp updatePlus(BusinessObjectReq req) {
        //编辑数据
        BusinessObjectResp resp = this.update(req);
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        ItemRevisionReq myReq = (ItemRevisionReq) req;
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        //如果没有附件直接返回
        if (CollectionUtil.isEmpty(myReq.getFiles())){
            return resp;
        }
            //断掉以前的所有关系并创建新的关系
            //获取相同版本系的版次
        String objectType = EntityUtil.getObjectType();
            List<WorkspaceObjectEntity> allSequences = itemRevision.getAllSequences(myReq.getUid(), objectType);
            List<String> ids = allSequences.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
            List<String> leftObjectRelations = dictUtil.getRightObjectRelations(objectType);
            List<WorkspaceObjectEntity> entities = EntityUtil.getDynamicQuery(leftObjectRelations.get(0), Triple.of(RelationEntity.LEFT_OBJECT, Ops.IN, ids)).fetch();
            if (CollUtil.isNotEmpty(entities)) {
                entities.forEach(data -> {
                    data.setDelFlag(true);
                    entityManager.merge(data);
                });
            }
        IWorkspaceObjectDomainService.super.saveAttachment(myReq.getFiles(),objectType,resp.getUid());
        return resp;
    }

    /**
     * 关联制造目标校验
     * @param
     * @author: 薛锦龙
     * @time: 2022/7/19
     * @return: {@link }
     */
    default WorkspaceObjectResp verifyManufacturingTargets(IdRequest id){
        //校验当前数据的权限
        WorkspaceObjectEntity entity = EntityUtil.getById(id.getObjectType(), id.getUid());
        WorkspaceObjectResp workspaceObjectResp = ReflectUtil.newInstance(EntityUtil.getRespPackage(entity.getObjectType()));
        BeanUtil.copyPropertiesIgnoreNull(entity,workspaceObjectResp);
//        IWorkspaceObjectDomainService.super.verifyAuthority(entity, OperatorEnum.Write);
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        List<WorkspaceObjectEntity> allSequences = itemRevision.getAllSequences(id.getUid(), id.getObjectType());
        List<String> collect = allSequences.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
        WorkspaceObjectEntity byLeftObject = SpringUtil.getBean(MfgTargetRLFactory.class).create().getByLeftObject(id.getObjectType(), collect);
        //获取关联对象的数据
        WorkspaceObjectResp resp = null;
        if (ObjectUtil.isNotEmpty(byLeftObject)){
            RelationEntity relationEntity = (RelationEntity)byLeftObject;
            WorkspaceObjectEntity objectEntity = EntityUtil.getById(relationEntity.getRightObjectType(), relationEntity.getRightObject());
            resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(objectEntity.getObjectType()));
            BeanUtil.copyPropertiesIgnoreNull(objectEntity,resp);
        }
        if(ObjectUtil.isNotEmpty(resp)){
            resp.setLeftObject(byLeftObject.getLeftObject());
        }
        return resp;
    }

    /**
     * 关联制造目标
     * @param req
     * @author: 薛锦龙
     * @time: 2022/7/20
     * @return: {@link MfgTargetRLResp}
     */
    @Transactional
    default MfgTargetRLResp manufacturingTargets(ShearReq req){
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        //校验关联关系
        WorkspaceObjectResp workspaceObjectResp = this.verifyManufacturingTargets(new IdRequest(req.getLeftObject(), req.getLeftObjectType()));
        //获取关系
        if (ObjectUtil.isNotEmpty(workspaceObjectResp)){
            String relation1 = dictUtil.getRelation(req.getLeftObjectType(), workspaceObjectResp.getObjectType());
            WorkspaceObjectEntity relationEntity = EntityUtil.getDynamicQuery(relation1, Triple.of(RelationEntity.LEFT_OBJECT,Ops.EQ,workspaceObjectResp.getLeftObject())).fetchFirst();
            //断开原来的关系
            relationEntity.setDelFlag(true);
            entityManager.merge(relationEntity);
        }
        //创建关联关系
        String relation = SpringUtil.getBean(IncludeRLFactory.class).create().saveRelation(req.getLeftObject(), req.getLeftObjectType(), req.getRightObject(), req.getRightObjectType());
        MfgTargetRLResp resp = new MfgTargetRLResp();
        BeanUtil.copyPropertiesIgnoreNull(req,resp);
        resp.setUid(relation);
        resp.setObjectType(ReflectUtil.newInstance(MfgTargetRLEntity.class).getObjectType());
        return resp;
    }

    /**
     * bom行修改
     * @param updateBomReq
     * @return
     */
    @Transactional
    default BusinessObjectResp updateBomReq(UpdateBomReq updateBomReq) {
        BOMNodeReq bomNodeReq= updateBomReq.getBomNodeReq();
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        if (!Objects.isNull(bomNodeReq)) {
            bomNodeReq.setObjectType(new BOMNodeEntity().getObjectType());
            //调用编辑bom属性方法
            BOMNodeResp bomNodeResp =(BOMNodeResp)SpringUtil.getBean(IBOMNodeDomainService.class).update(bomNodeReq);
            objBomResp.setBomNodeResp(bomNodeResp);
        }
        if (!Objects.isNull(updateBomReq.getReq())) {
            //调用编辑有版本的属性方法
            WorkspaceObjectResp update = (WorkspaceObjectResp) this.update(updateBomReq.getReq());
            objBomResp.setObjectResp(update);
        }
        return objBomResp;
    }


    /**
     * 通配根节点和子节点详情（针对有版本）
     * @param id
     * @author: 拓凯
     * @time: 2022/9/2
     * @return: {@link BusinessObjectResp}
     */
    default BusinessObjectResp getBomObject(IdRequest id) {
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        BOMNodeResp bomNodeResp = new BOMNodeResp();
        WorkspaceObjectResp objectResp;
//        作为根节点 查询对象属性
        if (!id.getObjectType().equals(new BOMNodeEntity().getObjectType())) {
            objectResp = (WorkspaceObjectResp) this.getObject(new IdRequest(id.getUid()));
        } else {
//        作为子节点 查询bom属性 + 查询对象属性
            BOMNodeEntity bomNode = EntityUtil.getById(new BOMNodeEntity().getObjectType(), id.getUid());
            if (Objects.isNull(bomNode)) {
                throw new ServiceException(ErrorCode.E_12);
            }
            BeanUtil.copyPropertiesIgnoreNull( bomNode,bomNodeResp);
//           根据零件uid获取最大版本最新激活的版本uid
            WorkspaceObjectEntity lastVersion = SpringUtil.getBean(ItemFactory.class).create().getLastVersion(bomNode.getChildItem(), bomNode.getChildItemType());
            if (Objects.isNull(lastVersion)) {
                throw new ServiceException(ErrorCode.E_12);
            }
            objectResp = (WorkspaceObjectResp)this.getObject(new IdRequest(lastVersion.getUid()));
            if (Objects.isNull(objectResp)) {
                throw new ServiceException(ErrorCode.E_12);
            }
        }
        objBomResp.setObjectResp(objectResp);
        objBomResp.setBomNodeResp(bomNodeResp);
        return objBomResp;
    }


    /**
     * 查找板子
     * @param req
     * @author: 薛锦龙
     * @time: 2022/8/30
     * @return: {@link WorkspaceObjectResp}
     */
    default WorkspaceObjectResp findAssociatedBoards(IdRequest req) {
        String objectType = new BOMNodeEntity().getObjectType();
        Item item = SpringUtil.getBean(ItemFactory.class).create();
        IBOMNodeDomainService ibomNodeDomainService = SpringUtil.getBean(IBOMNodeDomainService.class);
        List<WorkspaceObjectEntity> entities = ibomNodeDomainService.existParent(req.getUid(), objectType, false);
        WorkspaceObjectEntity activeSequence = item.getLastVersion(req.getUid(), req.getObjectType());
        Gte4PartRevisionEntity partRevisionEntity = (Gte4PartRevisionEntity)activeSequence;
        if(ObjectUtil.isEmpty(partRevisionEntity)){
            return null;
        }
        WorkspaceObjectResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(partRevisionEntity.getObjectType()));
        BeanUtil.copyPropertiesIgnoreNull(partRevisionEntity,resp);
        if (CollUtil.isEmpty(entities)){
            resp.setRootNode(true);
        }
        return resp;
    }



}
