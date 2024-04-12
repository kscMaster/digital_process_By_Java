package com.nancal.service.service;


import com.nancal.api.model.*;
import com.nancal.common.base.TableResponse;

import java.util.List;
import java.util.Map;

public interface IExtraPropertyDataDomainService extends IWorkspaceObjectDomainService {
   default void update(List<ExtraPropertyDataReq> extraPropertyDataReqs,String leftObject,String leftObjectType){

    }

    default List<ExtraPropertyDataResp> getList(WorkspaceObjectResp workspaceObjectResp){
       return null;
    }

    default void insert(List<ExtraPropertyDataReq> extraPropertyDataReqs, WorkspaceObjectResp save){

    }

    default void upgradeRelation(WorkspaceObjectReq req,List<ExtraPropertyDataReq> list){

    }

    default List<ExtraPropertyDataResp> getLikeList(List<ExtraPropertyDataReq> list,String uid){
       return null;
    }


    default TableResponse<List<Map<String,Object>>> getCustomObject(TableResponse<WorkspaceObjectResp> response,String dictType){
       return null;
    }
}
