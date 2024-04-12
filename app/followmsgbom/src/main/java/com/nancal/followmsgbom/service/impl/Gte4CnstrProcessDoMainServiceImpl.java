package com.nancal.followmsgbom.service.impl;

import com.nancal.api.model.BOMNodeResp;
import com.nancal.api.model.BomEditRevisionResp;
import com.nancal.api.model.BusinessObjectReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.service.IGte4CnstrProcessDomainService;
import com.nancal.service.service.IGte4MfgProcessDomainService;
import org.springframework.stereotype.Service;

@Service
public class Gte4CnstrProcessDoMainServiceImpl implements IGte4CnstrProcessDomainService {
    @Override
    public WorkspaceObjectResp save(BusinessObjectReq req) {
        WorkspaceObjectResp save = IGte4CnstrProcessDomainService.super.save(req);
        //根据版本uid和版本类型查询版本对象
        WorkspaceObjectEntity entity = EntityUtil.getById(save.getRightObjectType(), save.getRightObject());
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        WorkspaceObjectResp objectResp = new WorkspaceObjectResp();
        BOMNodeResp bomNodeResp = new BOMNodeResp();
        BeanUtil.copyPropertiesIgnoreNull(entity,objectResp);
        objectResp.setLeftObject(save.getUid());
        objectResp.setLeftObjectType(save.getObjectType());
        //添加返回根节点属性
        bomNodeResp.setParentItem("0");
        bomNodeResp.setChildItem(save.getUid());
        bomNodeResp.setChildItemType(save.getObjectType());
        bomNodeResp.setBomView(AppNameEnum.MSGBOM.name());
        objBomResp.setObjectResp(objectResp);
        objBomResp.setBomNodeResp(bomNodeResp);
        return objBomResp;
    }
}
