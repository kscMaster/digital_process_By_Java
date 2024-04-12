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
import com.nancal.model.entity.*;
import com.nancal.service.service.IGte4NoticeOrgEntryDomainService;
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
public class Gte4NoticeOrgEntryDomainServiceImpl implements IGte4NoticeOrgEntryDomainService {


    @Override
    public TableResponse<WorkspaceObjectResp> getList(TableRequest<IdRequest> req) {
        PageRequest pageReq = PageHelper.ofReq(req);
        //先根据更改后uid查询出相关联的所有的对象uid
        List<Triple<String, Ops, Object>> paramsList = Arrays.asList(Triple.of(Gte4NoticeOrgRLEntity.LEFT_OBJECT, Ops.EQ, req.getData().getUid()));
        List<WorkspaceObjectEntity> noticeRlList = EntityUtil.getDynamicQuery(new Gte4NoticeOrgRLEntity().getObjectType(), paramsList).fetch();
        if (noticeRlList.size()==0) {
            return TableResponse.<WorkspaceObjectResp>builder().data(CollUtil.newArrayList()).build();
        }
        List<String> rightList = noticeRlList.stream().map(data -> {
            Gte4NoticeOrgRLEntity noticeOrgRL = (Gte4NoticeOrgRLEntity) data;
            return noticeOrgRL.getRightObject();
        }).collect(Collectors.toList());
        //再根据通知对象uid查询发放对象集合
        List<Triple<String, Ops, Object>> paramsList2 = Arrays.asList(Triple.of(Gte4NoticeOrgEntryEntity.UID, Ops.IN, rightList));
        OrderSpecifier order = new OrderSpecifier(Order.DESC, ExpressionUtils.path(Gte4NoticeOrgEntryEntity.class, Gte4PartChangeEntity.CREATION_DATE));
        QueryResults<WorkspaceObjectEntity> queryResults = EntityUtil.getDynamicQuery(new Gte4NoticeOrgEntryEntity().getObjectType(), paramsList2)
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
