package com.nancal.mbom.controller;

import com.nancal.api.model.*;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.*;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IGte4ProcessRevisionDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/Gte4ProcessRevision")
@Api(tags = "工艺版本管理")
public class Gte4ProcessRevisionController {

    @Qualifier(value = "gte4ProcessRevisionDomainServiceImpl")
    @Autowired
    private IGte4ProcessRevisionDomainService service;
    @Qualifier(value = "BOMNodeDomainServiceImpl")
    @Autowired
    protected IBOMNodeDomainService bomService;


    @ApiOperation(value = "模糊查询")
    @PostMapping(value = "/getByLike")
    public TableResponse<WorkspaceObjectResp> getByLike(@RequestBody @Validated(LikeGroup.class) TableRequest<Gte4ProcessRevisionReq> req) {
        return service.multiPageLike(req);
    }

    @PreAuthorize
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated(UpdateGroup.class) Gte4ProcessBomReq req) {
        return Response.of(service.update(req));
    }

    @ApiOperation(value = "详情")
    @PostMapping("/details")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated MfgCheckReq req) {
        return Response.of(service.getObject(req));
    }

    @ApiOperation(value = "关联")
    @PostMapping(value = "/manufacturingTargets")
    @Transactional
    public Response<MfgTargetRLResp> manufacturingTargets(@RequestBody @Validated ShearReq req) {
        return Response.of(service.manufacturingTargets(req));
    }

    @ApiOperation(value = "校验关联制造目标是否存在关联关系")
    @PostMapping(value = "/verifyManufacturingTargets")
    public Response<WorkspaceObjectResp> verifyManufacturingTargets(@RequestBody @Validated IdRequest id) {
        return Response.of(service.verifyManufacturingTargets(id));
    }

    @ApiOperation(value = "升版")
    @PreAuthorize
    @PostMapping("/upgrade")
    public Response<WorkspaceObjectResp> upgrade(@RequestBody @Validated(UpdateGroup.class) Gte4ProcessRevisionReq req) {
        return Response.of(service.bomUpgrade(req, AppNameEnum.EBOM));
    }

    @ApiOperation(value = "升版权限校验")
    @PostMapping("/upgradeCheck")
    public Response<Boolean> upgradeCheck(@RequestBody @Validated IdRequest id) {
        return Response.of(bomService.upgradeCheck(id));
    }

    @ApiOperation(value = "查找关联的板子")
    @PostMapping(value = "/findAssociatedBoards")
    public Response<WorkspaceObjectResp> findAssociatedBoards(@RequestBody @Validated IdRequest req) {
        return Response.of(service.previewView(req));
    }

}
