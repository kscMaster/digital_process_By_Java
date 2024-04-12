package com.nancal.service.service;


import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.Gte4MfgStepContentReq;
import com.nancal.api.model.TaskAppointReq;
import com.nancal.api.model.common.ValidList;
import com.nancal.common.base.IdRequest;

public interface IGte4TechDocumentRevisionDomainService extends IItemRevisionDomainService{
   default BusinessObjectResp updateRichText(Gte4MfgStepContentReq req){
       return null;}

    default boolean taskAppoint(TaskAppointReq req){
        return false;}

    default boolean checkItemAndRev(ValidList<IdRequest> ids){
        return false;}

    default boolean upgradeCheck(IdRequest id){
        return false;}
}
