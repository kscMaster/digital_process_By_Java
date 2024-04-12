package com.nancal.msgbom.controller;

import cn.hutool.core.util.ObjectUtil;
import com.nancal.api.model.*;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.*;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.model.entity.Gte4MfgProcessRevisionEntity;
import com.nancal.remote.vo.ProcessTaskNodeVo;
import com.nancal.service.service.IGte4MfgProcessRevisionDomainService;
import com.nancal.service.service.IProcessInstanceDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/Gte4MfgProcessRevision")
@Api(tags = "工艺规程版本管理")
public class Gte4MfgProcessRevisionController {

    @Qualifier("gte4MfgProcessRevisionDomainServiceImpl")
    @Autowired
    private IGte4MfgProcessRevisionDomainService service;

    @Autowired
    private IProcessInstanceDomainService iProcessInstanceDomainService;


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

    @ApiOperation(value = "修改记录查询")
    @PostMapping(value = "/updateRecordList")
    public Response<List<Gte4MfgProcessRevisionResp>> updateRecordList(@RequestBody @Validated IdRequest req) {
       List<Gte4MfgProcessRevisionResp> list = new ArrayList<>();
        return Response.of(list);
    }

    @ApiOperation(value = "分页获取页签中的详情")
    @PostMapping(value = "/getTabDetail")
    public TableResponse<IncludeOrMasterRLResp> getTabDetail(@RequestBody @Validated TableRequest<IdRequest> req) {
        return service.getTabDetail(req);
    }

    @ApiOperation(value = "删除")
    @PostMapping(value = "/delete")
    public Response<BusinessObjectResp> delete(@RequestBody @Validated IdRequest id) {
        return Response.of(service.deleteObject(id));
    }

    @ApiOperation(value = "获取页签关系的右对象")
    @PostMapping(value = "/getRightRelation")
    public Response<List<IncludeOrMasterRLResp>> getRightRelation(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getRightObjects(id));
    }

    @ApiOperation(value = "获取页签关系的左对象", notes = "根据输入参数：对象类型、对象的UID查询此对象的所有左对象。")
    @PostMapping(value = "/getLeftRelation")
    public Response<List<IncludeOrMasterRLResp>> getLeftRelation(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getLeftObjects(id));
    }

    @ApiOperation(value = "工艺规程版本模糊查询")
    @PostMapping(value = "/getByLike")
    public TableResponse<WorkspaceObjectResp> getByLike(@RequestBody @Validated TableRequest<Gte4MfgProcessRevisionReq> req) {
        Gte4MfgProcessRevisionReq data = req.getData();
        if(ObjectUtil.isNull(data)){
            data = new Gte4MfgProcessRevisionReq();
            req.setData(data);
        }
        data.setObjectType(new Gte4MfgProcessRevisionEntity().getObjectType());
        return service.multiPageLike(req);
    }

    @ApiOperation(value = "分页获取页签的流程")
    @PostMapping(value = "/getProcessInstance")
    public TableResponse<ProcessTaskNodeVo> getProcessInstance(@RequestBody @Validated TableRequest<IdRequest> req) {
        return iProcessInstanceDomainService.getProcessNodePage(req);
    }

    @ApiOperation(value = "分页获取页签的流程实例ID")
    @PostMapping(value = "/getProcessInstanceId")
    public Response<String> getProcessInstanceId(@RequestBody @Validated IdRequest id) {
        return Response.of(iProcessInstanceDomainService.getProcessInstanceId(id));
    }

    @ApiOperation(value = "关联制造目标")
    @PostMapping(value = "/manufacturingTargets")
    @Transactional
    public Response<MfgTargetRLResp> manufacturingTargets(@RequestBody @Validated ShearReq req) {
        return Response.of(service.manufacturingTargets(req));
    }

    @ApiOperation(value = "校验关联制造目标是否存在关联关系")
    @PostMapping(value = "/verifyManufacturingTargets")
    public Response<WorkspaceObjectResp> verifyManufacturingTargets(@RequestBody @Validated IdRequest id) {
        return Response.of(service.verifyManufacturingTargets(id));
    }
//
//    @ApiOperation(value = "导出")
//    @PostMapping(value = "/exportBOM")
//    public Response<String> exportBOM(@RequestBody @Validated ExportBOMReq req, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
//        service.structureExport(req,modelMap,request,response, AppNameEnum.MSGBOM,"工艺规程");
//       return Response.of();
//    }


    @ApiOperation(value = "升版")
    @PreAuthorize
    @PostMapping("/upgrade")
    public Response<WorkspaceObjectResp> upgrade(@RequestBody @Validated(UpdateGroup.class) Gte4MfgProcessRevisionReq req) {
        return Response.of(service.bomUpgrade(req,AppNameEnum.MSGBOM));
    }


}
