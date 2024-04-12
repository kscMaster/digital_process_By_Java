package com.nancal.library.controller;

import com.nancal.api.model.BusinessObjectReq;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.api.model.common.*;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.service.service.IWorkspaceObjectDomainService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

public class BaseController<Biz extends IWorkspaceObjectDomainService, Req extends BusinessObjectReq> {

    @Autowired(required = false)
    protected HttpServletRequest request;
    @Autowired(required = false)
    protected Biz biz;

    @ApiOperation(value = "新增")
    @PostMapping(value = "/create")
    public Response<BusinessObjectResp> create(@RequestBody @Validated Req req) {
        return Response.of(biz.save(req));
    }

    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated Req req) {
        return Response.of(biz.update(req));
    }

    @ApiOperation(value = "删除")
    @PostMapping(value = "/delete")
    public Response<BusinessObjectResp> delete(@RequestBody @Validated IdRequest id) {
        return Response.of(biz.deleteObject(id));
    }

    @ApiOperation(value = "详情")
    @PostMapping(value = "/getObject")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated IdRequest id) {
        return Response.of(biz.getObject(id));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/page")
    public TableResponse<WorkspaceObjectResp> page(@RequestBody @Validated TableRequest req) {
        return biz.page(req);
    }

    @ApiOperation(value = "获取业务对象单属性")
    @PostMapping(value = "/getProperty")
    public Response<String> getProperty(@RequestBody @Validated BusinessSinglePropertyReq req) {
        return Response.of(biz.getProperty(req));
    }

    @ApiOperation(value = "获取业务对象多属性")
    @PostMapping(value = "/getProperties")
    public Response<Map<Object, Object>> getProperties(@RequestBody @Validated BusinessMultiPropertyReq req) {
        return Response.of(biz.getProperties(req));
    }

    @ApiOperation(value = "复制对象")
    @GetMapping(value = "/saveAs")
    public Response saveAs(@RequestParam(value = "uid") @NotBlank(message = "主键id不能为空") String uid) {
        return Response.of(biz.saveAs(uid));
    }

    @ApiOperation(value = "获取业务对象的所有属性名称")
    @GetMapping(value = "/getAllPropertyNames")
    public Response<List<String>> getAllPropertyNames() {
        return Response.of(biz.getAllPropertyNames());
    }

}
