package com.nancal.service.service;


import com.nancal.api.model.ItemRevisionReq;
import com.nancal.api.model.LibraryAttributeAuxiliaryReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;

import java.util.List;
import java.util.Map;

public interface IAuxiliaryMaterialRevisionDomainService extends IItemRevisionDomainService{
    default TableResponse<List<Map<String,Object>>> customPageLike(TableRequest<? extends ItemRevisionReq> req){
        return null;
    }

    default WorkspaceObjectResp customUpgrade(LibraryAttributeAuxiliaryReq req){
        return null;
    }
}
