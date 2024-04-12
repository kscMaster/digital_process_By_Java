package com.nancal.myworkspace.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.Gte4MfgProcessRevisionReq;
import com.nancal.api.model.IncludeOrMasterRLResp;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.base.*;
import com.nancal.remote.vo.ProcessTaskNodeVo;
import com.nancal.service.bo.Dataset;
import com.nancal.service.service.IGte4MfgProcessRevisionDomainService;
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
@RequestMapping("/Gte4MfgProcessRevision")
@Api(tags = "工艺规程版本管理")
public class Gte4MfgProcessRevisionController {

    @Qualifier("gte4MfgProcessRevisionDomainServiceImpl")
    @Autowired
    private IGte4MfgProcessRevisionDomainService service;
//
//    @Autowired
//    private IProcessInstanceDomainService iProcessInstanceDomainService;

    @PostMapping("/getObject")
    @ApiOperation(value = "属性")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getObject(id));
    }

    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated(UpdateGroup.class) Gte4MfgProcessRevisionReq req) {
        return Response.of(service.update(req));
    }


//
//    @ApiOperation(value = "修改记录查询")
//    @PostMapping(value = "/updateRecordList")
//    public Response<List<WorkspaceObjectResp>> updateRecordList(@RequestBody @Validated IdRequest req) {
//        return Response.of(service.updateRecordList(req));
//
//    }
//
//    @ApiOperation(value = "分页获取页签中的详情")
//    @PostMapping(value = "/getTabDetail")
//    public TableResponse<IncludeOrMasterRLResp> getTabDetail(@RequestBody @Validated TableRequest<IdRequest> req) {
//        return service.getTabDetail(req);
//    }

    @ApiOperation(value = "删除")
    @PostMapping(value = "/delete")
    public Response<BusinessObjectResp> delete(@RequestBody @Validated IdRequest id) {
        return Response.of(service.deleteObject(id));
    }

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

    @ApiOperation(value = "模糊查询")
    @PostMapping(value = "/getByLike")
    public TableResponse<WorkspaceObjectResp> getByLike(@RequestBody @Validated TableRequest<Gte4MfgProcessRevisionReq> req) {
        return service.multiPageLike(req);
    }

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

    @ApiOperation(value = "升版")
    @PostMapping(value = "/upgrade")
    public Response<WorkspaceObjectResp> upgrade(@RequestBody @Validated Gte4MfgProcessRevisionReq req){
       return Response.of( service.upgrade(req));

    }

    @PostMapping("/fileList")
    @ApiOperation(value = "文件列表")
    public Response<List<? extends BusinessObjectResp>> fileList(@RequestBody @Validated IdRequest id) {
        Dataset dataset= SpringUtil.getBean(Dataset.class);
        return Response.of(dataset.fileList(id));
    }
}
