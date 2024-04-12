package com.nancal.msgbom.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.model.dataset.FileAttrResp;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.model.entity.*;
import com.nancal.msgbom.service.IEsopDomainServiceAdaptor;
import com.nancal.service.bo.DatasetFileRL;
import com.nancal.service.bo.FileStorage;
import com.nancal.service.bo.SpecificationRL;
import com.nancal.service.dao.EsopGaStationOperationEntityRepository;
import com.nancal.service.factory.DatasetFileRLFactory;
import com.nancal.service.factory.SpecificationRLFactory;
import com.nancal.service.service.IBOMNodeDomainService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RefreshScope
public class EsopDomainServiceImpl implements IEsopDomainServiceAdaptor {

    @Autowired
    private IBOMNodeDomainService bomNodeDomainService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    protected EsopGaStationOperationEntityRepository gaStationOperationEntityRepository;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Value("${python.execute-path:python}")
    private String executePath;
    @Value("${python.script-execute-timeout:60000}")
    private Integer scriptExecuteTimeout;
    @Value("${python.script-path:D:\\idea_workspace\\digital_process\\app\\msgbom\\src\\main\\resources\\GA_MySQL.py}")
    private String scriptPath;

    /***
     * 生成ESOP
     *
     * @param req
     * @author 徐鹏军
     * @date 2022/8/30 19:00
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> creatEsop(EsopCreateReq req) {
        // 1：获取BOM结构树
        FindReq findReq = new FindReq(req.getUid(), req.getObjectType(), -1);
        BOPNodeViewResp viewResp = bomNodeDomainService.find(BOPNodeViewResp.class, findReq, AppNameEnum.MSGBOM);
        if (ObjectUtil.isNull(viewResp)) {
            return Collections.emptyList();
        }
        // 2：获取所有线体工艺
        List<BOMNodeResp> allLines = this.getAllGte4MfgLinePrBom(viewResp);
        if (CollUtil.isEmpty(allLines)) {
            throw new ServiceException(ErrorCode.FAIL, "视图中不存在线体工艺，无法进行线平衡");
        }
        // 3：校验线体工艺中的工位工艺数量
        if (allLines.stream().allMatch(data -> CollUtil.isEmpty(data.getChildren()))) {
            throw new ServiceException(ErrorCode.FAIL, "视图中线体工艺下不存在工位工艺，无法进行线平衡");
        }
        // 4：过滤工位工艺下工序为空的数据
        allLines = this.getAllOperationBom(allLines);
        if (CollUtil.isEmpty(allLines)) {
            throw new ServiceException(ErrorCode.FAIL, "视图中工位工艺下不存在工序，无法进行线平衡");
        }
        // 5：执行数据同步之前，先将之前同步过的数据删除。防止重复数据
        this.cleanLineBalance(allLines);
        // 6：线平衡前同步基础数据
        List<String> collect = allLines.stream().map(data -> {
            if (CollUtil.isEmpty(data.getChildren())) {
                return null;
            }
            // 获取工位数量
            int stationNum = data.getChildren().size();
            if (Objects.nonNull(req.getStationNum())) {
                if (req.getStationNum() > stationNum) {
                    throw new ServiceException(ErrorCode.FAIL, "当前输入工位数量超过最大数量：" + stationNum);
                }
                stationNum = req.getStationNum();
            }
            // 处理数据
            return this.lineBalanceBefore(data, stationNum, Convert.toStr(req.getEmptyStation()));
        }).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollUtil.isEmpty(collect)) {
            throw new ServiceException(ErrorCode.FAIL, "线平衡异常");
        }
        return collect;
    }

    /***
     * 获取所有线体工艺
     *
     * @param req BOM查询结果
     * @author 徐鹏军
     * @date 2022/8/31 10:34
     * @return {@link List < BOMNodeResp>}
     */
    @Override
    public List<BOMNodeResp> getAllGte4MfgLinePrBom(BOMNodeResp req) {
        if (EntityUtil.getObjectType(Gte4MfgPlantPrRevisionEntity.class).equals(req.getRevObjectType())) {
            // 如果是工厂工艺，则获取子节点，找出它下面的线体
            return req.getChildren();
        }
        if (EntityUtil.getObjectType(Gte4MfgLinePrRevisionEntity.class).equals(req.getRevObjectType())) {
            return Collections.singletonList(req);
        }
        return Collections.emptyList();
    }

