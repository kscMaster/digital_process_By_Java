package com.nancal.document.controller;

import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.Gte4TechDocumentBomReq;
import com.nancal.api.model.Gte4TechDocumentCloneReq;
import com.nancal.api.model.Gte4TechDocumentRevisionReq;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.Response;
import com.nancal.service.service.IGte4TechDocumentDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/Gte4TechDocument")
@Api(tags = "技术文档管理")
public class Gte4TechDocumentController {

    @Autowired
    private IGte4TechDocumentDomainService service;

    @PreAuthorize
    @ApiOperation(value = "新增")
    @PostMapping(value = "/create")
    public Response<BusinessObjectResp> create(@RequestBody @Validated(AddGroup.class) Gte4TechDocumentBomReq req) {
        return Response.of(service.saveBomReq(req));
    }

    @PreAuthorize
    @ApiOperation(value = "少量属性新增")
    @PostMapping(value = "/createFew")
    public Response<BusinessObjectResp> createFew(@RequestBody @Validated(Gte4TechDocumentRevisionReq.FewAddGroup.class) Gte4TechDocumentBomReq req) {
        return Response.of(service.saveBomReq(req));
    }

    @PreAuthorize
    @ApiOperation(value = "克隆技术文档")
    @PostMapping("/cloneTechDocument")
    public Response<BusinessObjectResp> cloneTechDocument(@RequestBody @Validated(AddGroup.class) Gte4TechDocumentCloneReq req) {
        return Response.of(service.cloneTechDocument(req));
    }

    @PreAuthorize
    @ApiOperation(value = "少量属性克隆")
    @PostMapping("/cloneTechDocumentFew")
    public Response<BusinessObjectResp> cloneTechDocumentFew(@RequestBody @Validated(Gte4TechDocumentRevisionReq.FewAddGroup.class) Gte4TechDocumentCloneReq req) {
        return Response.of(service.cloneTechDocument(req));
    }

}
