package com.nancal.followmsgbom.controller;

import cn.hutool.core.util.ObjectUtil;
import com.nancal.api.model.*;
import com.nancal.api.model.common.ValidList;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.TipServiceException;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



@Slf4j
@RestController
@RequestMapping("/BOMNode")
@Api(tags = "BOM节点管理")
public class FowMSGBOMNodeController {

    @Qualifier(value = "BOMNodeDomainServiceImpl")
    @Autowired
    protected IBOMNodeDomainService service;


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
        return Response.of(service.parseData(req,AppNameEnum.FOLLOW_MSGBOM, DictConstant.GTE4_CNSTRPROCESS_BOMSTRUCTURE));
    }

    @ApiOperation(value = "移除BOM节点")
    @PostMapping("/delBomNode")
    public Response<List<BusinessObjectResp>> delBomNode(@RequestBody @Validated ValidList<IdRequest> ids) {
        List<BusinessObjectResp> list = new ArrayList();
        ids.stream().distinct().forEach(id->  list.add(service.deleteObject(id)) );
        return Response.of(list);
    }

    @ApiOperation(value = "指派零件")
    @PreAuthorize
    @PostMapping("/appointPart")
    public Response<List<Object>> appointPart(@RequestBody @Validated(BOMNodeReq.appoint.class) ValidList<BOMNodeReq> req) {
        return Response.of(service.parseData(req, AppNameEnum.FOLLOW_MSGBOM, DictConstant.GTE4_CNSTRPROCESS_BOMSTRUCTURE));
    }

    @ApiOperation(value = "升版权限校验")
    @PostMapping(value = "/upgradeCheck")
    public Response<Boolean> upgradeCheck(@RequestBody @Validated MfgCheckReq req) {
        return Response.of(service.upgradeCheck(req));
    }

    @ApiOperation(value = "查询/刷新/新增刷新")
    @PostMapping("/find")
    public Response<List<BOMNodeResp>> find(@RequestBody @Validated FindReq id) {
        id.setDeep(-1);
        BOPNodeViewResp viewResp = service.find(BOPNodeViewResp.class, id, AppNameEnum.FOLLOW_MSGBOM);
        return Response.of(ObjectUtil.isNull(viewResp)?null: Collections.singletonList(viewResp));
    }

}
