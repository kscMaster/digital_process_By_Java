package com.nancal.service.service;



import cn.hutool.core.collection.CollUtil;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.config.TokenContext;
import com.nancal.common.config.TokenInfo;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.model.entity.*;
import com.nancal.service.bo.Item;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.factory.ItemRevisionFactory;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

public interface IBOMNodeSnapshotDomainService extends IBOMNodeDomainService {

    Logger logger = LoggerFactory.getLogger(BusinessObjectEntity.class);


    default void snapshotBomNode(List<BOMNodeEntity> childList, String snapshotId,Map<String, WorkspaceObjectEntity> lastVersionMap){
        if(CollUtil.isEmpty(childList)){
            return;
        }
        childList.forEach(bomNode->{
            BOMNodeSnapshotReq snapshotBomNodeReq = new BOMNodeSnapshotReq();
            BeanUtil.copyPropertiesIgnoreNull(bomNode,snapshotBomNodeReq);
            snapshotBomNodeReq.setHisBomId(snapshotBomNodeReq.getUid());
            snapshotBomNodeReq.setSnapshotId(snapshotId);
            WorkspaceObjectEntity entity = lastVersionMap.get(snapshotBomNodeReq.getChildItem());
            if(ObjectUtil.isNotNull(entity) && EntityUtil.checkItem(snapshotBomNodeReq.getChildItemType())){
                ItemRevisionEntity  itemRevisionEntity = (ItemRevisionEntity)entity;
                snapshotBomNodeReq.setChildItemRevId(itemRevisionEntity.getRevisionId());
                snapshotBomNodeReq.setChildItemRevision(itemRevisionEntity.getUid());
                snapshotBomNodeReq.setChildItemTypeRevision(itemRevisionEntity.getObjectType());
            }
            IBOMNodeDomainService.super.save(snapshotBomNodeReq);
        });
    }

    @Transactional
    @Retryable(value = ServiceException.class,maxAttempts=3, backoff = @Backoff(delay = 1000L, multiplier = 2))
    @Async
    default void snapshot(FindReq findReq, AppNameEnum appNameEnum,TokenInfo tokenInfo){
        TokenContext.setToken(tokenInfo);
        List<BomSnapshotEntity> bomSnapshotEntityList = listByRevUid(findReq);
        if(CollUtil.isNotEmpty(bomSnapshotEntityList)){
            log.error("快照已存在，json={}", JSONUtil.toJsonStr(findReq));
            return;
        }
        FindReq req = processItem(findReq);
        WorkspaceObjectEntity wEntity = processLastRevision(req);
        BomSnapshotReq bomSnapshotReq = new BomSnapshotReq();
        bomSnapshotReq.setBomView(appNameEnum.name());
        bomSnapshotReq.setRevObjectType(wEntity.getObjectType());
        bomSnapshotReq.setRevUid(wEntity.getUid());
        bomSnapshotReq.setUid(IdGeneratorUtil.generate());
        bomSnapshotReq.setObjectName(wEntity.getObjectName());
        bomSnapshotReq.setItemUid(req.getUid());
        try {
            bomSnapshotReq.setItemId(ReflectUtil.getFieldValue(wEntity, ItemRevisionEntity.ITEM_ID).toString());
            bomSnapshotReq.setRevisionId(ReflectUtil.getFieldValue(wEntity, ItemRevisionEntity.REVISION_ID).toString());
        }catch (Exception e){
            log.error("itemId获取失败，json={}", JSONUtil.toJsonStr(findReq));
        }
        WorkspaceObjectResp save = this.save(bomSnapshotReq);
        List<BOMNodeEntity> childList = new ArrayList<>();
        childList(EntityUtil.getObjectType(BOMNodeEntity.class),req.getUid(), req.getObjectType(), appNameEnum, childList);
        if(CollUtil.isEmpty(childList)){
            return;
        }
        Item item = SpringUtil.getBean(ItemFactory.class).create();
        Map<String, WorkspaceObjectEntity> lastVersionMap = new HashMap<>();
        childList.stream().collect(Collectors.groupingBy(BOMNodeEntity::getChildItemType)).forEach((objectType,entityList)->{
            CollUtil.split(entityList,500).forEach(list->{
                if(EntityUtil.checkItem(objectType)) {
                    Map<String, WorkspaceObjectEntity> lastVersion = item.getLastVersion(list.stream().map(BOMNodeEntity::getChildItem).collect(Collectors.toList()), objectType);
                    if (CollUtil.isNotEmpty(lastVersion)) {
                        lastVersionMap.putAll(lastVersion);
                    }
                }
            });
        });
        snapshotBomNode(childList,save.getUid(),lastVersionMap);
    }

