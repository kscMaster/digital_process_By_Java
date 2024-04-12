package com.nancal.msgbom.controller;

import com.nancal.api.model.*;
import com.nancal.api.model.common.ValidList;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.Response;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.service.service.IGte4MfgOperationDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/Gte4MfgOperation")
@Api(tags = "工序管理")
public class Gte4MfgOperationController {

    @Autowired
    private IGte4MfgOperationDomainService service;


    @PreAuthorize
    @ApiOperation(value = "新增")
    @PostMapping(value = "/create")
    @Deprecated
    public Response<List<BusinessObjectResp>> create(@RequestBody @Validated(AddGroup.class) ValidList<Gte4MfgOperationBomReq> reqs) {
        List<BusinessObjectResp> resps = new ArrayList<>();
        reqs.forEach(req -> resps.add(service.saveBomReq(req)));
        return Response.of(resps);
    }

    @PreAuthorize
    @ApiOperation(value = "克隆工序")
    @PostMapping("/cloneOperation")
    @Deprecated
    public Response<BusinessObjectResp> cloneOperation(@RequestBody @Validated(AddGroup.class) Gte4MfgOperationCloneReq req) {
        return Response.of(service.cloneOperation(req));
    }

    @PreAuthorize
    @ApiOperation(value = "新增工序")
    @PostMapping(value = "/createBomReq")
    public Response<BusinessObjectResp> createBomReq(@RequestBody @Validated(AddGroup.class) CreateBomReq<Gte4MfgOperationRevisionReq> createBomReq) {
        return Response.of(service.saveBomReq(createBomReq, DictConstant.GTE4_MFGPLANTPR_BOMSTRUCTURE,AppNameEnum.MSGBOM));
    }

    @PreAuthorize
    @ApiOperation(value = "克隆工序")
    @PostMapping("/cloneBomReq")
    public Response<BusinessObjectResp> cloneBomReq(@RequestBody @Validated(AddGroup.class) CloneReq<Gte4MfgOperationRevisionReq> cloneReq) {
        return Response.of(service.cloneBomReq(cloneReq, AppNameEnum.MSGBOM));
    }

}