    /***
     * 根据线体工艺，找出所有的工序
     *
     * @param resps 线体工艺bom
     * @author 徐鹏军
     * @date 2022/8/31 19:30
     * @return {@link List< BOMNodeResp>}
     */
    @Override
    public List<BOMNodeResp> getAllOperationBom(List<BOMNodeResp> resps) {
        // 工序类型
        String operationObjectType = EntityUtil.getObjectType(Gte4MfgOperationRevisionEntity.class);
        return resps.stream().filter(data -> {
            // 工位工艺
            List<BOMNodeResp> stations = data.getChildren();
            if (CollUtil.isEmpty(stations)) {
                return false;
            }
            for (BOMNodeResp station : stations) {
                if (CollUtil.isEmpty(station.getChildren())) {
                    continue;
                }
                // 工位或者工序
                List<BOMNodeResp> operationOrStations = station.getChildren();
                station.setChildren(null);
                operationOrStations.stream().filter(os -> operationObjectType.equals(os.getRevObjectType()))
                        .forEach(station::add);
            }
            return stations.stream().anyMatch(item -> CollUtil.isNotEmpty(item.getChildren()));
        }).collect(Collectors.toList());
    }

    /***
     * 线平衡前操作，需要先将基础表数据同步
     *
     * @param node 线体工艺的BOM对象
     * @author 徐鹏军
     * @date 2022/8/31 11:18
     * @return {@link String}
     */
    @Override
    public String lineBalanceBefore(BOMNodeResp node, Integer stationNum, String emptyStation) {
        // 同步线平衡需要的表数据
        // 任务表，GA_TASK
        EsopGaTaskEntity taskEntity = EsopGaTaskEntity.create(node.getRevUid(), emptyStation);
        // 线体表，GA_ASSEMLINE
        EsopGaAssemLineEntity assemLineEntity = EsopGaAssemLineEntity.create(taskEntity.getId(), node.getDisplayName(), stationNum);
        // 工序表，GA_OPERATION
        List<EsopGaOperationEntity> operationEntityList = new ArrayList<>();
        // 循环工位工艺
        for (BOMNodeResp stationBom : node.getChildren()) {
            // 注意事项属性
            String gte4ExceptionCautions = stationBom.getExtraProperty("resp_gte4ExceptionCautions", StrUtil.EMPTY);
            // 关键工序属性
            int gte4KeyProcess = BooleanUtil.toInt(stationBom.getExtraProperty("resp_gte4KeyProcess", Boolean.FALSE));
            if(CollUtil.isEmpty(stationBom.getChildren())){
                continue;
            }
            // 循环工序
            for (BOMNodeResp operationBom : stationBom.getChildren()) {
                BOPNodeViewResp respBom = (BOPNodeViewResp) operationBom;
                List<EsopGaOperationEntity> operations = recursionOperation(null, respBom, null);
                // 补填其余的属性
                for (EsopGaOperationEntity operation : operations) {
                    operation.setLineName(assemLineEntity.getLineName());
                    operation.setTaskId(taskEntity.getId());
                    operation.setLineId(assemLineEntity.getId());
                    operation.setKeyOperation(gte4KeyProcess);
                    operation.setAttention(gte4ExceptionCautions);
                }
                operationEntityList.addAll(operations);
            }
        }
        // 保存入库
        entityManager.persist(taskEntity);
        entityManager.persist(assemLineEntity);
        // 针对同一个工序，将他的preOp进行重组合并
        operationEntityList.stream().collect(Collectors.groupingBy(EsopGaOperationEntity::getOpUid)).forEach((revId, values) -> {
            if (values.size() >= 2) {
                String preOp = values.stream().map(EsopGaOperationEntity::getPreOp).distinct().collect(Collectors.joining(StrUtil.COMMA));
                EsopGaOperationEntity first = CollUtil.getFirst(values);
                first.setPreOp(preOp);
            }
            entityManager.persist(CollUtil.getFirst(values));
        });
        return taskEntity.getId();
    }

