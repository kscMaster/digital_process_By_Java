package com.nancal.changeorder.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.PageHelper;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.Gte4DistOrgEntryEntity;
import com.nancal.model.entity.Gte4DistOrgRLEntity;
import com.nancal.model.entity.Gte4PartChangeEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.service.IGte4DistOrgEntryDomainService;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Gte4DistOrgEntryDomainServiceImpl implements IGte4DistOrgEntryDomainService {

    @Override
    public TableResponse<WorkspaceObjectResp> getList(TableRequest<IdRequest> req) {
        PageRequest pageReq = PageHelper.ofReq(req);
        //先根据更改后uid查询出相关联的所有的发放对象uid
        List<Triple<String, Ops, Object>> paramsList = Arrays.asList(Triple.of(Gte4DistOrgRLEntity.LEFT_OBJECT, Ops.EQ, req.getData().getUid()));
        List<WorkspaceObjectEntity> distRlList = EntityUtil.getDynamicQuery(new Gte4DistOrgRLEntity().getObjectType(), paramsList).fetch();
        if (distRlList.size()==0) {
            return TableResponse.<WorkspaceObjectResp>builder().data(CollUtil.newArrayList()).build();
        }
        List<String> rightList = distRlList.stream().map(data -> {
            Gte4DistOrgRLEntity distOrgRL = (Gte4DistOrgRLEntity) data;
            return distOrgRL.getRightObject();
        }).collect(Collectors.toList());
        //再根据发放对象uid查询发放对象集合
        List<Triple<String, Ops, Object>> paramsList2 = Arrays.asList(Triple.of(Gte4DistOrgEntryEntity.UID, Ops.IN, rightList));
        OrderSpecifier order = new OrderSpecifier(Order.DESC, ExpressionUtils.path(Gte4DistOrgEntryEntity.class, Gte4PartChangeEntity.CREATION_DATE));
        QueryResults<WorkspaceObjectEntity> queryResults = EntityUtil.getDynamicQuery(new Gte4DistOrgEntryEntity().getObjectType(), paramsList2)
                .offset(pageReq.getOffset()).limit(pageReq.getPageSize()).orderBy(order).fetchResults();
        if (queryResults.getTotal()<=0) {
            return TableResponse.<WorkspaceObjectResp>builder().data(CollUtil.newArrayList()).build();
        }
        List<WorkspaceObjectResp> collect = queryResults.getResults().stream().map(data -> {
            WorkspaceObjectResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(data.getObjectType()));
            BeanUtil.copyPropertiesIgnoreNull(data, resp);
            return resp;
        }).collect(Collectors.toList());
        return TableResponse.<WorkspaceObjectResp>builder()
                .total(queryResults.getTotal())
                .data(collect)
                .build();
    }

}
