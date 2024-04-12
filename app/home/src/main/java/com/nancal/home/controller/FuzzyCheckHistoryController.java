package com.nancal.home.controller;

import com.nancal.api.model.SearchHistoryReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.FidHistoryGroup;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.service.service.IFuzzyCheckHistoryDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/FuzzyCheckHistory")
@Api(tags = "查找历史 的控制器")
public class FuzzyCheckHistoryController {

    @Autowired
    private IFuzzyCheckHistoryDomainService service;
    @ApiOperation(value = "查找历史记录", notes = "查找历史记录")
    @PostMapping("/findHistory")
    public Response<List<WorkspaceObjectResp>> findHistory(@RequestBody @Validated(FidHistoryGroup.class) SearchHistoryReq req){
        return Response.of(service.findHistory(req));
    }

    @ApiOperation(value = "保存历史记录", notes = "保存历史记录")
    @PostMapping("/savaHistory")
    public Response<WorkspaceObjectResp> savaHistory (@RequestBody @Validated(AddGroup.class) SearchHistoryReq req){
        WorkspaceObjectResp resp = service.save(req);
        return Response.of(resp);
    }

    @ApiOperation(value = "删除历史记录", notes = "删除历史记录")
    @PostMapping("/deleteHistory")
    public Response<Boolean> deleteHistory (@RequestBody @Validated IdRequest id){
        service.deleteObject(id);
        return Response.of(Boolean.TRUE);
    }
}
