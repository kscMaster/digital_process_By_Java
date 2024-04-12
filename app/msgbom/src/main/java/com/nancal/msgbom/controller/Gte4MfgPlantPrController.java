package com.nancal.msgbom.controller;

import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.CloneReq;
import com.nancal.api.model.Gte4MfgPlantPrCloneReq;
import com.nancal.api.model.Gte4MfgPlantPrRevisionReq;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.Response;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.service.service.IGte4MfgPlantPrDomainService;
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
@RequestMapping("/Gte4MfgPlantPr")
@Api(tags = "工厂工艺管理")
public class Gte4MfgPlantPrController {

    @Autowired
    private IGte4MfgPlantPrDomainService service;


    @PreAuthorize
    @ApiOperation(value = "新增")
    @PostMapping(value = "/create")
    public Response<BusinessObjectResp> create(@RequestBody @Validated(AddGroup.class) Gte4MfgPlantPrRevisionReq req) {
        return Response.of(service.saveBomReq(req));
    }

    @PreAuthorize
    @ApiOperation(value = "克隆工厂工艺")
    @PostMapping("/cloneBomReq")
    public Response<BusinessObjectResp> cloneBomReq(@RequestBody @Validated(AddGroup.class) CloneReq<Gte4MfgPlantPrRevisionReq> cloneReq) {
        return Response.of(service.cloneBomReq(cloneReq, AppNameEnum.MSGBOM));
    }

}
