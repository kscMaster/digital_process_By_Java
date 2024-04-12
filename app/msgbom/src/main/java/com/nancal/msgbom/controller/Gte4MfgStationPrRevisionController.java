package com.nancal.msgbom.controller;

import com.nancal.api.model.*;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.*;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.service.service.IGte4MfgStationPrRevisionDomainService;
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
@RequestMapping("/Gte4MfgStationPrRevision")
@Api(tags = "工位工艺版本管理")
public class Gte4MfgStationPrRevisionController {

    @Qualifier("gte4MfgStationPrRevisionDomainServiceImpl")
    @Autowired
    private IGte4MfgStationPrRevisionDomainService service;


    @PreAuthorize
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated(UpdateGroup.class) Gte4MfgStationPrBomReq req) {
        return Response.of(service.update(req));
    }

    @ApiOperation(value = "详情")
    @PostMapping("/details")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getObject(id));
    }

    @ApiOperation(value = "工位工艺模糊查询")
    @PostMapping(value = "/getByLike")
    public TableResponse<WorkspaceObjectResp> getByLike(@RequestBody @Validated(LikeGroup.class) TableRequest<Gte4MfgStationPrRevisionReq> req) {
        return service.multiPageLike(req);
    }

    @ApiOperation(value = "升版")
    @PreAuthorize
    @PostMapping("/upgrade")
    public Response<WorkspaceObjectResp> upgrade(@RequestBody @Validated(UpdateGroup.class) Gte4MfgStationPrRevisionReq req) {
        return Response.of(service.bomUpgrade(req, AppNameEnum.MSGBOM));
    }


}
