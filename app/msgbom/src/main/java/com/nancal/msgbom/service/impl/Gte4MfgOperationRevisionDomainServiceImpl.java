package com.nancal.msgbom.service.impl;


import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.BOMNodeEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IGte4MfgOperationRevisionDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
public class Gte4MfgOperationRevisionDomainServiceImpl implements IGte4MfgOperationRevisionDomainService{

    @Autowired
    private IBOMNodeDomainService bomNodeDomainService;
    @Autowired
    private DictUtil dictUtil;


    @Override
    public BusinessObjectResp getObject(IdRequest id) {
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        BOMNodeResp bomNodeResp = new BOMNodeResp();
//        查询bom属性
        BOMNodeEntity bomNode = EntityUtil.getById(new BOMNodeEntity().getObjectType(), id.getUid());
        if (Objects.isNull(bomNode)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        BeanUtil.copyPropertiesIgnoreNull( bomNode,bomNodeResp);
//        查询工序对象
//        根据工序零件uid获取最大版本最新激活的版本uid
        WorkspaceObjectEntity lastVersion = SpringUtil.getBean(ItemFactory.class).create().getLastVersion(bomNode.getChildItem(), bomNode.getChildItemType());
        if (Objects.isNull(lastVersion)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        WorkspaceObjectResp objectResp = (WorkspaceObjectResp)IGte4MfgOperationRevisionDomainService.super.getObject(new IdRequest(lastVersion.getUid()));
        if (Objects.isNull(objectResp)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        objBomResp.setObjectResp(objectResp);
        objBomResp.setBomNodeResp(bomNodeResp);
        dictUtil.translate(objBomResp);
        return objBomResp;
    }


    @Override
    @Transactional
    public BusinessObjectResp update(BusinessObjectReq req) {
        Gte4MfgOperationBomReq operationBomReq = (Gte4MfgOperationBomReq)req;
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        if (!Objects.isNull(operationBomReq.getBomNodeReq())) {
            operationBomReq.getBomNodeReq().setObjectType(new BOMNodeEntity().getObjectType());
            //调用编辑bom属性方法
            BOMNodeResp bomNodeResp =(BOMNodeResp)bomNodeDomainService.update(operationBomReq.getBomNodeReq());
            objBomResp.setBomNodeResp(bomNodeResp);
        }
        if (!Objects.isNull(operationBomReq.getOperationRevisionReq())) {
            //调用编辑有版本的属性方法
            WorkspaceObjectResp update = (WorkspaceObjectResp) IGte4MfgOperationRevisionDomainService.super.update(operationBomReq.getOperationRevisionReq());
            objBomResp.setObjectResp(update);
            //调用维护工时的方法
            bomNodeDomainService.fullTaskTime(4,update.getUid(),update.getObjectType());
        }
        return objBomResp;
    }


}
