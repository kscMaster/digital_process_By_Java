package com.nancal.myworkspace.controller;

import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.FolderReq;
import com.nancal.api.model.FolderResp;
import com.nancal.api.model.IncludeOrMasterRLResp;
import com.nancal.common.base.*;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.service.service.IFolderDomainService;
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
@RequestMapping("/Folder")
@Api(tags = "文件夹管理")
public class FolderController {

    @Qualifier("folderDomainServiceImpl")
    @Autowired
    protected IFolderDomainService service;

    @ApiOperation(value = "新增")
    @PostMapping(value = "/create")
    public Response<FolderResp> create(@RequestBody @Validated(AddGroup.class) FolderReq req) {
        FolderResp save = (FolderResp) service.save(req);
        return (Response.of(save));
    }

    @ApiOperation(value = "修改文件夹")
    @PostMapping(value = "/update")
    public Response<FolderResp> update(@RequestBody @Validated(UpdateGroup.class) FolderReq req) {
        FolderResp save = (FolderResp) service.update(req);
        return (Response.of(save));
    }

    @PostMapping("/getObject")
    @ApiOperation(value = "属性")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getObject(id));
    }

//    @ApiOperation(value = "分页获取页签中的详情")
//    @PostMapping(value = "/getTabDetail")
//    public TableResponse<IncludeOrMasterRLResp> getTabDetail(@RequestBody @Validated TableRequest<IdRequest> req) {
//        return service.getTabDetail(req);
//    }

    @ApiOperation(value = "删除")
    @PostMapping(value = "/delete")
    public Response<BusinessObjectResp> delete(@RequestBody @Validated IdRequest id) {
        return Response.of(service.deleteObject(id,AppNameEnum.MY_WORKSPACE));
    }

//    @ApiOperation(value = "获取页签关系的右对象")
//    @PostMapping(value = "/getRightRelation")
//    public Response<List<IncludeOrMasterRLResp>> getRightRelation(@RequestBody @Validated IdRequest id) {
//        return Response.of(service.getRightObjects(id));
//    }
//
//    @ApiOperation(value = "获取页签关系的左对象", notes = "根据输入参数：对象类型、对象的UID查询此对象的所有左对象。")
//    @PostMapping(value = "/getLeftRelation")
//    public Response<List<IncludeOrMasterRLResp>> getLeftRelation(@RequestBody @Validated IdRequest id) {
//        return Response.of(service.getLeftObjects(id));
//    }

}
