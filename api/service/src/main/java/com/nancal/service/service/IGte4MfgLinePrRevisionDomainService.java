package com.nancal.service.service;


import java.io.DataInput;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Collections;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.nancal.api.model.Gte4WorklineRevisionLikeReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.model.entity.*;
import com.nancal.service.bo.Item;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.factory.ItemRevisionFactory;
import com.querydsl.core.types.Ops;
import java.util.stream.Collectors;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import cn.hutool.core.util.ReflectUtil;
import javax.persistence.EntityManager;
import cn.hutool.extra.spring.SpringUtil;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Triple;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;


import com.nancal.api.model.Gte4MfgLinePrRevisionReq;
import com.nancal.api.model.Gte4MfgLinePrRevisionResp;

public interface IGte4MfgLinePrRevisionDomainService extends IItemRevisionDomainService {
//    //TODO 模糊查询工线
//    default List<WorkspaceObjectResp> getLine(Gte4WorklineRevisionLikeReq req){
//        IBOMNodeDomainService ibomNodeDomainService = SpringUtil.getBean(IBOMNodeDomainService.class);
//        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
//        Item item = SpringUtil.getBean(ItemFactory.class).create();
//        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
//        String plantType = new Gte4PlantRevisionEntity().getObjectType();
//        String plantPrType = new Gte4MfgPlantPrEntity().getObjectType();
//        String objectType = new BOMNodeEntity().getObjectType();
//        String workLineType = new Gte4WorklineEntity().getObjectType();
//        List<WorkspaceObjectEntity> entities = ibomNodeDomainService.existParent(req.getLeftObject(), objectType, false);
//        if (CollUtil.isNotEmpty(entities)){
//            //获取顶层的数据，添加查询条件
//            List<String> itemUids = entities.stream().map(BOMNodeEntity.class::cast).filter(data -> data.getParentItemType().equals(plantPrType)).
//                    map(BOMNodeEntity::getParentItem).collect(Collectors.toList());
//            req.setUid(itemUids);
//            List<WorkspaceObjectEntity> allRevisions = item.getAllRevisions(itemUids, EntityUtil.getRevision(plantPrType));
//            List<String> revisionUids = allRevisions.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
//            //找到关联的所有工厂
//            String relation = dictUtil.getRelation(EntityUtil.getRevision(plantPrType), plantType);
//            List<WorkspaceObjectEntity> relationEntity = EntityUtil.getDynamicQuery(relation, Triple.of(RelationEntity.LEFT_OBJECT, Ops.IN, revisionUids)).fetch();
//            List<String> plantUids = relationEntity.stream().map(RelationEntity.class::cast).map(RelationEntity::getRightObject).collect(Collectors.toList());
//            //获取左对象
//            List<WorkspaceObjectEntity> leftObjects = itemRevision.getLeftObjects(plantUids,plantType);
//            List<BOMNodeEntity> lineEntity = new ArrayList<>();
//            if (CollUtil.isNotEmpty(leftObjects)){
//                List<String> stringList = leftObjects.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
//                child(stringList,lineEntity,objectType,workLineType);
//            }
//            if (CollUtil.isNotEmpty(lineEntity)){
//                List<String> stringList = lineEntity.stream().map(BOMNodeEntity::getChildItem).collect(Collectors.toList());
//                item.getLastVersion(stringList,)
//            }
//        }
//        return null;
//    }
//
//    default void child(List<String> leftObjects,List<BOMNodeEntity> lineEntity,String type,String workLineType){
//        List<WorkspaceObjectEntity> workspaceObjectEntities = EntityUtil.getDynamicQuery(type, Triple.of(BOMNodeEntity.PARENT_ITEM, Ops.IN, leftObjects)).fetch();
//        if (CollUtil.isEmpty(workspaceObjectEntities)){
//            return;
//        }
//        List<String> uids = new ArrayList<>();
//        workspaceObjectEntities.stream().map(BOMNodeEntity.class::cast).forEach(data->{
//            if (workLineType.equals(data.getChildItemType())&&data.getChildItemType()!=null){
//                lineEntity.add(data);
//            }else if (StrUtil.isNotBlank(data.getChildItem())){
//                uids.add(data.getChildItem());
//            }
//        });
//        if (CollUtil.isNotEmpty(uids)){
//            child(uids,lineEntity,type,workLineType);
//        }
//    }

}
