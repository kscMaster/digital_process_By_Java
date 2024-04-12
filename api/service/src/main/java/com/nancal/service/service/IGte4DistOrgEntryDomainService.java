package com.nancal.service.service;


import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;

public interface IGte4DistOrgEntryDomainService extends IWorkspaceObjectDomainService {
    TableResponse<WorkspaceObjectResp> getList(TableRequest<IdRequest> req);
}
