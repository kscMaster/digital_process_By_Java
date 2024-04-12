package com.nancal.service.service;


import com.nancal.api.model.ItemRevisionReq;
import com.nancal.api.model.LibraryAttributeEquipmentReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;

import java.util.List;
import java.util.Map;

public interface IEquipmentRevisionDomainService extends IItemRevisionDomainService{
    default TableResponse<List<Map<String,Object>>> customPageLike(TableRequest<? extends ItemRevisionReq> req){
        return null;
    }


    default WorkspaceObjectResp customUpgrade(LibraryAttributeEquipmentReq req){
        return null;
    }
}
