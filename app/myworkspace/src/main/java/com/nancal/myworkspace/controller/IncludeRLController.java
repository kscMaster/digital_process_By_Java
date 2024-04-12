package com.nancal.myworkspace.controller;

import com.nancal.api.model.*;
import com.nancal.api.model.common.ValidList;
import com.nancal.common.base.Response;
import com.nancal.service.service.IIncludeRLDomainService;
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

/**
 * @ClassName IncludeRLController
 * @Description TODO
 * @Author fuming
 * @Date 2022/4/1 19:05
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/IncludeRL")
@Api(tags = "关系管理")
public class IncludeRLController {

    @Qualifier("includeRLDomainServiceImpl")
    @Autowired
    private IIncludeRLDomainService includeRlDomainService;

    @ApiOperation(value = "获取右对象数据", notes = "根据homeId获取个人工作台下级对象")
    @PostMapping("/getIncludeByHomeId")
    public Response<List<IncludeOrMasterRLResp>> getIncludeByHomeId(@RequestBody @Validated IncludeOrMasterRlReq req){
        return Response.of(includeRlDomainService.getIncludeByHomeId(req));
    }

    @ApiOperation(value = "剪切功能", notes = "验证剪切功能并断开关系")
    @PostMapping("/shear")
    public Response<Boolean> shear(@RequestBody @Validated ShearReq req)  {
        return Response.of(includeRlDomainService.shear(req));
    }


    @ApiOperation(value = "粘贴功能", notes = "粘贴功能校验并创建关系")
    @PostMapping("/paste")
    public Response<String> paste(@RequestBody @Validated ClipboardReq req){
        includeRlDomainService.paste(req);
        return Response.of();
    }


    @ApiOperation(value = "剪切板获取数据", notes = "剪切板获取数据")
    @PostMapping("/clipboardData")
    public Response<List<ClipboardDataResp>> clipboardData(@RequestBody @Validated ValidList<LabelReq> req){
        return Response.of(includeRlDomainService.clipboardData(req));
    }

    @ApiOperation(value = "发送到工作台数据查询接口，返回零件与版本")
    @PostMapping("/getItem")
    public Response<List<IncludeOrMasterRLResp>> getItem(@RequestBody @Validated IncludeOrMasterRlReq req){
        return Response.of(includeRlDomainService.getItem(req));
    }


}
