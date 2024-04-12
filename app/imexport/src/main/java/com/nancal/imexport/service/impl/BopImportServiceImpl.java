package com.nancal.imexport.service.impl;

import cn.hutool.core.lang.Pair;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.nancal.api.model.*;
import com.nancal.api.utils.*;
import com.nancal.api.utils.excel.analysis.CustomBopPoiWordAnalysis;
import com.nancal.api.utils.excel.verify.BopVerify;
import com.nancal.common.base.IdRequest;
import com.nancal.common.config.TokenContext;
import com.nancal.common.config.TokenInfo;
import com.nancal.common.constants.BopConstant;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.utils.BeanUtil;
import com.nancal.framework.common.annotations.JsonDict;
import com.nancal.imexport.service.IImexportServiceAdaptor;
import com.nancal.imexport.service.IImexportTaskDomainServiceAdaptor;
import com.nancal.model.entity.*;
import com.nancal.remote.service.RemoteLezaoIntegrationService;
import com.nancal.remote.to.MoreDictEntryReq;
import com.nancal.remote.vo.*;
import com.nancal.service.bo.Item;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.service.IBOMNodeDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author hewei
 * @date 2022/10/31 18:28
 * @Description
 */
@Service
public class BopImportServiceImpl extends CustomBopPoiWordAnalysis<MsgbImportReq> implements IImexportServiceAdaptor {

    @Autowired
    private IBOMNodeDomainService ibomNodeDomainService;

    @Autowired
    private IImportRevisionServiceImpl iImportRevisionService;

    @Autowired
    private IImexportTaskDomainServiceAdaptor iImexportTaskDomainService;

    @Autowired
    private RemoteLezaoIntegrationService remoteLezaoIntegrationService;

    @Autowired
    private DictUtil dictUtil;


    /**
     * 线体工艺新增
     */
    @Transactional(rollbackFor = Exception.class)
    public WorkspaceObjectResp saveGte4MfgLinePr(BusinessObjectReq req) {
        Gte4MfgLinePrBomReq objectBomReq = (Gte4MfgLinePrBomReq) req;
        BOMNodeReq nodeReq = objectBomReq.getBomNodeReq();
        Gte4MfgLinePrRevisionReq linePrRevisionReq = objectBomReq.getLinePrRevisionReq();
        WorkspaceObjectResp save;
        if (Objects.isNull(nodeReq)) {
            save = IImexportServiceAdaptor.super.save(linePrRevisionReq);
        } else {
            save = IImexportServiceAdaptor.super.save(linePrRevisionReq);
            nodeReq.setChildItem(save.getUid());
            nodeReq.setChildItemType(new Gte4MfgLinePrEntity().getObjectType());
            ibomNodeDomainService.createNode(nodeReq, AppNameEnum.MSGBOM);
            //调用维护工时的方法
            ibomNodeDomainService.fullTaskTime(1,save.getRightObject(),save.getRightObjectType());
        }
        return save;
    }

    /**
     * 创建工序对象，并创建关联的bom节点数据
     * 1、先将工序对象保存到数据库中 gte4mfg_operation（工序表） master_r_l (关系表) gte4mfg_operation_revision（工序版本表）
     * 2、再调用新增bom节点方法，新增bom节点  bomnode（bom节点表）
     */
    @Transactional(rollbackFor = Exception.class)
    public WorkspaceObjectResp saveGte4MfgOperation(BusinessObjectReq req) {
        Gte4MfgOperationBomReq operationBomReq = (Gte4MfgOperationBomReq) req;
        BOMNodeReq nodeReq = operationBomReq.getBomNodeReq();
        Gte4MfgOperationRevisionReq operationRevisionReq = operationBomReq.getOperationRevisionReq();
        //判断是否可以添加，取决于父级的是否具有编辑权限
        IImexportServiceAdaptor.super.checkParentRevEidt(nodeReq);
        WorkspaceObjectResp save = IImexportServiceAdaptor.super.save(operationRevisionReq);
        nodeReq.setChildItem(save.getUid());
        nodeReq.setChildItemType(new Gte4MfgOperationEntity().getObjectType());
        //维护手动工时
        nodeReq.setManualTaskTime(operationBomReq.getOperationRevisionReq().getGte4TaskTime());
        ibomNodeDomainService.createNode(nodeReq, AppNameEnum.MSGBOM);
        //调用维护工时的方法
        ibomNodeDomainService.fullTaskTime(1,save.getRightObject(),save.getRightObjectType());
        return save;
    }


    /**
     * 新增工厂工艺
     */
    @Transactional(rollbackFor = Exception.class)
    public WorkspaceObjectResp saveGte4MfgPlantPr(BusinessObjectReq req) {
        Gte4MfgPlantPrRevisionReq objectBomReq = (Gte4MfgPlantPrRevisionReq) req;
        return IImexportServiceAdaptor.super.save(objectBomReq);
    }


    /**
     * 新增工位工艺
     */
    @Transactional
    public WorkspaceObjectResp saveGte4MfgStationPr(BusinessObjectReq req) {
        Gte4MfgStationPrBomReq objectBomReq = (Gte4MfgStationPrBomReq) req;
        BOMNodeReq nodeReq = objectBomReq.getBomNodeReq();
        Gte4MfgStationPrRevisionReq stationPrRevisionReq = objectBomReq.getStationPrRevisionReq();
        //判断是否可以添加，取决于父级的是否具有编辑权限
        IImexportServiceAdaptor.super.checkParentRevEidt(nodeReq);
        WorkspaceObjectResp save = IImexportServiceAdaptor.super.save(stationPrRevisionReq);
        nodeReq.setChildItem(save.getUid());
        nodeReq.setChildItemType(new Gte4MfgStationPrEntity().getObjectType());
        ibomNodeDomainService.createNode(nodeReq, AppNameEnum.MSGBOM);
        //调用维护工时的方法
        ibomNodeDomainService.fullTaskTime(1,save.getRightObject(),save.getRightObjectType());
        return save;
    }


    /**
     *  创建工步对象，并创建关联的bom节点数据
     *  1、先将工步对象保存到数据库中 gte4mfg_step（工步表） 此时工步是一个最小的单元，没有版本
     *  2、再调用新增bom节点方法，新增bom节点  bomnode（bom节点表）
     */
    @Transactional(rollbackFor = Exception.class)
    public WorkspaceObjectResp saveGte4MfgStep(BusinessObjectReq req) {
        Gte4MfgStepBomReq stepBomReq = (Gte4MfgStepBomReq) req;
        Gte4MfgStepReq stepReq = stepBomReq.getStepReq();
        BOMNodeReq nodeReq = stepBomReq.getBomNodeReq();
        //判断是否可以添加，取决于父级的是否具有编辑权限
        this.checkParentRevEidt(nodeReq);
        String itemId = SpringUtil.getBean(CoderSetUtil.class).getOneCoderByObjectType(stepReq.getObjectType());
        stepReq.setItemId(itemId);
        WorkspaceObjectResp save = iImexportTaskDomainService.save(stepReq);
        nodeReq.setChildItem(save.getUid());
        nodeReq.setChildItemType(new Gte4MfgStepEntity().getObjectType());
        //维护手动工时
        nodeReq.setManualTaskTime(stepReq.getGte4TaskTime());
        ibomNodeDomainService.createNode(nodeReq, AppNameEnum.MSGBOM);
        //调用维护工时的方法
        ibomNodeDomainService.fullTaskTime(1,save.getUid(),save.getObjectType());
        return save;
    }




