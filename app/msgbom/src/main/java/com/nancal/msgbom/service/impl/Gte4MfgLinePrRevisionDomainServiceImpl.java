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
import com.nancal.model.entity.Gte4MfgLinePrRevisionEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IGte4MfgLinePrRevisionDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
public class Gte4MfgLinePrRevisionDomainServiceImpl implements IGte4MfgLinePrRevisionDomainService {

    @Autowired
    private IBOMNodeDomainService bomNodeDomainService;
    @Autowired
    private DictUtil dictUtil;


    @Override
    public BusinessObjectResp getObject(IdRequest id) {
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        BOMNodeResp bomNodeResp = new BOMNodeResp();
        WorkspaceObjectResp objectResp;
        //作为根节点 查询对象属性
        if (!id.getObjectType().equals(new BOMNodeEntity().getObjectType())) {
            objectResp = (WorkspaceObjectResp) IGte4MfgLinePrRevisionDomainService.super.getObject(new IdRequest(id.getUid()));
        } else {
//        作为子节点 查询bom属性 + 查询对象属性
            BOMNodeEntity bomNode = EntityUtil.getById(new BOMNodeEntity().getObjectType(), id.getUid());
            if (Objects.isNull(bomNode)) {
                throw new ServiceException(ErrorCode.E_12);
            }
            BeanUtil.copyPropertiesIgnoreNull( bomNode,bomNodeResp);
//           根据零件uid获取最大版本最新激活的版本uid
            WorkspaceObjectEntity lastVersion = SpringUtil.getBean(ItemFactory.class).create().getLastVersion(bomNode.getChildItem(), bomNode.getChildItemType());
            if (Objects.isNull(lastVersion)) {
                throw new ServiceException(ErrorCode.E_12);
            }
            objectResp = (WorkspaceObjectResp)IGte4MfgLinePrRevisionDomainService.super.getObject(new IdRequest(lastVersion.getUid()));
            if (Objects.isNull(objectResp)) {
                throw new ServiceException(ErrorCode.E_12);
            }
        }
        objBomResp.setObjectResp(objectResp);
        objBomResp.setBomNodeResp(bomNodeResp);
        dictUtil.translate(objBomResp);
        return objBomResp;
    }


    @Override
    @Transactional
    public BusinessObjectResp update(BusinessObjectReq req) {
        Gte4MfgLinePrBomReq objectBomReq = (Gte4MfgLinePrBomReq)req;
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        if (!Objects.isNull(objectBomReq.getBomNodeReq())) {
            objectBomReq.getBomNodeReq().setObjectType(new BOMNodeEntity().getObjectType());
            //调用编辑bom属性方法
            BOMNodeResp bomNodeResp =(BOMNodeResp)bomNodeDomainService.update(objectBomReq.getBomNodeReq());
            objBomResp.setBomNodeResp(bomNodeResp);
        }
        if (!Objects.isNull(objectBomReq.getLinePrRevisionReq())) {
            //调用编辑对象属性方法
            WorkspaceObjectResp update = (WorkspaceObjectResp) IGte4MfgLinePrRevisionDomainService.super.update(objectBomReq.getLinePrRevisionReq());
            objBomResp.setObjectResp(update);
            //调用维护工时的方法
            bomNodeDomainService.fullTaskTime(4,update.getUid(),update.getObjectType());
        }
        return objBomResp;
    }


}
