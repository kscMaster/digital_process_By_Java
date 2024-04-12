package com.nancal.myworkspace.controller;

import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.HomeFolderResp;
import com.nancal.api.model.IncludeOrMasterRLResp;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.service.service.IHomeFolderDomainService;
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
@RequestMapping("/HomeFolder")
@Api(tags = "HomeFolder管理")
public class HomeFolderController {

    @Qualifier("homeFolderDomainServiceImpl")
    @Autowired
    protected IHomeFolderDomainService service;

    @ApiOperation(value = "获取Home节点")
    @PostMapping(value = "/getByOwnerId")
    public Response<HomeFolderResp> getByOwnerId() {
        return Response.of(service.getByOwnerId());
    }

    @PostMapping("/getObject")
    @ApiOperation(value = "属性")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated IdRequest id) {
        return Response.of(service.getObject(id));
    }
//
//    @ApiOperation(value = "分页获取页签中的详情")
//    @PostMapping(value = "/getTabDetail")
//    public TableResponse<IncludeOrMasterRLResp> getTabDetail(@RequestBody @Validated TableRequest<IdRequest> req) {
//        return service.getTabDetail(req);
//    }
//
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
