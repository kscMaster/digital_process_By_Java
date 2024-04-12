package com.nancal.plant.controller;

import com.nancal.api.model.*;
import com.nancal.common.base.*;
import com.nancal.service.service.IGte4PlantRevisionDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Gte4PlantRevision")
@Api(tags = "Gte4PlantRevision 工厂 的控制器")
public class Gte4PlantRevisionController {
    @Autowired
    private IGte4PlantRevisionDomainService service;

    @PostMapping("/getObject")
    @ApiOperation(value = "属性")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getObject(id));
    }

    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated(UpdateGroup.class) Gte4PlantBomReq req) {
        return Response.of(service.update(req));
    }

    @ApiOperation(value = "工厂模糊查询")
    @PostMapping(value = "/getByLike")
    public TableResponse<WorkspaceObjectResp> getByLike(@RequestBody @Validated(LikeGroup.class) TableRequest<Gte4PlantRevisionReq> req) {
        return service.multiPageLike(req);
    }


}