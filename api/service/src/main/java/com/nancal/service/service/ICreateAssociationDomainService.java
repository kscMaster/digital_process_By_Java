package com.nancal.service.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.DeleteAssociationReq;
import com.nancal.api.model.RelatedProcessReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.OperatorEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.*;
import com.nancal.service.bo.Item;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.bo.Relation;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.factory.RelationFactory;
import com.querydsl.core.types.Ops;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface ICreateAssociationDomainService extends IWorkspaceObjectDomainService{
    /**
     * 获取已关联的线体或工厂
     * @param id
     * @author: 薛锦龙
     * @time: 2022/8/31
     * @return: {@link List <  WorkspaceObjectResp >}
     */
    default List<WorkspaceObjectResp> linked(IdRequest id){
        String plantType = new Gte4PlantRevisionEntity().getObjectType();
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        String workLineType = new Gte4WorklineRevisionEntity().getObjectType();
        String plantPrType = new Gte4MfgPlantPrRevisionEntity().getObjectType();
        String linePrType = new Gte4MfgLinePrRevisionEntity().getObjectType();
        Item item = SpringUtil.getBean(ItemFactory.class).create();
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        String type = null;
        List<WorkspaceObjectEntity> allRevisions = itemRevision.getAllSequences(id.getUid(), id.getObjectType());
        List<String> revisionUids = allRevisions.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
        if (id.getObjectType().equals(plantPrType)){
            type = plantType;
        }else if(id.getObjectType().equals(linePrType)){
            type = workLineType;
        }else {
            throw new ServiceException(ErrorCode.E_10,"数据类型匹配错误");
        }
        String relation = dictUtil.getRelation(id.getObjectType(), type);
        List<WorkspaceObjectEntity> relationEntity = EntityUtil.getDynamicQuery(relation, Triple.of(RelationEntity.LEFT_OBJECT, Ops.IN, revisionUids)).fetch();
        if (CollUtil.isEmpty(relationEntity)){
            return CollUtil.newArrayList();
        }
        List<RelationEntity> entities = relationEntity.stream().map(RelationEntity.class::cast).collect(Collectors.toList());
        entities = StreamEx.of(entities).distinct(RelationEntity::getRightObject).collect(Collectors.toList());
        List<WorkspaceObjectEntity> workspaceObjectEntities = new ArrayList<>();
        entities.forEach(data->{
            WorkspaceObjectEntity activeRevision = EntityUtil.getById(data.getRightObjectType(),data.getRightObject());
            workspaceObjectEntities.add(activeRevision);
        });
        List<WorkspaceObjectResp> resps = new ArrayList<>();
        workspaceObjectEntities.forEach(data->{
            WorkspaceObjectResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(data.getObjectType()));
            BeanUtil.copyPropertiesIgnoreNull(data,resp);
            resps.add(resp);
        });
        return resps;
    }

    /**
     * 关联工厂或线体
     * @param req
     * @author: 薛锦龙
     * @time: 2022/9/1
     * @return: {@link boolean}
     */
    @Transactional
    default boolean associativeTop(RelatedProcessReq req){
        //TODO 校验数据
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        Relation relationFactory = SpringUtil.getBean(RelationFactory.class).create();
        WorkspaceObjectEntity activeRevision = itemRevision.getActiveRevision(req.getUid(), req.getObjectType());
        IWorkspaceObjectDomainService.super.verifyAuthority(activeRevision, OperatorEnum.Write);
        req.getFactoryOrLine().forEach(data->{
            dictUtil.getRelation(activeRevision.getObjectType(), data.getObjectType());
        });
        //获取所有版次
        List<WorkspaceObjectEntity> allSequences = itemRevision.getAllSequences(activeRevision.getUid(), activeRevision.getObjectType());
        List<String> relationUids = allSequences.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
        String relation = dictUtil.getRelation(activeRevision.getObjectType(), req.getFactoryOrLine().get(0).getObjectType());
        List<WorkspaceObjectEntity> relations = EntityUtil.getDynamicQuery(relation, Triple.of(RelationEntity.LEFT_OBJECT, Ops.IN, relationUids)).fetch();
        Map<String, RelationEntity> relationMap = relations.stream().map(RelationEntity.class::cast).collect(Collectors.toMap(RelationEntity::getRightObject, Function.identity()));
        req.getFactoryOrLine().forEach(data->{
            if (relationMap.containsKey(data.getUid())){
                return;
            }
            relationFactory.saveRelation(activeRevision.getUid(),activeRevision.getObjectType(),data.getUid(),data.getObjectType());
        });
        return true;
    }

    /**
     * 移除关联关系
     * @param
     * @author: 薛锦龙
     * @time: 2022/9/1
     * @return: {@link boolean}
     */
    @Transactional
    default boolean deleteAssociation(DeleteAssociationReq req){
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        List<WorkspaceObjectEntity> allSequences = itemRevision.getAllSequences(req.getLeftObject(), req.getLeftObjectType());
        if (CollUtil.isEmpty(allSequences)){
            throw new ServiceException(ErrorCode.E_10,"数据不存在");
        }
        List<WorkspaceObjectEntity> objectEntities = new ArrayList<>();
        List<String> leftUids = allSequences.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
        req.getUids().forEach(data->{
            String relation = dictUtil.getRelation(req.getLeftObjectType(), data.getObjectType());
            List<Triple<String,Ops,Object>> triples = Arrays.asList(
                    Triple.of(RelationEntity.LEFT_OBJECT,Ops.IN,leftUids),
                    Triple.of(RelationEntity.RIGHT_OBJECT,Ops.EQ,data.getUid()),
                    Triple.of(RelationEntity.RIGHT_OBJECT_TYPE,Ops.EQ,data.getObjectType())
            );
            objectEntities.addAll(EntityUtil.getDynamicQuery(relation, triples).fetch());
        });
        objectEntities.forEach(data->{
            data.setDelFlag(true);
            entityManager.merge(data);
        });
        return true;
    }

    /**
     * 工艺规划预览板子
     * @param id
     * @author: 薛锦龙
     * @time: 2022/9/1
     * @return: {@link WorkspaceObjectResp}
     */
   default WorkspaceObjectResp previewView2(IdRequest id){
       String objectType = new Gte4PartRevisionEntity().getObjectType();
       DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
       ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
       List<WorkspaceObjectEntity> allSequences = itemRevision.getAllSequences(id.getUid(), id.getObjectType());
       List<String> stringList = allSequences.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
       //查找产品
       String relation = dictUtil.getRelation(id.getObjectType(), objectType);
       WorkspaceObjectEntity entities = EntityUtil.getDynamicQuery(relation, Triple.of(RelationEntity.LEFT_OBJECT, Ops.IN, stringList)).fetchFirst();
       if (ObjectUtil.isEmpty(entities)){
           return null;
       }
       RelationEntity entity = (RelationEntity)entities;
       WorkspaceObjectEntity activeRevision = itemRevision.getActiveRevision(entity.getRightObject(), entity.getRightObjectType());
       WorkspaceObjectResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(activeRevision.getObjectType()));
       BeanUtil.copyPropertiesIgnoreNull(activeRevision,resp);
       return resp;
   }


    /**
     * 工艺规划预览板子
     * @param id
     * @author: 薛锦龙
     * @time: 2022/9/1
     * @return: {@link WorkspaceObjectResp}
     */
