package com.nancal.msgbom.service.impl;


import cn.hutool.core.util.StrUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.model.entity.Gte4MfgStationPrEntity;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IGte4MfgStationPrDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;


@Service
public class Gte4MfgStationPrDomainServiceImpl implements IGte4MfgStationPrDomainService {

    @Autowired
    private IBOMNodeDomainService ibomNodeDomainService;

    /**
     *
     * @param req
     * @author: 拓凯
     * @time: 2022/6/7
     * @return: {@link BusinessObjectResp}
     */
    @Override
    @Transactional
    public WorkspaceObjectResp saveBomReq(BusinessObjectReq req) {
        Gte4MfgStationPrBomReq objectBomReq = (Gte4MfgStationPrBomReq) req;
        BOMNodeReq nodeReq = objectBomReq.getBomNodeReq();
        Gte4MfgStationPrRevisionReq stationPrRevisionReq = objectBomReq.getStationPrRevisionReq();
        String objectType = StrUtil.blankToDefault(stationPrRevisionReq.getObjectType(), EntityUtil.getObjectType());
        if(!ibomNodeDomainService.checkBomStructure(DictConstant.GTE4_MFGPLANTPR_BOMSTRUCTURE,nodeReq.getParentItemType(), objectType)){
            throw new ServiceException(ErrorCode.E_12, "工序类型不能挂在当前类型下");
        }
        //判断是否可以添加，取决于父级的是否具有编辑权限
        IGte4MfgStationPrDomainService.super.checkParentRevEidt(nodeReq);
        WorkspaceObjectResp save = IGte4MfgStationPrDomainService.super.save(stationPrRevisionReq);
        nodeReq.setChildItem(save.getUid());
        nodeReq.setChildItemType(new Gte4MfgStationPrEntity().getObjectType());
        ibomNodeDomainService.createNode(nodeReq, AppNameEnum.MSGBOM);
        //调用维护工时的方法
        ibomNodeDomainService.fullTaskTime(1,save.getRightObject(),save.getRightObjectType());
        return save;
    }

    /**
     *
     * @param req
     * @author: 拓凯
     * @time: 2022/7/11
     * @return: {@link BusinessObjectResp}
     */
    @Override
    @Transactional
    public BusinessObjectResp cloneOperation(Gte4MfgStationPrCloneReq req) {
        //1.先根据入参新建一个新的工序，拿到工序uid
        WorkspaceObjectResp operation = IGte4MfgStationPrDomainService.super.save(req.getStationPrRevisionReq());
        //2.再根据cloneId查询出要克隆的工序在bomNode中所有的子级（工步+工艺资源）
        ibomNodeDomainService.cloneBomNode(req.getCloneType(),req.getCloneId(),operation.getObjectType(),
                operation.getUid(),operation.getRightObject(),AppNameEnum.MSGBOM,new HashMap<>());
        return operation;
    }

}
