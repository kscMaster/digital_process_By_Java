package com.nancal.home.controller;

import com.nancal.api.model.*;
import com.nancal.api.model.common.ValidList;
import com.nancal.common.base.*;
import com.nancal.model.entity.BomHistoryEntity;
import com.nancal.service.service.IBomHistoryDomainService;
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
@RequestMapping("/BomHistory")
@Api(tags = "Bom 历史记录 的控制器")
public class BomHistoryController {

    @Qualifier("bomHistoryDomainServiceImpl")
    @Autowired
    private IBomHistoryDomainService service;

    @ApiOperation(value = "保存bom结构快捷栏历史记录", notes = "保存bom结构快捷栏历史记录")
    @PostMapping("/savaBomHistory")
    public Response<WorkspaceObjectResp> savaBomHistory (@RequestBody @Validated(AddGroup.class) BomHistoryReq req){
        return Response.of(service.saveBomHistory(req));
    }

    @ApiOperation(value = "查找bom结构快捷栏历史记录", notes = "查找bom结构快捷栏历史记录")
    @PostMapping("/findBomHistory")
    public Response<List<WorkspaceObjectResp>> findBomHistory(@RequestBody @Validated(FidHistoryGroup.class)BomHistoryReq req){
        return Response.of(service.findBomHistory(req));
    }

    @ApiOperation(value = "删除bom结构快捷栏历史记录", notes = "删除bom结构快捷栏历史记录")
    @PostMapping("/deleteBomHistory")
    public Response<Boolean> deleteBomHistory (@RequestBody @Validated IdRequest req){
        service.deleteBomHistory(req);
        return Response.of(Boolean.TRUE);
    }

    @ApiOperation(value = "bom结构快捷栏历史记录模糊查询")
    @PostMapping(value = "/getBomHistoryByLike")
    public TableResponse<WorkspaceObjectResp> getByLike(@RequestBody @Validated(LikeGroup.class) TableRequest<BomHistoryReq> req) {
        return service.getByLike(req);
    }


}