//    default WorkspaceObjectResp previewView1(IdRequest id){
//        String objectType = new Gte4PartRevisionEntity().getObjectType();
//        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
//        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
//        List<WorkspaceObjectEntity> allSequences = itemRevision.getAllSequences(id.getUid(), id.getObjectType());
//        List<String> stringList = allSequences.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
//        //查找产品
//        String relation = dictUtil.getRelation(id.getObjectType(), objectType);
//        //查找关联的MBOM
//        WorkspaceObjectEntity entities = EntityUtil.getDynamicQuery(relation, Triple.of(RelationEntity.LEFT_OBJECT, Ops.IN, stringList)).fetchFirst();
//        if (ObjectUtil.isEmpty(entities)){
//            return null;
//        }
//        RelationEntity entity = (RelationEntity)entities;
//        List<WorkspaceObjectEntity> sequences = itemRevision.getAllSequences(entity.getLeftObject(),entity.getLeftObjectType());
//        List<String> listUids = StreamEx.of(sequences).map(WorkspaceObjectEntity::getUid).toList();
//        String relationship = dictUtil.getRelation(sequences.get(0).getObjectType(), objectType);
//        WorkspaceObjectEntity workspaceObject = EntityUtil.getDynamicQuery(relationship, Triple.of(RelationEntity.LEFT_OBJECT, Ops.IN, listUids)).fetchFirst();
//        if (ObjectUtil.isEmpty(workspaceObject)){
//            return null;
//        }
//        RelationEntity relationEntity = (RelationEntity)workspaceObject;
//        WorkspaceObjectEntity activeRevision = itemRevision.getActiveRevision(relationEntity.getRightObject(), relationEntity.getRightObjectType());
//        WorkspaceObjectResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(activeRevision.getObjectType()));
//        BeanUtil.copyPropertiesIgnoreNull(activeRevision,resp);
//        return resp;
//    }



}
