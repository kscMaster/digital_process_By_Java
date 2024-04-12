package com.nancal.service.service;


import com.nancal.api.model.BusinessObjectReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.enums.AppNameEnum;

public interface IGte4ProcessDomainService extends IItemDomainService {

    default WorkspaceObjectResp saveBomReq(BusinessObjectReq req, AppNameEnum appNameEnum) {
        return null;}

}
