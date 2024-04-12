package com.nancal.service.service;


import com.nancal.api.model.BusinessObjectReq;
import com.nancal.api.model.Gte4ImportRevisionResp;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.base.Response;
import org.springframework.web.multipart.MultipartFile;

public interface IGte4PartDomainService extends IItemDomainService{

    default WorkspaceObjectResp saveBomReq(BusinessObjectReq req) {
        return null;}


    default Response<Gte4ImportRevisionResp> importExcel(MultipartFile file, Boolean isImport){
        return null;
    }

}
