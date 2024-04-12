package com.nancal.service.service;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.BomHistoryEntity;
import com.nancal.model.entity.ItemEntity;
import com.nancal.model.entity.ItemRevisionEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.bo.Item;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.factory.ItemRevisionFactory;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 此处存在一些问题
 * 1、bom历史记录，保存版本，还是零组件；当前保存的是版本
 * 2、显示版本记录列表；当前显示的是最新的版本数据
 * 3、删除最新的版本记录；删除所有的版本
 *
 * 存在的问题：精确bom与非精确bom的区分
 *
 */
public interface IBomHistoryDomainService extends IWorkspaceObjectDomainService {
    default List<WorkspaceObjectResp> findBomHistory(BomHistoryReq req){
        //1.先根据字典配置获取要展示的条数值
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        String objectType = new BomHistoryEntity().getObjectType();
        Map<String, String> nameByDictCode = dictUtil.getNameByDictCode(objectType + StrUtil.UNDERLINE + "Quantity");
        Long historyNumber = Long.valueOf(nameByDictCode.get("quantity"));
        //2.再根据用户id和BOM视图类型查询出数据返回
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        List<Triple<String, Ops, Object>> paramList = Arrays.asList(
                Triple.of(BomHistoryEntity.DEL_FLAG, Ops.EQ, false),
                Triple.of(BomHistoryEntity.BOM_VIEW, Ops.EQ, req.getBomView()),
                Triple.of(BomHistoryEntity.OWNER_ID, Ops.EQ, userUtils.getCurrentUserId())
        );
        OrderSpecifier order = new OrderSpecifier(Order.DESC, ExpressionUtils.path(BomHistoryEntity.class, BomHistoryEntity.CREATION_DATE));
        List<WorkspaceObjectEntity> bomHistoryList = EntityUtil.getDynamicQuery(new BomHistoryEntity().getObjectType(), paramList).limit(historyNumber).orderBy(order).fetch();
        return historyToLastVersion(bomHistoryList);
    }


    /**
     * 显示当前的最新状态，及对应的名称
     * @param bomHistoryList
     * @param <T>
     * @return
     */
    default <T extends WorkspaceObjectEntity> List<WorkspaceObjectResp> historyToLastVersion(List<T> bomHistoryList){
        if(CollUtil.isEmpty(bomHistoryList)){
            return CollUtil.newArrayList();
        }
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        Item item = SpringUtil.getBean(ItemFactory.class).create();
        List<WorkspaceObjectEntity> versionList = new ArrayList<>();
        bomHistoryList.stream().map(BomHistoryEntity.class::cast).collect(Collectors.groupingBy(BomHistoryEntity::getRevObjectType)).
                forEach((revObjectType,values)-> {
                    Map<String, WorkspaceObjectEntity> leftObjectMap = itemRevision.getLeftObjectMap(values.stream()
                            .map(his -> his.getRevUid()).collect(Collectors.toList()), revObjectType);
                    if(CollUtil.isNotEmpty(leftObjectMap)){
                        Map<String, WorkspaceObjectEntity> lastVersionList = item.getLastVersion(leftObjectMap.values().
                                stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList()), EntityUtil.getObjectTypeByRevisionType(revObjectType));
                        if(CollUtil.isNotEmpty(lastVersionList)){
                            versionList.addAll(lastVersionList.values());
                        }
                    }
                });
        List<WorkspaceObjectResp> respList = new ArrayList<>();
        versionList.stream().forEach(entity->{
            WorkspaceObjectResp workspaceObjectResp = EntityUtil.entityToResp(entity);
            workspaceObjectResp.putExtraProperty("revUid",workspaceObjectResp.getUid());
            workspaceObjectResp.putExtraProperty("revObjectType",workspaceObjectResp.getObjectType());
            respList.add(workspaceObjectResp);
        });
        return respList.stream().sorted(Comparator.comparing(WorkspaceObjectResp::getCreationDate).reversed()).collect(Collectors.toList());
    }

    default TableResponse<WorkspaceObjectResp> getByLike(TableRequest<BomHistoryReq> req){
        return null;}

    default WorkspaceObjectResp saveBomHistory(BusinessObjectReq req) {
        return null;}

    /**
     * 临时解决方案，存在一些问题，查看类前标记的疑问
     * @param req
     */
    @Transactional
    default void deleteBomHistory(IdRequest req){
        WorkspaceObjectEntity entity = EntityUtil.getById(req.getObjectType(), req.getUid());
        String itemId = "";
        if(EntityUtil.checkItem(req.getObjectType())){
            if(ItemRevisionEntity.class.isAssignableFrom(EntityUtil.getEntityClass(req.getObjectType()))){
                itemId = ((ItemRevisionEntity)entity).getItemId();
            }else{
                itemId =((ItemEntity)entity).getItemId();
            }
        }
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        if(StrUtil.isEmpty(itemId)){
            this.deleteObject(req);
        }else{
            List<Pair<String,Object>> paramList = Arrays.asList(
                    Pair.of(BomHistoryEntity.REV_OBJECT_TYPE, req.getObjectType()),
                    Pair.of(BomHistoryEntity.ITEM_ID, itemId),
                    Pair.of(BomHistoryEntity.OWNER_ID, userUtils.getCurrentUserId())
            );
            List<BomHistoryEntity> list = EntityUtil.getDynamicEqQuery(BomHistoryEntity.class, paramList).fetch();
            if(CollUtil.isNotEmpty(list)){
                list.forEach(bomHistoryEntity -> {
                    bomHistoryEntity.setDelFlag(true);
                    SpringUtil.getBean(EntityManager.class).merge(entity);
                });
            }
        }
    }
}