    @Transactional(rollbackFor = Exception.class)
    @Async
    public void importExcel(TokenInfo tokenInfo, String uid) {
        MultipartFile file = null;
        try {
            TokenContext.setToken(tokenInfo);
            file = TokenContext.getToken().getFile();
            Map<String,Object> statisticsMap = new ConcurrentHashMap<>(3);
            List<MsgbImportReq> analysisList = this.excelImportVerify(file,statisticsMap);
            Map<String, String> dataObjectType = getDataObjectType();
            Item item = SpringUtil.getBean(ItemFactory.class).create();
            ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
            Map<String, List<MsgbImportReq>> stringListMap = analysisList.stream().map(MsgbImportReq.class::cast).collect(Collectors.groupingBy(MsgbImportReq::getDataType));
            stringListMap.entrySet().forEach( l ->{
                List<MsgbImportReq> value = l.getValue();
                List<String> itemIds = value.stream().map(MsgbImportReq::getItemId).collect(Collectors.toList());
                if ("工步".equals(l.getKey())) {
                    Map<String, List<Gte4MfgStepEntity>> itemsLast = itemRevision.listByItemIds(itemIds, dataObjectType.get(l.getKey()));
                    analysisList.forEach(t ->{
                        List<Gte4MfgStepEntity> itemEntities = itemsLast.get(t.getItemId());
                        if (null != itemEntities && !itemEntities.isEmpty()){
                            List<Gte4MfgStepEntity> isExist = itemEntities.stream().filter(r -> r.getItemId().equals(t.getItemId())).collect(Collectors.toList());
                            if (!isExist.isEmpty()){
                                Gte4MfgStepEntity itemEntity = isExist.get(0);
                                t.setUid(itemEntity.getUid());
                                t.setObjectType(itemEntity.getObjectType());
                            }
                        }
                    });
                }else {
                    Map<String, List<ItemRevisionEntity>> itemsLastVersion = item.listByItemIds(itemIds, EntityUtil.getRevision(dataObjectType.get(l.getKey())));
                    analysisList.forEach(t ->{
                        List<ItemRevisionEntity> itemRevisionEntities = itemsLastVersion.get(t.getItemId());
                        if (null != itemRevisionEntities && !itemRevisionEntities.isEmpty()){
                            List<ItemRevisionEntity> isExist = itemRevisionEntities.stream().filter(r -> r.getRevisionId().equals(t.getRevisionId())).collect(Collectors.toList());
                            if (!isExist.isEmpty()){
                                ItemRevisionEntity itemRevisionEntity = isExist.get(0);
                                t.setUid(itemRevisionEntity.getUid());
                                t.setObjectType(itemRevisionEntity.getObjectType());
                                t.setLeftObject(itemRevisionEntity.getLeftObject());
                                t.setLeftObjectType(itemRevisionEntity.getLeftObjectType());
                            }
                        }
                    });
                }
            });
            List<MsgbImportReq> treeList = new ArrayList<>();
            for (int i = 1; i < 10000; i++) {
                String current = String.valueOf(i);
                List<MsgbImportReq> currentList = analysisList.stream().filter(it -> current.equals(it.getRank())).collect(Collectors.toList());
                currentList.forEach(gprir -> {
                    recursionChildren(gprir, analysisList);
                });
                treeList.addAll(currentList);
            }
            AtomicReference<WorkspaceObjectResp> workspaceObjectResp = new AtomicReference<>();
            // 递归执行

            treeList.forEach(parentObject ->{
                if (null != parentObject.getUid()) {
                    workspaceObjectResp.set( updateTopLevel(parentObject,dataObjectType));
                    comparison(parentObject,null,dataObjectType,item);
                }else {
                    workspaceObjectResp.set(createTopLevel(parentObject,dataObjectType,item));
                }
            });
            iImexportTaskDomainService.success(uid);
        }catch (Exception e){
            e.printStackTrace();
            iImexportTaskDomainService.error(uid,e.getMessage());
        }finally {
            try {
                Files.delete(FileUtils.getMultipartFile(file).toPath());
            } catch (IOException e) {
                e.printStackTrace();
                iImexportTaskDomainService.error(uid,"文件删除失败");
            }
        }
    }
    /**
     * 新增顶层节点
     *    并且讲新增的属性字段添加至导入数据中
     * @param msgbImportReq
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public WorkspaceObjectResp createTopLevel(MsgbImportReq msgbImportReq,Map<String, String> dataObjectType, Item item){
        WorkspaceObjectResp workspaceObjectResp = null;
        if (BopConstant.FACTORY.equals(msgbImportReq.getDataType())){
            msgbImportReq.setObjectType(new Gte4MfgPlantPrEntity().getObjectType());
            workspaceObjectResp = this.saveGte4MfgPlantPr(msgbImportReq);
        }
        else if (BopConstant.LINEAL_BODY.equals(msgbImportReq.getDataType())){
            msgbImportReq.setObjectType(new Gte4MfgLinePrEntity().getObjectType());
            Gte4MfgLinePrBomReq gte4MfgLinePrBomReq = new Gte4MfgLinePrBomReq();
            gte4MfgLinePrBomReq.setLinePrRevisionReq((Gte4MfgLinePrRevisionReq)msgbImportReq);
            workspaceObjectResp = this.saveGte4MfgLinePr(gte4MfgLinePrBomReq);
        }
        if (null != workspaceObjectResp){
            msgbImportReq.setUid(workspaceObjectResp.getRightObject());
            msgbImportReq.setLeftObject(workspaceObjectResp.getUid());
            msgbImportReq.setLeftObjectType(workspaceObjectResp.getObjectType());
            msgbImportReq.setObjectType(workspaceObjectResp.getRightObjectType());

            if (null != msgbImportReq.getChildren() && !msgbImportReq.getChildren().isEmpty()) {
               executeMethod(msgbImportReq.getChildren(),msgbImportReq,dataObjectType,item);
            }
            return workspaceObjectResp;
        }
        return null;
    }


    /**
     * 修改顶层节点
     *    并且讲新增的属性字段添加至导入数据中
     * @param msgbImportReq
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public WorkspaceObjectResp updateTopLevel(MsgbImportReq msgbImportReq,Map<String, String> dataObjectType){
        msgbImportReq.setObjectType(EntityUtil.getRevision(dataObjectType.get(msgbImportReq.getDataType())));
        msgbImportReq.setUid(msgbImportReq.getUid());
        msgbImportReq.setLeftObject(msgbImportReq.getLeftObject());
        msgbImportReq.setLeftObjectType(msgbImportReq.getLeftObjectType());
        if (BopConstant.FACTORY.equals(msgbImportReq.getDataType())){
            msgbImportReq.setObjectType(new Gte4MfgPlantPrEntity().getObjectType());
            ItemRevisionResp itemRevisionResp =(ItemRevisionResp)iImportRevisionService.updateGte4MfgPlantPr(msgbImportReq);
            if (null != itemRevisionResp){
                // 编辑完毕 赋值导入数据
                msgbImportReq.setUid(itemRevisionResp.getUid());
                msgbImportReq.setLeftObject(itemRevisionResp.getLeftObject());
                msgbImportReq.setLeftObjectType(itemRevisionResp.getLeftObjectType());
                msgbImportReq.setObjectType(itemRevisionResp.getObjectType());
                // 返回前端回显
                WorkspaceObjectResp workspaceObjectResp = new WorkspaceObjectResp();
                workspaceObjectResp.setLeftObject(itemRevisionResp.getLeftObject());
                workspaceObjectResp.setLeftObjectType(itemRevisionResp.getLeftObjectType());
                return workspaceObjectResp;
            }
        }
        else if (BopConstant.LINEAL_BODY.equals(msgbImportReq.getDataType())){
            msgbImportReq.setObjectType(new Gte4MfgLinePrEntity().getObjectType());
            Gte4MfgLinePrBomReq gte4MfgLinePrBomReq = new Gte4MfgLinePrBomReq();
            gte4MfgLinePrBomReq.setLinePrRevisionReq((Gte4MfgLinePrRevisionReq)msgbImportReq);
            BomEditRevisionResp bomEditRevisionResp = (BomEditRevisionResp)iImportRevisionService.updateGte4MfgLinePr(gte4MfgLinePrBomReq);
            if (null != bomEditRevisionResp){
                // 编辑完毕 赋值导入数据
                msgbImportReq.setUid(bomEditRevisionResp.getUid());
                msgbImportReq.setLeftObject(bomEditRevisionResp.getObjectResp().getLeftObject());
                msgbImportReq.setLeftObjectType(bomEditRevisionResp.getObjectResp().getLeftObjectType());
                msgbImportReq.setObjectType(bomEditRevisionResp.getObjectResp().getObjectType());
                // 返回前端回显
                WorkspaceObjectResp workspaceObjectResp = new WorkspaceObjectResp();
                workspaceObjectResp.setLeftObject(bomEditRevisionResp.getObjectResp().getLeftObject());
                workspaceObjectResp.setLeftObjectType(bomEditRevisionResp.getObjectResp().getLeftObjectType());
                return workspaceObjectResp;
            }
        }
        return null;
    }


    /**
     *
     * @param parentObject 已被新增完毕的父级版本数据。
     * @param dataObjectTypeMap 存储不同数据类型的objectType的集合
     * @param item
     *  判断执行方法
     *        一：顶层新增执行完毕，判断第二子级的执行方法。
     *              子级已存在执行 递归对比 方法
     *              子级不存在执行 递归创建 方法
     */
    @Transactional(rollbackFor = Exception.class)
    public void executeMethod(List<MsgbImportReq> parentObjects,MsgbImportReq parentObject,Map<String,String> dataObjectTypeMap,Item item){
        if (null != parentObjects && !parentObjects.isEmpty()) {
            parentObjects.forEach(childrenObject -> {
                if (null != childrenObject.getUid()) {
                    comparison(childrenObject,parentObject,dataObjectTypeMap,item);
                } else {
                    recursionChildrenCreate(childrenObject,parentObject,dataObjectTypeMap,item,true);
                }
            });
        }
    }



