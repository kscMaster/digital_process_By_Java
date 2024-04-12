package com.nancal.esop.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.nancal.api.model.BOMNodeResp;
import com.nancal.api.model.BOPNodeViewResp;
import com.nancal.common.base.Response;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.esop.consts.EsopConst;
import com.nancal.esop.db.*;
import com.nancal.esop.entity.*;
import com.nancal.esop.repository.*;
import com.nancal.esop.util.EsopUseUtil;
import com.nancal.esop.util.MD5Util;
import com.nancal.remote.service.RemoteEbomService;
import com.nancal.remote.service.RemoteMsgBomService;
import com.nancal.remote.to.MsgBomTo;
import com.nancal.remote.vo.FindVo;
import com.nancal.remote.vo.MsgBomVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/***
 * 同步ESOP数据业务功能
 *
 * @author 徐鹏军
 * @date 2022/8/9 16:52
 */
@RefreshScope
@Slf4j
@Component
public class EsopGeneratorService {

    @Autowired
    private ProductBopRepository productBopRepository;
    @Autowired
    private DesignRepository designRepository;
    @Autowired
    private OperationRelationRepository operationRelationRepository;
    @Autowired
    private PartitionRepository partitionRepository;
    @Autowired
    private ProcessRepository processRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private BopMgmtRepository bopMgmtRepository;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private PppRelationRepository pppRelationRepository;
    @Autowired
    private PartTopRepository partTopRepository;
    @Autowired
    private PartBomRepository partBomRepository;
    @Autowired
    private PartMaterialRepository partMaterialRepository;
    @Autowired
    private MoRepository moRepository;
    @Autowired
    private MoMaterialRepository moMaterialRepository;
    @Autowired
    private RemoteMsgBomService remoteMsgBomService;
    @Autowired
    private RemoteEbomService remoteEbomService;

    @ApiModelProperty("相册id")
    @Value("${esop.volume-id:410556007152156672}")
    private String volumeId;
    @ApiModelProperty("视频ID")
    @Value("${esop.video-id:410556540483076096}")
    private String videoId;

    /***
     * 生成ESOP主业务逻辑
     *
     * @param bomResp 工艺规划的BOM查询结果
     * @author 徐鹏军
     * @date 2022/8/9 16:56
     */
    @Transactional(rollbackFor = Exception.class)
    public void generateEsop(BOPNodeViewResp bomResp) {
        log.info("---- start into create ESOP business ----");
        // 1:生成产品
        String productName = bomResp.getExtraProperty("resp_gte4ProductNameDesc", bomResp.getExtraProperty("resp_gte4ProductName", StrUtil.EMPTY));
        ProductBopEntity productBopEntity = ProductBopEntity.create(productName);
        EsopBopMgmtDB mgmtDB = new EsopBopMgmtDB();
        mgmtDB.setBopRevUid(productBopEntity.getBopRevUid());
        mgmtDB.setBopUid(productBopEntity.getBopUid());
        mgmtDB.setLifecycle(0);
        String lifeCycleState = bomResp.getExtraProperty("resp_lifeCycleState", StrUtil.EMPTY);
        if (lifeCycleState.equalsIgnoreCase("Released")) {
            mgmtDB.setLifecycle(1);
        }
        BopMgmtEntity bopMgmtEntity = BopMgmtEntity.create(mgmtDB);
        // 2：创建分区
        String partitionName = bomResp.getExtraProperty("resp_gte4partitionDesc", "Assembly");
        PartitionEntity assemblyPartitionEntity = PartitionEntity.create(productBopEntity.getBopRevUid(), partitionName);
        // 3:递归创建工艺和工序
        List<Object> dataList = new ArrayList<>();
        recursionCreate(dataList, bomResp, productBopEntity.getBopRevUid(), assemblyPartitionEntity.getId().getPartitionUid(), null,null);
        // 4:创建订单
        createOrder(dataList, bomResp, productBopEntity.getBopRevUid());
        // 5:找出所有的工序，并按照顺序分好前区工序和后置工序
        createOperationRelation(dataList, bomResp, productBopEntity.getBopRevUid());
        System.out.println("入参数据：" + JSON.toJSONString(bomResp));
        System.out.println("产品数据：" + JSON.toJSONString(productBopEntity));
        System.out.println("分区数据：" + JSON.toJSONString(assemblyPartitionEntity));
        System.out.println("其它数据：" + JSON.toJSONString(dataList));
        // 6:将工艺,工序,资源等拆分入库
        save(dataList, productBopEntity, bopMgmtEntity, assemblyPartitionEntity);
        log.info("---- end into create ESOP business ----");
    }

