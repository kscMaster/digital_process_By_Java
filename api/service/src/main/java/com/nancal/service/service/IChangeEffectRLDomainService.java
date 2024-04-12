package com.nancal.service.service;


import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.ChangeEffectRLReq;
import com.nancal.api.model.CompareReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.base.IdRequest;
import com.nancal.model.entity.ChangeEffectRLEntity;
import com.nancal.service.bo.Change;
import com.nancal.service.factory.ChangeFactory;

import java.util.List;

public interface IChangeEffectRLDomainService extends IRelationDomainService {

    default boolean create(List<ChangeEffectRLReq> reqs) {
        return true;
    }

    default List<WorkspaceObjectResp> getEffectObject(IdRequest id){
        String type = new ChangeEffectRLEntity().getObjectType();
        Change change = SpringUtil.getBean(ChangeFactory.class).create();
        return change.getChangeObject(type,id);
    }

    default boolean deleteEffectRL(CompareReq req) {
        return true;
    }
}
