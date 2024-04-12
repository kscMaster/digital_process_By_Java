package com.nancal.service.service;


import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.ChangeAfterRLReq;
import com.nancal.api.model.CompareReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.base.IdRequest;
import com.nancal.model.entity.ChangeAfterRLEntity;
import com.nancal.service.bo.Change;
import com.nancal.service.factory.ChangeFactory;

import java.util.List;


public interface IChangeAfterRLDomainService extends IRelationDomainService {

    default boolean create(List<ChangeAfterRLReq> reqs) {
        return true;
    }

    default List<WorkspaceObjectResp> getAfterObject(IdRequest id){
        String type = new ChangeAfterRLEntity().getObjectType();
        Change change = SpringUtil.getBean(ChangeFactory.class).create();
        return change.getChangeObject(type,id);
    }

    default boolean deleteAfterRL(CompareReq req) {
        return true;
    }

}
