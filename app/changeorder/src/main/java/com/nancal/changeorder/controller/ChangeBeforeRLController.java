package com.nancal.changeorder.controller;

import com.nancal.api.model.ChangeBeforeRLReq;
import com.nancal.api.model.CompareReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.service.service.IChangeBeforeRLDomainService;
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
@RequestMapping("/ChangeBeforeRL")
@Api(tags = "更改前与设计零件关系 控制器")
public class ChangeBeforeRLController {

    @Autowired
    private IChangeBeforeRLDomainService service;


    @PreAuthorize
    @ApiOperation(value = "新增(粘贴)")
    @PostMapping(value = "/create")
    public Response<Boolean> create(@RequestBody @Validated(AddGroup.class) List<ChangeBeforeRLReq> reqs) {
        return Response.of(service.create(reqs));
    }

    @ApiOperation(value = "移除")
    @PostMapping("/delete")
    public Response<Boolean> delete(@RequestBody List<CompareReq> req) {
        req.forEach(data-> service.deleteBeforeRL(data));
        return Response.of(true);
    }

    @ApiOperation(value = "获取变更前数据")
    @PostMapping(value = "/getBeforeObject")
    public Response<List<WorkspaceObjectResp>> getBeforeObject(@RequestBody @Validated IdRequest id){
        return Response.of(service.getBeforeObject(id));
    }

}
