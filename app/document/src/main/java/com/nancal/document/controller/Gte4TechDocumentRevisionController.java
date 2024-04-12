package com.nancal.document.controller;

import com.nancal.api.model.*;
import com.nancal.api.model.common.ValidList;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.*;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.service.service.IGte4TechDocumentRevisionDomainService;
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


@Slf4j
@RestController
@RequestMapping("/Gte4TechDocumentRevision")
@Api(tags = "技术文档版本管理")
public class Gte4TechDocumentRevisionController {

    @Qualifier("gte4TechDocumentRevisionDomainServiceImpl")
    @Autowired
    private IGte4TechDocumentRevisionDomainService service;


    @PreAuthorize
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated(UpdateGroup.class) Gte4TechDocumentBomReq req) {
        return Response.of(service.update(req));
    }

    @PreAuthorize
    @ApiOperation(value = "少量属性修改")
    @PostMapping(value = "/updateFew")
    public Response<BusinessObjectResp> updateFew(@RequestBody @Validated(Gte4TechDocumentRevisionReq.FewUpdateGroup.class) Gte4TechDocumentBomReq req) {
        return Response.of(service.update(req));
    }

    @PreAuthorize
    @ApiOperation(value = "富文本编辑")
    @PostMapping(value = "/updateRichText")
    public Response<BusinessObjectResp> updateRichText(@RequestBody @Validated(UpdateGroup.class) Gte4MfgStepContentReq req) {
        return Response.of(service.updateRichText(req));
    }

    @ApiOperation(value = "详情")
        @PostMapping("/details")
        public Response<BusinessObjectResp> getObject(@RequestBody @Validated MfgCheckReq req) {
            return Response.of(service.getObject(req));
    }

    @ApiOperation(value = "模糊查询")
    @PostMapping(value = "/like")
    public TableResponse<WorkspaceObjectResp> getByLike(@RequestBody @Validated(LikeGroup.class) TableRequest<Gte4TechDocumentRevisionReq> req) {
        return service.multiPageLike(req);
    }

    @ApiOperation(value = "升版")
    @PreAuthorize
    @PostMapping("/upgrade")
    public Response<WorkspaceObjectResp> upgrade(@RequestBody @Validated(UpdateGroup.class) Gte4TechDocumentRevisionReq req) {
        return Response.of(service.bomUpgrade(req, AppNameEnum.DOCUMENTBOM));
    }

    @ApiOperation(value = "升版权限校验")
    @PostMapping("/upgradeCheck")
    public Response<Boolean> upgradeCheck(@RequestBody @Validated IdRequest id) {
        return Response.of(service.upgradeCheck(id));
    }

    @ApiOperation(value = "少量属性升版")
    @PreAuthorize
    @PostMapping("/upgradeFew")
    public Response<WorkspaceObjectResp> upgradeFew(@RequestBody @Validated(Gte4TechDocumentRevisionReq.FewUpdateGroup.class) Gte4TechDocumentRevisionReq req) {
        return Response.of(service.bomUpgrade(req, AppNameEnum.DOCUMENTBOM));
    }

    @ApiOperation(value = "章节任务指派")
    @PreAuthorize
    @PostMapping("/taskAppoint")
    public Response<Boolean> taskAppoint(@RequestBody @Validated TaskAppointReq req) {
        return Response.of(service.taskAppoint(req));
    }

    @ApiOperation(value = "章节任务指派编辑权限校验")
    @PostMapping(value = "/checkItemAndRev")
    public Response<Boolean> checkItemAndRev(@RequestBody @Validated ValidList<IdRequest> ids) {
        return Response.of(service.checkItemAndRev(ids));
    }

}