    /***
     * 递归获取所有的工艺和工序,以及资源
     *
     * @param dataList 结果集合
     * @param bom 乐造数据集
     * @param bopRevUid ESOP产品版本id
     * @param partitionUid ESOP分区id
     * @author 徐鹏军
     * @date 2022/8/10 15:21
     */
    public void recursionCreate(List<Object> dataList, BOMNodeResp bom, String bopRevUid, String partitionUid, String parentRevUid,String operationRevId) {
        addEntity(dataList, bom, bopRevUid, partitionUid, parentRevUid,operationRevId);
        if (CollUtil.isEmpty(bom.getChildren())) {
            return;
        }
        String revUid = bom.getRevUid();
        String operationRevIdNew = operationRevId;
        if (bom.getExtraProperty("resp_objectType", StrUtil.EMPTY).equalsIgnoreCase("Gte4MfgOperationRevision")){
            operationRevIdNew = revUid;
        }
        for (BOMNodeResp bomResp : bom.getChildren()) {
            String revisionObjectType = bomResp.getExtraProperty("resp_objectType", StrUtil.EMPTY);
            // 如果是工步，则直接跳过
            if (revisionObjectType.equalsIgnoreCase("Gte4MfgStep") || revisionObjectType.equalsIgnoreCase("Gte4MfgStepRevision")) {
                continue;
            }
            recursionCreate(dataList, bomResp, bopRevUid, partitionUid, revUid,operationRevIdNew);
        }
    }

    private void addEntity(List<Object> dataList, BOMNodeResp bomResp, String bopRevUid, String partitionUid, String parentRevUid,String operationRevId) {
        String revisionObjectType = bomResp.getExtraProperty("resp_objectType", StrUtil.EMPTY);
        // 如果是工艺，则创建ESOP的工艺
        if (revisionObjectType.equalsIgnoreCase("Gte4MfgProcessRevision")) {
            ProcessEntity processEntity = createProcess(bomResp, bopRevUid, partitionUid);
            dataList.add(processEntity);
        } else if (revisionObjectType.equalsIgnoreCase("Gte4MfgOperationRevision")) {
            // 如果是工序，则创建ESOP的工序
            OperationEntity operationEntity = createOperation(bomResp, bopRevUid, partitionUid, parentRevUid);
            dataList.add(operationEntity);
        } else if (revisionObjectType.equalsIgnoreCase("AuxiliaryMaterialRevision")) {
            // 如果是辅材
            ResourceEntity resourceEntity = createResource(bomResp, bopRevUid, partitionUid, EsopConst.RE_ACCESSORY);
            dataList.add(resourceEntity);
            // 创建关联对象
            OperationRelationEntity operationRelationEntity = createOperationRelation(bomResp, bopRevUid, EsopConst.ACCESSORY);
            dataList.add(operationRelationEntity);
        } else if (revisionObjectType.equalsIgnoreCase("EquipmentRevision")) {
            // 如果是设备
            ResourceEntity resourceEntity = createResource(bomResp, bopRevUid, partitionUid, EsopConst.RE_EQUIPMENT);
            dataList.add(resourceEntity);
            // 创建关联对象
            OperationRelationEntity operationRelationEntity = createOperationRelation(bomResp, bopRevUid, EsopConst.EQUIPMENT);
            dataList.add(operationRelationEntity);
        } else if (revisionObjectType.equalsIgnoreCase("Gte4PartRevision")) {
            // 如果是设计零组件
            DesignEntity designEntity = createDesign(bomResp);
            dataList.add(designEntity);
            // 创建关联对象
            OperationRelationEntity operationRelationEntity = createOperationRelation(bomResp, bopRevUid, EsopConst.DESIGN);
            dataList.add(operationRelationEntity);
            // 创建part_bom和operation关联对象
            EsopOperationRelationDB relationDB = new EsopOperationRelationDB();
            // 工序的版本id
            relationDB.setLezaoOpRevUid(operationRevId);
            relationDB.setEsopBopRevId(bopRevUid);
            relationDB.setLezaoRelatedObject(bomResp.getRevUid());
            relationDB.setEsopRelationType(EsopConst.DESIGN);
            OperationRelationEntity operationRelationEntity2 = OperationRelationEntity.create(relationDB);
            dataList.add(operationRelationEntity2);
        } else {
            // 其它就是资源（比如：工具夹，量具）
            ResourceEntity resourceEntity = createResource(bomResp, bopRevUid, partitionUid, EsopConst.RE_RESOURCE);
            dataList.add(resourceEntity);
            // 创建关联对象
            OperationRelationEntity operationRelationEntity = createOperationRelation(bomResp, bopRevUid, EsopConst.RESOURCE);
            dataList.add(operationRelationEntity);
        }
    }

