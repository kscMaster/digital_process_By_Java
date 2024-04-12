package com.nancal.plant.controller;

import com.nancal.api.model.*;
import com.nancal.common.base.*;
import com.nancal.service.service.IGte4PlantDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Gte4Plant")
@Api(tags = "Gte4Plant 工厂 的控制器")
public class Gte4PlantController {
    @Autowired
    private IGte4PlantDomainService service;

    @ApiOperation(value = "新增")
    @PostMapping(value = "/create")
    public Response<WorkspaceObjectResp> create(@RequestBody @Validated(AddGroup.class) Gte4PlantBomReq req) {
        return Response.of(service.saveBomReq(req));
    }

}