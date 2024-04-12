package com.nancal.library.controller;

import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.Gte4ImportRevisionResp;
import com.nancal.api.model.ItemRevisionReq;
import com.nancal.api.model.LibraryAttributeMeasureReq;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.Response;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.service.service.IMeasureDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/Measure")
@Api(tags = "量具管理")
public class MeasureController {

    @Qualifier("measureDomainServiceImpl")
    @Autowired
    private IMeasureDomainService service;


    @ApiOperation(value = "新增")
    @PreAuthorize
    @PostMapping(value = "/create")
    public Response<BusinessObjectResp> create(@RequestBody @Validated(AddGroup.class) LibraryAttributeMeasureReq req) {
        return Response.of(service.save(req));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/page")
    public TableResponse<List<Map<String,Object>>> page(@RequestBody @Validated TableRequest<ItemRevisionReq> req) {
        return service.customPage(req);
    }


    @ApiOperation(value = "导入")
    @PostMapping(value = "/import")
    public Response<Gte4ImportRevisionResp> importExcel(@RequestParam("file") MultipartFile file
            ,@RequestParam("leftObject") String leftObject
            ,@RequestParam("leftObjectType")String  leftObjectType
            ,@RequestParam("isExcel")boolean isExcel) {
        return service.importExcel(file,leftObject,leftObjectType,isExcel);
    }


    @ApiOperation(value = "导出模板")
    @PostMapping(value = "/export")
    public void exportExcelModel(HttpServletResponse request) {
        service.exportExcelModel(request);
    }
}
