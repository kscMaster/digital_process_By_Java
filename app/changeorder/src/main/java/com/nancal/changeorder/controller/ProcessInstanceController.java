package com.nancal.changeorder.controller;

import cn.hutool.json.JSONUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.DictUtil;
import com.nancal.changeorder.service.IProcessInstanceDomainServiceAdaptor;
import com.nancal.common.base.Response;
import com.nancal.common.schedule.ThreadCallbackPool;
import com.nancal.common.schedule.ThreadDictPool;
import com.nancal.remote.vo.DictItemVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ProcessInstance")
@Api(tags = "变更单送审")
public class ProcessInstanceController {

    @Autowired
    @Qualifier("processInstanceDomainServiceAdaptorImpl")
    private IProcessInstanceDomainServiceAdaptor serviceAdaptor;
    @Autowired
    private DictUtil dictUtil;

    @ApiOperation(value = "送审")
    @PostMapping("/submit")
    public Response<Boolean> submit(@RequestBody @Validated ProcessInstanceReq req) {
        serviceAdaptor.submit(req);
        return Response.of();
    }
}