    private ProcessEntity createProcess(BOMNodeResp bomResp, String bopRevUid, String partitionUid) {
        String uid = bomResp.getChildItem();
        String revUid = bomResp.getRevUid();
        String name = bomResp.getExtraProperty("resp_objectName", StrUtil.EMPTY);
        String foundNo = Convert.toStr(bomResp.getFoundNo(), "10");
        String revisionId = bomResp.getExtraProperty("resp_revisionId", "001");
        String itemId = bomResp.getExtraProperty("resp_itemId", StrUtil.EMPTY);
        EsopProcessDB processDB = new EsopProcessDB();
        processDB.setLezaoProcessUid(uid);
        processDB.setLezaoProcessRevUid(revUid);
        processDB.setLezaoProcessNum(foundNo);
        processDB.setLezaoProcessName(name);
        processDB.setLezaoRevisionId(revisionId);
        processDB.setEsopPartitionUid(partitionUid);
        processDB.setEsopBopRevId(bopRevUid);
        processDB.setLezaoItemId(itemId);
        return ProcessEntity.create(processDB);
    }

    private OperationEntity createOperation(BOMNodeResp bomResp, String bopRevUid, String partitionUid, String parentRevUid) {
        String uid = bomResp.getChildItem();
        String revUid = bomResp.getRevUid();
        String name = bomResp.getExtraProperty("resp_objectName", StrUtil.EMPTY);
        String foundNo = Convert.toStr(bomResp.getFoundNo(), "10");
        String revisionId = bomResp.getExtraProperty("resp_revisionId", "001");
        String itemId = bomResp.getExtraProperty("resp_itemId", StrUtil.EMPTY);
        EsopOperationDB operationDB = new EsopOperationDB();
        operationDB.setLezaoOpRevUid(revUid);
        operationDB.setLezaoOpName(name);
        operationDB.setEsopBopRevUid(bopRevUid);
        operationDB.setLezaoOpNum(foundNo);
        operationDB.setEsopVolumeId(videoId);
        Boolean isKey = bomResp.getExtraProperty("resp_isKey", false);
        operationDB.setLezaoKeyOperation(isKey ? 1 : 0);
        operationDB.setEsopPartitionUid(partitionUid);
        operationDB.setLezaoOpUid(uid);
        operationDB.setLezaoProcessRevUid(StrUtil.blankToDefault(parentRevUid, bomResp.getParentItemRev()));
        operationDB.setLezaoRevisionId(revisionId);
        operationDB.setLezaoItemId(itemId);
        return OperationEntity.create(operationDB);
    }

    private ResourceEntity createResource(BOMNodeResp bomResp, String bopRevUid, String partitionUid, Integer resourceType) {
        String revUid = bomResp.getRevUid();
        String name = bomResp.getExtraProperty("resp_objectName", StrUtil.EMPTY);
        String revisionId = bomResp.getExtraProperty("resp_revisionId", "001");
        String itemId = bomResp.getExtraProperty("resp_itemId", StrUtil.EMPTY);
        ResourceDB resourceDB = new ResourceDB();
        resourceDB.setLezaoItemId(itemId);
        resourceDB.setLezaoResourceName(name);
        resourceDB.setLezaoResourceRevUid(revUid);
        resourceDB.setLezaoSpec(null);
        resourceDB.setLezaoRemark(null);
        resourceDB.setEsopVolumeId(volumeId);
        resourceDB.setLezaoRevisionId(revisionId);
        resourceDB.setEsopResourceType(resourceType);
        return ResourceEntity.create(resourceDB);
    }

