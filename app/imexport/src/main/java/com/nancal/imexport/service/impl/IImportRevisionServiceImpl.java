package com.nancal.imexport.service.impl;

import com.nancal.api.model.*;
import com.nancal.imexport.service.IImexportRevisionServiceAdaptor;
import com.nancal.imexport.service.IImexportTaskDomainServiceAdaptor;
import com.nancal.model.entity.BOMNodeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author hewei
 * @date 2022/10/31 18:49
 * @Description
 */
@Service
public class IImportRevisionServiceImpl implements IImexportRevisionServiceAdaptor {


    @Autowired
    private BOMNodeDomainServiceImpl bomNodeDomainService;

    @Autowired
    private IImexportTaskDomainServiceAdaptor iImexportTaskDomainServiceAdaptor;

    /**
     * 线体工艺
     * @param req
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public BusinessObjectResp updateGte4MfgLinePr(BusinessObjectReq req) {
        Gte4MfgLinePrBomReq objectBomReq = (Gte4MfgLinePrBomReq)req;
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        if (!Objects.isNull(objectBomReq.getBomNodeReq())) {
            objectBomReq.getBomNodeReq().setObjectType(new BOMNodeEntity().getObjectType());
            //调用编辑bom属性方法
            BOMNodeResp bomNodeResp =(BOMNodeResp)bomNodeDomainService.update(objectBomReq.getBomNodeReq());
            objBomResp.setBomNodeResp(bomNodeResp);
        }
        if (!Objects.isNull(objectBomReq.getLinePrRevisionReq())) {
            //调用编辑对象属性方法
            WorkspaceObjectResp update = (WorkspaceObjectResp) IImexportRevisionServiceAdaptor.super.update(objectBomReq.getLinePrRevisionReq());
            objBomResp.setObjectResp(update);
            //调用维护工时的方法
            bomNodeDomainService.fullTaskTime(4,update.getUid(),update.getObjectType());
        }
        return objBomResp;
    }

    /**
     * 工序
     * @param req
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public BusinessObjectResp updateGte4MfgOperation(BusinessObjectReq req) {
        Gte4MfgOperationBomReq operationBomReq = (Gte4MfgOperationBomReq)req;
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        if (!Objects.isNull(operationBomReq.getBomNodeReq())) {
            operationBomReq.getBomNodeReq().setObjectType(new BOMNodeEntity().getObjectType());
            //调用编辑bom属性方法
            BOMNodeResp bomNodeResp =(BOMNodeResp)bomNodeDomainService.update(operationBomReq.getBomNodeReq());
            objBomResp.setBomNodeResp(bomNodeResp);
        }
        if (!Objects.isNull(operationBomReq.getOperationRevisionReq())) {
            //调用编辑有版本的属性方法
            WorkspaceObjectResp update = (WorkspaceObjectResp) IImexportRevisionServiceAdaptor.super.update(operationBomReq.getOperationRevisionReq());
            objBomResp.setObjectResp(update);
            //调用维护工时的方法
            bomNodeDomainService.fullTaskTime(4,update.getUid(),update.getObjectType());
        }
        return objBomResp;
    }

    /**
     * 工厂工艺
     * @param req
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public BusinessObjectResp updateGte4MfgPlantPr(BusinessObjectReq req) {
        Gte4MfgPlantPrRevisionReq objectBomReq = (Gte4MfgPlantPrRevisionReq) req;
        return IImexportRevisionServiceAdaptor.super.update(objectBomReq);
    }

    /**
     * 工位工艺
     * @param req
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public BusinessObjectResp updateGte4MfgStationPr(BusinessObjectReq req) {
        Gte4MfgStationPrBomReq objectBomReq = (Gte4MfgStationPrBomReq)req;
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        if (!Objects.isNull(objectBomReq.getBomNodeReq())) {
            objectBomReq.getBomNodeReq().setObjectType(new BOMNodeEntity().getObjectType());
            //调用编辑bom属性方法
            BOMNodeResp bomNodeResp =(BOMNodeResp)bomNodeDomainService.update(objectBomReq.getBomNodeReq());
            objBomResp.setBomNodeResp(bomNodeResp);
        }
        if (!Objects.isNull(objectBomReq.getStationPrRevisionReq())) {
            //调用编辑工步属性方法
            WorkspaceObjectResp update = (WorkspaceObjectResp) IImexportRevisionServiceAdaptor.super.update(objectBomReq.getStationPrRevisionReq());
            objBomResp.setObjectResp(update);
            //调用维护工时的方法
            bomNodeDomainService.fullTaskTime(4,update.getUid(),update.getObjectType());
        }
        return objBomResp;
    }


    /**
     * 工步
     * @param req
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public BusinessObjectResp updateGte4MfgStep(BusinessObjectReq req) {
        Gte4MfgStepBomReq stepBomReq = (Gte4MfgStepBomReq)req;
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        if (!Objects.isNull(stepBomReq.getBomNodeReq())) {
            stepBomReq.getBomNodeReq().setObjectType(new BOMNodeEntity().getObjectType());
            //调用编辑bom属性方法
            BOMNodeResp bomNodeResp =(BOMNodeResp)bomNodeDomainService.update(stepBomReq.getBomNodeReq());
            objBomResp.setBomNodeResp(bomNodeResp);
        }
        if (!Objects.isNull(stepBomReq.getStepReq())) {
            //调用编辑工步属性方法
            WorkspaceObjectResp update = (WorkspaceObjectResp) iImexportTaskDomainServiceAdaptor.update(stepBomReq.getStepReq());
            objBomResp.setObjectResp(update);
        }
        return objBomResp;
    }


    @Transactional
    public BusinessObjectResp updateGte4Part(BusinessObjectReq req) {
        Gte4PartBomReq partBomReq = (Gte4PartBomReq) req;
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        if (!Objects.isNull(partBomReq.getBomNodeReq())) {
            partBomReq.getBomNodeReq().setObjectType(new BOMNodeEntity().getObjectType());
            //调用编辑bom属性方法
            BOMNodeResp bomNodeResp = (BOMNodeResp) bomNodeDomainService.update(partBomReq.getBomNodeReq());
            objBomResp.setBomNodeResp(bomNodeResp);
        }
        if (!Objects.isNull(partBomReq.getPartRevisionReq())) {
            //调用父类编辑属性方法
            WorkspaceObjectResp update = (WorkspaceObjectResp) IImexportRevisionServiceAdaptor.super.update(partBomReq.getPartRevisionReq());
            objBomResp.setObjectResp(update);
        }
        return objBomResp;
    }
}
