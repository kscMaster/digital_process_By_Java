package com.nancal.service.service;


import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.ExportBOMReq;
import com.nancal.api.model.ImexportTaskReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.api.utils.EntityUtil;
import com.nancal.api.utils.FileUtils;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.base.Response;
import com.nancal.common.config.TokenInfo;
import com.nancal.common.constants.Constant;
import com.nancal.common.constants.ImportConstant;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.model.entity.ImexportTaskEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.bo.ImexportTask;
import com.querydsl.core.types.Ops;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 导入导出任务
 * @author hewei
 */
public interface IImexportTaskDomainService extends IWorkspaceObjectDomainService {

    /**
     * 导入资源库
     * @param file 文件
     * @param leftObject  资源库左侧菜单uid
     * @param leftObjectType 资源库左侧菜单类型
     * @param objectType 资源库数据类型
     * @return
     */
    default Response<BusinessObjectResp> saveLibrary(MultipartFile file, String leftObject, String  leftObjectType, String objectType){
        return null;
    }

    /**
     * 导入设计零组件
     * @param file 文件
     * @return
     */
    default Response<BusinessObjectResp> saveEbom(MultipartFile file){
        return null;
    }

    /**
     * 导入工艺规划
     * @param file 文件
     * @return
     */
    default Response<BusinessObjectResp> saveBop(MultipartFile file){
        return null;
    }

    /**
     * 导入工艺规划
     * @param uid
     * @return
     */
    default Response<BusinessObjectResp> msgbomExport(ExportBOMReq req){
        return null;
    }

    /**
     * 添加 导入导出任务
     * @param fileName
     * @return
     */
    default WorkspaceObjectResp saveImportTask(String fileName,String appCode,String operationObjectType){
        List<Triple<String, Ops, Object>> params = Arrays.asList(
                Triple.of(ImexportTaskEntity.FILE_NAME, Ops.EQ, fileName),
                Triple.of(ImexportTaskEntity.DEL_FLAG, Ops.EQ, false),
                Triple.of(ImexportTaskEntity.OPERATION_STATUS, Ops.EQ, ImportConstant.EXECUTE));
        // 查询中间表获取左对象数据
        List<WorkspaceObjectEntity> entityList = EntityUtil.getDynamicQuery(new ImexportTask().getObjectType(), params).fetch();
        if (!entityList.isEmpty()){
            Boolean isImport = entityList.stream().anyMatch(entity -> ((ImexportTaskEntity)entity).getFileName().equals(fileName) && ((ImexportTaskEntity)entity).getOperationStatus().equals(ImportConstant.EXECUTE));
            if (isImport){
                throw new ServiceException(ErrorCode.E_13,"当前文档正在导入中，不允许重复导入");
            }
        }

        return saveImexportTaskReq(fileName,ImportConstant.IMPORTS,operationObjectType,appCode,"文件导入任务");
    }

    default WorkspaceObjectResp saveImexportTaskReq(String fileName,String operationType,String operationObjectType,String appCode,String operation){
        ImexportTaskReq imexportTaskReq = new ImexportTaskReq();
        imexportTaskReq.setFileName(fileName);
        imexportTaskReq.setStartDate(LocalDateTime.now());
        imexportTaskReq.setOperationType(operationType);
        imexportTaskReq.setOperationObjectType(operationObjectType);
        imexportTaskReq.setOperationStatus(ImportConstant.EXECUTE);
        imexportTaskReq.setAppCode(appCode);
        imexportTaskReq.setTaskName(fileName+operation);
        return save(imexportTaskReq);
    }


    /**
     * 添加 获取封装TokenInfo
     * @return
     */
    default TokenInfo saveImportTask(MultipartFile file){
        File multipartFile = FileUtils.getMultipartFile(file);
        MultipartFile multipartFile1 = FileUtils.getMultipartFile(multipartFile);
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String token = (String)requestAttributes.getAttribute(Constant.TOKEN, RequestAttributes.SCOPE_REQUEST);
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setTenantId(userUtils.getTenantId());
        tokenInfo.setToken(token);
        tokenInfo.setFile(multipartFile1);
        return tokenInfo;
    }

    /**
     * 添加 获取封装TokenInfo
     * @return
     */
    default TokenInfo saveImportTask(){
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String token = (String)requestAttributes.getAttribute(Constant.TOKEN, RequestAttributes.SCOPE_REQUEST);
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setTenantId(userUtils.getTenantId());
        tokenInfo.setToken(token);
        return tokenInfo;
    }


}