    /***
     * 根据线体工艺零组件版本id清空之前生成的线平衡数据
     *
     * @param nodes 线体工艺的BOM对象集合
     * @author 徐鹏军
     * @date 2022/8/31 11:18
     * @return {@link String}
     */
    @Override
    public void cleanLineBalance(List<BOMNodeResp> nodes) {
        Optional.ofNullable(nodes).orElse(Collections.emptyList()).forEach(data -> {
            QEsopGaTaskEntity gaTask = QEsopGaTaskEntity.esopGaTaskEntity;
            BooleanBuilder where = new BooleanBuilder();
            where.and(gaTask.revId.eq(data.getRevUid()));
            jpaQueryFactory.delete(gaTask).where(where).execute();
        });
    }

    /***
     * 递归找出工序
     *
     * @param operations 存放工序结果的集合
     * @param parent 父节点工序
     * @param preOp 前区工序，即前一个工序节点的childItem
     * @author 徐鹏军
     * @date 2022/8/31 15:27
     * @return
     */
    private List<EsopGaOperationEntity> recursionOperation(List<EsopGaOperationEntity> operations, BOPNodeViewResp parent, String preOp) {
        if (operations == null) {
            operations = new ArrayList<>();
        }
        // 描述属性
        String desc = parent.getExtraProperty("resp_gte4ActionDescription", StrUtil.EMPTY);
        EsopGaOperationEntity operationEntity = new EsopGaOperationEntity();
        operationEntity.setId(IdGeneratorUtil.generate());
        operationEntity.setOpNum(parent.getChildItem());
        // 工序时间属性
        operationEntity.setOpTime(Convert.toInt(parent.getTaskTime(),0));
        operationEntity.setPreOp(preOp);
        operationEntity.setOpUid(parent.getRevUid());
        operationEntity.setOpName(parent.getObjectName());
        operationEntity.setOpId(parent.getUid());
        operationEntity.setOpDesc(desc);
        operations.add(operationEntity);
        if (CollUtil.isEmpty(parent.getChildren())) {
            return operations;
        }
        for (BOMNodeResp child : parent.getChildren()) {
            BOPNodeViewResp childRes = (BOPNodeViewResp) child;
            // 如果不是工序，则跳过
            if (!EntityUtil.getObjectType(Gte4MfgOperationRevisionEntity.class).equals(childRes.getRevObjectType())) {
                continue;
            }
            recursionOperation(operations, childRes, parent.getChildItem());
        }
        return operations;
    }

    /***
     * 执行Python脚本
     *
     * @param taskId 任务id,即线体工艺bomId
     * @author 徐鹏军
     * @date 2022/8/30 19:14
     * @return {@link int}
     */
    @Override
    public int executePython(String taskId) {
        log.info("----- 开始执行Python脚本 taskId：[{}] ----", taskId);
        log.info("python类库路径：" + executePath);
        log.info("脚本路径：" + scriptPath);
        CommandLine cmdLine = new CommandLine(executePath);
        cmdLine.addArgument(scriptPath);
        cmdLine.addArgument(taskId);
        DefaultExecutor executor = new DefaultExecutor();
        ExecuteWatchdog watchdog = new ExecuteWatchdog(scriptExecuteTimeout);
        executor.setWatchdog(watchdog);
        try {
            int statusCode = executor.execute(cmdLine);
            log.info("----- 结束执行Python脚本 taskId：[{}] ----", taskId);
            return statusCode;
        } catch (Exception e) {
            log.error("执行python脚本异常", e);
            return -1;
        }
    }

