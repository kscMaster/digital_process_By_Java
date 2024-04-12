package com.nancal.imexport.service;

import com.nancal.api.model.ImexportTaskReq;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.constants.ImportConstant;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.ImexportTaskEntity;
import com.nancal.service.service.IWorkspaceObjectDomainService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author hewei
 * @date 2022/11/1 18:28
 * @Description
 */
public interface IImexportTaskDomainServiceAdaptor extends IWorkspaceObjectDomainService {


    /**
     * 导出应用成功回调
     * @param uid
     * @param exportUrl 导出文件路径
     */
    @Transactional(rollbackFor = Exception.class)
    default void exportSuccess(String uid,String exportUrl){
        ImexportTaskEntity imexportTaskEntity = (ImexportTaskEntity) EntityUtil.getById(new ImexportTaskEntity().getObjectType(), uid);
        ImexportTaskReq imexportTaskReq = new ImexportTaskReq();
        BeanUtil.copyPropertiesIgnoreNull(imexportTaskEntity,imexportTaskReq);
        imexportTaskReq.setOperationStatus(ImportConstant.SUCCESS);
        imexportTaskReq.setEndDate(LocalDateTime.now());
        imexportTaskReq.setExportUrl(exportUrl);
        this.update(imexportTaskReq);
    }

    /**
     * 导入应用成功回调
     * @param uid
     */
    @Transactional(rollbackFor = Exception.class)
    default void success(String uid){
        ImexportTaskEntity imexportTaskEntity = (ImexportTaskEntity) EntityUtil.getById(new ImexportTaskEntity().getObjectType(), uid);
        ImexportTaskReq imexportTaskReq = new ImexportTaskReq();
        BeanUtil.copyPropertiesIgnoreNull(imexportTaskEntity,imexportTaskReq);
        imexportTaskReq.setOperationStatus(ImportConstant.SUCCESS);
        imexportTaskReq.setEndDate(LocalDateTime.now());
        this.update(imexportTaskReq);
    }

    /**
     * 导入导出应用失败回调
     * @param uid
     * @param errorLog
     */
    @Transactional(rollbackFor = Exception.class,propagation= Propagation.REQUIRES_NEW)
    default void error(String uid,String errorLog){
        ImexportTaskEntity imexportTaskEntity = (ImexportTaskEntity)EntityUtil.getById(new ImexportTaskEntity().getObjectType(), uid);
        ImexportTaskReq imexportTaskReq = new ImexportTaskReq();
        BeanUtil.copyPropertiesIgnoreNull(imexportTaskEntity,imexportTaskReq);
        imexportTaskReq.setOperationStatus(ImportConstant.FAIL);
        imexportTaskReq.setErrorLog(errorLog);
        imexportTaskReq.setEndDate(LocalDateTime.now());
        this.update(imexportTaskReq);
    }
}
