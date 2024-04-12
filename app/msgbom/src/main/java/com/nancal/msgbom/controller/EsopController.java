package com.nancal.msgbom.controller;

import cn.hutool.core.util.ObjectUtil;
import com.nancal.api.model.*;
import com.nancal.common.base.Response;
import com.nancal.common.enums.ErrorCode;
import com.nancal.msgbom.service.IEsopDomainServiceAdaptor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Validated
@RestController
@RequestMapping("/ESOP")
@Api(tags = "ESOP管理")
public class EsopController {

    @Autowired
    private IEsopDomainServiceAdaptor service;

    @ApiOperation(value = "生成ESOP")
    @PostMapping("/generateWI")
    public Response generateWI(@RequestBody @Validated EsopCreateReq req) {
        List<String> taskIds = service.creatEsop(req);
        boolean anyMatch = Optional.ofNullable(taskIds).orElse(Collections.emptyList()).stream().anyMatch(taskId -> service.executePython(taskId) != 0);
        if (anyMatch) {
            return Response.of(ErrorCode.FAIL, "线平衡异常");
        }
        return Response.of();
    }

    @ApiOperation(value = "获取线体工艺的最大工位数量")
    @PostMapping("/findMaxStationNum")
    public Response<Integer> findMaxStationNum(@RequestBody @Validated FindReq req) {
        req.setDeep(2);
        return Response.of(service.findMaxStationNum(req, true));
    }

    @ApiOperation(value = "获取线平衡柱状图")
    @GetMapping("/findLineBalancePic")
    public Response<List<EsopGaProcessStationResp>> findLineBalancePic(@ApiParam(name = "uid", value = "线体工艺版本id", required = true)
                                                                       @RequestParam("uid") String uid) {
        return Response.of(service.findLineBalancePic(uid));
    }

    @ApiOperation(value = "获取线平衡后左侧树")
    @PostMapping("/findLineBalanceAfterTree")
    public Response<BOMNodeResp> findLineBalanceAfterTree(@RequestBody @Validated FindReq req) {
        req.setDeep(-1);
        return Response.of(service.findLineBalanceAfterTree(req));
    }


    @ApiOperation(value = "获取线平衡柱状图")
    @GetMapping("/findLineBalancePicFront")
    public Response<List<EsopGaProcessStationResp>> findLineBalancePicFront(@ApiParam(name = "uid", value = "线体工艺版本id", required = true)
                                                                       @RequestParam("uid") String uid) {
        return Response.of(service.findLineBalancePicFront(uid));
    }

    @ApiOperation(value = "查询/刷新/新增刷新")
    @PostMapping("/find")
    public Response<List<BOMNodeResp>> find(@RequestBody @Validated FindReq req) {
        if(req.getDeep()==1){
            req.setDeep(-1);
        }
        BOPNodeViewResp viewResp = service.find(req);
        return Response.of(ObjectUtil.isNull(viewResp)?null:Collections.singletonList(viewResp));
    }

    @ApiOperation(value = "工序esop卡片")
    @PostMapping("/esopCard")
    public Response<List<BOMNodeResp>> esopCard(@RequestBody @Validated FindReq id) {
        id.setDeep(-1);
        BOPNodeViewResp viewResp = service.esopCard(id);
        if(ObjectUtil.isNull(viewResp)){
            Response.of(null);
        }
        return Response.of(ObjectUtil.isNull(viewResp)?null:Collections.singletonList(viewResp));
    }

}
