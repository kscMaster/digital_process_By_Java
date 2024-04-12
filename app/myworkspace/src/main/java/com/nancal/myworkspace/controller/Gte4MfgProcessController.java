package com.nancal.myworkspace.controller;

import com.nancal.api.model.*;
import com.nancal.common.base.*;
import com.nancal.remote.vo.ProcessTaskNodeVo;
import com.nancal.service.service.IGte4MfgProcessDomainService;
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

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/Gte4MfgProcess")
@Api(tags = "工艺规程管理")
public class Gte4MfgProcessController {

    @Qualifier("gte4MfgProcessDomainServiceImpl")
    @Autowired
    private IGte4MfgProcessDomainService service;
//
//    @Autowired
//    private IProcessInstanceDomainService iProcessInstanceDomainService;

    @ApiOperation(value = "新增")
    @PostMapping(value = "/create")
    public Response<WorkspaceObjectResp> create(@RequestBody @Validated(AddGroup.class) Gte4MfgProcessRevisionReq req) {
        req.setObjectType(null);
        return Response.of(service.save(req));
    }

    @PostMapping("/getObject")
    @ApiOperation(value = "属性")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getObject(id));
    }

//    @ApiOperation(value = "修改记录查询")
//    @PostMapping(value = "/updateRecordList")
//    public Response<List<WorkspaceObjectResp>> updateRecordList(@RequestBody @Validated IdRequest req) {
//        return Response.of(service.updateRecordList(req));
//    }

    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated(UpdateGroup.class) Gte4MfgProcessReq req) {
        return Response.of(service.update(req));
    }

//    @ApiOperation(value = "分页获取页签中的详情")
//    @PostMapping(value = "/getTabDetail")
//    public TableResponse<IncludeOrMasterRLResp> getTabDetail(@RequestBody @Validated TableRequest<IdRequest> req) {
//        return service.getTabDetail(req);
//    }
//
//    @ApiOperation(value = "获取页签关系的右对象")
//    @PostMapping(value = "/getRightRelation")
//    public Response<List<IncludeOrMasterRLResp>> getRightRelation(@RequestBody @Validated IdRequest id) {
//        return Response.of(service.getRightObjects(id));
//    }
//
//    @ApiOperation(value = "获取页签关系的左对象", notes = "根据输入参数：对象类型、对象的UID查询此对象的所有左对象。")
//    @PostMapping(value = "/getLeftRelation")
//    public Response<List<IncludeOrMasterRLResp>> getLeftRelation(@RequestBody @Validated IdRequest id) {
//        return Response.of(service.getLeftObjects(id));
//    }
//
//    @ApiOperation(value = "分页获取页签的流程")
//    @PostMapping(value = "/getProcessInstance")
//    public TableResponse<ProcessTaskNodeVo> getProcessInstance(@RequestBody @Validated TableRequest<IdRequest> req) {
//        return iProcessInstanceDomainService.getProcessNodePage(req);
//    }
//
//    @ApiOperation(value = "分页获取页签的流程实例ID")
//    @PostMapping(value = "/getProcessInstanceId")
//    public Response<String> getProcessInstanceId(@RequestBody @Validated IdRequest id) {
//        return Response.of(iProcessInstanceDomainService.getProcessInstanceId(id));
//    }

    @PostMapping("/getLatestRevision")
    @ApiOperation(value = "获取最新零组件版本")
    public Response<BusinessObjectResp> getLatestRevision(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getLatestRevision(id));
    }
}
