package com.nancal.library.service.impl;


import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.common.constants.DictConstant;
import com.nancal.library.service.IItemRevisionDomainServiceAdaptor;
import com.nancal.model.entity.AuxiliaryMaterialRevisionEntity;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.service.IAuxiliaryMaterialRevisionDomainService;
import com.nancal.service.service.IExtraPropertyDataDomainService;
import com.querydsl.core.types.Ops;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class AuxiliaryMaterialRevisionDomainServiceImpl implements IAuxiliaryMaterialRevisionDomainService, IItemRevisionDomainServiceAdaptor {


    @Autowired
    private IExtraPropertyDataDomainService iExtraPropertyDataDomainService;

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    public TableResponse<List<Map<String,Object>>> customPageLike(TableRequest<? extends ItemRevisionReq> req){
        AuxiliaryMaterialRevisionReq data = (AuxiliaryMaterialRevisionReq)req.getData();
        List<ExtraPropertyDataReq> customFieldList = data.getCustomFieldList();
        TableResponse<WorkspaceObjectResp> response = new TableResponse<>();
        List<ExtraPropertyDataResp> likeList = iExtraPropertyDataDomainService.getLikeList(customFieldList, req.getData().getUid());
        if (!likeList.isEmpty()) {
            List<String> uids = likeList.stream().map(ExtraPropertyDataResp::getLeftObject).collect(Collectors.toList());
            List<Triple<String, Ops, Object>> triples = Arrays.asList(
                    Triple.of(AuxiliaryMaterialRevisionEntity.UID, Ops.IN, uids));
             response = IAuxiliaryMaterialRevisionDomainService.super.pageLike(req, triples);
            return iExtraPropertyDataDomainService.getCustomObject(response, DictConstant.AUXILIARY_MATERIAL_EXTRADATA);
        }else {
            response = IAuxiliaryMaterialRevisionDomainService.super.pageLike(req);
        }
        return iExtraPropertyDataDomainService.getCustomObject(response, DictConstant.MEASURE_EXTRADATA);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BusinessObjectResp updatePlus(BusinessObjectReq req) {
        LibraryAttributeAuxiliaryReq auxiliaryMaterialReq = (LibraryAttributeAuxiliaryReq)req;
        AuxiliaryMaterialRevisionReq materialRevisionReq = auxiliaryMaterialReq.getAuxiliaryMaterialRevisionReq();
        BusinessObjectResp businessObjectResp = IAuxiliaryMaterialRevisionDomainService.super.updatePlus(materialRevisionReq);
        List<ExtraPropertyDataReq> extraPropertyDataReqs = auxiliaryMaterialReq.getExtraPropertyDataReq();
        iExtraPropertyDataDomainService.update(extraPropertyDataReqs,materialRevisionReq.getLeftObject(),materialRevisionReq.getLeftObjectType());
        return businessObjectResp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkspaceObjectResp customUpgrade(LibraryAttributeAuxiliaryReq req){
        WorkspaceObjectResp workspaceObject = IAuxiliaryMaterialRevisionDomainService.super.upgrade(req.getAuxiliaryMaterialRevisionReq());
        iExtraPropertyDataDomainService.upgradeRelation(req.getAuxiliaryMaterialRevisionReq(),req.getExtraPropertyDataReq());
        return workspaceObject;
    }
}
