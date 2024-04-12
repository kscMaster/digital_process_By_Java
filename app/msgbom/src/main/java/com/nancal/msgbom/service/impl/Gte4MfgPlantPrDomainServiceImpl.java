package com.nancal.msgbom.service.impl;


import com.nancal.api.model.*;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.model.entity.Gte4MfgOperationEntity;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IGte4MfgPlantPrDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;


@Service
public class Gte4MfgPlantPrDomainServiceImpl implements IGte4MfgPlantPrDomainService {

    @Autowired
    private IBOMNodeDomainService ibomNodeDomainService;

    /**
     * @param req
     * @author: 拓凯
     * @time: 2022/6/7
     * @return: {@link BusinessObjectResp}
     */
    @Override
    @Transactional
    public WorkspaceObjectResp saveBomReq(BusinessObjectReq req) {
        Gte4MfgPlantPrRevisionReq objectBomReq = (Gte4MfgPlantPrRevisionReq) req;
        return IGte4MfgPlantPrDomainService.super.save(objectBomReq);
    }

    /**
     * @param req
     * @author: 拓凯
     * @time: 2022/7/11
     * @return: {@link BusinessObjectResp}
     */
    @Override
    @Transactional
    public BusinessObjectResp cloneOperation(Gte4MfgPlantPrCloneReq req) {
        //1.先根据入参新建一个新的工序，拿到工序uid
        WorkspaceObjectResp operation = IGte4MfgPlantPrDomainService.super.save(req.getPlantPrRevisionReq());
        //2.再根据cloneId查询出要克隆的工序在bomNode中所有的子级（工步+工艺资源）
        ibomNodeDomainService.cloneBomNode(req.getCloneType(),req.getCloneId(),operation.getObjectType(),
                operation.getUid(),operation.getRightObject(),AppNameEnum.MSGBOM,new HashMap<>());
        return operation;
    }

}
