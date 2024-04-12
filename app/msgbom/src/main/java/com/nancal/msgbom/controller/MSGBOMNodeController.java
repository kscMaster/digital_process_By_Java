package com.nancal.msgbom.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.model.common.ValidList;
import com.nancal.api.model.dataset.FileAttrResp;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.TipServiceException;
import com.nancal.model.entity.*;
import com.nancal.remote.service.RemoteEsopService;
import com.nancal.remote.to.BOMNodeViewTo;
import com.nancal.service.bo.*;
import com.nancal.service.factory.*;
import com.nancal.service.service.IBOMNodeDomainService;
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

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/BOMNode")
@Api(tags = "BOM节点管理")
public class MSGBOMNodeController {

    @Autowired
    protected IBOMNodeDomainService service;
    @Autowired
    protected IBOMNodeSnapshotDomainService snapshotDomainService;


    @ApiOperation(value = "编辑权限校验")
    @PostMapping(value = "/check")
    public Response<MfgCheckResp> check(@RequestBody @Validated MfgCheckReq req) {
        MfgCheckResp check = service.check(req);
        if (!check.getIsBomCheck()&&!check.getIsObjectCheck()) {
            throw new TipServiceException(ErrorCode.FAIL,check.getCheckTip());
        }
        return Response.of(check);
    }

    @ApiOperation(value = "新增BOM节点")
    @PreAuthorize
    @PostMapping("/saveBomNode")
    public Response<List<Object>> saveBomNode(@RequestBody @Validated(BOMNodeReq.Paste.class) ValidList<BOMNodeReq> req) {
        return Response.of(service.parseData(req,AppNameEnum.MSGBOM, DictConstant.GTE4_MFGPLANTPR_BOMSTRUCTURE));
    }

    @ApiOperation(value = "编辑BOM节点属性信息")
    @PreAuthorize
    @PostMapping("/updateBomNode")
    public Response<BusinessObjectResp> updateBomNode(@RequestBody @Validated BOMNodeReq req) {
        return Response.of(service.update(req));
    }

    @ApiOperation(value = "移除BOM节点")
    @PostMapping("/delBomNode")
    public Response<List<BusinessObjectResp>> delBomNode(@RequestBody @Validated ValidList<IdRequest> ids) {
        List<BusinessObjectResp> list = new ArrayList();
        ids.stream().distinct().forEach(id->  list.add(service.deleteObject(id)) );
        return Response.of(list);
    }

    @ApiOperation(value = "查询/刷新/新增刷新")
    @PostMapping("/find")
    public Response<List<BOMNodeResp>> find(@RequestBody @Validated FindReq id) {
        if(id.getDeep()==1){
            id.setDeep(-1);
        }
        BOPNodeViewResp viewResp = service.find(BOPNodeViewResp.class, id, AppNameEnum.MSGBOM);
        return Response.of(ObjectUtil.isNull(viewResp)?null:Collections.singletonList(viewResp));
    }

    @ApiOperation(value = "查询单行数据")
    @PostMapping("/findOne")
    public Response<List<BOMNodeResp>> findOne(@RequestBody @Validated FindReq id) {
        BOPNodeViewResp viewResp = service.findOne(BOPNodeViewResp.class, id, AppNameEnum.MSGBOM);
        return Response.of(ObjectUtil.isNull(viewResp)?null:Collections.singletonList(viewResp));
    }

    @ApiOperation(value = "生成ESOP")
    @PostMapping("/generateWI")
    public Response generateWI(@RequestBody @Validated FindReq id) {
        id.setDeep(-1);
        BOPNodeViewResp viewResp = service.find(BOPNodeViewResp.class, id, AppNameEnum.MSGBOM);
        if (ObjectUtil.isNull(viewResp)) {
            return Response.of("未查询到数据，无法生成");
        }
        RemoteEsopService remoteEsopService = SpringUtil.getBean(RemoteEsopService.class);
        BOMNodeViewTo to = new BOMNodeViewTo();
        BeanUtil.copyProperties(viewResp,to);
        return remoteEsopService.esop(to);
    }

    @ApiOperation(value = "查询/刷新/新增刷新")
    @PostMapping("/findRoot")
    public Response<List<BOMNodeResp>> findRoot(@RequestBody @Validated FindReq id) {
        if(id.isSnapshot()){
            BOMNodeSnapshotResp viewResp = service.find(BOMNodeSnapshotResp.class, id, AppNameEnum.MSGBOM,true);
            return Response.of(ObjectUtil.isNull(viewResp)?null:Collections.singletonList(viewResp));
        }else{
            BOPNodeViewResp viewResp = service.find(BOPNodeViewResp.class, id, AppNameEnum.MSGBOM);
            return Response.of(ObjectUtil.isNull(viewResp)?null:Collections.singletonList(viewResp));
        }
    }

    @ApiOperation(value = "查询/刷新/新增刷新")
    @PostMapping("/findChildList")
    public Response<List<BOMNodeResp>> findChildList(@RequestBody @Validated FindReq id) {
        if(id.isSnapshot()){
            BOMNodeSnapshotResp root = service.find(BOMNodeSnapshotResp.class, id, AppNameEnum.MSGBOM,true);
            return Response.of(ObjectUtil.isNull(root)?null:root.getChildren());
        }else{
            BOPNodeViewResp root = service.find(BOPNodeViewResp.class, id, AppNameEnum.MSGBOM);
            return Response.of(ObjectUtil.isNull(root)?null:root.getChildren());
        }
    }

    @ApiOperation(value = "指派零件")
    @PreAuthorize
    @PostMapping("/appointPart")
    public Response<List<Object>> appointPart(@RequestBody @Validated(BOMNodeReq.appoint.class) ValidList<BOMNodeReq> req) {
        return Response.of(service.parseData(req, AppNameEnum.MSGBOM, DictConstant.GTE4_MFGPLANTPR_BOMSTRUCTURE));
    }

    @ApiOperation(value = "升版权限校验")
    @PostMapping(value = "/upgradeCheck")
    public Response<Boolean> upgradeCheck(@RequestBody @Validated MfgCheckReq req) {
        return Response.of(service.upgradeCheck(req));
    }

    @ApiOperation(value = "指派负责人")
    @PreAuthorize
    @PostMapping("/appointChargePerson")
    public Response<Boolean> appointChargePerson(@RequestBody @Validated TaskAppointReq req) {
        service.taskAppoint(req);
        return Response.of(true);
    }

    @ApiOperation(value = "指派负责人权限校验")
    @PostMapping(value = "/checkAppoint")
    public Response<Boolean> checkAppoint(@RequestBody @Validated ValidList<IdRequest> ids) {
        service.checkItemAndRev(ids);
        return Response.of(true);
    }

    @ApiOperation(value = "BOM对比")
    @PostMapping("/compare")
    public Response<CompareResp> compare(@RequestBody @Validated CompareReq req) {
        return Response.of(service.compare(req,AppNameEnum.MSGBOM));
    }

}
