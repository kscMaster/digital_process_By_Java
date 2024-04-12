package com.nancal.msgbom.controller;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.base.Response;
import com.nancal.common.config.TokenInfo;
import com.nancal.common.constants.Constant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.service.service.IBOMNodeSnapshotDomainService;
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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/BOMNodeSnapshot")
@Api(tags = "快照管理")
public class BOMNodeSnapshotController {

    @Autowired
    protected IBOMNodeSnapshotDomainService service;

    @ApiOperation(value = "快照")
    @PostMapping(value = "/snapshot")
    public Response<Boolean> snapshot(@RequestBody @Validated FindReq req) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String token = (String)requestAttributes.getAttribute(Constant.TOKEN, RequestAttributes.SCOPE_REQUEST);
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setTenantId(userUtils.getTenantId());
        tokenInfo.setToken(token);
        service.snapshot(req,AppNameEnum.MSGBOM,tokenInfo);
        return Response.of(true);
    }

    @ApiOperation(value = "快照列表")
    @PostMapping(value = "/list")
    public Response<List<WorkspaceObjectResp>> list(@RequestBody @Validated FindReq req) {
        return Response.of(service.list(req,AppNameEnum.MSGBOM));
    }


}
