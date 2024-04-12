package com.nancal.ebom.controller;

import com.nancal.api.model.*;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.*;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IGte4PartRevisionDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;


@Slf4j
@RestController
@RequestMapping("/Gte4PartRevision")
@Api(tags = "设计零件版本管理")
public class Gte4PartRevisionController {

    @Qualifier("gte4PartRevisionDomainServiceImpl")
    @Autowired
    private IGte4PartRevisionDomainService service;
    @Qualifier(value = "BOMNodeDomainServiceImpl")
    @Autowired
    protected IBOMNodeDomainService bomService;


    @PreAuthorize
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated(UpdateGroup.class) Gte4PartBomReq req) {
        return Response.of(service.update(req));
    }

    @ApiOperation(value = "详情")
        @PostMapping("/details")
        public Response<BusinessObjectResp> getObject(@RequestBody @Validated MfgCheckReq req) {
            return Response.of(service.getObject(req));
    }

    @ApiOperation(value = "设计零件版本模糊查询")
    @PostMapping(value = "/getByLike")
    public TableResponse<WorkspaceObjectResp> getByLike(@RequestBody @Validated(LikeGroup.class) TableRequest<Gte4PartRevisionReq> req) {
        return service.multiPageLike(req);
    }

    @ApiOperation(value = "导出")
    @PostMapping(value = "/exportBOM")
    public Response<String> exportBOM(@RequestBody @Validated ExportBOMReq req, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
//        service.structureExport(req,modelMap,request,response, AppNameEnum.EBOM,"EBOM");
        service.export(req,response, AppNameEnum.EBOM);
        return Response.of();
    }

    @ApiOperation(value = "导出")
    @PostMapping(value = "/export")
    public Response<String> export(@RequestBody @Validated ExportBOMReq req, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        service.export(req,response, AppNameEnum.EBOM);
        return Response.of();
    }

    @ApiOperation(value = "生成中间BOM")
        @PostMapping(value = "/generateMidBOM")
    public Response<WorkspaceObjectResp> generateMidBOM(@RequestBody @Validated FindReq id) {
        return Response.of(service.generateMidBOM(id));
    }


    //    @ApiOperation(value = "校验关联权限")
//    @PostMapping(value = "/verifyAssociatedPermissions")
//    public Response<Boolean> verifyAssociatedPermissions(@RequestBody @Validated BomAssociationReq req) {
//        return Response.of(true);
//    }

    @ApiOperation(value = "查找关联的板子")
    @PostMapping(value = "/findAssociatedBoards")
    public Response<WorkspaceObjectResp> findAssociatedBoards(@RequestBody @Validated IdRequest req) {
        return Response.of(service.findAssociatedBoards(req));
    }

    @ApiOperation(value = "关联板子")
    @PostMapping(value = "/associatedBoards")
    public Response<Boolean> associatedBoards(@RequestBody @Validated AssociatedBoardsReq req) {
        return Response.of(service.associatedBoards(req));
    }

    @ApiOperation(value = "升版")
    @PreAuthorize
    @PostMapping("/upgrade")
    public Response<WorkspaceObjectResp> upgrade(@RequestBody @Validated(UpdateGroup.class) Gte4PartRevisionReq req) {
        return Response.of(service.bomUpgrade(req, AppNameEnum.EBOM));
    }

    @ApiOperation(value = "升版权限校验")
    @PostMapping("/upgradeCheck")
    public Response<Boolean> upgradeCheck(@RequestBody @Validated IdRequest id) {
        return Response.of(bomService.upgradeCheck(id));
    }

}