    /***
     * 获取最大工位数量
     *
     * @param req
     * @param stationFlag
     * @author 徐鹏军
     * @date 2022/9/1 11:26
     * @return {@link Integer}
     */
    @Override
    public Integer findMaxStationNum(FindReq req, Boolean stationFlag) {
        // 校验是否线体工艺
        String objectType = EntityUtil.getObjectType(Gte4MfgLinePrRevisionEntity.class);
        if (!req.getObjectType().equals(objectType)) {
            throw new ServiceException(ErrorCode.FAIL, "当前查询的不是线体工艺，无法获取最大工位数量");
        }
        BOPNodeViewResp viewResp = bomNodeDomainService.find(BOPNodeViewResp.class, req, AppNameEnum.MSGBOM);
        if (ObjectUtil.isNull(viewResp)) {
            return 0;
        }
        return CollUtil.defaultIfEmpty(viewResp.getChildren(), Collections.emptyList()).size();
    }

    /***
     * 获取线平衡柱状图
     *
     * @param uid 线体工艺版本id
     * @author 徐鹏军
     * @date 2022/9/1 13:35
     * @return {@link List<EsopGaProcessStationResp>}
     */
    @Override
    public List<EsopGaProcessStationResp> findLineBalancePic(String uid) {
        // 根据线体工艺版本id获取任务
        QEsopGaTaskEntity gaTaskEntity = QEsopGaTaskEntity.esopGaTaskEntity;
        BooleanBuilder where = new BooleanBuilder();
        where.and(gaTaskEntity.revId.eq(uid));
        EsopGaTaskEntity gaTask = jpaQueryFactory.selectFrom(gaTaskEntity).where(where).fetchFirst();
        if (Objects.isNull(gaTask)) {
            return Collections.emptyList();
        }
        // 根据任务获取线平衡中的线体数据
        QEsopGaAssemLineEntity assemLineEntity = QEsopGaAssemLineEntity.esopGaAssemLineEntity;
        where = new BooleanBuilder();
        where.and(assemLineEntity.taskId.eq(gaTask.getId()));
        EsopGaAssemLineEntity line = jpaQueryFactory.selectFrom(assemLineEntity).where(where).fetchFirst();
        if (Objects.isNull(line)) {
            return Collections.emptyList();
        }
        // 获取原BOM中的工位工艺
        FindReq req = new FindReq(uid, EntityUtil.getObjectType(Gte4MfgLinePrRevisionEntity.class), 1);
        BOPNodeViewResp viewResp = bomNodeDomainService.find(BOPNodeViewResp.class, req, AppNameEnum.MSGBOM);
        if (Objects.isNull(viewResp) || CollUtil.isEmpty(viewResp.getChildren())) {
            return Collections.emptyList();
        }
        // 根据线体获取线平衡后的工位数据
        QEsopGaProcessStationEntity processStationEntity = QEsopGaProcessStationEntity.esopGaProcessStationEntity;
        where = new BooleanBuilder();
        where.and(processStationEntity.lineId.eq(line.getId()));
        OrderSpecifier<Long> order = new OrderSpecifier<>(Order.ASC, processStationEntity.stationNo);
        List<EsopGaProcessStationEntity> stations = jpaQueryFactory.selectFrom(processStationEntity).where(where).orderBy(order).fetch();
        if (CollUtil.isEmpty(stations)) {
            return Collections.emptyList();
        }
        List<EsopGaProcessStationResp> results = new ArrayList<>();
        for (int i = 0; i < stations.size(); i++) {
            // 根据当前索引，获取对应原工位工艺对象
            BOMNodeResp srcBom = CollUtil.get(viewResp.getChildren(), i);
            EsopGaProcessStationEntity current = stations.get(i);
            EsopGaProcessStationResp resp = new EsopGaProcessStationResp();
            resp.setId(current.getId().toString());
            resp.setLineId(current.getLineId());
            resp.setStationNo(current.getStationNo().toString());
            resp.setStationTime(current.getStationTime());
            if (Objects.nonNull(srcBom)) {
                resp.setStationName(srcBom.getObjectName());
                resp.setRevUid(srcBom.getRevUid());
            } else {
                resp.setStationName("工位工艺");
            }
            results.add(resp);
        }
        return results;
    }