    private DesignEntity createDesign(BOMNodeResp bomResp) {
        String uid = bomResp.getChildItem();
        String revUid = bomResp.getRevUid();
        String name = bomResp.getExtraProperty("resp_objectName", StrUtil.EMPTY);
        String revisionId = bomResp.getExtraProperty("resp_revisionId", "001");
        String itemId = bomResp.getExtraProperty("resp_itemId", StrUtil.EMPTY);
        EsopDesignDB designDB = new EsopDesignDB();
        designDB.setLezaoDegignRevUid(revUid);
        designDB.setLezaoDesignUid(uid);
        designDB.setLezaoDesignName(name);
        designDB.setLezaoRevisionId(revisionId);
        designDB.setEsopVolumeId(volumeId);
        designDB.setLezaoItemId(itemId);
        return DesignEntity.create(designDB);
    }

    private OperationRelationEntity createOperationRelation(BOMNodeResp bomResp, String bopRevUid, Integer relationType) {
        String revUid = bomResp.getRevUid();
        EsopOperationRelationDB relationDB = new EsopOperationRelationDB();
        relationDB.setLezaoOpRevUid(bomResp.getParentItemRev());
        relationDB.setEsopBopRevId(bopRevUid);
        relationDB.setLezaoRelatedObject(revUid);
        relationDB.setEsopRelationType(relationType);
        return OperationRelationEntity.create(relationDB);
    }

    private void createOperationRelation(List<Object> dataList, BOMNodeResp bomResp, String bopRevUid) {
        if (CollUtil.isNotEmpty(bomResp.getChildren()) && bomResp.getChildren().size() > 1) {
            for (int i = 0; i < bomResp.getChildren().size(); i++) {
                BOMNodeResp bom = bomResp.getChildren().get(i);
                if (i == bomResp.getChildren().size() - 1) {
                    // 针对最后一个，只有前区工序
                    BOMNodeResp beforeBom = CollUtil.get(bomResp.getChildren(), i - 1);
                    EsopOperationRelationDB relationDB = new EsopOperationRelationDB();
                    relationDB.setLezaoOpRevUid(beforeBom.getRevUid());
                    relationDB.setEsopBopRevId(bopRevUid);
                    relationDB.setLezaoRelatedObject(bom.getRevUid());
                    relationDB.setEsopRelationType(EsopConst.PREDECESSOR);
                    OperationRelationEntity operationRelationEntity = OperationRelationEntity.create(relationDB);
                    dataList.add(operationRelationEntity);
                    continue;
                }
                if (i != 0) {
                    // 中间的数据，既有前区工序，也有后置工序
                    BOMNodeResp beforeBom = CollUtil.get(bomResp.getChildren(), i - 1);
                    EsopOperationRelationDB relationDB = new EsopOperationRelationDB();
                    relationDB.setLezaoOpRevUid(beforeBom.getRevUid());
                    relationDB.setEsopBopRevId(bopRevUid);
                    relationDB.setLezaoRelatedObject(bom.getRevUid());
                    relationDB.setEsopRelationType(EsopConst.PREDECESSOR);
                    OperationRelationEntity operationRelationEntity = OperationRelationEntity.create(relationDB);
                    dataList.add(operationRelationEntity);
                }
                // 获取下一个工序数据
                BOMNodeResp nextBom = CollUtil.get(bomResp.getChildren(), i + 1);
                // 针对第一个，只有后置工序
                EsopOperationRelationDB relationDB = new EsopOperationRelationDB();
                relationDB.setLezaoOpRevUid(bom.getRevUid());
                relationDB.setEsopBopRevId(bopRevUid);
                relationDB.setLezaoRelatedObject(nextBom.getRevUid());
                relationDB.setEsopRelationType(EsopConst.SUCCESSOR);
                OperationRelationEntity operationRelationEntity = OperationRelationEntity.create(relationDB);
                dataList.add(operationRelationEntity);
            }
        }
    }

