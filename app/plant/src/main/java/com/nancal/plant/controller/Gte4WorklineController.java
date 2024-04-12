package com.nancal.plant.controller;

import com.nancal.api.model.*;
import com.nancal.common.base.*;
import com.nancal.service.service.IGte4WorklineDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Gte4Workline")
@Api(tags = "Gte4Workline 线体 的控制器")
public class Gte4WorklineController {
    @Qualifier("gte4WorklineDomainServiceImpl")
    @Autowired
    private IGte4WorklineDomainService service;

    @ApiOperation(value = "新增")
    @PostMapping(value = "/create")
    public Response<WorkspaceObjectResp> create(@RequestBody @Validated(AddGroup.class) Gte4WorklineBOMReq req) {
        return Response.of(service.saveBomReq(req));
    }

}