    /***
     * 获取线平衡后左侧树
     *
     * @param req
     * @author 徐鹏军
     * @date 2022/9/1 17:28
     * @return {@link BOMNodeResp}
     */
    @Override
    public BOMNodeResp findLineBalanceAfterTree(FindReq req) {
        // 1：查询BOM结构数据
        BOPNodeViewResp viewResp = bomNodeDomainService.find(BOPNodeViewResp.class, req, AppNameEnum.MSGBOM);
        if (ObjectUtil.isNull(viewResp)) {
            return null;
        }
        String plantObjectType = EntityUtil.getObjectType(Gte4MfgPlantPrRevisionEntity.class);
        String lineObjectType = EntityUtil.getObjectType(Gte4MfgLinePrRevisionEntity.class);
        if (!(plantObjectType.equals(viewResp.getRevObjectType()) || lineObjectType.equals(viewResp.getRevObjectType()))) {
            return null;
        }
        BOMNodeResp root = new BOMNodeResp();
        root.setParentItem("0");
        root.setChildItem(viewResp.getRevUid());
        root.setChildItemType(viewResp.getRevObjectType());
        root.setObjectName(viewResp.getObjectName());
        root.setRevUid(viewResp.getRevUid());
        root.setRevObjectType(viewResp.getRevObjectType());
        // 2：获取所有线体工艺零组件版本id
        Map<String, BOMNodeResp> lineRevBomMap = new HashMap<>();
        if (plantObjectType.equals(viewResp.getRevObjectType())) {
            // 3：获取所有线体工艺
            List<BOMNodeResp> allGte4MfgLinePrBom = this.getAllGte4MfgLinePrBom(viewResp);
            if (CollUtil.isEmpty(allGte4MfgLinePrBom)) {
                return root;
            }
            // 组装子节点
            for (BOMNodeResp lineResp : allGte4MfgLinePrBom) {
                BOMNodeResp child = new BOMNodeResp();
                child.setParentItem(root.getChildItem());
                child.setParentItemType(root.getChildItemType());
                child.setChildItem(lineResp.getRevUid());
                child.setChildItemType(lineResp.getRevObjectType());
                child.setObjectName(lineResp.getObjectName());
                child.setRevUid(lineResp.getRevUid());
                child.setRevObjectType(lineResp.getRevObjectType());
                root.add(child);
                root.setHasChildren(true);
                lineRevBomMap.put(lineResp.getRevUid(), child);
            }
        } else {
            lineRevBomMap.put(viewResp.getRevUid(), root);
        }
        String stationObjectType = EntityUtil.getObjectType(Gte4MfgStationPrRevisionEntity.class);
        String operationObjectType = EntityUtil.getObjectType(Gte4MfgOperationRevisionEntity.class);
        // 4：根据线体工艺版本id，循环获取线平衡下的工位工艺
        lineRevBomMap.forEach((revUid, bomNode) -> {
            List<EsopGaProcessStationResp> respList = this.findLineBalancePic(revUid);
            if (CollUtil.isEmpty(respList)) {
                return;
            }
            for (EsopGaProcessStationResp stationResp : respList) {
                BOMNodeResp child = new BOMNodeResp();
                child.setParentItem(bomNode.getChildItem());
                child.setParentItemType(bomNode.getChildItemType());
                child.setChildItem(stationResp.getId());
                child.setChildItemType(stationObjectType);
                child.setObjectName(stationResp.getStationName());
                child.setRevUid(stationResp.getRevUid());
                child.setRevObjectType(stationObjectType);
                bomNode.add(child);
                bomNode.setHasChildren(true);
                // 5：获取工位下的工序
                QEsopGaStationOperationEntity stationOperationEntity = QEsopGaStationOperationEntity.esopGaStationOperationEntity;
                BooleanBuilder where = new BooleanBuilder();
                where.and(stationOperationEntity.stationId.eq(Long.parseLong(stationResp.getId())));
                List<EsopGaStationOperationEntity> stationOperations = jpaQueryFactory.selectFrom(stationOperationEntity).where(where).fetch();
                if (CollUtil.isEmpty(stationOperations)) {
                    continue;
                }
                List<String> operationIds = stationOperations.stream().map(EsopGaStationOperationEntity::getOpId).collect(Collectors.toList());
                QEsopGaOperationEntity operationEntity = QEsopGaOperationEntity.esopGaOperationEntity;
                where = new BooleanBuilder();
                where.and(operationEntity.id.in(operationIds));
                List<EsopGaOperationEntity> operations = jpaQueryFactory.selectFrom(operationEntity).where(where).fetch();
                if (CollUtil.isEmpty(operations)) {
                    continue;
                }
                for (EsopGaOperationEntity operation : operations) {
                    BOMNodeResp opChild = new BOMNodeResp();
                    opChild.setParentItem(child.getChildItem());
                    opChild.setParentItemType(child.getChildItemType());
                    opChild.setChildItem(operation.getOpUid());
                    opChild.setChildItemType(operationObjectType);
                    opChild.setObjectName(operation.getOpName());
                    opChild.setRevUid(operation.getOpUid());
                    opChild.setRevObjectType(operationObjectType);
                    child.add(opChild);
                    child.setHasChildren(true);
                }
            }
        });
        return root;
    }

