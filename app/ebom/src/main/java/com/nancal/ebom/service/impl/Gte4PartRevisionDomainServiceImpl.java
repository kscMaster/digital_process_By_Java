package com.nancal.ebom.service.impl;


import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.EntityUtil;
import com.nancal.api.utils.TranslateUtil;
import com.nancal.common.annotation.TimeLog;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.BOMNodeEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.service.IGte4PartRevisionDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;


@Service
public class Gte4PartRevisionDomainServiceImpl implements IGte4PartRevisionDomainService {

    @Autowired
    private BOMNodeDomainServiceImpl bomNodeDomainService;
    @Autowired
    private TranslateUtil translateUtil;

    @TimeLog
    @Override
    public BusinessObjectResp getObject(MfgCheckReq req) {
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        BOMNodeResp bomNodeResp = new BOMNodeResp();
        IdRequest idRequest = new IdRequest();
        if (!req.getBomId().equals("0")) {
            //子节点查询bom属性
            BOMNodeEntity bomNode = EntityUtil.getById(new BOMNodeEntity().getObjectType(), req.getBomId());
            if (Objects.isNull(bomNode)) {
                throw new ServiceException(ErrorCode.E_12);
            }
            BeanUtil.copyPropertiesIgnoreNull(bomNode, bomNodeResp);
            //根据零件uid获取最大版本最新激活的版本uid
            WorkspaceObjectEntity lastVersion = SpringUtil.getBean(ItemFactory.class).create().getLastVersion(bomNode.getChildItem(), bomNode.getChildItemType());
            if (Objects.isNull(lastVersion)) {
                throw new ServiceException(ErrorCode.E_12);
            }
            idRequest.setUid(lastVersion.getUid());
        } else {
            //根节点根据版本uid查询
            idRequest.setUid(req.getId());
        }
        WorkspaceObjectResp objectResp = (WorkspaceObjectResp) IGte4PartRevisionDomainService.super.getObject(idRequest);
        if (Objects.isNull(objectResp)) {
            throw new ServiceException(ErrorCode.E_12);
        }
//        translateUtil.translate(objectResp);翻译子类时使用
        objBomResp.setObjectResp(objectResp);
        objBomResp.setBomNodeResp(bomNodeResp);
        return objBomResp;
    }


    @Override
    @Transactional
    public BusinessObjectResp update(BusinessObjectReq req) {
        Gte4PartBomReq partBomReq = (Gte4PartBomReq) req;
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        if (!Objects.isNull(partBomReq.getBomNodeReq())) {
            partBomReq.getBomNodeReq().setObjectType(new BOMNodeEntity().getObjectType());
            //调用编辑bom属性方法
            BOMNodeResp bomNodeResp = (BOMNodeResp) bomNodeDomainService.update(partBomReq.getBomNodeReq());
            objBomResp.setBomNodeResp(bomNodeResp);
        }
        if (!Objects.isNull(partBomReq.getPartRevisionReq())) {
            //调用父类编辑属性方法
            WorkspaceObjectResp update = (WorkspaceObjectResp) IGte4PartRevisionDomainService.super.update(partBomReq.getPartRevisionReq());
            objBomResp.setObjectResp(update);
        }
        return objBomResp;
    }


}
