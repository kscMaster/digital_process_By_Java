package com.nancal.midbom.controller;

import cn.hutool.core.util.ObjectUtil;
import com.nancal.api.model.*;
import com.nancal.common.base.Response;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.service.service.IBOMNodeDomainService;
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
import java.util.Collections;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/BOMNode")
@Api(tags = "BOM节点管理")
public class MIDBOMNodeController {

    @Qualifier(value = "BOMNodeDomainServiceImpl")
    @Autowired
    protected IBOMNodeDomainService service;


    @ApiOperation(value = "查询/刷新/新增刷新")
    @PostMapping("/find")
    public Response<List<BOMNodeResp>> find(@RequestBody @Validated FindReq req) {
        req.setDeep(-1);
        BOMNodeResp viewResp = service.find(BOMNodeResp.class, req, AppNameEnum.MIDBOM);
        return Response.of(ObjectUtil.isNull(viewResp)?null:Collections.singletonList(viewResp));
    }


}
