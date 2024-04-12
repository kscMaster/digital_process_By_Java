package com.nancal.service.service;


import com.nancal.api.model.*;


public interface IGte4MfgLinePrDomainService extends IItemDomainService {

    default WorkspaceObjectResp saveBomReq(BusinessObjectReq req){
        return null;}

    default BusinessObjectResp cloneOperation(Gte4MfgLinePrCloneReq req) {
        return null;
    }
}
