package com.nancal.home.controller;

import com.nancal.api.model.*;
import com.nancal.api.model.common.ValidList;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.FidHistoryGroup;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.service.service.IIncludeRLDomainService;
import com.nancal.service.service.IItemRevisionDomainService;
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
@Api(tags = "IncludeRL 包含关系 的控制器")
public class IncludeRLController {

    @Qualifier("includeRLDomainServiceImpl")
    @Autowired
    private IIncludeRLDomainService includeRlDomainService;

    @Qualifier("itemRevisionDomainServiceImpl")
    @Autowired
    private IItemRevisionDomainService itemRevisionDomainService;

    @ApiOperation(value = "剪切板获取数据", notes = "剪切板获取数据")
    @PostMapping("/clipboardData")
    public Response<List<ClipboardDataResp>> clipboardData(@RequestBody @Validated ValidList<LabelReq> req){
        return Response.of(includeRlDomainService.clipboardData(req));
    }
}
