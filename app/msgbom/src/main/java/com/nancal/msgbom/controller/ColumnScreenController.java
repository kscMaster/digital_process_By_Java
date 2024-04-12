package com.nancal.msgbom.controller;

import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.ColumnScreenReq;
import com.nancal.api.model.ColumnScreenResp;
import com.nancal.common.base.Response;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.service.service.IColumnScreenDomainService;
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


/**
 * @author： Wang Hui
 * @date： 2022/4/25 17:44
 * @description： 列设置controller
 **/
@Slf4j
@RestController
@RequestMapping("/ColumnScreen")
@Api(tags = "列设置管理")
public class ColumnScreenController {

    @Qualifier("columnScreenDomainServiceImpl")
    @Autowired
    private IColumnScreenDomainService service;

    @ApiOperation(value = "查询")
    @PostMapping(value = "/find")
    public Response<ColumnScreenResp> find(String objectType) {
        return Response.of(service.find(AppNameEnum.MSGBOM,objectType));
    }

    @ApiOperation(value = "编辑")
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated ColumnScreenReq req) {
        req.setAppName(AppNameEnum.MSGBOM);
        return Response.of(service.update(req));
    }

}
