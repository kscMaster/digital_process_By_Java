package com.nancal.home.controller;


import cn.hutool.json.JSONUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.DictUtil;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.annotation.TimeLog;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.schedule.ThreadDictPool;
import com.nancal.remote.vo.DictItemVo;
import com.nancal.remote.vo.ProcessTaskNodeVo;
import com.nancal.service.service.IItemRevisionDomainService;
import com.nancal.service.service.IProcessInstanceDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/processInstance")
@Api(tags = "送审、发布流程")
public class ProcessInstanceController {

    @Autowired
    private DictUtil dictUtil;

    @Autowired
    private IProcessInstanceDomainService iProcessInstanceDomainService;

    @ApiOperation(value = "发布的回调")
    @PostMapping(value = "/quickReleaseCallback")
    public Response<ProcessInstanceResp> quickReleaseCallback(@RequestBody @Validated ProcessInstanceCallbackReq req) {

        iProcessInstanceDomainService.processCallback(req);
//        try {
//            ThreadDictPool.exec(() -> {
//                try {
//                    iProcessInstanceDomainService.processCallback(req);
//                } catch (Exception e) {
//                    log.warn("工作流回调执行失败,", e);
//                }
//            }, "工作流回调");
//        }catch (Exception e){
//            e.printStackTrace();
//            System.out.println(JSONUtil.toJsonStr(req));
//        }
        return Response.of(null);
    }

    @ApiOperation(value = "流程列表")
    @GetMapping(value = "/getProcessListByObjectType")
    public Response<List<DictItemVo>> getProcessListByObjectType(String objectType) {
        return Response.of(dictUtil.getProcessListByObjectType(objectType));
    }

    @ApiOperation(value ="发布")
    @PreAuthorize
    @PostMapping(value = "/release")
    public Response<ProcessInstanceResp> release(@RequestBody @Validated ProcessInstanceReq req){
        AppNameEnum appNameEnum = AppNameEnum.getAppNameEnum(req.getAppName());
        return Response.of(iProcessInstanceDomainService.release(req,appNameEnum));
    }

    @ApiOperation(value ="获取审核信息")
    @PostMapping(value = "/auditInformation")
    public Response<BusinessObjectResp> auditInformation(@RequestBody @Validated IdRequest id){
        return Response.of(iProcessInstanceDomainService.auditInformation(id.getUid()));
    }

    @ApiOperation(value = "获取页签的流程实例ID")
    @PostMapping(value = "/getProcessInstanceId")
    public Response<String> getProcessInstanceId(@RequestBody @Validated IdRequest id) {
        return Response.of(iProcessInstanceDomainService.getProcessInstanceId(id));
    }


    @ApiOperation(value = "分页获取页签的流程")
    @PostMapping(value = "/getProcessInstance")
    public TableResponse<ProcessTaskNodeVo> getProcessInstance(@RequestBody @Validated TableRequest<IdRequest> req) {
        //版本、数据集、工步
        return iProcessInstanceDomainService.getProcessNodePage(req);

    }

    @TimeLog
    @ApiOperation(value = "送审或发布的校验")
    @PostMapping(value = "/submitVerification")
    public Response<String> submitVerification(@RequestBody @Validated ProcessInstanceReq req){
        AppNameEnum appNameEnum = AppNameEnum.getAppNameEnum(req.getAppName());
        iProcessInstanceDomainService.submitVerification(req,appNameEnum);
        return Response.of();
    }

    @ApiOperation(value ="获取通知单位审核信息")
    @PostMapping(value = "/noticeAuditInformation")
    public Response<NoticeAuditInformationResp> noticeAuditInformation(@RequestBody @Validated IdRequest id){
        return Response.of(iProcessInstanceDomainService.noticeAuditInformation(id));
    }


}
