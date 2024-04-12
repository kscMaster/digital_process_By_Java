package com.nancal.myworkspace.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.*;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.*;
import com.nancal.remote.vo.DictItemVo;
import com.nancal.remote.vo.ProcessTaskNodeVo;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.bo.MasterRL;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.factory.MasterRLFactory;
import com.nancal.service.service.*;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@RestController
    @RequestMapping("/Tab")
@Api(tags = "Tab管理")
public class TabController {

    @Autowired
    private IDatasetDomainService datasetDomainService;
    @Autowired
    private ITabDomainService service;
    @Autowired
    private ITabRevisionDomainService serviceRevision;
    @Autowired
    private IFolderDomainService folderDomainService;

    @Autowired
    private IProcessInstanceDomainService iProcessInstanceDomainService;

    @Autowired
    private DictUtil dictUtil;

    @PostMapping("/getTabConfig")
    @ApiOperation(value = "页签")
    public Response<List<DictItemVo>> getTabConfig(@RequestBody @Validated IdRequest id){
        if (StrUtil.isBlank(id.getObjectType())){
            throw new ServiceException(ErrorCode.E_10,"类型不能为空");
        }
        return Response.of(dictUtil.getTabConfigByObjectType(id.getObjectType()));
    }

    @PostMapping("/getObject")
    @ApiOperation(value = "属性")
    public Response<BusinessObjectResp> getObject(@RequestBody @Validated IdRequest id) {
        Class<?> entityClass = EntityUtil.getEntityClass(id.getObjectType());
        if (ItemRevisionEntity.class.isAssignableFrom(entityClass)){
            return Response.of(serviceRevision.getObject(id));
        }else if (ItemEntity.class.isAssignableFrom(entityClass)){
            return Response.of(service.getObject(id));
        }else if (DatasetEntity.class.isAssignableFrom(entityClass)){
            return Response.of(datasetDomainService.getObject(id));
        }
        return Response.of(folderDomainService.getObject(id));
    }

    @ApiOperation(value = "修改记录查询")
    @PostMapping(value = "/updateRecordList")
    public Response<List<WorkspaceObjectResp>> updateRecordList(@RequestBody @Validated IdRequest id) {
        Class<?> entityClass = EntityUtil.getEntityClass(id.getObjectType());
        if (ItemRevisionEntity.class.isAssignableFrom(entityClass)){
            return Response.of(serviceRevision.updateRecordList(id));
        }else if (ItemEntity.class.isAssignableFrom(entityClass)){
            return Response.of(service.updateRecordList(id));
        }
        return Response.of(CollUtil.newArrayList());
    }

    @ApiOperation(value = "分页获取页签中的详情")
    @PostMapping(value = "/getTabDetail")
    public TableResponse<IncludeOrMasterRLResp> getTabDetail(@RequestBody @Validated TableRequest<IdRequest> req) {
        Class<?> entityClass = EntityUtil.getEntityClass(req.getData().getObjectType());
        if (ItemRevisionEntity.class.isAssignableFrom(entityClass)){
            return serviceRevision.getTabDetail(req);
        }else if (ItemEntity.class.isAssignableFrom(entityClass)){
            return service.getTabDetail(req);
        }
        return folderDomainService.getTabDetail(req);
    }

    @ApiOperation(value = "获取页签关系的右对象")
    @PostMapping(value = "/getRightRelation")
    public Response<List<IncludeOrMasterRLResp>> getRightRelation(@RequestBody @Validated IdRequest id)  {
        Class<?> entityClass = EntityUtil.getEntityClass(id.getObjectType());
        if (ItemRevisionEntity.class.isAssignableFrom(entityClass)){
            return Response.of(serviceRevision.getRightObjects(id));
        }else if (ItemEntity.class.isAssignableFrom(entityClass)){
            return Response.of(service.getRightObjects(id));
        }else if (DatasetEntity.class.isAssignableFrom(entityClass)){
            return Response.of(datasetDomainService.getRightObjects(id));
        }
        return Response.of(folderDomainService.getRightObjects(id));
    }

    @ApiOperation(value = "获取页签关系的左对象", notes = "根据输入参数：对象类型、对象的UID查询此对象的所有左对象。")
    @PostMapping(value = "/getLeftRelation")
    public Response<List<IncludeOrMasterRLResp>> getLeftRelation(@RequestBody @Validated IdRequest id) {
        Class<?> entityClass = EntityUtil.getEntityClass(id.getObjectType());
        if (ItemRevisionEntity.class.isAssignableFrom(entityClass)){
            return Response.of(serviceRevision.getLeftObjects(id));
        }else if (ItemEntity.class.isAssignableFrom(entityClass)){
            return Response.of(service.getLeftObjects(id));
        }else if (DatasetEntity.class.isAssignableFrom(entityClass)){
            return Response.of(datasetDomainService.getLeftObjects(id));
        }
        return Response.of(folderDomainService.getLeftObjects(id));
    }

    @ApiOperation(value = "分页获取页签的流程实例ID")
    @PostMapping(value = "/getProcessInstanceId")
    public Response<String> getProcessInstanceId(@RequestBody @Validated IdRequest id) {
        return Response.of(iProcessInstanceDomainService.getProcessInstanceId(id));
    }


    @ApiOperation(value = "分页获取页签的流程")
    @PostMapping(value = "/getProcessInstance")
    public TableResponse<ProcessTaskNodeVo> getProcessInstance(@RequestBody @Validated TableRequest<IdRequest> req) {
        //版本、数据集、工步
        return iProcessInstanceDomainService.getProcessNodePage(req);

    }

    @ApiOperation(value = "编辑权限校验")
    @PostMapping(value = "/editPermissionCheck")
    public Response<Boolean> editPermissionCheck(@RequestBody @Validated IdRequest id) {
        return Response.of(service.editPermissionCheck(id));
    }

    @ApiOperation(value = "新建权限校验")
    @PostMapping(value = "/newPermissionCheck")
    public Response<String> newPermissionCheck(@RequestBody @Validated IdRequest id) {
        service.newPermissionCheck(id);
        return Response.of();
    }

    @ApiOperation(value = "获取数据集")
    @PostMapping(value = "/getDataSet")
    public Response<List<WorkspaceObjectResp>> getDataSet(@RequestBody @Validated IdRequest id) {
        return Response.of(folderDomainService. getDataSet(id));
    }
}
