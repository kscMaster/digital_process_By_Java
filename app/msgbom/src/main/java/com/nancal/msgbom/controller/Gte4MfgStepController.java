package com.nancal.msgbom.controller;

import com.nancal.api.model.*;
import com.nancal.api.model.common.ValidList;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.*;
import com.nancal.msgbom.service.impl.BOMNodeDomainServiceImpl;
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

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/Gte4MfgStep")
@Api(tags = "工步管理")
public class Gte4MfgStepController {

    @Qualifier("gte4MfgStepDomainServiceImpl")
    @Autowired
    private IGte4MfgStepDomainService service;
    @Autowired
    private IBOMNodeDomainService bomNodeDomainService;

    @PreAuthorize
    @ApiOperation(value = "新增")
    @PostMapping(value = "/create")
    public Response<List<BusinessObjectResp>> create(@RequestBody @Validated(AddGroup.class) ValidList<Gte4MfgStepBomReq> reqs) {
        List<BusinessObjectResp> resps = new ArrayList<>();
        reqs.stream().forEach(req -> resps.add(service.saveBomReq(req)));
        return Response.of(resps);
    }

    @PreAuthorize
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated(UpdateGroup.class) Gte4MfgStepBomReq req) {
        BomEditRevisionResp update = (BomEditRevisionResp) service.update(req);
        WorkspaceObjectResp objectResp = update.getObjectResp();
        //调用维护工时的方法
        bomNodeDomainService.fullTaskTime(4,objectResp.getUid(),objectResp.getObjectType());
        return Response.of(update);
    }

    @ApiOperation(value = "详情")
    @PostMapping("/details")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getObject(id));
    }

    @ApiOperation(value = "工步模糊查询")
    @PostMapping(value = "/getByLike")
    public TableResponse<WorkspaceObjectResp> getByLike(@RequestBody @Validated(LikeGroup.class) TableRequest<Gte4MfgStepReq> req) {
        return service.findLike(req);
    }

    @PreAuthorize
    @ApiOperation(value = "克隆工步")
    @PostMapping("/cloneStep")
    public Response<BusinessObjectResp> cloneStep(@RequestBody @Validated(AddGroup.class) Gte4MfgStepCloneReq req) {
        return Response.of(service.cloneStep(req));
    }

    @PreAuthorize
    @ApiOperation(value = "升版")
    @PostMapping("/upgrade")
    public Response<BusinessObjectResp> upgrade(@RequestBody @Validated(AddGroup.class) Gte4MfgStepUpgradeReq req) {
        return Response.of(service.stepUpgrade(req));
    }

}
