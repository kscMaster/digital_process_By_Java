package com.nancal.service.service;


import com.nancal.api.model.ColumnReq;
import com.nancal.api.model.Gte4ImportRevisionResp;
import com.nancal.common.base.Response;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ProcessResourcesDomainService extends IItemRevisionDomainService{

    default Response<Gte4ImportRevisionResp> importExcel(@RequestParam("file") MultipartFile file, boolean isImport) {
        return null;
    }

    default void msgbomExport(HttpServletResponse response){

    }

    default void bopFieldExport(HttpServletResponse response,String uid, String objectType, List<ColumnReq> columnReqs){

    }
}
