package com.nancal.msgbom.service.impl;


import cn.hutool.core.util.StrUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.model.entity.Gte4MfgLinePrEntity;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IGte4MfgLinePrDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Objects;


@Service
public class Gte4MfgLinePrDomainServiceImpl implements IGte4MfgLinePrDomainService {

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
        Gte4MfgLinePrBomReq objectBomReq = (Gte4MfgLinePrBomReq) req;
        BOMNodeReq nodeReq = objectBomReq.getBomNodeReq();
        Gte4MfgLinePrRevisionReq linePrRevisionReq = objectBomReq.getLinePrRevisionReq();
        String objectType = StrUtil.blankToDefault(linePrRevisionReq.getObjectType(), EntityUtil.getObjectType());
        WorkspaceObjectResp save;
        if (Objects.isNull(nodeReq)) {
            save = IGte4MfgLinePrDomainService.super.save(linePrRevisionReq);
        } else {
            if(!ibomNodeDomainService.checkBomStructure(DictConstant.GTE4_MFGPLANTPR_BOMSTRUCTURE,nodeReq.getParentItemType(), objectType)){
                throw new ServiceException(ErrorCode.E_12, "线体工艺类型不支持在当前类型下添加！");
            }
            //判断是否可以作为子级添加，取决于父级的是否具有编辑权限
//            IGte4MfgLinePrDomainService.super.checkParentRevEidt(nodeReq);
            save = IGte4MfgLinePrDomainService.super.save(linePrRevisionReq);
            nodeReq.setChildItem(save.getUid());
            nodeReq.setChildItemType(new Gte4MfgLinePrEntity().getObjectType());
            ibomNodeDomainService.createNode(nodeReq, AppNameEnum.MSGBOM);
            //调用维护工时的方法
            ibomNodeDomainService.fullTaskTime(1,save.getRightObject(),save.getRightObjectType());
        }
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
    public BusinessObjectResp cloneOperation(Gte4MfgLinePrCloneReq req) {
        //1.先根据入参新建一个新的工序，拿到工序uid
        WorkspaceObjectResp operation = IGte4MfgLinePrDomainService.super.save(req.getLinePrRevisionReq());
        //2.再根据cloneId查询出要克隆的工序在bomNode中所有的子级（工步+工艺资源）
        ibomNodeDomainService.cloneBomNode(req.getCloneType(),req.getCloneId(),operation.getObjectType(),
                operation.getUid(),operation.getRightObject(),AppNameEnum.MSGBOM,new HashMap<>());
        return operation;
    }

}