    @Override
    public List<EsopGaProcessStationResp> findLineBalancePicFront(String uid) {
        FindReq req = new FindReq(uid, EntityUtil.getObjectType(Gte4MfgLinePrRevisionEntity.class), 2);
        BOPNodeViewResp viewResp = bomNodeDomainService.find(BOPNodeViewResp.class, req, AppNameEnum.MSGBOM);
        if(ObjectUtil.isNull(viewResp)){
            return Collections.emptyList();
        }
        List<BOMNodeResp> stationPrList = viewResp.getChildren();
        if(CollUtil.isEmpty(stationPrList)){
            return Collections.emptyList();
        }
        List<EsopGaProcessStationResp> respList = new ArrayList<>();
        for (int i = 0; i < stationPrList.size(); i++) {
            BOMNodeResp stationPr = stationPrList.get(i);
            List<BOMNodeResp> operationPrList = stationPr.getChildren();

            if(CollUtil.isEmpty(operationPrList)){
                EsopGaProcessStationResp resp = new EsopGaProcessStationResp();
                resp.setId(stationPr.getRevUid());
                resp.setStationTime(0d);
                resp.setStationName(stationPr.getObjectName());
                resp.setStationNo((i+1)+"");
                respList.add(resp);
                continue;
            }
            BOMNodeResp station = null;
            Double stationTime = 0d;
            for (BOMNodeResp bomNodeResp : operationPrList) {
                if(bomNodeResp.getChildItemType().equals(new Gte4StationEntity().getObjectType())){
                    station = bomNodeResp;
                }else if(bomNodeResp.getChildItemType().equals(new Gte4MfgOperationEntity().getObjectType())){
//                    Integer taskTime = bomNodeResp.getExtraProperty("resp_gte4TaskTime");
                    Integer taskTime = bomNodeResp.getTaskTime();
                    if(ObjectUtil.isNotNull(taskTime)){
                        stationTime=stationTime+Double.parseDouble(taskTime.toString());
                    }
                }
            }
            EsopGaProcessStationResp resp = new EsopGaProcessStationResp();
            resp.setId(stationPr.getRevUid());
            resp.setLineId(viewResp.getRevUid());
            resp.setStationNo((i+1)+"");
            resp.setStationName(stationPr.getObjectName());
            resp.setStationTime(stationTime);
            respList.add(resp);
        }
        return respList;
    }