    private void createOrder(List<Object> dataList, BOMNodeResp bomResp, String bopRevUid) {
        // 2.1:获取关联的根节点
        Response<MsgBomVo> response = remoteMsgBomService.findMfgTargetRL(new MsgBomTo(bomResp.getRevUid(), bomResp.getRevObjectType()));
        System.out.println("关联根节点数据：" + JSON.toJSONString(response));
        if (Objects.isNull(response) || response.isFail()) {
            log.info("查询不到关联的数据，不进行订单创建");
            return;
        }
        log.info("---- 获取关联根节点的树 ----");
        MsgBomVo msgBomVo = response.getData();
        Response<List<FindVo>> listResponse = remoteEbomService.find(new MsgBomTo(msgBomVo.getRightObject(), msgBomVo.getRightObjectType()));
        System.out.println("关联根节点的树数据：" + JSON.toJSONString(listResponse));
        if (Objects.isNull(listResponse) || listResponse.isFail()) {
            log.info("查询关联的子数据，不进行订单创建");
            return;
        }
        String orderId = IdGeneratorUtil.generate();
        String lineCode = EsopUseUtil.randomStringUpper(1);
        String productModel = StrUtil.subSufByLength(bopRevUid, 4);
        PppRelationDB pppRelationDB = new PppRelationDB();
        pppRelationDB.setPppUid(encryptPlantAndModel(EsopConst.sopb_bop_mgmt$sit_name,lineCode,productModel));
        pppRelationDB.setBopUid(bopRevUid);
        pppRelationDB.setProductModel(productModel);
        pppRelationDB.setLineCode(lineCode);
        pppRelationDB.setBopRevUid(bopRevUid);
        // 创建关系
        PppRelationEntity pppRelationEntity = PppRelationEntity.create(pppRelationDB);
        dataList.add(pppRelationEntity);
        // 创建线体
        LineEntity lineEntity = LineEntity.create(lineCode);
        dataList.add(lineEntity);
        recursionCreate(dataList, CollUtil.getFirst(listResponse.getData()), productModel, orderId, lineCode);
    }

    public void recursionCreate(List<Object> dataList, FindVo vo, String bopRevUid, String orderId, String lineCode) {
        // 如果是根节点，则保存SOP_PART_TOP表
        if (vo.getParentItem().equals("0")) {
            // 创建SOPB_PART_TOP
            PartTopDB partTopDB = new PartTopDB();
            partTopDB.setItemId(vo.getResp_itemId());
            partTopDB.setPartName(vo.getResp_objectName());
            partTopDB.setPartUid(vo.getChildItem());
            partTopDB.setRevisionId(vo.getResp_revisionId());
            partTopDB.setPartRevUid(vo.getRevUid());
            partTopDB.setLinkDesign(vo.getRevUid());
            partTopDB.setVolumeId(volumeId);
            partTopDB.setProductModule(bopRevUid);
            PartTopEntity partTopEntity = PartTopEntity.create(partTopDB);
            dataList.add(partTopEntity);
            // 创建订单
            MoDB moDB = new MoDB();
            moDB.setId(orderId);
            moDB.setMtmDesc(vo.getResp_objectName());
            moDB.setItemId(vo.getResp_itemId());
            moDB.setLineCodes(lineCode);
            moDB.setProductModule(bopRevUid);
            MoEntity moEntity = MoEntity.create(moDB);
            dataList.add(moEntity);
        } else {
            // 创建SOPB_PART_BOM
            PartBomEntity partBomEntity = PartBomEntity.create(vo.getResp_itemId(), vo.getRevUid(), volumeId, bopRevUid);
            dataList.add(partBomEntity);
            // 创建SOPB_PART_MATERIAL
            PartMaterialDB partMaterialDB = new PartMaterialDB();
            partMaterialDB.setItemId(vo.getResp_itemId());
            partMaterialDB.setPartName(vo.getResp_objectName());
            partMaterialDB.setRevisionId(vo.getResp_revisionId());
            partMaterialDB.setPartRevUid(vo.getRevUid());
            partMaterialDB.setLinkDesignUid(vo.getRevUid());
            partMaterialDB.setVolumeId(volumeId);
            PartMaterialEntity partMaterialEntity = PartMaterialEntity.create(partMaterialDB);
            dataList.add(partMaterialEntity);
            EsopDesignDB designDB = new EsopDesignDB();
            designDB.setLezaoDegignRevUid(vo.getRevUid());
            designDB.setLezaoDesignUid(vo.getChildItem());
            designDB.setLezaoDesignName(vo.getResp_objectName());
            designDB.setLezaoRevisionId(vo.getResp_revisionId());
            designDB.setEsopVolumeId(volumeId);
            designDB.setLezaoItemId(vo.getResp_itemId());
            DesignEntity designEntity = DesignEntity.create(designDB);
            dataList.add(designEntity);
            // 创建订单物料
            MoMaterialEntity moMaterialEntity = MoMaterialEntity.create(orderId, vo.getResp_itemId());
            dataList.add(moMaterialEntity);
        }
        if (CollUtil.isEmpty(vo.getChildren())) {
            return;
        }
        for (FindVo bomResp : vo.getChildren()) {
            recursionCreate(dataList, bomResp, bopRevUid, orderId, lineCode);
        }
    }

