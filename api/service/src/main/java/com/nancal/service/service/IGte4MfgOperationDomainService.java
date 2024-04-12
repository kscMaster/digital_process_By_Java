package com.nancal.service.service;


import com.nancal.api.model.*;
import org.springframework.transaction.annotation.Transactional;

public interface IGte4MfgOperationDomainService extends IItemDomainService{

    @Transactional
    default WorkspaceObjectResp saveBomReq(BusinessObjectReq req){
        return null;}

    @Transactional
    default BusinessObjectResp cloneOperation(Gte4MfgOperationCloneReq req) {
        return null;
    }
}