    /**
     *  更新数据
     * @param childrenObject 需要被修改的子级
     * @param bopNodeViewResp 根据 childrenObject 查询的bom行
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void recursionChildrenUpdate(MsgbImportReq childrenObject,BOMNodeResp bopNodeViewResp,Map<String,String> dataObjectTypeMap) {
        BomEditRevisionResp bomEditRevisionResp = null;
        /**
         *  创建 bom行 数据
         */
        BOMNodeReq bomNodeReq = new BOMNodeReq();
        if (null != bopNodeViewResp.getUid() && !"0".equals(bopNodeViewResp.getUid())) {
            if (null != childrenObject.getQuantity() && 0 != childrenObject.getQuantity()) {
                bomNodeReq.setQuantity(childrenObject.getQuantity());
            }else {
                bomNodeReq.setQuantity(bopNodeViewResp.getQuantity());
            }
            if (null != childrenObject.getFoundNo() && 0 != childrenObject.getFoundNo()) {
                bomNodeReq.setFoundNo(childrenObject.getFoundNo());
            }else {
                bomNodeReq.setFoundNo(bopNodeViewResp.getFoundNo());
            }
            bomNodeReq.setUid(bopNodeViewResp.getUid());
        }
        childrenObject.setUid(childrenObject.getUid());
        childrenObject.setLeftObject(bopNodeViewResp.getChildItem());
        childrenObject.setLeftObjectType(bopNodeViewResp.getChildItemType());
        switch (childrenObject.getDataType()) {
            // 线体
            case BopConstant.LINEAL_BODY:
                childrenObject.setObjectType(EntityUtil.getRevision(dataObjectTypeMap.get(childrenObject.getDataType())));
                Gte4MfgLinePrBomReq gte4MfgLinePrBomReq = new Gte4MfgLinePrBomReq();
                if (null != bopNodeViewResp.getUid() && !"0".equals(bopNodeViewResp.getUid())) {
                    gte4MfgLinePrBomReq.setBomNodeReq(bomNodeReq);
                }
                gte4MfgLinePrBomReq.setLinePrRevisionReq((Gte4MfgLinePrRevisionReq) childrenObject);
                bomEditRevisionResp = (BomEditRevisionResp)iImportRevisionService.updateGte4MfgLinePr(gte4MfgLinePrBomReq);
                break;
            case BopConstant.POSITION:
                childrenObject.setObjectType(EntityUtil.getRevision(dataObjectTypeMap.get(childrenObject.getDataType())));
                Gte4MfgStationPrBomReq gte4MfgStationPrBomReq = new Gte4MfgStationPrBomReq();
                gte4MfgStationPrBomReq.setStationPrRevisionReq((Gte4MfgStationPrRevisionReq) childrenObject);
                if (null != bopNodeViewResp.getUid() && !"0".equals(bopNodeViewResp.getUid())) {
                    gte4MfgStationPrBomReq.setBomNodeReq(bomNodeReq);
                }
                bomEditRevisionResp = (BomEditRevisionResp)iImportRevisionService.updateGte4MfgStationPr(gte4MfgStationPrBomReq);
                break;
            case BopConstant.PROCEDURE:
                childrenObject.setObjectType(EntityUtil.getRevision(dataObjectTypeMap.get(childrenObject.getDataType())));
                Gte4MfgOperationBomReq gte4MfgOperationBomReq = new Gte4MfgOperationBomReq();
                gte4MfgOperationBomReq.setOperationRevisionReq((Gte4MfgOperationRevisionReq) childrenObject);
                if (null != bopNodeViewResp.getUid() && !"0".equals(bopNodeViewResp.getUid())) {
                    gte4MfgOperationBomReq.setBomNodeReq(bomNodeReq);
                }
                bomEditRevisionResp = (BomEditRevisionResp)iImportRevisionService.updateGte4MfgOperation(gte4MfgOperationBomReq);
                break;
            case BopConstant.STEP:
                childrenObject.setObjectType(dataObjectTypeMap.get(childrenObject.getDataType()));
                Gte4MfgStepBomReq gte4MfgStepBomReq = new Gte4MfgStepBomReq();
                gte4MfgStepBomReq.setStepReq((Gte4MfgStepReq) childrenObject);
                if (null != bopNodeViewResp.getUid() && !"0".equals(bopNodeViewResp.getUid())) {
                    gte4MfgStepBomReq.setBomNodeReq(bomNodeReq);
                }
                bomEditRevisionResp = (BomEditRevisionResp)iImportRevisionService.updateGte4MfgStep(gte4MfgStepBomReq);
                break;
            case BopConstant.AUXILIARY_MATERIAL:
            case BopConstant.EQUIPMENT:
            case BopConstant.MEASURE:
            case BopConstant.TOOL:
            case BopConstant.GTE4PART:
            case BopConstant.WORKING:
            case BopConstant.FICTITIOUS:
                if (StringUtils.isNotBlank(bomNodeReq.getUid())) {
                    bomNodeReq.setObjectType(new BOMNodeEntity().getObjectType());
                    //调用编辑bom属性方法  此方法有bug 父级为工步 没有版本 会抛异常
//                    bomNodeDomainService.update(bomNodeReq);
                }
                break;
            default:
                break;

        }
        if (null != bomEditRevisionResp) {
            if (BopConstant.STEP.equals(childrenObject.getDataType())){
                Gte4MfgStepResp itemRevisionResp = (Gte4MfgStepResp)bomEditRevisionResp.getObjectResp();
                childrenObject.setUid(itemRevisionResp.getUid());
                childrenObject.setLeftObject(itemRevisionResp.getUid());
                childrenObject.setLeftObjectType(itemRevisionResp.getObjectType());
                return;
            }
            ItemRevisionResp itemRevisionResp = (ItemRevisionResp)bomEditRevisionResp.getObjectResp();
            childrenObject.setUid(itemRevisionResp.getUid());
            childrenObject.setRevisionId(itemRevisionResp.getRevisionId());
            childrenObject.setLeftObject(itemRevisionResp.getLeftObject());
            childrenObject.setLeftObjectType(itemRevisionResp.getLeftObjectType());
            childrenObject.setObjectType(itemRevisionResp.getObjectType());
        }
    }

    /**
     * 递归创建 子级 数据
     * @param childrenObject 需要被新增的子级
     * @param isRecursion 是否需要继续执行递归
     * @param parentObject 需要被子级关联bom行的父级
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void recursionChildrenCreate(MsgbImportReq childrenObject,MsgbImportReq parentObject
            ,Map<String,String> dataObjectTypeMap,Item item,boolean isRecursion){
        WorkspaceObjectResp workspaceObjectResp = null;
        /**
         *  创建 bom行 数据
         */
        BOMNodeReq bomNodeReq = new BOMNodeReq();
        if(null != childrenObject.getQuantity() && 0 != childrenObject.getQuantity()){
            bomNodeReq.setQuantity(childrenObject.getQuantity());
        }
        if(null != childrenObject.getFoundNo() && 0 != childrenObject.getFoundNo()){
            bomNodeReq.setFoundNo(childrenObject.getFoundNo());
        }
        // 父组件
        bomNodeReq.setParentItem(parentObject.getLeftObject());
        bomNodeReq.setParentItemType(parentObject.getLeftObjectType());
        // 父组件版本
        bomNodeReq.setParentItemRev(parentObject.getUid());
        childrenObject.setObjectType(dataObjectTypeMap.get(childrenObject.getDataType()));
        switch (childrenObject.getDataType()){
            // 线体
            case BopConstant.LINEAL_BODY:
                Gte4MfgLinePrBomReq gte4MfgLinePrBomReq = new Gte4MfgLinePrBomReq();
                gte4MfgLinePrBomReq.setBomNodeReq(bomNodeReq);
                gte4MfgLinePrBomReq.setLinePrRevisionReq((Gte4MfgLinePrRevisionReq)childrenObject);
                workspaceObjectResp =this.saveGte4MfgLinePr(gte4MfgLinePrBomReq);
                break;
            case BopConstant.POSITION:
                Gte4MfgStationPrBomReq gte4MfgStationPrBomReq = new Gte4MfgStationPrBomReq();
                gte4MfgStationPrBomReq.setStationPrRevisionReq((Gte4MfgStationPrRevisionReq)childrenObject);
                gte4MfgStationPrBomReq.setBomNodeReq(bomNodeReq);
                workspaceObjectResp = this.saveGte4MfgStationPr(gte4MfgStationPrBomReq);
                break;
            case BopConstant.PROCEDURE:
                Gte4MfgOperationBomReq gte4MfgOperationBomReq = new Gte4MfgOperationBomReq();
                gte4MfgOperationBomReq.setOperationRevisionReq((Gte4MfgOperationRevisionReq)childrenObject);
                gte4MfgOperationBomReq.setBomNodeReq(bomNodeReq);
                workspaceObjectResp = this.saveGte4MfgOperation(gte4MfgOperationBomReq);
                break;
            case BopConstant.STEP:
                Gte4MfgStepBomReq gte4MfgStepBomReq = new Gte4MfgStepBomReq();
                gte4MfgStepBomReq.setStepReq((Gte4MfgStepReq)childrenObject);
                gte4MfgStepBomReq.setBomNodeReq(bomNodeReq);
                workspaceObjectResp = this.saveGte4MfgStep(gte4MfgStepBomReq);
                break;
            case BopConstant.AUXILIARY_MATERIAL:
            case BopConstant.EQUIPMENT:
            case BopConstant.MEASURE:
            case BopConstant.TOOL:
            case BopConstant.GTE4PART:
            case BopConstant.WORKING:
            case BopConstant.FICTITIOUS:
                this.addBomNodeRelation(childrenObject,parentObject,dataObjectTypeMap);
                break;
            default:
                break;

        }
        if (null != workspaceObjectResp) {
            childrenObject.setRevisionId(workspaceObjectResp.getRightObjectRevId());
            childrenObject.setUid(workspaceObjectResp.getRightObject());
            childrenObject.setLeftObject(workspaceObjectResp.getUid());
            childrenObject.setLeftObjectType(workspaceObjectResp.getObjectType());
            childrenObject.setObjectType(workspaceObjectResp.getRightObjectType());
        }
        if (isRecursion) {
            executeMethod(childrenObject.getChildren(),childrenObject, dataObjectTypeMap, item);
        }
    }

    /**
     *  获取已存在数据的树形结构。
     *  通过原树形结构和导入的树形结构 执行递归对比方法
     * @param childrenObject 导入节点版本信息
     * @param parentObject 导入节点父级对象
     * @param dataObjectTypeMap 存储不同数据类型的objectType的集合
     * @param item
     */
    @Transactional(rollbackFor = Exception.class)
    public void comparison(MsgbImportReq childrenObject,MsgbImportReq parentObject,Map<String,String> dataObjectTypeMap,Item item){
        FindReq findReq = new FindReq();
        findReq.setDeep(10);
        findReq.setUid(childrenObject.getLeftObject());
        findReq.setObjectType(childrenObject.getLeftObjectType());
        BOPNodeViewResp bopNodeViewResp = ibomNodeDomainService.find(BOPNodeViewResp.class, findReq, AppNameEnum.EBOM);
        /**
         *       更新当前节点
         *       当父类为新增节点,但子类为已存在节点。添加父类于子类的 bom行关联
         */
        if (null != parentObject){
            this.recursionChildrenUpdate(childrenObject,bopNodeViewResp,dataObjectTypeMap);
            this.addBomNodeRelation(childrenObject, parentObject,dataObjectTypeMap);
        }
        /**
         *  此方法出现的情况：
         *      当父类为已存在节点,更新子级数据并且添加父类于子类的 bom行关联
         *      会调用这个方法的地方只有当父类已存在 子类已存在会调用此方法，进入executeMethod 在进行递归对比方法。其他情况都不会进入此方法中
         */
        else if (null == bopNodeViewResp.getChildren()){
            executeMethod(childrenObject.getChildren(),childrenObject,dataObjectTypeMap,item);
        }else {
            // 第二层级 对比递归方法
            // 因为当父类未关联 并且已存在 进入executeMethod 在进行递归对比方法。所以不需要在次执行 递归对比
            recursionComparison(bopNodeViewResp.getChildren(), childrenObject.getChildren(),childrenObject,dataObjectTypeMap,item);
        }
    }

    /**
     * @param bopNodeViewResp bom行第二级级列表信息
     * @param childrenObjectList 导入版本信息子级集合
     * @param parentObject 导入版本父级信息
     * @param dataObjectTypeMap 存储不同数据类型的objectType的集合
     * @param item
     *  递归对比第二级方法
     *  1.当 原始数据不为空 并且 导入数据为空 删除所以原始数据
     *  2.当 原始数据为空 并且 导入数据不为空 循环 新增所以导入数据
     *  3.当都不为空时 判断集合数据
     *      3.1 集合原始数据存在 导入数据不存在 删除原始数据
     *      3.2 都存在更新原始数据
     *              判断原始二级节点已存在于导入节点数据中更新
     *              判断原始二级节点不存在导入节点数据中删除
     *              判断导入节点不存在于原始节点中新增
     *      3.3 原始数据不存在 导入数据存在 新增导入数据
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public void recursionComparison(List<BOMNodeResp> bopNodeViewResp,List<MsgbImportReq> childrenObjectList,MsgbImportReq parentObject,Map<String,String> dataObjectTypeMap,Item item){

        // 原始数据不为空 并且 导入数据为空 删除所以原始数据
        if (null != bopNodeViewResp && null == childrenObjectList){
            bopNodeViewResp.forEach(bomNodeResp -> {
                IdRequest idRequest = IdRequest.builder().uid(bomNodeResp.getUid()).objectType(new BOMNodeEntity().getObjectType()).build();
                ibomNodeDomainService.deleteObject(idRequest);
            });
        }
        //原始数据为空 并且 导入数据不为空 循环 新增所有导入数据
        else if (null == bopNodeViewResp && null != childrenObjectList){
            childrenObjectList.forEach(msgbImportReq ->{
                recursionChildrenCreate(msgbImportReq,parentObject,dataObjectTypeMap,item,true);
            });
            //判断集合数据
        }else if (null != bopNodeViewResp && null != childrenObjectList){
            List<ItemRevisionEntity> originalPartRevision = new ArrayList<>();
            // 判断原始数据和导入数据的对比 执行 删除 或者 更新方法
            bopNodeViewResp.forEach(bomNodeResp -> {
                // 通过bom行获取版本信息
                ItemRevisionEntity revisionEntity = (ItemRevisionEntity)item.getLastVersion(bomNodeResp.getChildItem(), bomNodeResp.getChildItemType());
                revisionEntity.setLeftObject(bomNodeResp.getChildItem());
                revisionEntity.setLeftObjectType(bomNodeResp.getChildItemType());
                originalPartRevision.add(revisionEntity);

                List<MsgbImportReq> collect = childrenObjectList.stream().filter(t ->
                        null != t.getItemId() &&
                                t.getItemId().equals(revisionEntity.getItemId()) &&
                                t.getRevisionId().equals(revisionEntity.getRevisionId()))
                        .collect(Collectors.toList());
                if (!collect.isEmpty()){
                    this.recursionChildrenUpdate(collect.get(0),bomNodeResp,dataObjectTypeMap);
                }else {
                    IdRequest idRequest = IdRequest.builder().uid(bomNodeResp.getUid()).objectType(new BOMNodeEntity().getObjectType()).build();
                    ibomNodeDomainService.deleteObject(idRequest);
                }
            });
            // 判断导入数据和 原始数据的 执行新增方法
            childrenObjectList.forEach(msgbImportReq ->{
                if (null == msgbImportReq.getItemId()){
                    this.recursionChildrenCreate(msgbImportReq,parentObject,dataObjectTypeMap,item,false);
                }else {
                    insertOrRelation(item,dataObjectTypeMap,originalPartRevision,msgbImportReq,childrenObjectList,parentObject);
                }
            });
        }
        if ( (null != bopNodeViewResp || null != childrenObjectList )) {
            recursionComparisonChildren(bopNodeViewResp, childrenObjectList, item,dataObjectTypeMap);
        }
    }

    /**
     * 递归对比第三层及以下节点
     * @param bopNodeViewResp
     * @param trees
     */
    @Transactional(rollbackFor = Exception.class)
    public void recursionComparisonChildren(List<BOMNodeResp> bopNodeViewResp,List<MsgbImportReq> trees,Item item,Map<String,String> dataObjectTypeMap){
        List<MsgbImportReq> reqList = new ArrayList<>();
        if (null != trees) {
            trees.forEach(tree -> {
                if (null != tree.getChildren()) {
                    tree.getChildren().forEach(t -> {
                        reqList.add(t);
                    });
                }
            });
        }
        List<BOMNodeResp> bomNodeRespList = new ArrayList<>();
        if (null != bopNodeViewResp) {
            bopNodeViewResp.forEach(bomNodeResp -> {
                if (null != bomNodeResp.getChildren()) {
                    bomNodeResp.getChildren().forEach(t -> {
                        bomNodeRespList.add(t);
                    });
                }
            });
        }
        List<ItemRevisionEntity> originalPartRevision = new ArrayList<>();
        List<Gte4MfgStepEntity> originalPart = new ArrayList<>();
        bopNodeViewResp.forEach(bomNodeResp -> {
            List<BOMNodeResp> children = bomNodeResp.getChildren();
            // 原始数据不为空 并且 导入数据为空 删除所以原始数据
            if (null != children && reqList.isEmpty()){
                children.forEach( childrenBomNodeResp -> {
                    IdRequest idRequest = IdRequest.builder().uid(childrenBomNodeResp.getUid()).objectType(new BOMNodeEntity().getObjectType()).build();
                    ibomNodeDomainService.deleteObject(idRequest);
                    bomNodeRespList.remove(childrenBomNodeResp);

                });
            }
            else if (null != children && null != reqList){
                // 判断原始数据和导入数据的对比 执行更新方法
                children.forEach(childrenBomNodeResp -> {
                    // 工步没有版本
                    if ("Gte4MfgStep".equals(childrenBomNodeResp.getChildItemType())){
                        Gte4MfgStepEntity itemEntity = getItemEntity(childrenBomNodeResp.getChildItem());
                        originalPart.add(itemEntity);
                        List<MsgbImportReq> collect = reqList.stream().filter(t ->
                                null != t.getItemId() &&
                                        t.getItemId().equals(itemEntity.getItemId()))
                                .collect(Collectors.toList());
                        if (!collect.isEmpty()) {
                            //  解决 当可以匹配ItemId 需要在去判断 这条数据的bom行父级uid 是否等级 导入的结构的父级 uid
                            MsgbImportReq parentObject = this.getImportParent(collect.get(0), trees);
                            if (parentObject.getLeftObject() .equals(childrenBomNodeResp.getParentItem())) {
                                this.recursionChildrenUpdate(collect.get(0), childrenBomNodeResp, dataObjectTypeMap);
                            }else {
                                IdRequest idRequest = IdRequest.builder().uid(childrenBomNodeResp.getUid()).objectType(new BOMNodeEntity().getObjectType()).build();
                                ibomNodeDomainService.deleteObject(idRequest);
                                bomNodeRespList.remove(childrenBomNodeResp);
                            }
                        } else {
                            IdRequest idRequest = IdRequest.builder().uid(childrenBomNodeResp.getUid()).objectType(new BOMNodeEntity().getObjectType()).build();
                            ibomNodeDomainService.deleteObject(idRequest);
                            bomNodeRespList.remove(childrenBomNodeResp);
                        }
                    }else {
                        // 通过bom行获取版本信息
                        ItemRevisionEntity itemRevisionEntity = (ItemRevisionEntity)item.getLastVersion(childrenBomNodeResp.getChildItem(), childrenBomNodeResp.getChildItemType());
                        originalPartRevision.add(itemRevisionEntity);
                        List<MsgbImportReq> collect = reqList.stream().filter(t ->
                                null != t.getItemId() &&
                                        t.getItemId().equals(itemRevisionEntity.getItemId()) &&
                                        t.getRevisionId().equals(itemRevisionEntity.getRevisionId()))
                                .collect(Collectors.toList());
                        if (!collect.isEmpty()) {
                            this.recursionChildrenUpdate(collect.get(0), childrenBomNodeResp, dataObjectTypeMap);
                        } else {
                            IdRequest idRequest = IdRequest.builder().uid(childrenBomNodeResp.getUid()).objectType(new BOMNodeEntity().getObjectType()).build();
                            ibomNodeDomainService.deleteObject(idRequest);
                            bomNodeRespList.remove(childrenBomNodeResp);
                        }
                    }
                });
            }
        });

        /**
         * 第二级以下：
         *      原始数据为空，导入数据不为空，新增导入数据入库。
         *      新入库父级节点为 trees(第二级节点) 新增或者编辑第二级节点时 将 该节点的 左对象uid 左对象类型 右对象uid 全部重新赋值上去
         *      再次新增以下节点时 可以直接绑定父级节点并赋值。
         */
        if (originalPartRevision.isEmpty() && originalPart.isEmpty() && !reqList.isEmpty()){
            reqList.forEach(msgbImportReq ->{
                MsgbImportReq parentObject = getImportParent(msgbImportReq, trees);
                this.recursionChildrenCreate(msgbImportReq,parentObject,dataObjectTypeMap,item,true);

            });
        }
        /**
         * 当第二级以下：
         *   原始数据和导入数据都不为空时。 循环判断需要新增的 导入数据节点。
         *   当itemId和版本都匹配不上时。则证明该导入节点并不存在于原始节点
         *   新增该节点
         */
        if (!originalPartRevision.isEmpty() || !originalPart.isEmpty()) {
            if (!reqList.isEmpty()) {
                // 判断导入数据和 原始数据的 执行新增方法
                reqList.forEach(msgbImportReq -> {
                    MsgbImportReq parentObject = getImportParent(msgbImportReq, trees);
                    if (null == msgbImportReq.getItemId()) {
                        this.recursionChildrenCreate(msgbImportReq, parentObject, dataObjectTypeMap, item, false);
                    } else {
                        if (!originalPartRevision.isEmpty()) {
                            insertOrRelation(item, dataObjectTypeMap, originalPartRevision, msgbImportReq, trees, parentObject);
                        }
                        else if (!originalPart.isEmpty()){
                            insertOrRelation(originalPart, msgbImportReq, trees, parentObject,item, dataObjectTypeMap);
                        }
                    }
                });
            }
        }
        if (!bomNodeRespList.isEmpty() || !reqList.isEmpty()){
            recursionComparisonChildren(bomNodeRespList , reqList,item,dataObjectTypeMap);
        }

    }


    /**
     * 新增节点或者重新关联节点方法
     *  当excel填入的 itemId 存在于数据库则添加节点于ItemId节点的bom行关联 反之则新增节点
     *  当itemId已存在时判断 上级是否与关联下级 无关联新增关联 有则不动
     * @param originalPartRevision
     * @param msgbImportReq
     * @param trees
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertOrRelation(Item item,Map<String,String> dataObjectTypeMap,List<ItemRevisionEntity> originalPartRevision
            ,MsgbImportReq msgbImportReq,List<MsgbImportReq> trees,MsgbImportReq parentObject){
        ItemRevisionEntity itemRevisionEntity = new ItemRevisionEntity();
        if (null != msgbImportReq.getUid()){
            itemRevisionEntity.setUid(msgbImportReq.getUid());
            itemRevisionEntity.setObjectType(msgbImportReq.getObjectType());
            itemRevisionEntity.setLeftObject(msgbImportReq.getLeftObject());
            itemRevisionEntity.setLeftObjectType(msgbImportReq.getLeftObjectType());
        }
        if (null == itemRevisionEntity) {
            List<ItemRevisionEntity> collect =
                    originalPartRevision.stream().filter(t ->
                            t.getItemId().equals(msgbImportReq.getItemId()) &&
                                    t.getRevisionId().equals(msgbImportReq.getRevisionId())).collect(Collectors.toList());
            if (collect.isEmpty()) {
                recursionChildrenCreate(msgbImportReq,parentObject,dataObjectTypeMap,item,false);
            }
        }else {
            if (null == parentObject){
                MsgbImportReq msgbImport = getImportParent(msgbImportReq, trees);
                parentObject = msgbImport;

            }
            if (!getChildrenBOMNodeIsExist(parentObject,itemRevisionEntity)) {
                /**
                 *  当 parentItemUid 出现的场景为 添加一个已存在的节点 并且该节点和源节点没有关系。这样的节点不会触发更新因为匹配itemId匹配不上，不会触发新增因为已存在。
                 *  所以不会被赋值左对象和右对象信息 当他在验证他自己带的或者新增的下一级子级时他的左对象和右对象都会为null 需要重新赋值
                 */
                // 更新 已存在节点数据
                BOMNodeResp grr = new BOMNodeResp();
                grr.setChildItem(itemRevisionEntity.getLeftObject());
                grr.setParentItemType(parentObject.getLeftObjectType());
                grr.setChildItemType(itemRevisionEntity.getLeftObjectType());
                this.addBomNodeRelation(msgbImportReq,parentObject,dataObjectTypeMap);

            }
        }
    }


    /**
     * 新增节点或者重新关联节点方法
     *  当excel填入的 itemId 存在于数据库则添加节点于ItemId节点的bom行关联 反之则新增节点
     *  当itemId已存在时判断 上级是否与关联下级 无关联新增关联 有则不动
     * @param originalPart
     * @param msgbImportReq
     * @param trees
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertOrRelation(List<Gte4MfgStepEntity> originalPart,MsgbImportReq msgbImportReq,List<MsgbImportReq> trees,MsgbImportReq parentObject,Item item,Map<String,String> dataObjectTypeMap){
        ItemRevisionEntity itemRevisionEntity = new ItemRevisionEntity();
        //当已存在节点并且改节点不在原结构中时 uid会为null 需要查询该节点的原始版本信息
        if (null != msgbImportReq.getUid()){
            itemRevisionEntity.setUid(msgbImportReq.getUid());
            itemRevisionEntity.setObjectType(msgbImportReq.getObjectType());
            itemRevisionEntity.setLeftObject(msgbImportReq.getLeftObject());
            itemRevisionEntity.setLeftObjectType(msgbImportReq.getLeftObjectType());
        }
        if (null == itemRevisionEntity) {
            List<Gte4MfgStepEntity> collect =
                    originalPart.stream().filter(t ->
                            t.getItemId().equals(msgbImportReq.getItemId())).collect(Collectors.toList());
            if (collect.isEmpty()) {
                recursionChildrenCreate(msgbImportReq,parentObject,dataObjectTypeMap,item,false);
            }
        }else {
            if (null == parentObject){
                MsgbImportReq msgbImport = getImportParent(msgbImportReq, trees);
                parentObject = msgbImport;

            }
            if (!getChildrenBOMNodeIsExist(parentObject,itemRevisionEntity)) {
                /**
                 *  当 parentItemUid 出现的场景为 添加一个已存在的节点 并且该节点和源节点没有关系。这样的节点不会触发更新因为匹配itemId匹配不上，不会触发新增因为已存在。
                 *  所以不会被赋值左对象和右对象信息 当他在验证他自己带的或者新增的下一级子级时他的左对象和右对象都会为null 需要重新赋值
                 */
                // 更新 已存在节点数据
                BOMNodeResp grr = new BOMNodeResp();
                grr.setChildItem(itemRevisionEntity.getLeftObject());
                grr.setParentItemType(parentObject.getLeftObjectType());
                grr.setChildItemType(itemRevisionEntity.getLeftObjectType());
                this.addBomNodeRelation(msgbImportReq,parentObject,dataObjectTypeMap);
            }
        }
    }


    /**
     * 获取父级节点对象
     * @param msgbImportReq 子级级导入层级结构
     * @param trees 父级层级集合
     * @return
     */
    public MsgbImportReq getImportParent(MsgbImportReq msgbImportReq,List<MsgbImportReq> trees){
        String rank = msgbImportReq.getRank();
        String childrenRank = rank.substring(0, rank.lastIndexOf("."));
        return trees.stream().filter(originals -> originals.getRank().equals(childrenRank)).findFirst().get();
    }


    /**
     * 添加bom行关联
     * @param childrenObject 导入单个新增数据对象
     * @param parentObject 导入节点父级对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void addBomNodeRelation(MsgbImportReq childrenObject,MsgbImportReq parentObject,Map<String,String> dataObjectTypeMap){
        BOMNodeReq bomNodeReq = new BOMNodeReq();
        if(null != childrenObject.getQuantity() && 0 != childrenObject.getQuantity()){
            bomNodeReq.setQuantity(childrenObject.getQuantity());
        }
        if(null != childrenObject.getFoundNo() && 0 != childrenObject.getFoundNo()){
            bomNodeReq.setFoundNo(childrenObject.getFoundNo());
        }
        bomNodeReq.setParentItemRev(parentObject.getUid());
        bomNodeReq.setParentItemRevId(parentObject.getRevisionId());
        bomNodeReq.setParentItemType(parentObject.getLeftObjectType());
        bomNodeReq.setChildItem(childrenObject.getLeftObject());
        bomNodeReq.setParentItem(parentObject.getLeftObject());
        bomNodeReq.setChildItemType(dataObjectTypeMap.get(childrenObject.getDataType()));
        ibomNodeDomainService.createNode(bomNodeReq, AppNameEnum.EBOM);
    }


    /**
     * 解析excel 获取集合  失败数 成功数  失败描述
     * @param file
     * @return
     */
    public List<MsgbImportReq> excelImportVerify(MultipartFile file,Map<String,Object> statisticsMap){
        Map<String,MsgbImportReq> objectReqMap = new ConcurrentHashMap<>(15);
        objectReqMap.put(BopConstant.FACTORY,new Gte4MfgPlantPrRevisionReq());
        objectReqMap.put(BopConstant.LINEAL_BODY,new Gte4MfgLinePrRevisionReq());
        objectReqMap.put(BopConstant.POSITION,new Gte4MfgStationPrRevisionReq());
        objectReqMap.put(BopConstant.PROCEDURE,new Gte4MfgOperationRevisionReq());
        objectReqMap.put(BopConstant.STEP,new Gte4MfgStepReq());
        objectReqMap.put(BopConstant.TOOL,new ToolRevisionReq());
        objectReqMap.put(BopConstant.MEASURE,new MeasureRevisionReq());
        objectReqMap.put(BopConstant.AUXILIARY_MATERIAL,new AuxiliaryMaterialRevisionReq());
        objectReqMap.put(BopConstant.EQUIPMENT,new EquipmentRevisionReq());
        objectReqMap.put(BopConstant.GTE4PART,new Gte4PartRevisionReq());
        objectReqMap.put(BopConstant.WORKING,new Gte4StationRevisionReq());
        objectReqMap.put(BopConstant.FICTITIOUS,new Gte4ProcessRevisionReq());
        Map<String,MsgbImportReq> objectReqVerifyMap = new ConcurrentHashMap<>(5);
        objectReqVerifyMap.putAll(objectReqMap);
        BopVerify bopVerify = new BopVerify();
        statisticsMap.put("okNum",0);
        statisticsMap.put("failNum",0);
        statisticsMap.put("errorMsg","");
        bopVerify.setStatisticsMap(statisticsMap);
        bopVerify.setObjectMap(objectReqVerifyMap);
        bopVerify.setDictUtil(dictUtil);
        super.setObjectMap(objectReqMap);
        super.setCheck(bopVerify);
        super.setNumber("层级");
        super.setRowNum(3);
        super.setDictUtil(dictUtil);
        return super.analysis(file);
    }


    public Map<String,String> getDataObjectType(){
        Map<String,String> DataObjectTypeMap = new HashMap<>();
        DataObjectTypeMap.put(BopConstant.FACTORY,"Gte4MfgPlantPr");
        DataObjectTypeMap.put(BopConstant.LINEAL_BODY,"Gte4MfgLinePr");
        DataObjectTypeMap.put(BopConstant.POSITION,"Gte4MfgStationPr");
        DataObjectTypeMap.put(BopConstant.PROCEDURE,"Gte4MfgOperation");
        DataObjectTypeMap.put(BopConstant.STEP,"Gte4MfgStep");
        DataObjectTypeMap.put(BopConstant.TOOL,"Tool");
        DataObjectTypeMap.put(BopConstant.MEASURE,"Measure");
        DataObjectTypeMap.put(BopConstant.AUXILIARY_MATERIAL,"AuxiliaryMaterial");
        DataObjectTypeMap.put(BopConstant.EQUIPMENT,"Equipment");
        DataObjectTypeMap.put(BopConstant.GTE4PART,"Gte4Part");
        DataObjectTypeMap.put(BopConstant.WORKING,"Gte4Station");
        DataObjectTypeMap.put(BopConstant.FICTITIOUS,"Gte4Process");
        return DataObjectTypeMap;
    }

    /**
     * 判断 itemId 和 版本是否有存在的
     *  如果存在 通过 数据类型 和 itemId 和 版本 查询具体主键信息并查询最新版本
     * @param msgbImportReq
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ItemRevisionEntity isExist(MsgbImportReq msgbImportReq,Map<String,String> dataObjectTypeMap,Item item){
        if (null != msgbImportReq.getItemId()) {
            List<Pair<String, Object>> params = Arrays.asList(
                    Pair.of(ItemEntity.ITEM_ID, msgbImportReq.getItemId()));
            List<WorkspaceObjectEntity> fetch = EntityUtil.getDynamicEqQuery(dataObjectTypeMap.get(msgbImportReq.getDataType()), params).fetch();
            if (!fetch.isEmpty()){
                WorkspaceObjectEntity workspaceObjectEntity = fetch.get(0);
                ItemRevisionEntity lastVersion = (ItemRevisionEntity)item.getLastVersion(workspaceObjectEntity.getUid(), workspaceObjectEntity.getObjectType());
                lastVersion.setLeftObject(workspaceObjectEntity.getUid());
                lastVersion.setLeftObjectType(workspaceObjectEntity.getObjectType());
                return lastVersion;
            }
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public WorkspaceObjectEntity isExistGte4MfgStepEntity(MsgbImportReq msgbImportReq,Map<String,String> dataObjectTypeMap,Item item){
        if (null != msgbImportReq.getItemId()) {
            List<Pair<String, Object>> params = Arrays.asList(
                    Pair.of(ItemEntity.ITEM_ID, msgbImportReq.getItemId()));
            List<WorkspaceObjectEntity> fetch = EntityUtil.getDynamicEqQuery(dataObjectTypeMap.get(msgbImportReq.getDataType()), params).fetch();
            if (!fetch.isEmpty()){
                return fetch.get(0);
            }
        }
        return null;
    }


    @Transactional(rollbackFor = Exception.class)
    public MsgbImportReq getMsgbImportReqVersion(MsgbImportReq msgbImportReq,Map<String,String> dataObjectTypeMap,Item item){
        if (null != msgbImportReq.getItemId() && null != msgbImportReq.getRevisionId()) {
            List<Pair<String, Object>> params = Arrays.asList(
                    Pair.of(Gte4PartRevisionEntity.ITEM_ID, msgbImportReq.getItemId()));
            List<WorkspaceObjectEntity> fetch = EntityUtil.getDynamicEqQuery(dataObjectTypeMap.get(msgbImportReq.getDataType()), params).fetch();
            if (!fetch.isEmpty()){
                WorkspaceObjectEntity workspaceObjectEntity = fetch.get(0);
                WorkspaceObjectEntity lastVersion = item.getLastVersion(workspaceObjectEntity.getUid(), workspaceObjectEntity.getObjectType());
                msgbImportReq.setUid(lastVersion.getUid());
                msgbImportReq.setLeftObject(workspaceObjectEntity.getUid());
                msgbImportReq.setLeftObjectType(workspaceObjectEntity.getObjectType());
                // 重新添加关联的节点需要在此处更新
                return msgbImportReq;
            }
        }
        return msgbImportReq;
    }

    @Transactional(rollbackFor = Exception.class)
    public MsgbImportReq getMsgbImportReq(MsgbImportReq msgbImportReq,Map<String,String> dataObjectTypeMap,Item item){
        if (null != msgbImportReq.getItemId() && null != msgbImportReq.getRevisionId()) {
            List<Pair<String, Object>> params = Arrays.asList(
                    Pair.of(Gte4PartRevisionEntity.ITEM_ID, msgbImportReq.getItemId()));
            List<WorkspaceObjectEntity> fetch = EntityUtil.getDynamicEqQuery(dataObjectTypeMap.get(msgbImportReq.getDataType()), params).fetch();
            if (!fetch.isEmpty()){
                WorkspaceObjectEntity workspaceObjectEntity = fetch.get(0);
                msgbImportReq.setObjectType(workspaceObjectEntity.getObjectType());
                msgbImportReq.setUid(workspaceObjectEntity.getUid());
                // 重新添加关联的节点需要在此处更新
                return msgbImportReq;
            }
        }
        return msgbImportReq;
    }


    @Transactional(rollbackFor = Exception.class)
    public Gte4MfgStepEntity getItemEntity(String uid){
        if (StringUtils.isNotBlank(uid)) {
            List<Pair<String, Object>> params = Arrays.asList(
                    Pair.of(ItemEntity.UID, uid));
            List<WorkspaceObjectEntity> fetch = EntityUtil.getDynamicEqQuery(new Gte4MfgStepEntity().getObjectType(), params).fetch();
            if (!fetch.isEmpty()){
                return (Gte4MfgStepEntity)fetch.get(0);
            }
        }
        return null;
    }


    /**
     *
     * 判断父级和子级是否存在关联关系
     * @param parentObject 父级对象
     * @return children 子级
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean getChildrenBOMNodeIsExist(MsgbImportReq parentObject,WorkspaceObjectEntity children){
        /**
         *  当 parentItemUid 出现的场景为 添加一个已存在的节点 并且该节点和源节点没有关系。这样的节点不会触发更新因为匹配itemId匹配不上，不会触发新增因为已存在。
         *  所以不会被赋值左对象和右对象信息 只需要添加关联就行
         */
        if (null != parentObject){
            List<Pair<String, Object>> params = Arrays.asList(
                    Pair.of(BOMNodeEntity.PARENT_ITEM, parentObject.getLeftObject()),
                    Pair.of(BOMNodeEntity.PARENT_ITEM_TYPE, parentObject.getLeftObjectType()),
                    Pair.of(BOMNodeEntity.CHILD_ITEM,children.getLeftObject()),
                    Pair.of(BOMNodeEntity.CHILD_ITEM_TYPE,children.getLeftObjectType()));
            List<WorkspaceObjectEntity> fetch = EntityUtil.getDynamicEqQuery(new BOMNodeEntity().getObjectType(), params).fetch();
            if (!fetch.isEmpty()){
                return true;
            }
        }

        return false;
    }

    /**
     * 递归解析 层级 组成树形解构
     * @param current
     * @param originalList
     */
    public void recursionChildren(MsgbImportReq current, List<MsgbImportReq> originalList) {
        List<MsgbImportReq> countList = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            String currentNumber = String.valueOf(i);
            StringBuffer currentChildren = new StringBuffer(current.getRank()).append(".").append(currentNumber);
            List<MsgbImportReq> collect = originalList.stream().filter(gprirs -> currentChildren.toString().equals(gprirs.getRank())).collect(Collectors.toList());
            countList.addAll(collect);
            if (!collect.isEmpty()){
                collect.forEach(gprir -> {
                    recursionChildren(gprir,originalList);
                });
            }else {
                return;
            }
            current.setChildren(countList);
        }
    }



    @Async
    public void bopExport(String uid,String objectType, String taskUid,TokenInfo tokenInfo){
        TokenContext.setToken(tokenInfo);
        try {
            // 获得需要导出的字典数据
            MoreDictEntryReq moreDictEntryReq = new MoreDictEntryReq();
            List<String> appCodes = new ArrayList<>(1);
            appCodes.add(AppNameEnum.IMEXPORT.getCode());
            List<String> dictTypes = new ArrayList<>();
            dictTypes.add(DictConstant.IMEXPORTTASK_BOP_EXPORT);
            moreDictEntryReq.setAppCodes(appCodes);
            moreDictEntryReq.setDictTypes(dictTypes);
            List<MoreDictEntryGroupVo> codeValueList = dictUtil.getMoreDictEntryVo(moreDictEntryReq);
            /**
             * 组装3层表头
             */
            //组装成5个分类
            Map<String, List<MoreFieldEntryVo>> mapMoreFieldEntryVo = new LinkedHashMap<>(5);
            List<String> countField = new ArrayList<>();
            //用来统计一共多少行 便于合并单元格
            AtomicInteger atomicInteger = new AtomicInteger();
            codeValueList.forEach(moreDictEntryGroup -> {
                List<MoreDictEntryVo> dictList = moreDictEntryGroup.getDictList();
                Map<String, List<MoreDictEntryVo>> listMoreDictEntryVo = new LinkedHashMap<>(5);
                //根据类型先分组
                dictList.forEach(moreDictEntryVo -> {
                    if (StringUtils.isBlank(moreDictEntryVo.getParentId())) {
                        List<MoreDictEntryVo> collect = dictList.stream().filter(l -> StringUtils.isNotBlank(l.getParentId()) && l.getParentId().equals(moreDictEntryVo.getId())).collect(Collectors.toList());
                        if (collect.isEmpty()) {
                            listMoreDictEntryVo.put(moreDictEntryVo.getValue(), Arrays.asList(moreDictEntryVo));
                            return;
                        }
                        listMoreDictEntryVo.put(moreDictEntryVo.getValue(), collect);
                    }
                });
                //组建表头对象
                Iterator<Map.Entry<String, List<MoreDictEntryVo>>> iterator = listMoreDictEntryVo.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, List<MoreDictEntryVo>> next = iterator.next();
                    List<MoreDictEntryVo> moreDictEntryVos = next.getValue();
                    // 是否必填和显示列组装
                    List<MoreFieldEntryVo> moreFieldEntryVos = new ArrayList<>();
                    if (null != moreDictEntryVos && !moreDictEntryVos.isEmpty()) {
                        moreDictEntryVos.forEach(moreDictEntryVo -> {
                            atomicInteger.incrementAndGet();
                            MoreFieldEntryVo moreFieldEntryVo = new MoreFieldEntryVo();
                            AuxiliaryMaterialRevisionImportReq parseObject = JSON.parseObject(moreDictEntryVo.getRemark(), AuxiliaryMaterialRevisionImportReq.class);
                            moreFieldEntryVo.setFieldName(moreDictEntryVo.getValue());
                            countField.add(moreDictEntryVo.getCode());
                            if (Boolean.TRUE.equals(parseObject.getRequired())) {
                                moreFieldEntryVo.setRequired("必填项");
                                moreFieldEntryVos.add(moreFieldEntryVo);
                                return;
                            }
                            moreFieldEntryVo.setRequired("非必填项");
                            moreFieldEntryVos.add(moreFieldEntryVo);
                        });
                    }
                    mapMoreFieldEntryVo.put(next.getKey(), moreFieldEntryVos);
                }
            });

            FindReq findReq = new FindReq();
            findReq.setDeep(10);
            findReq.setUid(uid);
            findReq.setObjectType(objectType);
            BOPNodeViewResp bopNodeViewResp = ibomNodeDomainService.find(BOPNodeViewResp.class, findReq, AppNameEnum.EBOM);

            // 先组装顶层数据
            List<List<String>> listList = new ArrayList<>();
            Item item = SpringUtil.getBean(ItemFactory.class).create();
            WorkspaceObjectEntity lastVersion = item.getLastVersion(bopNodeViewResp.getChildItem(), bopNodeViewResp.getChildItemType());
            List<String> values = new ArrayList<>();
            values.add("1");
            listList.add(analysis(lastVersion, values, countField, getDateType()));
            resursion(bopNodeViewResp, countField, item, "1", listList);
            File file = ExportExcelUtil.customBopExportExcels(mapMoreFieldEntryVo, "工艺规划导出(*为下拉选项,#为数字选项,&为单选选项)", atomicInteger.get(), listList);
            LezaoResult<IntegrationFileVo> lzdigit = remoteLezaoIntegrationService.upload(FileUtils.getMultipartFile(file), "lzdigit");
            Files.delete(file.toPath());
            iImexportTaskDomainService.exportSuccess(taskUid,lzdigit.getData().getUrl());
        }catch (Exception e){
            e.printStackTrace();
            iImexportTaskDomainService.error(taskUid,e.getMessage());
        }
    }



    public void resursion(BOMNodeResp bopNodeViewResp,List<String> countField, Item item,String rank, List<List<String>> listList){
        if (!Objects.isNull(bopNodeViewResp.getChildren())){
            AtomicReference<String> childrenRank = new AtomicReference<>("1");
            bopNodeViewResp.getChildren().forEach(bomNodeResp -> {
                List<String> values = new ArrayList<>();
                WorkspaceObjectEntity lastVersion = null;
                //工步没有版本
                if ("Gte4MfgStep".equals(bomNodeResp.getChildItemType())){
                    List<Pair<String, Object>> params = Arrays.asList(
                            Pair.of(ItemEntity.UID, bomNodeResp.getChildItem()));
                    lastVersion = EntityUtil.getDynamicEqQuery(new Gte4MfgStepEntity().getObjectType(), params).fetchFirst();
                }else {
                    lastVersion = item.getLastVersion(bomNodeResp.getChildItem(), bomNodeResp.getChildItemType());
                }
                values.add(rank+"."+childrenRank.get());
                listList.add(analysis(lastVersion, values, countField,getDateType()));
                resursion(bomNodeResp,countField,  item, rank+"."+childrenRank.get(),listList);
                childrenRank.set(Integer.parseInt(childrenRank.get()) + 1 + "");
            });
        }
    }

    /**
     * 将对象类型转换成不同的对象 去拼接行数据
     * @param lastVersion
     * @param values
     * @param countField
     * @return
     */
    public List<String> analysis(WorkspaceObjectEntity lastVersion,List<String> values,List<String> countField,Map<String,String> getDateType){
        Class<? extends MsgbImportReq> aClass = null;
        if (lastVersion instanceof Gte4MfgPlantPrRevisionEntity){
            Gte4MfgPlantPrRevisionEntity entity = (Gte4MfgPlantPrRevisionEntity)lastVersion;
            Gte4MfgPlantPrRevisionReq gte4MfgPlantPrRevisionReq = new Gte4MfgPlantPrRevisionReq();
            BeanUtil.copyPropertiesIgnoreNull(entity,gte4MfgPlantPrRevisionReq);
            aClass = gte4MfgPlantPrRevisionReq.getClass();
            analysisClassObject(aClass, gte4MfgPlantPrRevisionReq,values,countField);
            values.set(3,getDateType.get(entity.getObjectType()));
            return values;
        }else if (lastVersion instanceof Gte4MfgLinePrRevisionEntity){
            Gte4MfgLinePrRevisionEntity entity = (Gte4MfgLinePrRevisionEntity)lastVersion;
            Gte4MfgLinePrRevisionReq gte4MfgLinePrRevisionReq = new Gte4MfgLinePrRevisionReq();
            BeanUtil.copyPropertiesIgnoreNull(entity,gte4MfgLinePrRevisionReq);
            aClass = gte4MfgLinePrRevisionReq.getClass();
            analysisClassObject(aClass, gte4MfgLinePrRevisionReq,values,countField);
            values.set(3,getDateType.get(entity.getObjectType()));
            return values;
        }else if (lastVersion instanceof Gte4MfgStationPrRevisionEntity){
            Gte4MfgStationPrRevisionEntity entity = (Gte4MfgStationPrRevisionEntity)lastVersion;
            Gte4MfgStationPrRevisionReq gte4MfgStationPrRevisionReq = new Gte4MfgStationPrRevisionReq();
            BeanUtil.copyPropertiesIgnoreNull(entity,gte4MfgStationPrRevisionReq);
            aClass = gte4MfgStationPrRevisionReq.getClass();
            analysisClassObject(aClass, gte4MfgStationPrRevisionReq,values,countField);
            values.set(3,getDateType.get(entity.getObjectType()));
            return values;
        }else if (lastVersion instanceof Gte4MfgOperationRevisionEntity){
            Gte4MfgOperationRevisionEntity entity = (Gte4MfgOperationRevisionEntity)lastVersion;
            Gte4MfgOperationRevisionReq gte4MfgOperationRevisionReq = new Gte4MfgOperationRevisionReq();
            BeanUtil.copyPropertiesIgnoreNull(entity,gte4MfgOperationRevisionReq);
            aClass = gte4MfgOperationRevisionReq.getClass();
            analysisClassObject(aClass, gte4MfgOperationRevisionReq,values,countField);
            values.set(3,getDateType.get(entity.getObjectType()));
            return values;
        }else if (lastVersion instanceof Gte4MfgStepEntity){
            Gte4MfgStepEntity entity = (Gte4MfgStepEntity)lastVersion;
            Gte4MfgStepReq gte4MfgStepReq = new Gte4MfgStepReq();
            BeanUtil.copyPropertiesIgnoreNull(entity,gte4MfgStepReq);
            aClass = gte4MfgStepReq.getClass();
            analysisClassObject(aClass, gte4MfgStepReq,values,countField);
            values.set(3,getDateType.get(entity.getObjectType()));
            return values;
        }else{
            ItemRevisionEntity entity = (ItemRevisionEntity)lastVersion;
            values.add(entity.getItemId());
            values.add(entity.getRevisionId());
            values.add(getDateType.get(entity.getObjectType()));
            return values;
        }
    }


    public Map<String,String> getDateType(){
        Map<String,String> DataObjectTypeMap = new HashMap<>();
        DataObjectTypeMap.put("Gte4MfgPlantPrRevision",BopConstant.FACTORY);
        DataObjectTypeMap.put("Gte4MfgLinePrRevision",BopConstant.LINEAL_BODY);
        DataObjectTypeMap.put("Gte4MfgStationPrRevision",BopConstant.POSITION);
        DataObjectTypeMap.put("Gte4MfgOperationRevision",BopConstant.PROCEDURE);
        DataObjectTypeMap.put("Gte4MfgStep",BopConstant.STEP);
        DataObjectTypeMap.put("ToolRevision",BopConstant.TOOL);
        DataObjectTypeMap.put("MeasureRevision",BopConstant.MEASURE);
        DataObjectTypeMap.put("AuxiliaryMaterialRevision",BopConstant.AUXILIARY_MATERIAL);
        DataObjectTypeMap.put("EquipmentRevision",BopConstant.EQUIPMENT);
        DataObjectTypeMap.put("Gte4PartRevision",BopConstant.GTE4PART);
        DataObjectTypeMap.put("Gte4StationRevision",BopConstant.WORKING);
        DataObjectTypeMap.put("Gte4ProcessRevision",BopConstant.FICTITIOUS);
        return DataObjectTypeMap;
    }





    /**
     *  拼接一行数据 根据导出列的顺序 查询传入对象的值，查不到则为null
     * @param aClass 需要被递归查找值的对象class
     * @param revisionReq 有值对象被反射出值
     * @param values 按顺序存值的集合 (有序)
     * @param countField 按顺序被查询的字段 (有序)
     * @return
     */
    protected List<String> analysisClassObject(Class<? extends MsgbImportReq> aClass,MsgbImportReq revisionReq, List<String> values,List<String> countField) {
        countField.forEach(l ->{
            Field field = getField(aClass, l);
            Object object = "";
            Object value = "";
            if (null != field) {
                field.setAccessible(true);
                try {
                    object = field.get(revisionReq);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                final JsonDict jsonDict = field.getAnnotation(JsonDict.class);
                if (null != jsonDict && null != object) {
                    super.setDictUtil(dictUtil);
                    value = paramDictionary(jsonDict, object);
                    values.add(value.toString());
                }else {
                    if (object != null){
                        if (Boolean.TRUE.equals(object)){
                            values.add("是");
                        }else if (Boolean.FALSE.equals(object)){
                            values.add("否");
                        }else {
                            values.add(object.toString());
                        }
                    }else {
                        values.add("");
                    }

                }
            }else {
                values.add(value.toString());
            }
        });

        return values;
    }

    /**
     * 递归反射获取字段
     * @param aClass
     * @param name
     * @return
     */
    protected Field getField(Class<?> aClass, String name){
        while (null != aClass){
            Field[] fields = aClass.getDeclaredFields();
            for (Field field:fields){
                if (name.equals(field.getName())){
                    return field;
                }
            }
            aClass =  aClass.getSuperclass();
        }
        return null;
    }

}