    default WorkspaceObjectEntity processLastRevision(FindReq req){
        WorkspaceObjectEntity entity=null;
        if(EntityUtil.checkItem(req.getObjectType())){
            Class<?> entityClass = EntityUtil.getEntityClass(req.getObjectType());
            if(ItemEntity.class.isAssignableFrom(entityClass)){
                Item item = SpringUtil.getBean(ItemFactory.class).create();
                entity = item.getLastVersion(req.getUid(), req.getObjectType());
            }
        }else{
            entity = EntityUtil.getById(req.getObjectType(), req.getUid());
        }
        if(ObjectUtil.isNull(entity)){
            log.error("数据不存在，objectType={}，uid={}",req.getObjectType(), req.getUid());
            throw new ServiceException(ErrorCode.E_12);
        }
        return entity;
    }

    default FindReq processItem(FindReq req){
        if(EntityUtil.checkItem(req.getObjectType())){
            Class<?> entityClass = EntityUtil.getEntityClass(req.getObjectType());
            if(ItemRevisionEntity.class.isAssignableFrom(entityClass)){
                ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
                WorkspaceObjectEntity entity = itemRevision.getLeftObjectByRightObject(req.getUid(),req.getObjectType());
                if(ObjectUtil.isNull(entity)){
                    log.error("数据不存在，objectType={}，uid={}",req.getObjectType(), req.getUid());
                    throw new ServiceException(ErrorCode.E_12);
                }
                req = new FindReq(entity.getUid(),entity.getObjectType(),req.getDeep());
            }
        }
        return req;
    }

    default List<WorkspaceObjectResp> list(FindReq req,AppNameEnum appNameEnum){
        FindReq findReq = processItem(req);
        //当前快照
        WorkspaceObjectEntity wEntity = processLastRevision(findReq);
        BomSnapshotResp lastRevisionResp = new BomSnapshotResp();
        lastRevisionResp.setBomView(appNameEnum.name());
        lastRevisionResp.setRevObjectType(wEntity.getObjectType());
        lastRevisionResp.setRevUid(wEntity.getUid());
        lastRevisionResp.setUid(IdGeneratorUtil.generate());
        lastRevisionResp.setObjectName(wEntity.getObjectName());
        lastRevisionResp.setItemUid(req.getUid());
        lastRevisionResp.setIsSnapshot(false);
        try {
            lastRevisionResp.setItemId(ReflectUtil.getFieldValue(wEntity, ItemRevisionEntity.ITEM_ID).toString());
            lastRevisionResp.setRevisionId(ReflectUtil.getFieldValue(wEntity, ItemRevisionEntity.REVISION_ID).toString());
        }catch (Exception e){
            log.error("itemId获取失败，json={}", JSONUtil.toJsonStr(findReq));
        }
        lastRevisionResp.setDisplayDate(DateUtil.format(wEntity.getCreationDate(),"yyyy-MM-dd HH:mm:ss"));
        List<WorkspaceObjectResp> respList = new ArrayList<>();
        respList.add(lastRevisionResp);

        Pair<String,Object> pair = Pair.of(BomSnapshotEntity.ITEM_UID,findReq.getUid());
        OrderSpecifier order = new OrderSpecifier(Order.DESC, ExpressionUtils.path(BomSnapshotEntity.class, WorkspaceObjectEntity.CREATION_DATE));
        List<BomSnapshotEntity> list = EntityUtil.getDynamicEqQuery(BomSnapshotEntity.class, pair).orderBy(order).fetch();

        list.stream().forEach(entity->{
            BomSnapshotResp bomSnapshotResp = (BomSnapshotResp)EntityUtil.entityToResp(entity);
            bomSnapshotResp.setIsSnapshot(true);
            bomSnapshotResp.setDisplayDate(DateUtil.format(bomSnapshotResp.getCreationDate(),"yyyy-MM-dd HH:mm:ss"));
            respList.add(bomSnapshotResp);
        });
        return respList;
    }

    default List<BomSnapshotEntity> listByRevUid(FindReq req){
        List<Pair<String,Object>> params = Arrays.asList(
                Pair.of(BomSnapshotEntity.REV_UID,req.getUid()),
                Pair.of(BomSnapshotEntity.REV_OBJECT_TYPE,req.getObjectType())
        );
        List<BomSnapshotEntity> list = EntityUtil.getDynamicEqQuery(BomSnapshotEntity.class, params).fetch();
        return list;
    }

}
