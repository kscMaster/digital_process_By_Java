package com.nancal.library.service.impl;


import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.common.constants.DictConstant;
import com.nancal.library.service.IItemRevisionDomainServiceAdaptor;
import com.nancal.model.entity.MeasureRevisionEntity;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.service.IEquipmentRevisionDomainService;
import com.nancal.service.service.IExtraPropertyDataDomainService;
import com.nancal.service.service.IMeasureRevisionDomainService;
import com.querydsl.core.types.Ops;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class MeasureRevisionDomainServiceImpl implements IMeasureRevisionDomainService, IItemRevisionDomainServiceAdaptor {


    @Autowired
    private IExtraPropertyDataDomainService iExtraPropertyDataDomainService;

    @Override
    public BusinessObjectResp getObject(IdRequest id) {
        BusinessObjectResp resp = IItemRevisionDomainServiceAdaptor.super.getObject(id);
        if (Objects.isNull(resp)) {
            return null;
        }

        ItemRevisionResp workspaceObjectResp = (ItemRevisionResp) resp;
        workspaceObjectResp.setExtraPropertyDataRespList(iExtraPropertyDataDomainService.getList(workspaceObjectResp));
        ItemRevision revision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        String objectType =Objects.isNull(id.getObjectType()) ? EntityUtil.getObjectType() : id.getObjectType();
        boolean permission = revision.editItemIdPermission(id.getUid(), objectType);
        workspaceObjectResp.setHasDrawingCode(permission);
        return workspaceObjectResp;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BusinessObjectResp updatePlus(BusinessObjectReq req) {
        LibraryAttributeMeasureReq measureReq = (LibraryAttributeMeasureReq)req;
        final MeasureRevisionReq measureRevisionReq = measureReq.getMeasureRevisionReq();
        BusinessObjectResp businessObjectResp = IMeasureRevisionDomainService.super.updatePlus(measureRevisionReq);
        List<ExtraPropertyDataReq> extraPropertyDataReqs = measureReq.getExtraPropertyDataReq();
        iExtraPropertyDataDomainService.update(extraPropertyDataReqs,measureRevisionReq.getLeftObject(),measureRevisionReq.getLeftObjectType());
        return businessObjectResp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkspaceObjectResp customUpgrade(LibraryAttributeMeasureReq req){
        WorkspaceObjectResp workspaceObject = IMeasureRevisionDomainService.super.upgrade(req.getMeasureRevisionReq());
        iExtraPropertyDataDomainService.upgradeRelation(req.getMeasureRevisionReq(),req.getExtraPropertyDataReq());
        return workspaceObject;
    }

    @Override
    public TableResponse<List<Map<String,Object>>> customPageLike(TableRequest<? extends ItemRevisionReq> req){
        MeasureRevisionReq data = (MeasureRevisionReq)req.getData();
        List<ExtraPropertyDataReq> customFieldList = data.getCustomFieldList();
        List<ExtraPropertyDataResp> likeList = iExtraPropertyDataDomainService.getLikeList(customFieldList, req.getData().getUid());
        TableResponse<WorkspaceObjectResp> response = new TableResponse<>();;
        if (!likeList.isEmpty()) {
            List<String> uids = likeList.stream().map(ExtraPropertyDataResp::getLeftObject).collect(Collectors.toList());
            List<Triple<String, Ops, Object>> triples = Arrays.asList(
                    Triple.of(MeasureRevisionEntity.UID, Ops.IN, uids));
            response = IMeasureRevisionDomainService.super.pageLike(req, triples);
            return iExtraPropertyDataDomainService.getCustomObject(response, DictConstant.MEASURE_EXTRADATA);
        }else {
            response = IMeasureRevisionDomainService.super.pageLike(req);
        }
        return iExtraPropertyDataDomainService.getCustomObject(response, DictConstant.MEASURE_EXTRADATA);
    }
}
