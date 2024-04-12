package com.nancal.msgbom.controller;

import com.nancal.api.model.DeleteAssociationReq;
import com.nancal.api.model.RelatedProcessReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.service.service.ICreateAssociationDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Association")
@Api(tags = "关联关系的控制器")
public class CreateAssociationController {
    @Autowired
    private ICreateAssociationDomainService service;


    @ApiOperation(value = "关联工厂/线体")
    @PostMapping(value = "/associativeTop")
    public Response<Boolean> associativeTop(@RequestBody @Validated RelatedProcessReq req) {
        service.associativeTop(req);
        return Response.of(true);
    }

    @ApiOperation(value = "查找已关联的工厂/线体")
    @PostMapping(value = "/linked")
    public Response<List<WorkspaceObjectResp>> linked(@RequestBody @Validated IdRequest id) {
        return Response.of( service.linked(id));
    }

    @ApiOperation(value = "删除工厂/线体关联关系")
    @PostMapping(value = "/deleteAssociation")
    public Response<Boolean> deleteAssociation(@RequestBody @Validated DeleteAssociationReq req) {
        service.deleteAssociation(req);
        return Response.of(true);
    }
//
//    @ApiOperation(value = "校验关联权限")
//    @PostMapping(value = "/verifyAssociatedPermissions")
//    public Response<Boolean> verifyAssociatedPermissions(@RequestBody @Validated BomAssociationReq req) {
//        return Response.of(true);
//    }

    @ApiOperation(value = "预览视图")
    @PostMapping(value = "/previewView")
    public Response<WorkspaceObjectResp> previewView(@RequestBody @Validated IdRequest id) {
        return Response.of(service.previewView(id));
    }

}
