package com.nancal.midbom.service;


import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.ItemRevisionResp;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.api.model.dataset.FileAttrResp;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.LifeCycleStateEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.ItemRevisionEntity;
import com.nancal.model.entity.LibraryFolderEntity;
import com.nancal.model.entity.LibraryFolderRLEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.bo.FileStorage;
import com.nancal.service.bo.Item;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.service.IItemRevisionDomainService;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface IItemRevisionDomainServiceAdaptor extends IItemRevisionDomainService {

    @Override
    default BusinessObjectResp getObject(IdRequest id) {
        BusinessObjectResp object = IItemRevisionDomainService.super.getObject(id);
        ItemRevisionResp itemRevisionResp = (ItemRevisionResp)object;
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        String objectType = Objects.isNull(id.getObjectType()) ? EntityUtil.getObjectType() : id.getObjectType();
        List<FileStorage> fileStorageByIn = itemRevision.getFileStorage(itemRevisionResp.getUid(),objectType);
        List<FileAttrResp> fileAttrResps = fileStorageByIn.stream().map(resp -> {
            FileAttrResp fileAttrResp = new FileAttrResp();
            BeanUtil.copyPropertiesIgnoreNull(resp, fileAttrResp);
            fileAttrResp.setFileType(resp.getType());
            return fileAttrResp;
        }).collect(Collectors.toList());
        itemRevisionResp.setFiles(fileAttrResps);
        return itemRevisionResp;
    }
//
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
     * 删除版本
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
        //根据零件uid查询该零件下是否含有工作中状态的版本
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



}
