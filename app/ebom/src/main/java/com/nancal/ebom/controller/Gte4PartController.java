package com.nancal.ebom.controller;

import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.Gte4ImportRevisionResp;
import com.nancal.api.model.Gte4PartBomReq;
import com.nancal.api.model.Gte4PartRevisionReq;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.Response;
import com.nancal.service.service.IGte4PartDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequestMapping("/Gte4Part")
@Api(tags = "设计零件管理")
public class Gte4PartController {

    @Autowired
    private IGte4PartDomainService service;

    @PreAuthorize
    @ApiOperation(value = "新增")
    @PostMapping(value = "/create")
    public Response<BusinessObjectResp> create(@RequestBody @Validated(AddGroup.class) Gte4PartBomReq req) {
        Gte4PartRevisionReq partRevisionReq = req.getPartRevisionReq();
        partRevisionReq.setPartNo(partRevisionReq.getGte4PartNo());
        req.setPartRevisionReq(partRevisionReq);
        return Response.of(service.saveBomReq(req));
    }


    @ApiOperation(value = "导入")
    @PostMapping(value = "/import")
    public Response<Gte4ImportRevisionResp> importExcel(@RequestParam("file") MultipartFile file, @RequestParam("isImport")Boolean isImport) {
        return service.importExcel(file,isImport);
    }

}
