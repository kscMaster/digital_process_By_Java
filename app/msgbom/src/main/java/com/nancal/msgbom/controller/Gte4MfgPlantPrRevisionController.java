package com.nancal.msgbom.controller;


import com.nancal.api.model.*;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.Gte4MfgPlantPrRevisionReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.*;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.service.service.IGte4MfgPlantPrRevisionDomainService;
import com.nancal.service.service.IWorkspaceObjectDomainService;
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
@RequestMapping("/Gte4MfgPlantPrRevision")
@Api(tags = "工厂工艺版本管理")
public class Gte4MfgPlantPrRevisionController {

    @Qualifier("gte4MfgPlantPrRevisionDomainServiceImpl")
    @Autowired
    private IGte4MfgPlantPrRevisionDomainService service;

    @Qualifier("gte4MfgStepDomainServiceImpl")
    @Autowired
    private IWorkspaceObjectDomainService iService;


    @PreAuthorize
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated(UpdateGroup.class) Gte4MfgPlantPrRevisionReq req) {
        return Response.of(service.update(req));
    }

    @ApiOperation(value = "详情")
    @PostMapping("/details")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getObject(id));
    }

    @ApiOperation(value = "获取工厂")
    @PostMapping("/getFactory")
    public Response<List<WorkspaceObjectResp>> getFactory(@RequestBody @Validated Gte4PlantRevisionReq req) {
        TableRequest tableRequest = new TableRequest<>();
        tableRequest.setData(req);
        tableRequest.setSkip(1);
        tableRequest.setTake(999999);
        TableResponse<WorkspaceObjectResp> workspaceObjectRespTableResponse = service.multiPageLike(tableRequest);
        return  Response.of(workspaceObjectRespTableResponse.getData());
    }

    @ApiOperation(value = "工厂工艺模糊查询")
    @PostMapping(value = "/getByLike")
    public TableResponse<WorkspaceObjectResp> getByLike(@RequestBody @Validated(LikeGroup.class) TableRequest<Gte4MfgPlantPrRevisionReq> req) {
        return service.multiPageLike(req);
    }

    @ApiOperation(value = "升版")
    @PreAuthorize
    @PostMapping("/upgrade")
    public Response<WorkspaceObjectResp> upgrade(@RequestBody @Validated(UpdateGroup.class) Gte4MfgPlantPrRevisionReq req) {
        return Response.of(service.bomUpgrade(req, AppNameEnum.MSGBOM));
    }

    @ApiOperation(value = "获取封面")
    @PostMapping("/getCoverPage")
    public Response<WorkspaceObjectResp> getCoverPage(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getCoverPage(id));
    }

}
