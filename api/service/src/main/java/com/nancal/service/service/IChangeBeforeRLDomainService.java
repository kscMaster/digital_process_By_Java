package com.nancal.service.service;


import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.ChangeBeforeRLReq;
import com.nancal.api.model.CompareReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.base.IdRequest;
import com.nancal.model.entity.ChangeBeforeRLEntity;
import com.nancal.service.bo.Change;
import com.nancal.service.factory.ChangeFactory;

import java.util.List;

public interface IChangeBeforeRLDomainService extends IRelationDomainService {

    default boolean create(List<ChangeBeforeRLReq> reqs) {
        return true;
    }

    default List<WorkspaceObjectResp> getBeforeObject(IdRequest id){
        String type = new ChangeBeforeRLEntity().getObjectType();
        Change change = SpringUtil.getBean(ChangeFactory.class).create();
       return change.getChangeObject(type, id);
    }

    default boolean deleteBeforeRL(CompareReq req) {
        return true;
    }
}
