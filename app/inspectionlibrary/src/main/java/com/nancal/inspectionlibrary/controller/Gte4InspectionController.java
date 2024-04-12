package com.nancal.inspectionlibrary.controller;

import com.nancal.api.model.*;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.Response;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.service.service.IGte4InspectionDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping("/Gte4Inspection")
@Api(tags = "检验项目")
public class Gte4InspectionController {
    @Qualifier("gte4InspectionDomainServiceImpl")
    @Autowired
    protected IGte4InspectionDomainService service;

    @PreAuthorize
    @ApiOperation(value = "新增")
    @PostMapping(value = "/create")
    public Response<BusinessObjectResp> create(@RequestBody @Validated Gte4InspectionRevisionReq req) {
        return Response.of(service.save(req));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/page")
    public TableResponse<WorkspaceObjectResp> page(@RequestBody @Validated TableRequest<ItemRevisionReq> req) {
      return service.pagePlus(req);
    }

    @ApiOperation(value = "导出")
    @PostMapping(value = "/export")
    public void export(@RequestBody @Validated TableRequest<ItemRevisionReq> req, HttpServletResponse response) {
         service.treeExport(req,response);
    }

    @ApiOperation(value = "导入验证")
    @PostMapping(value = "/verify")
    public Response<Gte4ImportRevisionResp> verify(MultipartFile file) {
        return service.importExcelVerify(file);
    }


    @ApiOperation(value = "导入")
    @PostMapping(value = "/import")
    public Response<Gte4ImportRevisionResp> importExcel(MultipartFile file, @RequestParam("leftObject") String leftObject) throws ZipException, UnsupportedEncodingException {
        return service.importExcel(file,leftObject);
    }

}
