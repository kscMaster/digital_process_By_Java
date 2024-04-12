package com.nancal.changeorder.service.impl;

import cn.hutool.core.util.ReflectUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.PageHelper;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.OperatorEnum;
import com.nancal.common.exception.TipServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.MessageSourceUtil;
import com.nancal.common.utils.SqlUtil;
import com.nancal.model.entity.*;
import com.nancal.service.service.IGte4PartChangeDomainService;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class Gte4PartChangeDomainServiceImpl implements IGte4PartChangeDomainService {


    @Override
    @Transactional
    public WorkspaceObjectResp create(Gte4PartChangeReq req) {
        checkChangeIdOnly(req);
        return IGte4PartChangeDomainService.super.save(req);
    }

    @Override
    @Transactional
    public BusinessObjectResp modify(Gte4PartChangeReq req) {
        checkChangeIdOnly(req);
        return IGte4PartChangeDomainService.super.update(req);
    }

    /**  校验更改单号唯一（过滤掉本身）
     *
     * @param req
     */
    public void checkChangeIdOnly(Gte4PartChangeReq req) {
        List<Triple<String, Ops, Object>> paramsList = Arrays.asList(Triple.of(Gte4PartChangeEntity.CHANGE_ID, Ops.EQ, req.getChangeId()));
        List<WorkspaceObjectEntity> fetch = EntityUtil.getDynamicQuery(new Gte4PartChangeEntity().getObjectType(), paramsList).fetch();
        List<WorkspaceObjectEntity> collect = fetch.stream().filter(data -> !(data.getUid().equals(req.getUid()))).collect(Collectors.toList());
        if (collect.size()!=0) {
            throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("changeIdExist", req.getChangeId()));
        }
    }

    @Override
    public TableResponse<WorkspaceObjectResp> find(TableRequest<Gte4PartChangeReq> req) {
        PageRequest pageReq = PageHelper.ofReq(req);
        List<Triple<String, Ops, Object>> paramsList;
        if (Objects.isNull(req.getData())) {
             paramsList = Arrays.asList();
        }else {
            paramsList = new ArrayList<>();
            if (!Objects.isNull(req.getData().getChangeId())) {
                paramsList.add(Triple.of(Gte4PartChangeEntity.CHANGE_ID, Ops.LIKE, SqlUtil.like(req.getData().getChangeId())));
            }
            if (!Objects.isNull(req.getData().getObjectName())) {
                paramsList.add(Triple.of(Gte4PartChangeEntity.OBJECT_NAME, Ops.LIKE, SqlUtil.like(req.getData().getObjectName())));
            }
            if (!Objects.isNull(req.getData().getGte4ChangeType())) {
                paramsList.add(Triple.of(Gte4PartChangeEntity.GTE4CHANGE_TYPE, Ops.EQ, req.getData().getGte4ChangeType()));
            }
            if (!Objects.isNull(req.getData().getLifeCycleState())) {
                paramsList.add(Triple.of(Gte4PartChangeEntity.LIFE_CYCLE_STATE, Ops.EQ, req.getData().getLifeCycleState()));
            }
        }
        OrderSpecifier order = new OrderSpecifier(Order.DESC, ExpressionUtils.path(Gte4PartChangeEntity.class, Gte4PartChangeEntity.CREATION_DATE));
        QueryResults<WorkspaceObjectEntity> queryResults = EntityUtil.getDynamicQuery(new Gte4PartChangeEntity().getObjectType(), paramsList)
                .offset(pageReq.getOffset()).limit(pageReq.getPageSize()).orderBy(order).fetchResults();
        if (queryResults.getTotal()<=0) {
            return TableResponse.<WorkspaceObjectResp>builder().total(0).data(Collections.emptyList()).build();
        }
        List<WorkspaceObjectResp> collect = queryResults.getResults().stream().map(data -> {
            WorkspaceObjectResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(data.getObjectType()));
            BeanUtil.copyPropertiesIgnoreNull(data, resp);
            return resp;
        }).collect(Collectors.toList());
        return TableResponse.<WorkspaceObjectResp>builder().total(queryResults.getTotal()).data(collect).build();
    }

    @Override
    public ChangeOrderResp getChangeOrder(IdRequest id) {
        ChangeOrderResp resp = new ChangeOrderResp();
        //先查询更改单后信息
        ChangeAfterRLEntity afterRL = EntityUtil.getById(id.getObjectType(), id.getUid());
        ChangeAfterRLResp afterRLResp = new ChangeAfterRLResp();
        BeanUtil.copyPropertiesIgnoreNull(afterRL,afterRLResp);
        resp.setChangeAfterRLResp(afterRLResp);

        //再查询通知单位列表信息         gte4notice_orgrl
        String noticeLeftObject = Gte4NoticeOrgRLEntity.LEFT_OBJECT;
        String noticeObjectType = new Gte4NoticeOrgRLEntity().getObjectType();
        List<WorkspaceObjectEntity> noticeResults = getRelationList(noticeLeftObject, id,noticeObjectType);
        //先收集所有右id
        List<String> noticeIds = noticeResults.stream().map(data -> {
            Gte4NoticeOrgRLEntity noticeOrgRL = (Gte4NoticeOrgRLEntity) data;
            return noticeOrgRL.getRightObject();
        }).collect(Collectors.toList());
        //根据右id获取右对象集合  gte4notice_org_entry
        List<Gte4NoticeOrgEntryResp> noticeResps = new ArrayList<>();
        noticeIds.forEach(data->{
            Gte4NoticeOrgEntryEntity noticeOrgEntry = EntityUtil.getById(new Gte4NoticeOrgEntryEntity().getObjectType(), data);
            if (!Objects.isNull(noticeOrgEntry)) {
                Gte4NoticeOrgEntryResp noticeOrgEntryResp = new Gte4NoticeOrgEntryResp();
                BeanUtil.copyPropertiesIgnoreNull(noticeOrgEntry,noticeOrgEntryResp);
                noticeResps.add(noticeOrgEntryResp);
            }
        });
        resp.setNoticeListResp(noticeResps);

        //再查询发放单位列表信息  gte4dist_orgrl
        String distLeftObject = Gte4DistOrgRLEntity.LEFT_OBJECT;
        String distObjectType = new Gte4DistOrgRLEntity().getObjectType();
        List<WorkspaceObjectEntity> distResults = getRelationList(distLeftObject, id,distObjectType);
        //先收集所有右id
        List<String> distIds = distResults.stream().map(data -> {
            Gte4DistOrgRLEntity distOrgRL = (Gte4DistOrgRLEntity) data;
            return distOrgRL.getRightObject();
        }).collect(Collectors.toList());
        //根据右id获取右对象集合  gte4dist_org_entry
        List<Gte4DistOrgEntryResp> distResps = new ArrayList<>();
        distIds.forEach(data->{
            Gte4DistOrgEntryEntity distOrgEntry = EntityUtil.getById(new Gte4DistOrgEntryEntity().getObjectType(), data);
            if (!Objects.isNull(distOrgEntry)) {
                Gte4DistOrgEntryResp distOrgEntryResp = new Gte4DistOrgEntryResp();
                BeanUtil.copyPropertiesIgnoreNull(distOrgEntry,distOrgEntryResp);
                distResps.add(distOrgEntryResp);
            }
        });
        resp.setDistListResp(distResps);
        return resp;
    }


    public List<WorkspaceObjectEntity> getRelationList(String leftObject,IdRequest id,String noticeObjectType) {
        List<Triple<String, Ops, Object>> paramsList = Arrays.asList(
                Triple.of(leftObject, Ops.EQ, id.getUid()));
        QueryResults<WorkspaceObjectEntity> queryResults = EntityUtil.getDynamicQuery(noticeObjectType, paramsList).fetchResults();
        return queryResults.getResults();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveChangeOrder(ChangeOrderReq req) {
        //先对当前更改单的编辑权限进行校验
        WorkspaceObjectEntity afterRL = EntityUtil.getById(new ChangeAfterRLEntity().getObjectType(), req.getChangeAfterRLReq().getUid());
        if (Objects.isNull(afterRL)) {
            throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("objIsNull", req.getChangeAfterRLReq().getUid()));
        }
        WorkspaceObjectEntity entity = EntityUtil.getById( afterRL.getLeftObjectType(),afterRL.getLeftObject());
        if (Objects.isNull(entity)) {
            throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("changeIsNull", afterRL.getLeftObject()));
        }
        this.verifyAuthority(entity, OperatorEnum.Write,"updateError",entity.getObjectName());

        //1.处理更改后对象 编辑操作
        IGte4PartChangeDomainService.super.update(req.getChangeAfterRLReq());

        //2.处理通知列表 新增  编辑  删除
        List<Gte4NoticeOrgEntryReq> noticeList = req.getNoticeList();
        List<String> newNoticeIds = noticeList.stream().map(Gte4NoticeOrgEntryReq::getUid).collect(Collectors.toList());
        //判断是否有id,无则新增，有则编辑
        List<String> saveIds = new ArrayList<>();
        noticeList.forEach(notice->{
            if (Objects.isNull(notice.getUid())) {
                WorkspaceObjectResp save = IGte4PartChangeDomainService.super.save(notice);
                //保存关系表
                Gte4NoticeOrgRLReq noticeOrgRLReq = new Gte4NoticeOrgRLReq();
                noticeOrgRLReq.setLeftObject(req.getChangeAfterRLReq().getUid());
                noticeOrgRLReq.setLeftObjectType(req.getChangeAfterRLReq().getObjectType());
                noticeOrgRLReq.setRightObject(save.getUid());
                noticeOrgRLReq.setRightObjectType(new Gte4NoticeOrgEntryEntity().getObjectType());
                noticeOrgRLReq.setObjectType(new Gte4NoticeOrgRLEntity().getObjectType());
                IGte4PartChangeDomainService.super.save(noticeOrgRLReq);
                saveIds.add(save.getUid());
            } else {
                IGte4PartChangeDomainService.super.update(notice);
            }
        });
        //最后查询该更改后的id关联的所有数据，对比传参，多余的进行删除操作
        String afterRLId = req.getChangeAfterRLReq().getUid();
        ChangeOrderResp changeOrderResp = this.getChangeOrder(new IdRequest(afterRLId, new ChangeAfterRLResp().getObjectType()));
        List<String> oldNoticeIds = changeOrderResp.getNoticeListResp().stream().map(Gte4NoticeOrgEntryResp::getUid).collect(Collectors.toList());
        //先求入参集合与原本数据集合的差值
        oldNoticeIds.removeAll(newNoticeIds);
        //再用差值与这次新添加的集合求差值
        oldNoticeIds.removeAll(saveIds);
        oldNoticeIds.stream().forEach(id->{
            IGte4PartChangeDomainService.super.deleteObject(new IdRequest(id, new Gte4NoticeOrgEntryEntity().getObjectType()));
        });

        //3.处理发放列表 新增  编辑  删除
        List<Gte4DistOrgEntryReq> distList = req.getDistList();
        List<String> newDistIds = distList.stream().map(Gte4DistOrgEntryReq::getUid).collect(Collectors.toList());
        //判断是否有id,无则新增，有则编辑
        List<String> saveIds2 = new ArrayList<>();
        distList.forEach(dist->{
            if (Objects.isNull(dist.getUid())) {
                WorkspaceObjectResp save = IGte4PartChangeDomainService.super.save(dist);
                //保存关系表
                Gte4DistOrgRLReq distOrgRLReq = new Gte4DistOrgRLReq();
                distOrgRLReq.setLeftObject(req.getChangeAfterRLReq().getUid());
                distOrgRLReq.setLeftObjectType(req.getChangeAfterRLReq().getObjectType());
                distOrgRLReq.setRightObject(save.getUid());
                distOrgRLReq.setRightObjectType(new Gte4DistOrgEntryEntity().getObjectType());
                distOrgRLReq.setObjectType(new Gte4DistOrgRLEntity().getObjectType());
                IGte4PartChangeDomainService.super.save(distOrgRLReq);
                saveIds2.add(save.getUid());
            } else {
                IGte4PartChangeDomainService.super.update(dist);
            }
        });
        //最后查询该更改后的id关联的所有数据，对比传参，多余的进行删除操作
        List<String> oldDistIds = changeOrderResp.getDistListResp().stream().map(Gte4DistOrgEntryResp::getUid).collect(Collectors.toList());
        //先求入参集合与原本数据集合的差值
        oldDistIds.removeAll(newDistIds);
        //再用差值与这次新添加的集合求差值
        oldDistIds.removeAll(saveIds2);
        oldDistIds.stream().forEach(id->{
            IGte4PartChangeDomainService.super.deleteObject(new IdRequest(id, new Gte4DistOrgEntryEntity().getObjectType()));
        });
        return true;
    }


}
