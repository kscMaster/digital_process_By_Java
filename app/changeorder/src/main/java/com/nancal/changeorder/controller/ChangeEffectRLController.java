package com.nancal.changeorder.controller;

import com.nancal.api.model.ChangeEffectRLReq;
import com.nancal.api.model.CompareReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.service.service.IChangeEffectRLDomainService;
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
@RequestMapping("/ChangeEffectRL")
@Api(tags = "受影响与设计零件关系 控制器")
public class ChangeEffectRLController {

    @Autowired
    private IChangeEffectRLDomainService service;


    @PreAuthorize
    @ApiOperation(value = "新增(粘贴)")
    @PostMapping(value = "/create")
    public Response<Boolean> create(@RequestBody @Validated(AddGroup.class) List<ChangeEffectRLReq> reqs) {
        return Response.of(service.create(reqs));
    }

    @ApiOperation(value = "移除")
    @PostMapping("/delete")
    public Response<Boolean> delete(@RequestBody List<CompareReq> req) {
        req.forEach(data-> service.deleteEffectRL(data));
        return Response.of(true);
    }

    @ApiOperation(value = "获取受影响数据")
    @PostMapping(value = "/getEffectObject")
    public Response<List<WorkspaceObjectResp>> getEffectObject(@RequestBody @Validated IdRequest id){
        return Response.of(service.getEffectObject(id));
    }

}
