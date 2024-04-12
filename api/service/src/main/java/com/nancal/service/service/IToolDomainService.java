package com.nancal.service.service;


import com.nancal.api.model.Gte4ImportRevisionResp;
import com.nancal.api.model.ItemRevisionReq;
import com.nancal.common.base.Response;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface IToolDomainService extends IItemDomainService{
    default Response<Gte4ImportRevisionResp> importExcelVerify(MultipartFile file){
        return null;
    }

    default Response<Gte4ImportRevisionResp> importExcel(MultipartFile file, String leftObjec, String  leftObjectType,boolean isExcel){
        return null;
    }

    default void exportExcelModel(HttpServletResponse request){
    }

    default TableResponse<List<Map<String,Object>>> customPage(TableRequest<? extends ItemRevisionReq> req){
        return null;
    }
}
