package com.nancal.inspectionlibrary.controller;

import com.nancal.api.model.*;
import com.nancal.api.model.common.ValidList;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.*;
import com.nancal.service.service.IGte4InspectionDomainService;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.service.service.IGte4InspectionRevisionDomainService;
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

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Gte4InspectionRevision")
@Api(tags = "检验项目版本")
public class Gte4InspectionRevisionController {
    @Qualifier("gte4InspectionRevisionDomainServiceImpl")
    @Autowired
    private IGte4InspectionRevisionDomainService service;

    @Qualifier("gte4InspectionDomainServiceImpl")
    @Autowired
    private IGte4InspectionDomainService iGte4InspectionDomainService;

    @Autowired
    private IProcessInstanceDomainService iProcessInstanceDomainService;

    @PreAuthorize
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated(UpdateGroup.class) Gte4InspectionRevisionReq req) {
        return Response.of(service.updatePlus(req));
    }

    @PreAuthorize
    @ApiOperation(value ="发布")
    @PostMapping(value = "/release")
    public Response<ProcessInstanceResp> release(@RequestBody @Validated ProcessInstanceReq req){
        return Response.of(iProcessInstanceDomainService.release(req,AppNameEnum.INSPECTION_LIBRARY));
    }

    @PostMapping("/getObject")
    @ApiOperation(value = "属性")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getObject(id));
    }

    @PreAuthorize
    @PostMapping("/upgrade")
    @ApiOperation(value = "升版")
    public Response<WorkspaceObjectResp> upgrade(@RequestBody @Validated(UpdateGroup.class) Gte4InspectionRevisionReq req) {
        return Response.of(service.upgradeAndDataSet(req));
    }

    @PostMapping("/likePage")
    @ApiOperation(value = "模糊查询")
    public TableResponse<WorkspaceObjectResp> likePage(@RequestBody @Validated(LikeGroup.class) TableRequest<Gte4InspectionRevisionReq> req) {
        return service.pageAndFiles(req);
    }

    @PostMapping("/export")
    @ApiOperation(value = "模糊查询导出")
    public void export(@RequestBody @Validated(LikeGroup.class) TableRequest<Gte4InspectionRevisionReq> req, HttpServletResponse response) {
        iGte4InspectionDomainService.likeExport(req,response);
    }


        @PreAuthorize
    @ApiOperation(value = "删除")
    @PostMapping(value = "/delete")
    public Response<List<BusinessObjectResp>> delete(@RequestBody @Validated ValidList<IdRequest> ids) {
        List<BusinessObjectResp> list = new ArrayList<>();
        ids.forEach(id->{
            BusinessObjectResp businessObjectResp = service.deleteObject(id);
            list.add(businessObjectResp);
        });
        return Response.of(list);
    }


}
