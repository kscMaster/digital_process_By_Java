package com.nancal.library.controller;

import com.nancal.api.model.*;
import com.nancal.api.model.common.ValidList;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.*;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.service.service.IEquipmentRevisionDomainService;
import com.nancal.service.service.IProcessInstanceDomainService;
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
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/EquipmentRevision")
@Api(tags = "设备版本管理")
public class EquipmentRevisionController {

    @Qualifier("equipmentRevisionDomainServiceImpl")
    @Autowired
    private IEquipmentRevisionDomainService service;

    @Autowired
    private IProcessInstanceDomainService iProcessInstanceDomainService;

    @ApiOperation(value = "修改")
    @PreAuthorize
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated(UpdateGroup.class) LibraryAttributeEquipmentReq req) {
        return Response.of(service.updatePlus(req));
    }

    @ApiOperation(value ="发布")
    @PreAuthorize
    @PostMapping(value = "/release")
    public Response<ProcessInstanceResp> release(@RequestBody @Validated ProcessInstanceReq req){
        return Response.of(iProcessInstanceDomainService.release(req,  AppNameEnum.LIBRARY));
    }

    @ApiOperation(value = "属性")
    @PostMapping("/getObject")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getObject(id));
    }

    @ApiOperation(value = "升版")
    @PreAuthorize
    @PostMapping("/upgrade")
    public Response<WorkspaceObjectResp> upgrade(@RequestBody @Validated(UpdateGroup.class) LibraryAttributeEquipmentReq req) {
        return Response.of(service.customUpgrade(req));
    }

    @PostMapping("/like")
    @ApiOperation(value = "模糊查询")
    public TableResponse<List<Map<String,Object>>> like(@RequestBody @Validated(LikeGroup.class) TableRequest<EquipmentRevisionReq> req) {
        return service.customPageLike(req);
    }

    @ApiOperation(value = "删除")
    @PreAuthorize
    @PostMapping(value = "/delete")
    public Response<List<BusinessObjectResp>> delete(@RequestBody @Validated ValidList<IdRequest> ids) {
        List<BusinessObjectResp> list = new ArrayList<>();
        ids.forEach(id->{
            list.add(service.deleteObject(id));
        });
        return Response.of(list);
    }

}
