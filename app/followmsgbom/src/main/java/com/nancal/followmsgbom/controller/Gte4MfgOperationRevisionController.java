package com.nancal.followmsgbom.controller;

import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.Gte4MfgOperationBomReq;
import com.nancal.api.model.Gte4MfgOperationRevisionReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.*;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.service.service.IGte4MfgOperationRevisionDomainService;
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
@RequestMapping("/Gte4MfgOperationRevision")
@Api(tags = "随工工序版本管理")
public class Gte4MfgOperationRevisionController {

    @Qualifier("gte4MfgOperationRevisionDomainServiceImpl")
    @Autowired
    private IGte4MfgOperationRevisionDomainService service;


    @PreAuthorize
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated(UpdateGroup.class) Gte4MfgOperationBomReq req) {
        return Response.of(service.update(req));
    }

    @ApiOperation(value = "详情")
    @PostMapping("/details")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getObject(id));
    }

    @ApiOperation(value = "升版")
    @PreAuthorize
    @PostMapping("/upgrade")
    public Response<WorkspaceObjectResp> upgrade(@RequestBody @Validated(UpdateGroup.class) Gte4MfgOperationRevisionReq req) {
        return Response.of(service.bomUpgrade(req, AppNameEnum.FOLLOW_MSGBOM));
    }

}
