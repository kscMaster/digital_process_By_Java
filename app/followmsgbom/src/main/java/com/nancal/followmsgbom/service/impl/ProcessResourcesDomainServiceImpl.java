package com.nancal.followmsgbom.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.BOMNodeResp;
import com.nancal.api.model.BomEditRevisionResp;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.followmsgbom.service.IItemRevisionDomainServiceAdaptor;
import com.nancal.model.entity.BOMNodeEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.service.ProcessResourcesDomainService;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class ProcessResourcesDomainServiceImpl implements ProcessResourcesDomainService, IItemRevisionDomainServiceAdaptor {


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
//        根据零件uid获取最大版本最新激活的版本uid
        WorkspaceObjectEntity lastVersion = SpringUtil.getBean(ItemFactory.class).create().getLastVersion(bomNode.getChildItem(), bomNode.getChildItemType());
        if (Objects.isNull(lastVersion)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        WorkspaceObjectResp objectResp = (WorkspaceObjectResp)IItemRevisionDomainServiceAdaptor.super.getObject(new IdRequest(lastVersion.getUid(),id.getObjectType()));
        if (Objects.isNull(objectResp)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        objBomResp.setObjectResp(objectResp);
        objBomResp.setBomNodeResp(bomNodeResp);
        return objBomResp;
    }

}
