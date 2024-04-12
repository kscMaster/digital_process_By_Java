package com.nancal.plant.service.impl;

import com.nancal.api.model.*;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.model.entity.Gte4MfgOperationEntity;
import com.nancal.model.entity.Gte4MfgProcessEntity;
import com.nancal.model.entity.Gte4WorklineEntity;
import com.nancal.model.entity.Gte4WorklineRevisionEntity;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IGte4MfgOperationDomainService;
import com.nancal.service.service.IGte4WorklineDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("gte4WorklineDomainServiceImpl")
public class Gte4WorklineDomainServiceImpl implements IGte4WorklineDomainService {

    @Autowired
    private IBOMNodeDomainService ibomNodeDomainService;

    @Transactional
    public WorkspaceObjectResp saveBomReq(BusinessObjectReq req) {
        Gte4WorklineBOMReq operationBomReq = (Gte4WorklineBOMReq) req;
        BOMNodeReq nodeReq = operationBomReq.getBomNodeReq();
        //判断是否可以添加，取决于父级的是否具有编辑权限
        IGte4WorklineDomainService.super.checkParentRevEidt(nodeReq);
        WorkspaceObjectResp save = IGte4WorklineDomainService.super.save(operationBomReq.getWorklineRevisionReq());
        nodeReq.setChildItem(save.getUid());
        nodeReq.setChildItemType(new Gte4WorklineEntity().getObjectType());
        ibomNodeDomainService.createNode(nodeReq, AppNameEnum.PLANT);
        return save;
    }

}