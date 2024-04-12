package com.nancal.service.service;


import com.nancal.api.model.BusinessObjectReq;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.Gte4TechDocumentCloneReq;
import com.nancal.api.model.WorkspaceObjectResp;

public interface IGte4TechDocumentDomainService extends IItemDomainService {

    default BusinessObjectResp cloneTechDocument(Gte4TechDocumentCloneReq req){return null;}

    default WorkspaceObjectResp saveBomReq(BusinessObjectReq req){return null;}
}