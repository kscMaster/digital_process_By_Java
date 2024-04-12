package com.nancal.service.service;


import com.nancal.api.model.BusinessObjectReq;
import com.nancal.api.model.Gte4ImportRevisionResp;
import com.nancal.api.model.ItemRevisionReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.base.Response;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface IAuxiliaryMaterialDomainService extends IItemDomainService{

    default Response<Gte4ImportRevisionResp> importExcel(MultipartFile file, String leftObjec, String  leftObjectType, boolean isExcel){
        return null;
    }

    default void exportExcelModel(HttpServletResponse request){
    }

    default TableResponse<List<Map<String,Object>>> customPage(TableRequest<? extends ItemRevisionReq> req){
        return null;
    }

    default WorkspaceObjectResp customSava(BusinessObjectReq req){
        return null;
    }
}
