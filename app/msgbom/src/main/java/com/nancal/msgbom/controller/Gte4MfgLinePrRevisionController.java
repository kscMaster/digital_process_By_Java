package com.nancal.msgbom.controller;

import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.Gte4MfgLinePrBomReq;
import com.nancal.api.model.*;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.*;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.service.service.IGte4MfgLinePrRevisionDomainService;
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
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/Gte4MfgLinePrRevision")
@Api(tags = "线体工艺版本管理")
public class Gte4MfgLinePrRevisionController {

    @Qualifier("gte4MfgLinePrRevisionDomainServiceImpl")
    @Autowired
    private IGte4MfgLinePrRevisionDomainService service;


    @PreAuthorize
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated(UpdateGroup.class)Gte4MfgLinePrBomReq req) {
        return Response.of(service.update(req));
    }

    @ApiOperation(value = "详情")
    @PostMapping("/details")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getObject(id));
    }

    @ApiOperation(value = "获取线体")
    @PostMapping("/getLine")
    public Response<List<WorkspaceObjectResp>> getLine(@RequestBody @Validated Gte4WorklineRevisionLikeReq req) {
        return null;
    }

    @ApiOperation(value = "线体工艺模糊查询")
    @PostMapping(value = "/getByLike")
    public TableResponse<WorkspaceObjectResp> getByLike(@RequestBody @Validated(LikeGroup.class) TableRequest<Gte4MfgLinePrRevisionReq> req) {
        return service.multiPageLike(req);
    }

    @ApiOperation(value = "升版")
    @PreAuthorize
    @PostMapping("/upgrade")
    public Response<WorkspaceObjectResp> upgrade(@RequestBody @Validated(UpdateGroup.class) Gte4MfgLinePrRevisionReq req) {
        return Response.of(service.bomUpgrade(req, AppNameEnum.MSGBOM));
    }
}
