package com.nancal.myworkspace.controller;

import com.nancal.api.model.*;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.*;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IGte4MfgStepDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/Gte4MfgStep")
@Api(tags = "工步管理")
public class Gte4MfgStepController {

    @Qualifier("gte4MfgStepDomainServiceImpl")
    @Autowired
    private IGte4MfgStepDomainService service;

    @PreAuthorize
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated(UpdateGroup.class) Gte4MfgStepContentReq req) {
        return Response.of(service.contentUpdate(req));
    }


    @ApiOperation(value = "编辑权限校验")
    @PostMapping(value = "/check")
    public Response<String> check(@RequestBody @Validated IdRequest id) {
        service.check(id);
       return Response.of();
    }







}
