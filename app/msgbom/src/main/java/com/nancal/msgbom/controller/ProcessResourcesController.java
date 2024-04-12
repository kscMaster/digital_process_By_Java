package com.nancal.msgbom.controller;

import com.nancal.api.model.*;
import com.nancal.common.base.*;
import com.nancal.service.service.ProcessResourcesDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


@Slf4j
@RestController
@RequestMapping("/ProcessResources")
@Api(tags = "工艺资源管理")
public class ProcessResourcesController {

    @Qualifier("processResourcesDomainServiceImpl")
    @Autowired
    private ProcessResourcesDomainService service;


    @ApiOperation(value = "查询BOM行详情")
    @PostMapping("/getObject")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getObject(id));
    }

    @ApiOperation(value = "工艺规划导入")
    @PostMapping("/import")
    public Response<Gte4ImportRevisionResp> importExcel(@RequestParam("file")MultipartFile file,boolean isImport) {
        return service.importExcel(file,isImport);
    }


    @ApiOperation(value = "工艺规划导出模板")
    @PostMapping("/export")
    public void export(HttpServletResponse response) {
         service.msgbomExport(response);
    }

    /**
     * 导出工艺规划任务
     * @param req req
     * @return
     */
    @ApiOperation(value = "新增")
    @PostMapping(value = "file/export")
    public void bopFieldExport(@RequestBody @Validated ExportBOMReq req,HttpServletResponse response) {
         service.bopFieldExport(response,req.getUid(),req.getObjectType(),req.getColumnReqList());
    }
}
