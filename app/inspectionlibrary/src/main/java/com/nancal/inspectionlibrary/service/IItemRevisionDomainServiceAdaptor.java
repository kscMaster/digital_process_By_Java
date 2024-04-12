package com.nancal.inspectionlibrary.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.model.dataset.FileAttrResp;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.LifeCycleStateEnum;
import com.nancal.common.enums.OperatorEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.common.utils.VersionUtil;
import com.nancal.model.entity.*;
import com.nancal.service.bo.FileStorage;
import com.nancal.service.bo.IncludeRL;
import com.nancal.service.bo.Item;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.*;
import com.nancal.service.service.IDatasetDomainService;
import com.nancal.service.service.IItemRevisionDomainService;
import com.querydsl.core.types.Ops;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

public interface IItemRevisionDomainServiceAdaptor extends IItemRevisionDomainService {
    /**
     * 基础库修改（不需要校验拥有者权限）
     * @param req
     * @author: 薛锦龙
     * @time: 2022/5/18
     * @return: {@link BusinessObjectResp}
     */
//    @Override
//    default BusinessObjectResp update(BusinessObjectReq req) {
//        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
//        ItemRevisionReq myReq = (ItemRevisionReq) req;
//        String revision = EntityUtil.getRevision(StrUtil.blankToDefault(myReq.getObjectType(), EntityUtil.getObjectType()));
//        // 根据uid获取数据库记录
//        WorkspaceObjectEntity objectEntity = EntityUtil.getById(revision, req.getUid());
//        if (ObjectUtil.isEmpty(objectEntity)) {
//            throw new ServiceException(ErrorCode.E_12);
//        }
//        ItemRevisionEntity dbRevision = (ItemRevisionEntity) objectEntity;
//        // 权限校验
//        this.verifyAuthority(dbRevision, OperatorEnum.Write);
//        ItemRevisionResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(revision));
//        // 比较对象是否存在修改
//        if (myReq.equals(dbRevision)) {
//            BeanUtil.copyPropertiesIgnoreNull(dbRevision, resp);
//            resp.setLeftObject(myReq.getLeftObject());
//            resp.setLeftObjectType(myReq.getLeftObjectType());
//            return resp;
//        }
//        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
//        // 针对新请求数据进行升版次，并保存
//        ItemRevisionEntity newRevision = ReflectUtil.newInstance(EntityUtil.getEntityPackage(revision));
//        BeanUtil.copyPropertiesIgnoreNull(dbRevision, newRevision);
//        BeanUtil.copyPropertiesIgnoreNull(myReq, newRevision);
//        newRevision.setUid(IdGeneratorUtil.generate());
//        newRevision.setSequence(VersionUtil.createSequence(dbRevision.getSequence()));
//        newRevision.setOwnerId(userUtils.getCurrentUserId());
//        newRevision.setOwnerName(userUtils.getCurrentUserName());
//        newRevision.setActive(true);
//        newRevision.setObjectType(revision);
//        entityManager.persist(newRevision);
//        SpringUtil.getBean(ObjectIndexFactory.class).create(newRevision).saveOrUpdate();
//        // 设置数据库对象作为老数据(老版次)
//        dbRevision.setActive(false);
//        entityManager.merge(dbRevision);
//        // 建立新关系
//        IncludeRL includeRL = SpringUtil.getBean(IncludeRLFactory.class).create();
//        includeRL.saveRelation(myReq.getLeftObject(), myReq.getLeftObjectType(), newRevision.getUid(), revision);
//        //判断是否上传了新的数据集
//        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
//        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
//        //字典获取所有附件类型
//        Map<String, String> dataSetType = dictUtil.getCodeValueMap("DataSetType");
//        Set<Map.Entry<String, String>> entries = dataSetType.entrySet();
//        String filePath = myReq.getFiles().get(0).getFilePath();
//        String name = filePath.substring(filePath.lastIndexOf("/") + 1);
//        if (CollectionUtil.isNotEmpty(myReq.getFiles())) {
//            if (StrUtil.isBlank(myReq.getFiles().get(0).getUid())) {
//                //断掉以前的所有关系并创建新的关系
//                //获取相同版本系的版次
//                List<WorkspaceObjectEntity> allSequences = itemRevision.getAllSequences(myReq.getUid(), EntityUtil.getObjectType());
//                List<String> ids = allSequences.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
//                List<String> leftObjectRelations = dictUtil.getLeftObjectRelations(EntityUtil.getObjectType());
//                List<WorkspaceObjectEntity> entities = EntityUtil.getDynamicQuery(leftObjectRelations.get(0), Triple.of(RelationEntity.LEFT_OBJECT, Ops.IN, ids)).fetch();
//                if (CollUtil.isNotEmpty(entries)) {
//                    entities.forEach(data -> {
//                        data.setDelFlag(true);
//                        entityManager.merge(data);
//                    });
//                }
//
////                //获取相同版本下所有的附件
////                Map<String, List<FileStorage>> map = new HashMap<>();
////                Map<String, String> idss = new HashMap<>();
////                List<FileStorage> list = new ArrayList<>();
////                ids.forEach(id -> {
////                    List<FileStorage> file = itemRevision.getFileStorage(id, EntityUtil.getObjectType());
////                    list.addAll(file);
////                    map.put(id, file);
////                });
////                //去掉重复的附件
////                list.stream().
////
////                ids.forEach(id->{
////                    List<FileStorage> fileStorages = map.get(id);
////                    fileStorages.forEach(idd->{
////                        idss.put(idd.getUid(),id);
////                    });
////
////                });
////
////                if (CollectionUtil.isNotEmpty(list)) {
////                    //遍历查看是否存在相同名称的附件
////                    list.forEach(data -> {
////                        //判断名称是否相同,相同删除关系
////                        if ((data.getOriginFileName() + StrUtil.DOT + data.getType()).equals(name)) {
////                            String id = idss.get(data.getUid());
////                            //查找字典类型
////                            List<String> rightObjectRelations = dictUtil.getRightObjectRelations(EntityUtil.getObjectType());
////                            List<Triple<String, Ops, Object>> triples = new ArrayList<>();
////                            triples.add(Triple.of(RelationEntity.LEFT_OBJECT, Ops.EQ, id));
////                            triples.add(Triple.of(RelationEntity.LEFT_OBJECT_TYPE, Ops.EQ, EntityUtil.getObjectType()));
////                            WorkspaceObjectEntity workspaceObject = EntityUtil.getDynamicQuery(rightObjectRelations.get(0), triples).fetchFirst();
////                            if (ObjectUtil.isNotEmpty(workspaceObject)) {
////                                workspaceObject.setDelFlag(true);
////                                entityManager.merge(workspaceObject);
////                            }
////                        }
////                    });
////                }
//                String value = "";
//                for (Map.Entry<String, String> entry : entries) {
//                    boolean contains = entry.getKey().contains(myReq.getFiles().get(0).getFileType());
//                    if (contains) {
//                        value = entry.getValue();
//                        break;
//                    }
//                }
//                //新增附件
//                AttachmentReq attachmentReq = new AttachmentReq();
//                attachmentReq.setObjectName(filePath.substring(filePath.lastIndexOf("/") + 1));
//                attachmentReq.setFiles(myReq.getFiles());
//                attachmentReq.setLeftObjectType(EntityUtil.getRevision(newRevision.getObjectType()));
//                attachmentReq.setObjectType(value);
//                attachmentReq.setLeftObject(newRevision.getUid());
//                WorkspaceObjectResp save = SpringUtil.getBean(IDatasetDomainService.class).save(attachmentReq);
//            }
//        }
//
//        BeanUtil.copyPropertiesIgnoreNull(newRevision, resp);
//        resp.setObjectName(newRevision.getObjectName());
//        resp.setLeftObject(myReq.getLeftObject());
//        resp.setLeftObjectType(myReq.getLeftObjectType());
//        return resp;
//    }
    @Override
    default BusinessObjectResp getObject(IdRequest id) {

        BusinessObjectResp object = IItemRevisionDomainService.super.getObject(id);
        ItemRevisionResp itemRevisionResp = (ItemRevisionResp)object;
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        List<FileStorage> fileStorageByIn = itemRevision.getFileStorage(itemRevisionResp.getUid(), EntityUtil.getObjectType());
        List<FileAttrResp> fileAttrResps = fileStorageByIn.stream().map(resp -> {
            FileAttrResp fileAttrResp = new FileAttrResp();
            BeanUtil.copyPropertiesIgnoreNull(resp, fileAttrResp);
            fileAttrResp.setFileType(resp.getType());
            return fileAttrResp;
        }).collect(Collectors.toList());
        itemRevisionResp.setFiles(fileAttrResps);
        return itemRevisionResp;
    }

//    /**
//     *  删除附件
//     * @param storage
//     * @author: 薛锦龙
//     * @time: 2022/5/27
//     * @return: {@link boolean}
//     */
//    @Transactional
//    default boolean deleteAttachment(FileStorage storage){
//        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
//        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
//        Triple triple = Triple.of(WorkspaceObjectEntity.RIGHT_OBJECT, Ops.EQ, storage.getUid());
//        DatasetFileRLEntity datasetFileRL = (DatasetFileRLEntity) EntityUtil.getDynamicQuery("DatasetFileRL", triple).fetchFirst();
//        //执行附件删除操作
//        String objectType = datasetFileRL.getLeftObjectType();
//        String leftObject = datasetFileRL.getLeftObject();
//        MasterRLFactory masterRLFactory = SpringUtil.getBean(MasterRLFactory.class);
//        IncludeRLFactory includeRLFactory = SpringUtil.getBean(IncludeRLFactory.class);
//        // 1：查询要删除的对象
//        WorkspaceObjectEntity workspaceObjectEntity = EntityUtil.getById(objectType, leftObject);
//        if (Objects.isNull(workspaceObjectEntity)) {
//            throw new ServiceException(ErrorCode.E_12);
//        }
//        // 2：校验状态和权限
//        this.verifyAuthority(workspaceObjectEntity, OperatorEnum.Delete);
//        // 3：校验我是否有对父级的操作权限
//        masterRLFactory.create().getParents(leftObject, objectType, null).forEach(entity -> this.verifyAuthority(entity, OperatorEnum.Write));
//        // 4：根据字典查询当前类型被哪些关系引用，去这些引用表中查询左对象是否存在
//        List<String> checkRelations = dictUtil.getCheckAllRLs(objectType);
//        for (String relation : checkRelations) {
//            List<WorkspaceObjectEntity> relationList = EntityUtil.getDynamicEqQuery(relation, Pair.of(RelationEntity.RIGHT_OBJECT, workspaceObjectEntity.getUid())).fetch();
//            if (CollUtil.isEmpty(relationList)) {
//                continue;
//            }
//            relationList.forEach(data -> {
//                WorkspaceObjectEntity first = CollUtil.getFirst(relationList);
//                WorkspaceObjectEntity entity = EntityUtil.getById(first.getObjectType(), first.getUid());
//                if (!(entity instanceof SpecificationRLEntity)){
//                    String msg = "对象" + workspaceObjectEntity.getObjectName() + "被" + entity.getObjectName() + "引用，无法执行删除";
//                    throw new ServiceException(ErrorCode.FORBIDDEN, msg);
//                }
//            });
//        }
//        // 5：删除当前对象
//        workspaceObjectEntity.setDelFlag(Boolean.TRUE);
//        entityManager.merge(workspaceObjectEntity);
//
//        // 6：删除对象的上下关系以及版本
//        List<String> relations = dictUtil.getUpDownAllRLs(objectType);
//        includeRLFactory.create().deleteUpDownAllRls(leftObject, relations);
//        return Boolean.TRUE;
//    }

