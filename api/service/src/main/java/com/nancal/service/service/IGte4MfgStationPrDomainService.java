package com.nancal.service.service;


import com.nancal.api.model.*;

public interface IGte4MfgStationPrDomainService extends IItemDomainService {

    default WorkspaceObjectResp saveBomReq(BusinessObjectReq req){
        return null;}

    default BusinessObjectResp cloneOperation(Gte4MfgStationPrCloneReq req) {
        return null;
    }
}
