package com.nancal.imexport.controller;

import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.ExportBOMReq;
import com.nancal.api.model.ImexportTaskReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.base.Response;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.service.service.IImexportTaskDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hewei
 * @date 2022/11/1 10:05
 * @Description
 */
@Slf4j
@RestController
@RequestMapping("/ImexportTask")
@Api(tags = "导入应用")
public class ImexportTaskController {

    @Autowired
    private IImexportTaskDomainService service;

    /**
     *
     * @param file 文件
     * @param leftObject 资源库左侧树uid
     * @param leftObjectType 资源库左侧树类型
     * @param objectType 资源库数据类型
     * @return
     */
    @ApiOperation(value = "新增")
    @PostMapping(value = "{objectType}/create")
    public Response<BusinessObjectResp> create(@RequestParam("file") MultipartFile file
                                                , @RequestParam("leftObject") String leftObject
                                                , @RequestParam("leftObjectType") String leftObjectType
                                                , @PathVariable String objectType) {
        return service.saveLibrary(file,leftObject,leftObjectType,objectType);
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/page")
    public TableResponse<WorkspaceObjectResp> page(@RequestBody @Validated TableRequest<ImexportTaskReq> req) {
        return service.page(req);
    }


    /**
     * 添加ebom任务
     * @param file 文件
     * @return
     */
    @ApiOperation(value = "新增")
    @PostMapping(value = "Gte4Part/create")
    public Response<BusinessObjectResp> gte4PartCreate(@RequestParam("file") MultipartFile file) {
        return service.saveEbom(file);
    }


    /**
     * 添加工艺规划任务
     * @param file 文件
     * @return
     */
    @ApiOperation(value = "新增")
    @PostMapping(value = "Bop/create")
    public Response<BusinessObjectResp> msgbomCreate(@RequestParam("file") MultipartFile file) {
        return service.saveBop(file);
    }



}
