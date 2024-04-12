package com.nancal.ebom.controller;

import com.nancal.api.model.*;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.common.ValidList;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
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
@RequestMapping("/BOMNode")
@Api(tags = "BOM节点管理")
public class BOMNodeController {

//    @Qualifier("bOMNodeDomainServiceImpl")
//    @Autowired
//    protected IBOMNodeDomainServiceAdaptor service;
//
//    @ApiOperation(value = "查询BOM行详情")
//    @PostMapping(value = "/findDetails")
//    public Response<NodeViewResp> findDetails(@RequestBody @Validated FindDetailsReq req) {
//        return Response.of(service.findDetails(req));
//    }
//
//    @ApiOperation(value = "首页查询")
//    @PostMapping(value = "/find")
//    public Response<List<BusinessObjectResp>> find(@RequestBody @Validated FindReq req) {
//        return Response.of(service.find(req));
//    }
//
//    @ApiOperation(value = "BOM对比")
//    @PostMapping(value = "/compare")
//    public Response<CompareResp> compare(@RequestBody @Validated CompareReq req) {
//        return Response.of(service.compare(req));
//    }
//
//    @ApiOperation(value = "编辑权限校验")
//    @PostMapping(value = "/check")
//    public Response<CheckResp> check(@RequestBody @Validated CheckReq req) {
//        CheckResp check = service.check(req);
//        if (!check.getAuxMaterialCheckPower()&&!check.getPriMaterialCheckPower()&&!check.getQuantityCheckPower()&&!check.getItemIdAndModelNoCheckPower()) {
//            throw new ServiceException(ErrorCode.FAIL, "您没有编辑权限，操作无法完成");
//        }
//        return Response.of(check);
//    }
//
//
//    @ApiOperation(value = "批量保存")
//    @PostMapping("/update")
//    public Response<Boolean> update(@RequestBody @Validated ValidList<NodeViewReq> reqs){
//        return Response.of(service.updateData(reqs));
//    }
//
//    @ApiOperation(value = "生成PBOM校验")
//    @PostMapping("/producePbomCheck")
//    public Response<EBOMCheckResp> producePbomCheck(@RequestBody @Validated IdRequest id){
//        return Response.of(service.producePbomCheck(id));
//    }
//
//    @ApiOperation(value = "生成PBOM")
//    @PostMapping("/producePbom")
//    public Response<EBOMCheckResp> producePbom(@RequestBody @Validated IdRequest id){
//        return Response.of(service.producePbom(id));
//    }
//
//    @ApiOperation(value = "新增BOM节点")
//    @PostMapping("/saveBomNode")
//    public Response<Boolean> saveBomNode(@RequestBody @Validated ValidList<BOMNodeReq> req) {
//        return Response.of(service.createNode(req, AppNameEnum.MSGBOM));
//    }
//
//    @ApiOperation(value = "剪切BOM节点")
//    @PostMapping("/cutBomNode")
//    public Response<Boolean> cutBomNode(@RequestBody @Validated ValidList<IdRequest> req) {
//        return Response.of(service.deleteObject(req));
//    }
//
//    @ApiOperation(value = "删除BOM节点")
//    @PostMapping("/delBomNode")
//    public Response<Boolean> delBomNode(@RequestBody @Validated ValidList<IdRequest> req) {
//        return Response.of(service.deleteObject(req));
//    }
//
//    @ApiOperation(value = "编辑BOM节点及关联材料信息")
//    @PostMapping("/updateBomNode")
//    public Response<Boolean> updateBomNode(@RequestBody @Validated NodeViewReq req) {
//        return Response.of(service.updateBomNode(req));
//    }
}
