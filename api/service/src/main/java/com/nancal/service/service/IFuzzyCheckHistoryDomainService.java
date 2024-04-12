package com.nancal.service.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.SearchHistoryReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.SearchHistoryEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface IFuzzyCheckHistoryDomainService extends IRelationDomainService{
    /**
     * 查找历史记录
     * @param req
     * @author: 薛锦龙
     * @time: 2022/9/15
     * @return: {@link List <  WorkspaceObjectEntity >}
     */
    default List<WorkspaceObjectResp> findHistory(SearchHistoryReq req){
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        String objectType = new SearchHistoryEntity().getObjectType();
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        int size = 20;//默认20条
        int page = 0;
        Map<String, String> nameByDictCode = dictUtil.getNameByDictCode(objectType + StrUtil.UNDERLINE + "Quantity");
        if (nameByDictCode.containsKey("quantity")){
            String quantity = nameByDictCode.get("quantity");
            size = Integer.parseInt(quantity);
        }
        //字典获取条数
        OrderSpecifier orderSpecifier = new OrderSpecifier(Order.DESC,
                ExpressionUtils.path(EntityUtil.getEntityClass(objectType), WorkspaceObjectEntity.CREATION_DATE));
        List<Triple<String, Ops,Object>> triples = Arrays.asList(
                Triple.of(SearchHistoryEntity.APP_CODE,Ops.EQ,req.getAppCode()),
                Triple.of(SearchHistoryEntity.CONDITION_OBJECT_TYPE,Ops.EQ,req.getConditionObjectType()),
                Triple.of(SearchHistoryEntity.OWNER_ID,Ops.EQ,userUtils.getCurrentUserId())
        );
        JPAQuery<WorkspaceObjectEntity> dynamicQuery = EntityUtil.getDynamicQuery(objectType, triples);
        List<WorkspaceObjectEntity> entityList = dynamicQuery.offset(page).limit(size).orderBy(orderSpecifier).fetch();
        if(CollUtil.isEmpty(entityList)){
            return CollUtil.newArrayList();
        }
        List<WorkspaceObjectResp> resps = StreamEx.of(entityList).map(data -> {
            WorkspaceObjectResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(data.getObjectType()));
            BeanUtil.copyPropertiesIgnoreNull(data, resp);
            return resp;
        }).toList();
        return resps;
    }


}
