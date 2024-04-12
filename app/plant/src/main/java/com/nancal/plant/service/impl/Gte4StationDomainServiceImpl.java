package com.nancal.plant.service.impl;

import com.nancal.api.model.*;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.model.entity.Gte4MfgOperationEntity;
import com.nancal.model.entity.Gte4MfgProcessEntity;
import com.nancal.model.entity.Gte4StationEntity;
import com.nancal.model.entity.Gte4StationRevisionEntity;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IGte4MfgOperationDomainService;
import com.nancal.service.service.IGte4StationDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("gte4StationDomainServiceImpl")
public class Gte4StationDomainServiceImpl implements IGte4StationDomainService {
    @Autowired
    private IBOMNodeDomainService ibomNodeDomainService;

    @Transactional
    public WorkspaceObjectResp saveBomReq(BusinessObjectReq req) {
        Gte4StationBomReq operationBomReq = (Gte4StationBomReq) req;
        BOMNodeReq nodeReq = operationBomReq.getBomNodeReq();
//        TODO 优化后代码，未启用
//        if(!ibomNodeDomainService.checkBomStructure(DictConstant.GTE4_MFGPROCESS_BOMSTRUCTURE,nodeReq.getParentItemType(),EntityUtil.getObjectType())){
//            throw new ServiceException(ErrorCode.E_12, "工序类型不能挂在当前类型下");
//        }
//        if (!(new Gte4MfgProcessEntity().getObjectType().equalsIgnoreCase(nodeReq.getParentItemType()))&&
//                !(new Gte4MfgOperationEntity().getObjectType().equalsIgnoreCase(nodeReq.getParentItemType()))) {
//            throw new ServiceException(ErrorCode.E_12, "工序类型只能在工艺规程下新建！");
//        }
        //判断是否可以添加，取决于父级的是否具有编辑权限
        IGte4StationDomainService.super.checkParentRevEidt(nodeReq);
        WorkspaceObjectResp save = IGte4StationDomainService.super.save(operationBomReq.getStationRevisionReq());
        nodeReq.setChildItem(save.getUid());
        nodeReq.setChildItemType(new Gte4StationEntity().getObjectType());
        ibomNodeDomainService.createNode(nodeReq, AppNameEnum.PLANT);
        return save;
    }

}