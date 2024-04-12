package com.nancal.followmsgbom.controller;

import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.Gte4CnstrProcessRevisionReq;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.service.service.IGte4CnstrProcessDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/Gte4CnstrProcess")
@Api(tags = "随工工艺规程管理")
public class Gte4CnstrProcessController {
    @Autowired
    private IGte4CnstrProcessDomainService service;

    @PreAuthorize
    @ApiOperation(value = "新增")
    @PostMapping(value = "/create")
    public Response<BusinessObjectResp> create(@RequestBody @Validated(AddGroup.class) Gte4CnstrProcessRevisionReq req) {
        return Response.of(service.save(req));
    }

    @ApiOperation(value = "查询制造目标")
    @PostMapping(value = "/findMfgTargetRL")
    public Response<Object> findMfgTargetRL(@RequestBody @Validated IdRequest id) {
        return Response.of(service.findMfgTargetRL(id));
    }

}
