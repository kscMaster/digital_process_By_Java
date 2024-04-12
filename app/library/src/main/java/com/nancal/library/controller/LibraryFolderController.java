package com.nancal.library.controller;

import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.LibraryFolderReq;
import com.nancal.api.model.LibraryResp;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.common.base.UpdateGroup;
import com.nancal.service.service.ILibraryFolderDomainService;
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
@RequestMapping("/LibraryFolder")
@Api(tags = "资源库树结构管理管理")
public class LibraryFolderController {

    @Qualifier("libraryFolderDomainServiceImpl")
    @Autowired
    protected ILibraryFolderDomainService service;

    @ApiOperation(value = "资源库新增")
    @PreAuthorize
    @PostMapping(value = "/create")
    public Response<WorkspaceObjectResp> create(@RequestBody @Validated(AddGroup.class) LibraryFolderReq req) {
        return Response.of(service.save(req));
    }

    @ApiOperation(value = "资源库删除")
    @PreAuthorize
    @PostMapping(value = "/delete")
    public Response<BusinessObjectResp> delete(@RequestBody @Validated IdRequest id) {
        return Response.of(service.deleteObject(id));
    }

    @ApiOperation(value ="查询资源库树结构")
    @PostMapping(value = "/tree")
    public Response<List<LibraryResp>> tree() {
        return Response.of(service.treeLibrary());
    }

    @ApiOperation(value ="资源库修改")
    @PreAuthorize
    @PostMapping(value = "/update")
    public Response<BusinessObjectResp> update(@RequestBody @Validated(UpdateGroup.class) LibraryFolderReq req) {
        return Response.of(service.update(req));
    }

    @ApiOperation(value ="库数据回显")
    @PostMapping(value = "/getObject")
    public Response<LibraryResp> getObject(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getLibraryObject(id));
    }

}
