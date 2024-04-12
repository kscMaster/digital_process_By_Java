package com.nancal.service.service;


import com.nancal.api.model.ItemRevisionReq;
import com.nancal.api.model.LibraryAttributeMeasureReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;

import java.util.List;
import java.util.Map;

public interface IMeasureRevisionDomainService extends IItemRevisionDomainService{

    default TableResponse<List<Map<String,Object>>> customPageLike(TableRequest<? extends ItemRevisionReq> req){
        return null;
    }

    default WorkspaceObjectResp customUpgrade(LibraryAttributeMeasureReq req){
        return null;
    }
}
