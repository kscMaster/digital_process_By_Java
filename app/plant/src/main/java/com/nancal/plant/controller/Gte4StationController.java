package com.nancal.plant.controller;

import com.nancal.api.model.*;
import com.nancal.common.base.*;
import com.nancal.service.service.IGte4StationDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Gte4Station")
@Api(tags = "Gte4Station 工位 的控制器")
public class Gte4StationController {
    @Qualifier("gte4StationDomainServiceImpl")
    @Autowired
    private IGte4StationDomainService service;

    @ApiOperation(value = "新增")
    @PostMapping(value = "/create")
    public Response<WorkspaceObjectResp> create(@RequestBody @Validated(AddGroup.class) Gte4StationBomReq req) {
        return Response.of(service.saveBomReq(req));
    }

}