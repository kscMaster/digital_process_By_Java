package com.nancal.service.service;


import com.nancal.api.model.BusinessObjectReq;
import com.nancal.api.model.WorkspaceObjectResp;
import org.springframework.transaction.annotation.Transactional;

public interface IGte4PlantDomainService extends IItemDomainService {
    @Transactional
    default WorkspaceObjectResp saveBomReq(BusinessObjectReq req){
        return null;}
}
