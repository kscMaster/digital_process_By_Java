package com.nancal.followmsgbom.controller;

import cn.hutool.core.util.ObjectUtil;
import com.nancal.api.model.*;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.*;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.model.entity.Gte4CnstrProcessRevisionEntity;
import com.nancal.service.service.IGte4MfgProcessRevisionDomainService;
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
@RequestMapping("/Gte4CnstrProcessRevision")
@Api(tags = "随工工艺规程版本管理")
public class Gte4CnstrProcessRevisionController {

    @Qualifier("gte4MfgProcessRevisionDomainServiceImpl")
    @Autowired
    private IGte4MfgProcessRevisionDomainService service;


    @PostMapping("/getObject")
    @ApiOperation(value = "详情")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getObject(id));
    }

    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated(UpdateGroup.class) Gte4CnstrProcessRevisionReq req) {
        return Response.of(service.update(req));
    }

    @ApiOperation(value = "随工工艺规程版本模糊查询")
    @PostMapping(value = "/getByLike")
    public TableResponse<WorkspaceObjectResp> getByLike(@RequestBody @Validated TableRequest<Gte4CnstrProcessRevisionReq> req) {
        Gte4CnstrProcessRevisionReq data = req.getData();
        if (ObjectUtil.isNull(data)) {
            data = new Gte4CnstrProcessRevisionReq();
            req.setData(data);
        }
        data.setObjectType(new Gte4CnstrProcessRevisionEntity().getObjectType());
        return service.multiPageLike(req);
    }

    @ApiOperation(value = "升版")
    @PreAuthorize
    @PostMapping("/upgrade")
    public Response<WorkspaceObjectResp> upgrade(@RequestBody @Validated(UpdateGroup.class) Gte4CnstrProcessRevisionReq req) {
        return Response.of(service.bomUpgrade(req, AppNameEnum.FOLLOW_MSGBOM));
    }

    @ApiOperation(value = "更新制造目标")
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

}
