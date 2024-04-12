package com.nancal.plant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.Gte4PlantEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IGte4PlantDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("gte4PlantDomainServiceImpl")
public class Gte4PlantDomainServiceImpl implements IGte4PlantDomainService {

    @Override
    public WorkspaceObjectResp save(BusinessObjectReq req) {
        WorkspaceObjectResp save = IGte4PlantDomainService.super.save(req);
        //根据版本uid和版本类型查询版本对象
        WorkspaceObjectEntity entity = EntityUtil.getById(save.getRightObjectType(), save.getRightObject());
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        Gte4PlantRevisionResp objectResp = new Gte4PlantRevisionResp();
        BOMNodeResp bomNodeResp = new BOMNodeResp();
        BeanUtil.copyPropertiesIgnoreNull(entity,objectResp);
        objectResp.setLeftObject(save.getUid());
        objectResp.setLeftObjectType(save.getObjectType());
        //添加返回根节点属性
        bomNodeResp.setParentItem("0");
        bomNodeResp.setChildItem(save.getUid());
        bomNodeResp.setChildItemType(save.getObjectType());
        bomNodeResp.setBomView(AppNameEnum.PLANT.name());
        objBomResp.setObjectResp(objectResp);
        objBomResp.setBomNodeResp(bomNodeResp);
        return objBomResp;
    }

    @Autowired
    private IBOMNodeDomainService ibomNodeDomainService;

    @Override
    @Transactional
    public WorkspaceObjectResp saveBomReq(BusinessObjectReq req) {
        Gte4PlantBomReq operationBomReq = (Gte4PlantBomReq) req;
        if (ObjectUtil.isEmpty(operationBomReq.getBomNodeReq())){
            WorkspaceObjectResp save = this.save(operationBomReq.getPlantRevisionReq());
            return save;
        }
        BOMNodeReq nodeReq = operationBomReq.getBomNodeReq();
        //判断是否可以添加，取决于父级的是否具有编辑权限
        IGte4PlantDomainService.super.checkParentRevEidt(nodeReq);
        WorkspaceObjectResp save = IGte4PlantDomainService.super.save(operationBomReq.getPlantRevisionReq());
        nodeReq.setChildItem(save.getUid());
        nodeReq.setChildItemType(new Gte4PlantEntity().getObjectType());
        ibomNodeDomainService.createNode(nodeReq, AppNameEnum.PLANT);
        return save;
    }



}