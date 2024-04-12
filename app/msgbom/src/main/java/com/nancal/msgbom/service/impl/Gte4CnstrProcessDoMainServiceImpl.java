package com.nancal.msgbom.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.FindReq;
import com.nancal.api.model.GenerateFollowMsgBomReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.annotation.TimeLog;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.model.entity.Gte4CnstrProcessEntity;
import com.nancal.model.entity.ItemEntity;
import com.nancal.model.entity.ItemRevisionEntity;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IGte4CnstrProcessDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
public class Gte4CnstrProcessDoMainServiceImpl implements IGte4CnstrProcessDomainService {

    @Autowired
    private IBOMNodeDomainService ibomNodeDomainService;

    @Override
    @Transactional
    @TimeLog
    public BusinessObjectResp generateFollowMsgBom(GenerateFollowMsgBomReq req){
        req.getGte4CnstrProcessRevisionReq().setObjectType(new Gte4CnstrProcessEntity().getObjectType());
        WorkspaceObjectResp process = IGte4CnstrProcessDomainService.super.save(req.getGte4CnstrProcessRevisionReq());
        String pid = req.getUid();
        String ptype = req.getUid();
        if (ItemRevisionEntity.class.isAssignableFrom(EntityUtil.getEntityClass(req.getObjectType()))){
            ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
            ItemEntity itemEntity = itemRevision.getLeftObjectByRightObject(req.getUid(), req.getObjectType());
            if(ObjectUtil.isNull(itemEntity)){
                return null;
            }
            pid = itemEntity.getUid();
            ptype = itemEntity.getObjectType();
        }
        ibomNodeDomainService.cloneBomNode(ptype,pid,process.getObjectType(),
                process.getUid(),process.getRightObject(),AppNameEnum.MSGBOM, AppNameEnum.FOLLOW_MSGBOM,req.getBomNodeId());
        return process;
    }

}
