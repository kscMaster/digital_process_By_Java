package com.nancal.myworkspace.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.DatasetReq;
import com.nancal.api.model.IncludeOrMasterRLResp;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.*;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.remote.vo.ProcessTaskNodeVo;
import com.nancal.service.bo.Dataset;
import com.nancal.service.factory.DataSetFactory;
import com.nancal.service.service.IDatasetDomainService;
import com.nancal.service.service.IPDFDomainService;
import com.nancal.service.service.IProcessInstanceDomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Api(tags = "DataSet管理")
@Slf4j
@Controller
@RequestMapping("/DataSet")
public class DataSetController {

    @Autowired
    private IDatasetDomainService service;
//
//    @Autowired
//    private IProcessInstanceDomainService iProcessInstanceDomainService;

    @ResponseBody
    @PostMapping("/create")
    @ApiOperation(value = "新增")
    public Response<BusinessObjectResp> create(@RequestBody @Validated(AddGroup.class) DatasetReq req) {
        return Response.of(service.save(req));
    }

    @ResponseBody
    @PostMapping("/update")
    @ApiOperation(value = "更新")
    public Response<BusinessObjectResp> update(@RequestBody @Validated(UpdateGroup.class) DatasetReq req) {
        return Response.of(service.update(req));
    }

    @ResponseBody
    @PostMapping("/getObject")
    @ApiOperation(value = "属性")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated IdRequest id) {
        if (StrUtil.isBlank(id.getObjectType())){
            throw new ServiceException(ErrorCode.E_10);
        }
        return Response.of(service.getObject(id));
    }

    @ResponseBody
    @PostMapping("/fileList")
    @ApiOperation(value = "文件列表")
    public Response<List<? extends BusinessObjectResp>> fileList(@RequestBody @Validated IdRequest id) {
        if (StrUtil.isBlank(id.getObjectType())){
            throw new ServiceException(ErrorCode.E_10);
        }
        Dataset dataset= SpringUtil.getBean(DataSetFactory.class).create();
        return Response.of(dataset.fileList(id));
    }

    @GetMapping("/download")
    @ApiOperation(value = "下载文件")
    public void download(@RequestParam("filePath") @NotBlank(message = "文件路径不能为空") String filePath,
                         HttpServletResponse response) {
       service.download(filePath,response);
    }

    @ResponseBody
    @ApiOperation(value = "删除")
    @PostMapping(value = "/delete")
    public Response<BusinessObjectResp> delete(@RequestBody @Validated IdRequest id) {
        if (StrUtil.isBlank(id.getObjectType())){
            throw new ServiceException(ErrorCode.E_10);
        }
        return Response.of(service.deleteObject(id, AppNameEnum.MY_WORKSPACE));
    }

//    @ResponseBody
//    @ApiOperation(value = "获取页签关系的右对象")
//    @PostMapping(value = "/getRightRelation")
//    public Response<List<IncludeOrMasterRLResp>> getRightRelation(@RequestBody @Validated IdRequest id) {
//        if (StrUtil.isBlank(id.getObjectType())){
//            throw new ServiceException(ErrorCode.E_10);
//        }
//        return Response.of(service.getRightObjects(id));
//    }
//
//    @ResponseBody
//    @ApiOperation(value = "获取页签关系的左对象", notes = "根据输入参数：对象类型、对象的UID查询此对象的所有左对象。")
//    @PostMapping(value = "/getLeftRelation")
//    public Response<List<IncludeOrMasterRLResp>> getLeftRelation(@RequestBody @Validated IdRequest id) {
//        if (StrUtil.isBlank(id.getObjectType())){
//            throw new ServiceException(ErrorCode.E_10);
//        }
//        return Response.of(service.getLeftObjects(id));
//    }
//
//    //TODO
//    @ResponseBody
//    @ApiOperation(value = "分页获取页签的流程")
//    @PostMapping(value = "/getProcessInstance")
//    public TableResponse<ProcessTaskNodeVo> getProcessInstance(@RequestBody @Validated TableRequest<IdRequest> req) {
//        if (StrUtil.isBlank(req.getData().getObjectType())){
//            throw new ServiceException(ErrorCode.E_10);
//        }
//        return iProcessInstanceDomainService.getProcessNodePage(req);
//    }
//
//    @ResponseBody
//    @ApiOperation(value = "分页获取页签的流程实例ID")
//    @PostMapping(value = "/getProcessInstanceId")
//    public Response<String> getProcessInstanceId(@RequestBody @Validated IdRequest id) {
//        if (StrUtil.isBlank(id.getObjectType())){
//            throw new ServiceException(ErrorCode.E_10);
//        }
//        return Response.of(iProcessInstanceDomainService.getProcessInstanceId(id));
//    }

}
