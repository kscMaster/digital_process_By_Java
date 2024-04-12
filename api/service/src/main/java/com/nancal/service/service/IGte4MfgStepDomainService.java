package com.nancal.service.service;


import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.CoderSetUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.OperatorEnum;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.Gte4MfgStepEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

public interface IGte4MfgStepDomainService extends IWorkspaceObjectDomainService {


    @Transactional
    default BusinessObjectResp cloneStep(Gte4MfgStepCloneReq req) {
        return null;
    }

    @Transactional
    default WorkspaceObjectResp saveBomReq(BusinessObjectReq req) {
        return null;
    }

    default BusinessObjectResp stepUpgrade(Gte4MfgStepUpgradeReq req){
        return null;}


    /**
     * 编辑工步
     * @param req
     * @author: 薛锦龙
     * @time: 2022/7/6
     * @return: {@link BusinessObjectResp}
     */
    @Transactional
    default BusinessObjectResp contentUpdate(BusinessObjectReq req) {
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        Gte4MfgStepContentReq gte4MfgStepContentReq= (Gte4MfgStepContentReq)req;
        WorkspaceObjectEntity entity = EntityUtil.getById(gte4MfgStepContentReq.getObjectType(), gte4MfgStepContentReq.getUid());
        //校验编辑权限
        IWorkspaceObjectDomainService.super.verifyAuthority(entity, OperatorEnum.Write);
        Gte4MfgStepEntity gte4MfgStepEntity = (Gte4MfgStepEntity)entity;
        gte4MfgStepEntity.setStepContent(gte4MfgStepContentReq.getStepContent());
        entityManager.persist(gte4MfgStepEntity);
        Gte4MfgStepResp resp = new Gte4MfgStepResp();
        BeanUtil.copyPropertiesIgnoreNull(gte4MfgStepEntity,resp);
        return resp;
    }

    /**
     *  校验工步编辑权限
     * @param id
     * @author: 薛锦龙
     * @time: 2022/7/6
     * @return: {@link }
     */
    default void check(IdRequest id){
        WorkspaceObjectEntity entity = EntityUtil.getById(id.getObjectType(), id.getUid());
        IWorkspaceObjectDomainService.super.verifyAuthority(entity, OperatorEnum.Write);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    default WorkspaceObjectResp save(BusinessObjectReq req) {
        String objectType = StrUtil.blankToDefault(req.getObjectType(), EntityUtil.getObjectType());
        String itemId = SpringUtil.getBean(CoderSetUtil.class).getOneCoderByObjectType(objectType);
        ((Gte4MfgStepReq)req).setItemId(itemId);
        return IWorkspaceObjectDomainService.super.save(req);
    }

}
