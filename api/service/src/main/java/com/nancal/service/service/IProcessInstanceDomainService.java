package com.nancal.service.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.nancal.api.model.*;
import com.nancal.api.model.common.WorkBaseReq;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.common.config.TokenInfo;
import com.nancal.common.constants.Constant;
import com.nancal.common.enums.*;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.exception.TipServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.model.entity.*;
import com.nancal.remote.service.RemoteLezaoFlowableService;
import com.nancal.remote.to.ProcessPageQueryTo;
import com.nancal.remote.to.StartFlowableTo;
import com.nancal.remote.vo.ProcessTaskNodeVo;
import com.nancal.service.bo.*;
import com.nancal.service.factory.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Triple;
import org.bouncycastle.crypto.prng.ThreadedSeedGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.persistence.EntityManager;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public interface IProcessInstanceDomainService extends IWorkspaceObjectDomainService{


    Logger log = LoggerFactory.getLogger(IProcessInstanceDomainService.class);

    /***
     *零件送审、组件送审
     *
     * @param req req
     * @author: 王辉
     * @date: 2022/5/3 18:47
     * @return:  {@link WorkspaceObjectResp}
     */
    @Transactional
    default ProcessInstanceResp release(ProcessInstanceReq req,AppNameEnum appNameEnum) {
        if(CollUtil.isEmpty(req.getReqList()) && CollUtil.isEmpty(req.getUidList())){
            throw new ServiceException(ErrorCode.FAIL, "当前无匹配的送审数据");
        }
        List<WorkBaseReq> initReqList = req.getReqList();
        if(CollUtil.isEmpty(req.getReqList())){
            String objectType = StrUtil.blankToDefault(req.getObjectType(), EntityUtil.getObjectType());
            req.setReqList(req.getUidList().stream().map(uid->new WorkBaseReq(uid,objectType)).collect(Collectors.toList()));
        }
        req.setBusinessKey(req.getReqList().stream().map(re->re.getUid()).collect(Collectors.joining(",")));
        //零件送审
        List<Pair<String, String>> params = submitVerification(req,appNameEnum);
        //校验审批节点
        if (StrUtil.isBlank(req.getAssigneeJson())) {
            throw new ServiceException(ErrorCode.E_10);
        }
        ProcessInstanceResp release = release(params, req);
        snapshot(initReqList);
        return release;
    }


    default void snapshot(List<WorkBaseReq> reqList){
        if(CollUtil.isEmpty(reqList)){
            return;
        }
        for (WorkBaseReq baseReq : reqList) {
            Class<?> entityClass = EntityUtil.getEntityClass(baseReq.getObjectType());
            if(Gte4MfgPlantPrRevisionEntity.class.isAssignableFrom(entityClass) ||
                    Gte4MfgLinePrRevisionEntity.class.isAssignableFrom(entityClass)){
                FindReq findReq = new FindReq(baseReq.getUid(),baseReq.getObjectType(),-1);
                RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
                String token = (String)requestAttributes.getAttribute(Constant.TOKEN, RequestAttributes.SCOPE_REQUEST);
                UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
                TokenInfo tokenInfo = new TokenInfo();
                tokenInfo.setTenantId(userUtils.getTenantId());
                tokenInfo.setToken(token);
                IBOMNodeSnapshotDomainService service = SpringUtil.getBean(IBOMNodeSnapshotDomainService.class);
                service.snapshot(findReq,AppNameEnum.MSGBOM,tokenInfo);
            }
        }
    }


    /**
     * 获取流程实例id
     *
     * @param id 工作对象id
     * @author: 王辉
     * @date: 2022/5/6 15:08
     * @return: {@link String}
     */
    default String getProcessInstanceId(IdRequest id) {
        String uid = id.getUid();
        String objectType = StrUtil.blankToDefault(id.getObjectType(),EntityUtil.getObjectType());
        WorkspaceObjectEntity workspaceObjectEntity = ReflectUtil.newInstance(EntityUtil.getEntityPackage(objectType));
        if (workspaceObjectEntity instanceof ItemEntity) {
            //获取零组件的最新激活版本
            ItemFactory itemFactory = SpringUtil.getBean(ItemFactory.class);
            WorkspaceObjectEntity itemRevision = itemFactory.create().getLastVersion(uid, objectType);
            //零组件id转化为零组件版本id
            uid = itemRevision.getUid();
        }
        //根据零组件版本id查询流程实例与工作对象关系表，获取流程实例id
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        QProcessInstanceRLEntity qProcessInstanceRLEntity = QProcessInstanceRLEntity.processInstanceRLEntity;
        ProcessInstanceRLEntity processInstanceRLEntity = jpaQueryFactory.selectFrom(qProcessInstanceRLEntity)
                .where(qProcessInstanceRLEntity.rightObject.eq(uid))
                .orderBy(qProcessInstanceRLEntity.creationDate.desc()).fetchFirst();
        if (ObjectUtil.isEmpty(processInstanceRLEntity)) {
            return "";
        }
        QProcessInstanceEntity qProcessInstanceEntity = QProcessInstanceEntity.processInstanceEntity;
        ProcessInstanceEntity processInstanceEntity = jpaQueryFactory.selectFrom(qProcessInstanceEntity)
                .where(qProcessInstanceEntity.uid.eq(processInstanceRLEntity.getLeftObject())).fetchFirst();
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        return processInstanceEntity.getProcessDefinitionId() + "/" + userUtils.getTenantId();
    }

    /**
     * 零件或组件送审权限校验
     *         1.通过送审类型判断是零件送审还是组件送审
     *         2.零件送审：校验个人权限与工作状态
     *         3.组件送审：校验个人权限与工作状态（如果子节点或孙子节点是别人的数据且不是已发布状态提示不能送审）
     * @param req
     * @author: 薛锦龙
     * @time: 2022/7/5
     * @return: {@link }
     */

    default List<Pair<String, String>> submitVerification(ProcessInstanceReq req,AppNameEnum appNameEnum){
        if (CollUtil.isEmpty(req.getReqList())){
            throw new ServiceException(ErrorCode.E_10);
        }
        List<Pair<String, String>> pairs = new ArrayList<>();
        //不是自己的数据且不是已发布状态
        StringBuilder parentError = new StringBuilder();
        StringBuilder error = new StringBuilder();
        //通过id去重
        List<WorkBaseReq> collect = StreamEx.of(req.getReqList()).distinct(WorkBaseReq::getUid).collect(Collectors.toList());
        if (StrUtil.isBlank(req.getBusinessKey())){
            req.setBusinessKey(collect.stream().map(re->re.getUid()).collect(Collectors.joining(StrUtil.COMMA)));
        }
        //零件送审
        if(req.getApprovalType().equals(ApprovalTypeEnum.RELEASE.getKey())){
            pairs = this.processParams(req,parentError,error,false);
        }else if(req.getApprovalType().equals(ApprovalTypeEnum.RELEASE_ITEMS.getKey())) {
            IBOMNodeDomainService domainService = SpringUtil.getBean(IBOMNodeDomainService.class);
            //组件送审
            //先校验组件权限
            pairs.addAll(this.processParams(req,parentError,error,true));
            IWorkspaceObjectDomainService.super.tipBack(parentError,"submitVerificationParentError");
            //获取组件子集进行权限校验
            ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
            List<BOMNodeEntity> list = new ArrayList<>();
            collect.forEach(data -> {
                //通过版本获取零组件
                if (ReflectUtil.newInstance(Gte4MfgStepEntity.class).getObjectType().equals(data.getObjectType())){
                    domainService.childList(data.getUid(), data.getObjectType(), appNameEnum, list);
                }else {
                    ItemEntity itemEntity = itemRevision.getLeftObjectByRightObject(data.getUid(), data.getObjectType());
                    //获取BOM行信息
                    domainService.childList(itemEntity.getUid(), itemEntity.getObjectType(),appNameEnum, list);
                }

            });
            List<BOMNodeEntity> bomNodeEntities = dataFiltering(list, appNameEnum);
            List<WorkBaseReq> collect1 = bomNodeEntities.stream().map(item -> {
                if (StrUtil.isNotBlank(item.getBomRelationView())){
                    return null;
                }
                WorkBaseReq workBaseReq = new WorkBaseReq();
                workBaseReq.setUid(item.getChildItem());
                workBaseReq.setObjectType(item.getChildItemType());
                return workBaseReq;
            }).filter(data->data!=null).collect(Collectors.toList());
            req.setReqList(collect1);
            pairs.addAll(this.processParams(req,parentError,error,false));
        }
        IWorkspaceObjectDomainService.super.tipBack(parentError,"submitVerificationParentError");
        IWorkspaceObjectDomainService.super.tipBack(error,"submitVerificationError");
        if(CollUtil.isEmpty(pairs)){
            throw new ServiceException(ErrorCode.FAIL, " 以下数据已发布或流程中，不支持送审，请确认！");
        }

        return pairs;
    }

    //送审数据筛选
    default List<BOMNodeEntity> dataFiltering(List<BOMNodeEntity> list,AppNameEnum appNameEnum){
        Item item = SpringUtil.getBean(ItemFactory.class).create();
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        Map<String, String> msgbomCheckRelease = dictUtil.getCodeValueMap("MSGBOM_checkRelease");
        Map<String, String> codeValueMap = dictUtil.getCodeValueMap(appNameEnum.name());
        List<BOMNodeEntity> newList = new ArrayList<>();
        List<BOMNodeEntity> unqualifiedList = new ArrayList<>();
        if(!CollUtil.isEmpty(codeValueMap)){
            for (BOMNodeEntity bomNode : list) {
                if(codeValueMap.containsKey(bomNode.getChildItemType()) && !msgbomCheckRelease.containsKey(bomNode.getChildItemType())){
                    newList.add(bomNode);
                    continue;
                }
                if (!msgbomCheckRelease.containsKey(bomNode.getChildItemType())){
                    unqualifiedList.add(bomNode);
                }
            }
            StringBuilder errorData = new StringBuilder();
            //校验不符合的数据
            if (CollUtil.isNotEmpty(unqualifiedList)){
                unqualifiedList.forEach(data->{
                    WorkspaceObjectEntity lastVersion;
                    boolean b = EntityUtil.checkItem(data.getChildItemType());
                    if (StrUtil.isNotBlank(data.getChildItemTypeRevision())||!b){
                        //精确BOM与工步
                        lastVersion = EntityUtil.getById(data.getChildItemTypeRevision(), data.getChildItemTypeRevision());
                    }else{
                        //非精确BOM
                        lastVersion = item.getLastVersion(data.getChildItem(), data.getChildItemType());
                    }
                    if (!lastVersion.getLifeCycleState().equals(LifeCycleStateEnum.Released.name())){
                        WorkspaceObjectResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(lastVersion.getObjectType()));
                        BeanUtil.copyPropertiesIgnoreNull(lastVersion,resp);
                        errorData.append(resp.getDisplayName()+";");
                    }
                });
                IWorkspaceObjectDomainService.super.tipBack(errorData,"unqualifiedData");
            }
            return newList;
        }else {
            return list;
        }

    }

    default List<Pair<String, String>> processParams(ProcessInstanceReq req,StringBuilder parentError,StringBuilder error,boolean flag) {
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        if(StrUtil.isBlank(req.getBusinessKey())){
            throw new ServiceException(ErrorCode.FAIL, "送审的业务id为空");
        }
        //通过id去重
        List<WorkBaseReq> workBaseReqs = StreamEx.of(req.getReqList()).distinct(WorkBaseReq::getUid).collect(Collectors.toList());
        final List<Pair<String, String>> params = new ArrayList<>();
        Item item = SpringUtil.getBean(ItemFactory.class).create();
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        List<WorkspaceObjectEntity> entities1 = new ArrayList<>();
        workBaseReqs.stream().collect(Collectors.groupingBy(WorkBaseReq::getObjectType)).forEach((versionObjectType,reqList)->{
            List<String> uids = reqList.stream().map(WorkBaseReq::getUid).collect(Collectors.toList());
            Class<?> entityClass = EntityUtil.getEntityClass(versionObjectType);
            //如果是ItemEntity类型
            List<WorkspaceObjectEntity> entities;
            if (ItemEntity.class.isAssignableFrom(entityClass)){
                //获取最新的版本
                Map<String, WorkspaceObjectEntity> lastVersion = item.getLastVersion(uids, versionObjectType);
                entities = lastVersion.values().stream().collect(Collectors.toList());
            }else if (ItemRevisionEntity.class.isAssignableFrom(entityClass)){
                //如果是ItemRevisionEntity
                //获取当前版本的最新版次
                entities = itemRevision.getItemRevision(versionObjectType, uids);
            }else {
                //其他的获取当前信息
                //直接获取信息
                entities = EntityUtil.getByIds(versionObjectType, uids);
            }
            entities1.addAll(entities);
            params.addAll(entities.stream().filter(v->{
                WorkspaceObjectResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(v.getObjectType()));
                BeanUtil.copyPropertiesIgnoreNull(v,resp);
                //不是自己的数据不是发布状态不能送审
                if (!v.getOwnerId().equals(userUtils.getCurrentUserId())){
                    if (!v.getLifeCycleState().equals(LifeCycleStateEnum.Released.name())){
                        parentError.append(resp.getDisplayName()+";");
                    }
                }
                if (flag){
                    try{
                    this.verifyAuthority(v, OperatorEnum.SubmitTrial);
                    }catch (TipServiceException e){
                        error.append(resp.getDisplayName());
                        return false;
                    }
                    return true;
                }else {
                    return this.verifyAuthorityReturn(v, OperatorEnum.SubmitTrial);
                }
            }).map(v -> Pair.of(v.getUid(), v.getObjectType())).collect(Collectors.toList()));
        });
        return params;
    }


    /**
     * 通过流程Id获取流程中的数据信息
     *          * 1.通过流程实例id获取流程数据
     *          * 2.通过左对象获取中间表数据
     *          * 3.分组查询具体数据
     *          * 4.按照具体数据的创建时间排序
     * @param processDefinitionId 流程Id
     * @author: 薛锦龙
     * @time: 2022/7/4
     * @return: {@link BusinessObjectResp}
     */
    default BusinessObjectResp auditInformation(String processDefinitionId){
        ProcessInstanceResp byProcessDefinitionId = SpringUtil.getBean(ProcessInstanceFactory.class).create().getByProcessDefinitionId(processDefinitionId);
        List<ProcessInstanceRLResp> rlResps = SpringUtil.getBean(ProcessInstanceRLFactory.class).create().getLeftObjectType(byProcessDefinitionId.getUid());
        List<WorkspaceObjectEntity> list = new ArrayList();
        rlResps.stream().collect(Collectors.groupingBy(ProcessInstanceRLResp::getRightObjectType)).forEach((key,value)->{
            List<String> rightObjects = value.stream().map(ProcessInstanceRLResp::getRightObject).collect(Collectors.toList());
            List<WorkspaceObjectEntity> fetch = EntityUtil.getDynamicQuery(key, Triple.of(BusinessObjectEntity.UID, Ops.IN, rightObjects)).fetch();
            list.addAll(fetch);
        });
        //转换类型
        List<WorkspaceObjectResp> workspaceObjectResps = list.stream().map(data->{
            WorkspaceObjectResp instance = ReflectUtil.newInstance(EntityUtil.getRespPackage(data.getObjectType()));
            BeanUtil.copyPropertiesIgnoreNull(data,instance);
            return instance;
        }).collect(Collectors.toList());
        //按照创建时间排序
        List<WorkspaceObjectResp> resps = workspaceObjectResps.stream().sorted(Comparator.comparing(WorkspaceObjectResp::getCreationDate).reversed()).collect(Collectors.toList());
        byProcessDefinitionId.setWorkspaceObjectRespList(resps);
        return byProcessDefinitionId;
    }

    /**
     * 分页查询流程信息
     *
     * @param req 分页请求
     * @author: 王辉
     * @date: 2022/5/4 21:44
     * @return: {@link TableResponse < ProcessInstanceResp >}
     */
    default TableResponse<ProcessTaskNodeVo> getProcessNodePage(TableRequest<IdRequest> req) {
        String uid = req.getData().getUid();
        String objectType = StrUtil.blankToDefault(req.getData().getObjectType(),EntityUtil.getObjectType());
        WorkspaceObjectEntity workspaceObjectEntity = ReflectUtil.newInstance(EntityUtil.getEntityPackage(objectType));
        if (workspaceObjectEntity instanceof ItemEntity) {
            //获取零组件的最新激活版本
            ItemFactory itemFactory = SpringUtil.getBean(ItemFactory.class);
            WorkspaceObjectEntity itemRevision = itemFactory.create().getLastVersion(uid, objectType);
            //零组件id转化为零组件版本id
            uid = itemRevision.getUid();
        }
        //根据零组件版本id查询流程实例与工作对象关系表，获取流程实例id
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        QProcessInstanceRLEntity qProcessInstanceRLEntity = QProcessInstanceRLEntity.processInstanceRLEntity;
        ProcessInstanceRLEntity processInstanceRLEntity = jpaQueryFactory.selectFrom(qProcessInstanceRLEntity)
                .where(qProcessInstanceRLEntity.rightObject.eq(uid))
                .orderBy(qProcessInstanceRLEntity.creationDate.desc()).fetchFirst();
        if (ObjectUtil.isEmpty(processInstanceRLEntity)) {
            return TableResponse.<ProcessTaskNodeVo>builder()
                    .total(0)
                    .data(Collections.emptyList())
                    .build();
        }
        QProcessInstanceEntity qProcessInstanceEntity = QProcessInstanceEntity.processInstanceEntity;
        ProcessInstanceEntity processInstanceEntity = jpaQueryFactory.selectFrom(qProcessInstanceEntity)
                .where(qProcessInstanceEntity.uid.eq(processInstanceRLEntity.getLeftObject())).fetchFirst();
        //组装查询分页条件
        ProcessPageQueryTo processPageQueryTo = new ProcessPageQueryTo(req.getSkip(), req.getTake(), null);
        //调用流程引起分页查询
        RemoteLezaoFlowableService remoteLezaoFlowableService = SpringUtil.getBean(RemoteLezaoFlowableService.class);
        TableResponse<ProcessTaskNodeVo> processNodePage = remoteLezaoFlowableService.getProcessNodePage(processInstanceEntity.getProcessDefinitionId(), processPageQueryTo);
        if (ObjectUtil.isNotEmpty(processNodePage.getData())){
            processNodePage.getData().forEach(data->{
                Map<String, Object> newMapData = new HashMap<>();
                Map<String, Object> processVariables = data.getProcessVariables();
                if (CollUtil.isNotEmpty(processVariables)){
                    processVariables.keySet().forEach(key->newMapData.put("resp_"+key,processVariables.get(key)));
                    if(CollUtil.isEmpty(data.getExtraMap())){
                        data.setExtraMap(newMapData);
                    }else{
                        data.getExtraMap().putAll(newMapData);
                    }
                }
            });
        }

        if (processNodePage.isFail()) {
            throw new ServiceException(ErrorCode.ERROR, processNodePage.getMsg());
        }
        return processNodePage;
    }


    /***
     * 发布的回调：改变实例状态与业务对象的状态
     * API更新流程数据
     *
     * @param req req
     * @author: 王辉
     * @date: 2022/5/3 17:29
     * @return:  {@link ProcessInstanceResp}
     */
    @Transactional
    @Retryable(value = ServiceException.class,maxAttempts=10, backoff = @Backoff(delay = 1000L, multiplier = 3))
    default ProcessInstanceResp processCallback(ProcessInstanceCallbackReq req) {
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        log.info("收到回调信息，{}", JSONUtil.toJsonStr(req));
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        //查询流程实例信息
        QProcessInstanceEntity qProcessInstanceEntity = QProcessInstanceEntity.processInstanceEntity;
        ProcessInstanceEntity instanceEntity = jpaQueryFactory.selectFrom(qProcessInstanceEntity)
                .where(qProcessInstanceEntity.processDefinitionId.eq(req.getProcessInstanceId()),
                        qProcessInstanceEntity.delFlag.eq(Boolean.FALSE)).fetchFirst();
        if (ObjectUtil.isEmpty(instanceEntity)) {
            log.error("找不到对应的流程实例数据");
            throw new ServiceException(ErrorCode.E_12);
        }
        //查询流程实体与对象的关系表
        QProcessInstanceRLEntity qProcessInstanceRLEntity = QProcessInstanceRLEntity.processInstanceRLEntity;
        List<ProcessInstanceRLEntity> processInstanceRLEntities = jpaQueryFactory.selectFrom(qProcessInstanceRLEntity)
                .where(qProcessInstanceRLEntity.leftObject.eq(instanceEntity.getUid()),
                        qProcessInstanceRLEntity.delFlag.eq(Boolean.FALSE)
//                        qProcessInstanceRLEntity.lifeCycleState.in(LifeCycleStateEnum.InJob.name(),LifeCycleStateEnum.Prepare.name())
                ).fetch();
        if (CollUtil.isEmpty(processInstanceRLEntities)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        String processState = ProcessStateEnum.getValueByKey(req.getProcessState());
        log.info("流程回调状态："+req.getProcessState());
        //更新流程实例的状态
        instanceEntity.setLifeCycleState(processState);
        entityManager.merge(instanceEntity);
        //更新关系表的状态
        for (ProcessInstanceRLEntity processInstanceRLEntity : processInstanceRLEntities) {
            processInstanceRLEntity.setLifeCycleState(processState);
            entityManager.merge(processInstanceRLEntity);
        }
        //更新业务对象的状态
        // 按照右对象类型进行分组，这样就可以将查询同一张表的数据进行一次查询
        List<String> lifeState = new ArrayList<>();
        lifeState.add(LifeCycleStateEnum.InJob.name());
        lifeState.add(LifeCycleStateEnum.Prepare.name());
        processInstanceRLEntities.stream().map(RelationEntity.class::cast).collect(Collectors.groupingBy(RelationEntity::getRightObjectType)).forEach((rightObjectType, values) -> {
            // 获取右对象id列表
            List<String> rightUidList = values.stream().map(RelationEntity::getRightObject).collect(Collectors.toList());
            // 分别去查询各自表中获取数据
            //获取激活的版本数据
            List<WorkspaceObjectEntity> rightObjects = new ArrayList<>();
            if (ItemRevisionEntity.class.isAssignableFrom(EntityUtil.getEntityClass(rightObjectType))){
                rightObjects = itemRevision.getActiveRevisions(rightUidList, rightObjectType, lifeState);
            }else {
                rightObjects = EntityUtil.getByIds(rightObjectType,rightUidList);
            }
//            List<WorkspaceObjectEntity> rightObjects = EntityUtil.getDynamicQuery(rightObjectType, clazz -> {
//                BooleanBuilder where = new BooleanBuilder();
//                where.and(new BeanPath<Object>(clazz, BusinessObjectEntity.DEL_FLAG).eq(Boolean.FALSE));
//                where.and(new BeanPath<Object>(clazz, BusinessObjectEntity.UID).in(rightUidList));
//                where.and(new BeanPath<Object>(clazz,ItemRevisionEntity.ACTIVE).eq(true));
//                where.and(new BeanPath<Object>(clazz, WorkspaceObjectEntity.LIFE_CYCLE_STATE).in(LifeCycleStateEnum.InJob.name(),LifeCycleStateEnum.Prepare.name()));
//                return where;
//            }).fetch();
            if (CollUtil.isEmpty(rightObjects)) {
                return;
            }
            for (WorkspaceObjectEntity rightObject : rightObjects) {
                if (Gte4NoticeOrgEntryEntity.class.isAssignableFrom(EntityUtil.getEntityClass(rightObject.getObjectType())) && processState.equals(ProcessStateEnum.Released.name())){
                    //通知单位回调
                    Gte4NoticeOrgEntryEntity noticeEntity = (Gte4NoticeOrgEntryEntity)rightObject;
                    noticeEntity.setGte4CheckTime(LocalDateTime.now());
                    noticeEntity.setGte4Check(true);
                    noticeEntity.setLifeCycleState(processState);
                    entityManager.merge(noticeEntity);
                }else {
                    rightObject.setLifeCycleState(processState);
                    entityManager.merge(rightObject);
                }
            }
        });

        //组装响应实体
        ProcessInstanceResp processInstanceResp = new ProcessInstanceResp();
        BeanUtil.copyPropertiesIgnoreNull(instanceEntity, processInstanceResp);
        return processInstanceResp;
    }

    /***
     *零件送审、组件送审
     *
     * @param params 选中的当前对象
     * @param req req
     * @author: 王辉
     * @date: 2022/5/3 14:41
     * @return:  {@link ProcessInstanceResp}
     */
    default ProcessInstanceResp release(List<Pair<String,String>> params,ProcessInstanceReq req) {
        RemoteLezaoFlowableService flowableService = SpringUtil.getBean(RemoteLezaoFlowableService.class);
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        //流程实例变量
        Map<String, Object> objectMap = new HashMap<>(16);
        objectMap.put("processTitle", req.getProcessName());
        //是否是在流程启动是指派办理人  true/false
        objectMap.put("isStartAssignee", Boolean.TRUE);
        if (StrUtil.isNotBlank(req.getAssigneeJson())) {
            // 指派的办理人  例子："assigneeJson":{"节点id1":"办理人1,办理人2","节点id2":"办理人1,办理人2"}
            objectMap.put("assigneeJson", JSONUtil.toBean(req.getAssigneeJson(),Map.class));
        }
        objectMap.put("assignee", userUtils.getCurrentUserId());
        //组装启动流程请求
        StartFlowableTo startFlowableTo = new StartFlowableTo();
        startFlowableTo.setBusinessKey(req.getBusinessKey());
        startFlowableTo.setProcessKey(req.getProcessKey());
        startFlowableTo.setTenantId(userUtils.getTenantId());
        startFlowableTo.setUserId(userUtils.getCurrentUserId());
        startFlowableTo.setProcessVariables(objectMap);
        log.info("提交流程参数：" + JSONUtil.toJsonStr(startFlowableTo));
        Response<String> flowable = flowableService.startFlowableAndDealTask(startFlowableTo);
        if (Boolean.TRUE.equals(flowable.isFail())) {
            log.error("远程调用发起流程失败",JSONUtil.toJsonStr(flowable));
            throw new ServiceException(ErrorCode.ERROR, flowable.getMsg());
        }
        log.info("流程响应：" + JSONUtil.toJsonStr(flowable));
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        //保存流程实体
        ProcessInstanceEntity processInstanceEntity = new ProcessInstanceEntity();
        processInstanceEntity.setUid(IdGeneratorUtil.generate());
        processInstanceEntity.setProcessKey(startFlowableTo.getProcessKey());
        processInstanceEntity.setBusinessKey(startFlowableTo.getBusinessKey());
        processInstanceEntity.setProcessTitle(req.getProcessTitle());
        //添加返回的流程实例id
        processInstanceEntity.setProcessDefinitionId(flowable.getData());
        processInstanceEntity.setObjectName(req.getProcessName());
        processInstanceEntity.setOwnerId(userUtils.getCurrentUserId());
        processInstanceEntity.setOwnerName(userUtils.getCurrentUserName());
        processInstanceEntity.setRemark(req.getRemark());
        processInstanceEntity.setLifeCycleState(StrUtil.isBlank(req.getLifeCycleState()) ? LifeCycleStateEnum.InJob.name():req.getLifeCycleState());
        entityManager.persist(processInstanceEntity);
        // 更新状态
        processStatusUpdate(processInstanceEntity,params);
        //组装响应实体
        ProcessInstanceResp processInstanceResp = new ProcessInstanceResp();
        BeanUtil.copyPropertiesIgnoreNull(processInstanceEntity, processInstanceResp);
        return processInstanceResp;
    }

    /***
     * 流程状态更新
     *
     * @param params bom数据
     * @author: 王辉
     * @date: 2022/5/3 14:41
     * @return:  {@link ProcessInstanceResp}
     */
    default void processStatusUpdate(ProcessInstanceEntity processInstanceEntity, List<Pair<String, String>> params) {
        MasterRL masterRL = SpringUtil.getBean(MasterRLFactory.class).create();
        ProcessInstanceRL instanceRL = SpringUtil.getBean(ProcessInstanceRLFactory.class).create();
        //新增实例与零组件版本下的数据集、辅材版本（包含辅材版本下的数据集）的关系表
        for (Pair<String, String> param : params) {
            //查询当前零组件版本的子级（辅材版本、数据集）
            //所有零组件版本下的辅材版本和数据集
            List<WorkspaceObjectEntity> entityList = masterRL.getRecursionChild(param.getKey(), param.getValue(), item -> {
                boolean status = LifeCycleStateEnum.Working.name().equals(item.getLifeCycleState());
                boolean type = !(item instanceof FolderEntity);
                boolean active = true;
                if (item instanceof ItemRevisionEntity) {
                    ItemRevisionEntity revision = (ItemRevisionEntity) item;
                    active = revision.getActive();
                }
                return BooleanUtil.and(status, type, active);
            },DictRelEnum.RELEASE_REL);
            if (CollUtil.isEmpty(entityList)) {
                continue;
            }
            List<WorkspaceObjectEntity> result = new ArrayList<>();
            // 在这里你需要根据uid进行去重以后,将不是最新的版本剔除
            StreamEx.of(entityList).distinct(WorkspaceObjectEntity::getUid).collect(Collectors.groupingBy(WorkspaceObjectEntity::getObjectType)).forEach((objectType, values) -> {
                Class<?> revisionClazz = EntityUtil.getEntityClass(objectType);
                if (!ItemRevisionEntity.class.isAssignableFrom(revisionClazz)) {
                    result.addAll(values);
                    return;
                }
                ItemRevisionEntity revision = values.stream().map(ItemRevisionEntity.class::cast).max(Comparator.comparing(ItemRevisionEntity::getRevisionId)).get();
                result.add(revision);
            });
            //实例对象与辅材版本、数据集的关系表
            instanceRL.create(processInstanceEntity,result);
        }
        //更新当前零组件版本以及递归的零组件版本对象的状态,先按照类型分组，在进行查询更新
        params.stream().collect(Collectors.groupingBy(Pair::getValue)).forEach((objectType, values) -> {
            List<WorkspaceObjectEntity> entityList = EntityUtil.getByIds(objectType, values.stream().map(Pair::getKey).collect(Collectors.toList()));
            instanceRL.create(processInstanceEntity,entityList);
        });
    }

    default NoticeAuditInformationResp noticeAuditInformation(IdRequest id){
        //获取流程实例
        NoticeAuditInformationResp resp = new NoticeAuditInformationResp();
        String noticeRlType = new Gte4NoticeOrgRLEntity().getObjectType();
        ProcessInstanceResp byProcessDefinitionId = SpringUtil.getBean(ProcessInstanceFactory.class).create().getByProcessDefinitionId(id.getUid());
        if (ObjectUtil.isEmpty(byProcessDefinitionId)){
            return null;
        }
        List<ProcessInstanceRLResp> rlResps = SpringUtil.getBean(ProcessInstanceRLFactory.class).create().getLeftObjectType(byProcessDefinitionId.getUid());
        if (CollUtil.isEmpty(rlResps)){
            return null;
        }
        ProcessInstanceRLResp firstRl = CollUtil.getFirst(rlResps);
        WorkspaceObjectEntity fetchFirst = EntityUtil.getDynamicQuery(noticeRlType, Triple.of(RelationEntity.RIGHT_OBJECT, Ops.EQ, firstRl.getRightObject())).fetchFirst();
        if (ObjectUtil.isEmpty(fetchFirst)){
            return null;
        }
        WorkspaceObjectEntity afterRlEntity = EntityUtil.getById(fetchFirst.getLeftObjectType(), fetchFirst.getLeftObject());//返回

        if (ObjectUtil.isEmpty(afterRlEntity)){
            return null;
        }
        RelationEntity relation = (RelationEntity)afterRlEntity;
        WorkspaceObjectEntity changeEntity = EntityUtil.getById(relation.getLeftObjectType(), relation.getLeftObject());//返回
        resp.setChangeResp(getWorkspaceObjectResp(changeEntity));
        resp.setAfterChangeResp(getWorkspaceObjectResp(afterRlEntity));
        return resp;
    }

    private WorkspaceObjectResp getWorkspaceObjectResp(WorkspaceObjectEntity changeEntity) {
        if (ObjectUtil.isEmpty(changeEntity)){
            return null;
        }
        WorkspaceObjectResp objectResp = ReflectUtil.newInstance(EntityUtil.getRespPackage(changeEntity.getObjectType()));
        BeanUtil.copyPropertiesIgnoreNull(changeEntity,objectResp);
        return objectResp;
    }

}
