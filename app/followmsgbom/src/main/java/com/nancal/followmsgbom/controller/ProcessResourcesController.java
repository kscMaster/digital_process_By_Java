package com.nancal.followmsgbom.controller;

import com.nancal.api.model.BusinessObjectResp;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.service.service.ProcessResourcesDomainService;
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


}