    /**
     * 删除版本并删除对应的附件关系
     * @param id
     * @author: 薛锦龙
     * @time: 2022/5/27
     * @return: {@link BusinessObjectResp}
     */
    @Transactional
    @Override
    default BusinessObjectResp deleteObject(IdRequest id) {
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        //删除版本表
        WorkspaceObjectResp businessObjectResp = (WorkspaceObjectResp)IItemRevisionDomainService.super.deleteObject(id);
        //先根据版本uid获取组件的uid,再根据组件uid删除LibraryFolderRL
        WorkspaceObjectEntity libraryFolderRL = EntityUtil.getDynamicEqQuery(new LibraryFolderRLEntity().getObjectType(), Pair.of(LibraryFolderRLEntity.RIGHT_OBJECT, businessObjectResp.getLeftObject())).fetchFirst();
        if (ObjectUtil.isEmpty(libraryFolderRL)){
            throw  new ServiceException(ErrorCode.E_10,"数据不存在，请刷新后重试");
        }
        //根据零件uid查询该零件下是否含有非工作中状态的版本
        Item item = SpringUtil.getBean(ItemFactory.class).create();
        List<WorkspaceObjectEntity> allRevisions = item.getAllRevisions(businessObjectResp.getLeftObject(), EntityUtil.getObjectTypeByRevisionType(EntityUtil.getObjectType()));
        boolean b = allRevisions.stream().map(workspaceObjectEntity -> {
            ItemRevisionEntity itemRevisionEntity = (ItemRevisionEntity) workspaceObjectEntity;
            return itemRevisionEntity;
        }).anyMatch(s -> !s.getLifeCycleState().equals(LifeCycleStateEnum.Working.name()) || s.getActive());
        if (!b) {
            //根据零件uid，直接删除版本对应的零件，并修改数量
            WorkspaceObjectEntity entity = EntityUtil.getById(EntityUtil.getObjectTypeByRevisionType(EntityUtil.getObjectType()), businessObjectResp.getLeftObject());
            entity.setDelFlag(true);
            entityManager.merge(entity);
            //修改数量
            LibraryFolderEntity byId = EntityUtil.getById(new LibraryFolderEntity().getObjectType(), libraryFolderRL.getLeftObject());
            byId.setQuantity(byId.getQuantity()-1);
            //删除关系
            libraryFolderRL.setDelFlag(true);
            entityManager.merge(libraryFolderRL);
        }
        return businessObjectResp;
    }

//    @Override
//    default WorkspaceObjectResp upgrade(WorkspaceObjectReq req) {
//        WorkspaceObjectResp resp = IItemRevisionDomainService.super.upgrade(req);
//        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
//        IncludeRL includeRL = SpringUtil.getBean(IncludeRLFactory.class).create();
//        // 获取Master关系
//        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
//        ItemRevisionReq revisionReq = (ItemRevisionReq)req;
//
//        String relation = dictUtil.getRelation(EntityUtil.getObjectTypeByRevisionType(EntityUtil.getObjectType()), EntityUtil.getObjectType());
//        List<WorkspaceObjectEntity> master = EntityUtil.getDynamicQuery(relation, Triple.of(ItemRevisionEntity.LEFT_OBJECT, Ops.EQ, revisionReq.getLeftObject())).fetch();
//        //获取相同版本的版次
//        List<String> ids = master.stream().map(WorkspaceObjectEntity::getRightObject).collect(Collectors.toList());
//        List<Triple<String, Ops,Object>> list = new ArrayList<>();
//        list.add(Triple.of(BusinessObjectEntity.UID,Ops.IN,ids));
//        list.add(Triple.of(ItemRevisionEntity.REVISION_ID,Ops.EQ,revisionReq.getRevisionId()));
//        List<WorkspaceObjectEntity> fetch1 = EntityUtil.getDynamicQuery(EntityUtil.getObjectType(), list).fetch();
//        List<String> collect = fetch1.stream().map(BusinessObjectEntity::getUid).collect(Collectors.toList());
//
//        //获取数据集
//        List<WorkspaceObjectEntity> specificationRL = EntityUtil.getDynamicQuery("SpecificationRL", Triple.of(ItemRevisionEntity.LEFT_OBJECT, Ops.IN, collect)).fetch();
//        //创建版本与附件关系
//        specificationRL.forEach(data -> {
//            RelationEntity relationEntity = (RelationEntity) data;
//            includeRL.saveRelation(resp.getUid(), EntityUtil.getObjectType(), relationEntity.getUid(), relationEntity.getRightObjectType());
//        });
//
//        //判断是否上传了新的数据集
//        QFileStorageEntity fileStorage = QFileStorageEntity.fileStorageEntity;
//        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
//        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
//        //字典获取附件类型
//        Map<String, String> dataSetType = dictUtil.getCodeValueMap("DataSetType");
//        Set<Map.Entry<String, String>> entries = dataSetType.entrySet();
//        if (CollectionUtil.isNotEmpty(revisionReq.getFiles())) {
//            if (StrUtil.isBlank(revisionReq.getFiles().get(0).getUid())) {
//                //获取相同版本系的版次
//                List<WorkspaceObjectEntity> allSequences = itemRevision.getAllSequences(revisionReq.getUid(), EntityUtil.getObjectType());
//                List<String> uids = allSequences.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
//                //获取附件
//                QSpecificationRLEntity specificationRLEntity = QSpecificationRLEntity.specificationRLEntity;
//                BooleanBuilder booleanBuilder = new BooleanBuilder();
//                booleanBuilder.and(specificationRLEntity.delFlag.isFalse());
//                booleanBuilder.and(specificationRLEntity.leftObject.in(uids));
//                List<SpecificationRLEntity> fetch = jpaQueryFactory.selectFrom(specificationRLEntity).where(booleanBuilder).fetch();
//                Map<String, WorkspaceObjectEntity> map = new HashMap<>();
//                String filePath = revisionReq.getFiles().get(0).getFilePath();
//                String name = filePath.substring(filePath.lastIndexOf("/") + 1);
//
//                if (CollectionUtil.isNotEmpty(fetch)){
//                    fetch.forEach(data -> {
//                        map.put(data.getRightObject(), data);
//                    });
//                    List<WorkspaceObjectEntity> workspaceObjectEntities = fetch.stream().map(data -> {
//                        //字典获取表类型
//                        String type = "";
//                        for (Map.Entry<String, String> entry : entries) {
//                            boolean contains = entry.getKey().contains(data.getRightObjectType());
//                            if (contains) {
//                                type = entry.getValue();
//                                break;
//                            }
//                        }
//                        //查找实体类
//                        WorkspaceObjectEntity workspaceObject = EntityUtil.getDynamicQuery(data.getRightObjectType(), Triple.of(BusinessObjectEntity.UID, Ops.EQ, data.getRightObject())).fetchFirst();
//                        return workspaceObject;
//                    }).collect(Collectors.toList());
//
//                    if (CollectionUtil.isNotEmpty(workspaceObjectEntities)) {
//                        //遍历查看是否存在相同名称的附件
//                        workspaceObjectEntities.forEach(data -> {
//                            //判断名称是否相同,相同删除关系
//                            if (data.getObjectName().equals(name)) {
//                                WorkspaceObjectEntity workspaceObject = map.get(data.getUid());
//                                workspaceObject.setDelFlag(true);
//                                entityManager.merge(workspaceObject);
//                            }
//                        });
//                    }
//                }
//
//                //新增附件
//                String value = "";
//                for (Map.Entry<String, String> entry : entries) {
//                    boolean contains = entry.getKey().contains(revisionReq.getFiles().get(0).getFileType());
//                    if (contains) {
//                        value = entry.getValue();
//                        break;
//                    }
//                }
//                AttachmentReq attachmentReq = new AttachmentReq();
//                attachmentReq.setObjectName(filePath.substring(filePath.lastIndexOf("/") + 1));
//                attachmentReq.setFiles(revisionReq.getFiles());
//                attachmentReq.setLeftObjectType(resp.getObjectType());
//                attachmentReq.setObjectType(value);
//                attachmentReq.setLeftObject(resp.getUid());
//                SpringUtil.getBean(IDatasetDomainService.class).save(attachmentReq);
//            }
//        }
//
//        return resp;
//    }
}
