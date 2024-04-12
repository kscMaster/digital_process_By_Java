package com.nancal.myworkspace.service.impl;


import com.nancal.api.model.*;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.OperatorEnum;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.Gte4MfgStepEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.service.IGte4MfgStepDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;


@Service
public class Gte4MfgStepDomainServiceImpl implements IGte4MfgStepDomainService {

    @Override
    public BusinessObjectResp cloneStep(Gte4MfgStepCloneReq req) {
        return null;
    }

    @Override
    public WorkspaceObjectResp saveBomReq(BusinessObjectReq req) {
        return null;
    }

}
