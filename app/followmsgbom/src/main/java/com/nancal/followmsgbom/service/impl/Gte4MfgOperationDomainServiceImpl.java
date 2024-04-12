package com.nancal.followmsgbom.service.impl;

import com.nancal.api.model.*;
import com.nancal.api.utils.DictUtil;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.model.entity.Gte4CnstrProcessEntity;
import com.nancal.model.entity.Gte4MfgProcessEntity;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IGte4MfgOperationDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Gte4MfgOperationDomainServiceImpl implements IGte4MfgOperationDomainService {
    @Autowired
    private IBOMNodeDomainService ibomNodeDomainService;
    @Override
    public WorkspaceObjectResp saveBomReq(BusinessObjectReq req) {
        Gte4MfgOperationBomReq gte4MfgOperationBomReq= (Gte4MfgOperationBomReq)req;
        if (!(new Gte4CnstrProcessEntity().getObjectType().equalsIgnoreCase(gte4MfgOperationBomReq.getBomNodeReq().getParentItemType()))) {
            throw new ServiceException(ErrorCode.E_12, "工序类型只能在随工工艺规程下新建！");
        }
        //校验父级权限
        IGte4MfgOperationDomainService.super.checkParentRevEidt(gte4MfgOperationBomReq.getBomNodeReq());
        //创建数据
        WorkspaceObjectResp save = IGte4MfgOperationDomainService.super.save(gte4MfgOperationBomReq.getOperationRevisionReq());
        gte4MfgOperationBomReq.getBomNodeReq().setChildItemRevision(save.getRightObject());
        gte4MfgOperationBomReq.getBomNodeReq().setChildItemTypeRevision(save.getRightObjectType());
        gte4MfgOperationBomReq.getBomNodeReq().setChildItem(save.getUid());
        gte4MfgOperationBomReq.getBomNodeReq().setChildItemType(save.getObjectType());
        //创建精确BOM行
        BOMNodeResp node = ibomNodeDomainService.createNode(gte4MfgOperationBomReq.getBomNodeReq(), AppNameEnum.FOLLOW_MSGBOM);
        return save;
    }
}