    @Override
    public BOPNodeViewResp find(FindReq req) {
        BOPNodeViewResp viewResp = bomNodeDomainService.find(BOPNodeViewResp.class, req, AppNameEnum.MSGBOM);
        if(ObjectUtil.isNull(viewResp)){
            return viewResp;
        }
        if(viewResp.getChildItemType().equals(new Gte4MfgPlantPrEntity().getObjectType())){
            List<BOMNodeResp> children = viewResp.getChildren();
            if(CollUtil.isNotEmpty(children)){
                children.forEach(resp->processWorklinePrResp(resp));
            }
        }else if(viewResp.getChildItemType().equals(new Gte4MfgLinePrEntity().getObjectType())){
            processWorklinePrResp(viewResp);
        }
        return viewResp;
    }


    private void processWorklinePrResp(BOMNodeResp lineResp){
        if(ObjectUtil.isNull(lineResp)){
            return;
        }
        List<BOMNodeResp> stationList = lineResp.getChildren();
        if(CollUtil.isEmpty(stationList)){
            return;
        }
        stationList.forEach(station->{
            List<BOMNodeResp> operationList = station.getChildren();
            if(ObjectUtil.isNotNull(operationList)){
                List<BOMNodeResp> result = new ArrayList<>();
                operationList.forEach(op->{
                    if(op.getChildItemType().equals(new Gte4MfgOperationEntity().getObjectType())){
                        result.add(op);
                    }
                });
                station.setChildren(result);
            }
        });

    }

    @Override
    public BOPNodeViewResp esopCard(FindReq req) {
        BOPNodeViewResp viewResp = bomNodeDomainService.find(BOPNodeViewResp.class, req, AppNameEnum.MSGBOM);
        processFile(viewResp);
        List<BOMNodeResp> children = viewResp.getChildren();
        if(CollUtil.isEmpty(children)){
            return viewResp;
        }
        children.stream().filter(item->item.getChildItemType().equals(new Gte4MfgStepEntity().getObjectType())).forEach(resp->{
            processFile(resp);
        });
        return viewResp;
    }

    private void processFile(BOMNodeResp viewResp){
        List<FileStorage> fileStorageList = null;
        if(EntityUtil.checkItem(viewResp.getChildItemType())){
            fileStorageList = this.getFileStorage(viewResp.getRevUid(), viewResp.getRevObjectType());
        }else{
            fileStorageList = this.getFileStorage(viewResp.getChildItem(), viewResp.getChildItemType());
        }
        if(CollUtil.isEmpty(fileStorageList)){
            return;
        }
        List<FileAttrResp> respList = fileStorageList.stream().map(file -> {
            FileAttrResp fileAttrResp = new FileAttrResp();
            com.nancal.common.utils.BeanUtil.copyPropertiesIgnoreNull(file, fileAttrResp);
            return fileAttrResp;
        }).collect(Collectors.toList());
        viewResp.putExtraProperty("resp_files",respList);
    }

    //TODO 临时
    private List<FileStorage> getFileStorage(String uid, String leftObjectType) {
        SpecificationRL specificationRL= SpringUtil.getBean(SpecificationRLFactory.class).create();
        List<WorkspaceObjectEntity> entityList = specificationRL.getRightObjectList(Collections.singletonList(uid), leftObjectType);
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        List<FileStorage> list = new ArrayList<>();
        DatasetFileRL datasetFileRL = SpringUtil.getBean(DatasetFileRLFactory.class).create();
        entityList.stream().collect(Collectors.groupingBy(WorkspaceObjectEntity::getObjectType)).forEach((objectType,values)->{
            List<String> uidList = values.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
            List<FileStorage> fileStorages = datasetFileRL.getFileStorages(uidList, objectType);
            list.addAll(Optional.ofNullable(fileStorages).orElse(new ArrayList<>()));
        });
        return list;
    }

}
