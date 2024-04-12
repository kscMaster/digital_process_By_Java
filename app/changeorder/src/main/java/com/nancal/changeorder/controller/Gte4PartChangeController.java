package com.nancal.changeorder.controller;

import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.common.ValidList;
import com.nancal.api.model.*;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.common.base.UpdateGroup;
import com.nancal.common.base.*;
import com.nancal.common.constants.DictConstant;
import com.nancal.service.service.IGte4PartChangeDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@RestController
@RequestMapping("/Gte4PartChange")
@Api(tags = "设计零组件变更单")
public class Gte4PartChangeController {

    @Qualifier("gte4PartChangeDomainServiceImpl")
    @Autowired
    private IGte4PartChangeDomainService service;


    @ApiOperation(value = "获取设计零组件变更单页签属性")
    @PostMapping("/getTabProperties")
    public Response<BusinessObjectResp> getTabProperties(@RequestBody @Validated IdRequest id) {
       return Response.of(service.getTabProperties(id));
    }

    @ApiOperation(value = "刪除设计零组件变更单")
    @PostMapping("/deleteObject")
    public Response<String> deleteObject(@RequestBody ValidList<IdRequest> ids) {
        return Response.of(service.deleteObjects(ids));
    }

    @PreAuthorize
    @ApiOperation(value = "新增")
    @PostMapping(value = "/create")
    public Response<BusinessObjectResp> create(@RequestBody @Validated(AddGroup.class) Gte4PartChangeReq req) {
        return Response.of(service.create(req));
    }

    @ApiOperation(value = "详情")
    @PostMapping("/details")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getObject(id));
    }

    @PreAuthorize
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated(UpdateGroup.class) Gte4PartChangeReq req) {
        return Response.of(service.modify(req));
    }

    @ApiOperation(value = "获取对象")
    @PostMapping(value = "/getTabObject")
    public Response<ChangeObjectResp> getTabObject(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getTabObject(id));
    }

    @ApiOperation(value = "更改信息查询")
    @PostMapping(value = "/getChangeOrder")
    public Response<ChangeOrderResp> getChangeOrder(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getChangeOrder(id));
    }

    @PreAuthorize
    @ApiOperation(value = "更改信息保存")
    @PostMapping(value = "/saveChangeOrder")
    public Response<Boolean> saveChangeOrder(@RequestBody @Validated(UpdateGroup.class) ChangeOrderReq req) {
        return Response.of(service.saveChangeOrder(req));
    }

    @ApiOperation(value = "获取对比预览数据")
    @PostMapping(value = "/changeData")
    public Response<RunTimeChangeAttachOuterResp> changeData(@RequestBody @Validated IdRequest id) {
        return Response.of(service.changeData(id, DictConstant.PART_CHANGE_COMPARE,Gte4RunTimeChangeAttachResp.class));
//        return Response.of(Collections.singletonList(new Gte4RunTimeChangeAttachResp(s)));
    }

    @ApiOperation(value = "首页")
    @PostMapping("/find")
    public TableResponse<WorkspaceObjectResp> find(@RequestBody @Validated(LikeGroup.class) TableRequest<Gte4PartChangeReq> req) {
        return service.find(req);
    }

}