    private void save(List<Object> dataList, ProductBopEntity productBopEntity, BopMgmtEntity bopMgmtEntity, PartitionEntity partitionEntity) {
        productBopRepository.saveAndFlush(productBopEntity);
        bopMgmtRepository.saveAndFlush(bopMgmtEntity);
        partitionRepository.saveAndFlush(partitionEntity);
        List<DesignEntity> designList = new ArrayList<>();
        List<OperationRelationEntity> operationRelationList = new ArrayList<>();
        List<ProcessEntity> processList = new ArrayList<>();
        List<ResourceEntity> resourceList = new ArrayList<>();
        List<OperationEntity> operationList = new ArrayList<>();
        List<PppRelationEntity> pppRelationList = new ArrayList<>();
        List<MoEntity> moList = new ArrayList<>();
        List<MoMaterialEntity> moMaterialList = new ArrayList<>();
        List<LineEntity> lineList = new ArrayList<>();
        List<PartTopEntity> partTopList = new ArrayList<>();
        List<PartBomEntity> partBomList = new ArrayList<>();
        List<PartMaterialEntity> partMaterialList = new ArrayList<>();
        for (Object o : dataList) {
            if (o instanceof DesignEntity) {
                designList.add((DesignEntity) o);
            }
            if (o instanceof OperationRelationEntity) {
                operationRelationList.add((OperationRelationEntity) o);
            }
            if (o instanceof ProcessEntity) {
                processList.add((ProcessEntity) o);
            }
            if (o instanceof ResourceEntity) {
                resourceList.add((ResourceEntity) o);
            }
            if (o instanceof OperationEntity) {
                operationList.add((OperationEntity) o);
            }
            if (o instanceof PppRelationEntity) {
                pppRelationList.add((PppRelationEntity) o);
            }
            if (o instanceof MoEntity) {
                moList.add((MoEntity) o);
            }
            if (o instanceof MoMaterialEntity) {
                moMaterialList.add((MoMaterialEntity) o);
            }
            if (o instanceof LineEntity) {
                lineList.add((LineEntity) o);
            }
            if (o instanceof PartTopEntity) {
                partTopList.add((PartTopEntity) o);
            }
            if (o instanceof PartBomEntity) {
                partBomList.add((PartBomEntity) o);
            }
            if (o instanceof PartMaterialEntity) {
                partMaterialList.add((PartMaterialEntity) o);
            }
        }
        if (CollUtil.isNotEmpty(designList)) {
            designRepository.saveAllAndFlush(designList);
        }
        if (CollUtil.isNotEmpty(operationRelationList)) {
            operationRelationRepository.saveAllAndFlush(operationRelationList);
        }
        if (CollUtil.isNotEmpty(processList)) {
            processRepository.saveAllAndFlush(processList);
        }
        if (CollUtil.isNotEmpty(resourceList)) {
            resourceRepository.saveAllAndFlush(resourceList);
        }
        if (CollUtil.isNotEmpty(operationList)) {
            operationRepository.saveAllAndFlush(operationList);
        }
        if (CollUtil.isNotEmpty(partTopList)) {
            partTopRepository.saveAll(partTopList);
        }
        if (CollUtil.isNotEmpty(partBomList)) {
            partBomRepository.saveAll(partBomList);
        }
        if (CollUtil.isNotEmpty(partMaterialList)) {
            partMaterialRepository.saveAllAndFlush(partMaterialList);
        }
        if (CollUtil.isNotEmpty(lineList)) {
            lineRepository.saveAllAndFlush(lineList);
        }
        if (CollUtil.isNotEmpty(pppRelationList)) {
            pppRelationRepository.saveAllAndFlush(pppRelationList);
        }
        if (CollUtil.isNotEmpty(moList)) {
            moRepository.saveAllAndFlush(moList);
        }
        if (CollUtil.isNotEmpty(moMaterialList)) {
            moMaterialRepository.saveAllAndFlush(moMaterialList);
        }
    }
    /**
     * 加密工厂名称与机型，生成唯一的识别号
     *
     * @param plantName    工厂名称
     * @param lineCode     产线代号
     * @param productModel 机型（4位）
     * @return 识别号
     */
    public static String encryptPlantAndModel(String plantName, String lineCode, String productModel) {
        return MD5Util.encrypt(plantName + EsopConst.PASSWORD_KEY + lineCode + EsopConst.PASSWORD_KEY + productModel);
    }

    public static void main(String[] args) {
        System.out.println(encryptPlantAndModel("IPP", "A", "5456"));
    }
}
