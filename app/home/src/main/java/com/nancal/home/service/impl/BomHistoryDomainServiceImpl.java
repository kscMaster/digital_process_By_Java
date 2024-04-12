package com.nancal.home.service.impl;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.BomHistoryReq;
import com.nancal.api.model.BusinessObjectReq;
import com.nancal.api.model.ItemRevisionResp;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.PageHelper;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.SqlUtil;
import com.nancal.model.entity.BomHistoryEntity;
import com.nancal.model.entity.QBomHistoryEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.service.IBomHistoryDomainService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Ops;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BomHistoryDomainServiceImpl implements IBomHistoryDomainService {

    @Autowired
    private UserUtils userUtils;

    @Override
    @Transactional
    public WorkspaceObjectResp saveBomHistory(BusinessObjectReq req) {
        //防止重复添加数据（删除后重新添加）
        BomHistoryReq bomHistoryReq = (BomHistoryReq) req;
        List<Triple<String, Ops, Object>> paramList = Arrays.asList(
                Triple.of(BomHistoryEntity.DEL_FLAG, Ops.EQ, false),
                Triple.of(BomHistoryEntity.BOM_VIEW, Ops.EQ, bomHistoryReq.getBomView()),
                Triple.of(BomHistoryEntity.REV_UID, Ops.EQ, bomHistoryReq.getRevUid()),
                Triple.of(BomHistoryEntity.OWNER_ID, Ops.EQ, userUtils.getCurrentUserId())
        );
        List<WorkspaceObjectEntity> fetch = EntityUtil.getDynamicQuery(new BomHistoryEntity().getObjectType(), paramList).fetch();
        if(fetch.size()!=0){
            fetch.forEach(data->{
                IBomHistoryDomainService.super.deleteObject(IdRequest.builder().uid(data.getUid()).objectType(data.getObjectType()).build());
            });
        }
        return IBomHistoryDomainService.super.save(bomHistoryReq);
    }


    @Override
    public TableResponse<WorkspaceObjectResp> getByLike(TableRequest<BomHistoryReq> req){
        PageRequest page = PageHelper.ofReq(req);
        QBomHistoryEntity entity = QBomHistoryEntity.bomHistoryEntity;
        BooleanBuilder where = new BooleanBuilder();
        where.and(entity.delFlag.isFalse());
        where.and(entity.bomView.eq(req.getData().getBomView()));
        where.and(entity.ownerId.eq(userUtils.getCurrentUserId()));
        //动态查询条件
        if (!StringUtils.isEmpty(req.getData().getObjectName())) {
            where.and(entity.objectName.like(SqlUtil.like(req.getData().getObjectName())).or(entity.itemId.like(SqlUtil.like(req.getData().getObjectName()))));
        }
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        //查询sql 根据更新时间倒序
        QueryResults<BomHistoryEntity> results = jpaQueryFactory.selectFrom(entity).where(where).orderBy(entity.lastUpdate.desc())
                .offset(page.getOffset()).limit(page.getPageSize()).fetchResults();
        //总数量
        if (results.getTotal() <= 0) {
            return TableResponse.<WorkspaceObjectResp>builder().build();
        }
        List<WorkspaceObjectResp> data = historyToLastVersion(results.getResults());
        return TableResponse.<WorkspaceObjectResp>builder()
                .total(results.getTotal())
                .data(data)
                .build();
    }


}