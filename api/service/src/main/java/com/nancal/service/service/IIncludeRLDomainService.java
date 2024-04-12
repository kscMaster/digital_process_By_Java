package com.nancal.service.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.annotation.TimeLog;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.DictRelEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.OperatorEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.exception.TipServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.model.entity.*;
import com.nancal.remote.vo.DictItemVo;
import com.nancal.service.bo.*;
import com.nancal.service.factory.*;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import javax.persistence.EntityManager;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @ClassName IIncludeRlDomainService
 * @Description TODO
 * @Author fuming
 * @Date 2022/4/1 19:10
 * @Version 1.0
 **/
public interface IIncludeRLDomainService extends IRelationDomainService {
    /***
     * 根据左对象获取所有对象列表数据
     *
     * @param req 请求参数
     * @author 郑复明
     * @date 2022/4/7 10:59
     * @return {@link List< IncludeOrMasterRLResp>}
     */
    @TimeLog
    default List<IncludeOrMasterRLResp> getIncludeByHomeId(IncludeOrMasterRlReq req) {
        Class<?> entityClass = EntityUtil.getEntityClass(req.getObjectType());
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        MasterRL masterRL = SpringUtil.getBean(MasterRLFactory.class).create();
        //版本和工步特殊处理需要添加虚拟节点
        if (ItemRevisionEntity.class.isAssignableFrom(entityClass)||Gte4MfgStepEntity.class.isAssignableFrom(entityClass)){
            return getVirtualNode(req);
        }
        //类型是BOMNode节点的需要特殊处理(虚拟节点)
        if (BOMNodeEntity.class.isAssignableFrom(entityClass)){
            return this.findByBomNode(req.getUid(), req.getObjectType(),req.getLeftObjectType());
        }
//        if (RelationEntity.class.isAssignableFrom(entityClass)){
//            req.setObjectType(req.getLeftObjectType());
//        }
        List<WorkspaceObjectEntity> childs = masterRL.getChilds(req.getUid(), req.getObjectType(), entity -> {
            // 如果是版本，则只获取激活状态下的
            if (entity instanceof ItemRevisionEntity) {
                ItemRevisionEntity revisionEntity = (ItemRevisionEntity) entity;
                return revisionEntity.getActive();
            }
            return true;
        });
        if (CollUtil.isEmpty(childs)) {
            return Collections.emptyList();
        }
        ThreadPoolTaskExecutor executor = SpringUtil.getBean("applicationTaskExecutor",ThreadPoolTaskExecutor.class);
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        List<CompletableFuture<Void>> all = new ArrayList<>();
        List<IncludeOrMasterRLResp> respList = new CopyOnWriteArrayList<>();
        // 转换数据
        childs.forEach(data -> {
            data.setRelationId(IdGeneratorUtil.generate());
            //数据集下没有子集
            if (DatasetEntity.class.isAssignableFrom(EntityUtil.getEntityClass(data.getObjectType()))) {
                IncludeOrMasterRLResp resp = new IncludeOrMasterRLResp();
                BeanUtil.copyPropertiesIgnoreNull(data, resp);
                resp.setOwnerGroupName("工艺技术处.型号组");
                resp.setLeftObject(req.getUid());
                resp.setLeftObjectType(req.getObjectType());
                resp.setHasChildren(false);
                respList.add(resp);
                return;
            }
            // 如果是零组件的话，一定有子节点。因为它必须存在至少一个版本
            if (ItemEntity.class.isAssignableFrom(EntityUtil.getEntityClass(data.getObjectType()))) {
                IncludeOrMasterRLResp resp = new IncludeOrMasterRLResp();
                BeanUtil.copyPropertiesIgnoreNull(data, resp);
                resp.setOwnerGroupName("工艺技术处.型号组");
                resp.setLeftObject(req.getUid());
                resp.setLeftObjectType(req.getObjectType());
                resp.setHasChildren(true);
                respList.add(resp);
                return;
            }
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                RequestContextHolder.setRequestAttributes(requestAttributes);
                IncludeOrMasterRLResp resp = new IncludeOrMasterRLResp();
                BeanUtil.copyPropertiesIgnoreNull(data, resp);
                resp.setOwnerGroupName("工艺技术处.型号组");
                resp.setLeftObject(req.getUid());
                resp.setLeftObjectType(req.getObjectType());
                // 判断右对象列表数据是否还有子节点
                boolean hasChildren = false;
                if (ReflectUtil.newInstance(EntityUtil.getEntityClass(resp.getObjectType())) instanceof ItemRevisionEntity){
                    hasChildren = masterRL.bomNodeHasChildren(resp.getUid(),resp.getObjectType(),false,req.getUid());
                }else {
                    hasChildren= masterRL.hasChildren(data.getUid(), data.getObjectType(), false);
                }
                resp.setHasChildren(hasChildren);
                respList.add(resp);
            }, executor);
            all.add(future);
        });
        if (CollUtil.isNotEmpty(all)) {
            CompletableFuture.allOf(all.toArray(new CompletableFuture[]{})).join();
        }
        respList.sort(Comparator.comparing(IncludeOrMasterRLResp::getCreationDate).reversed());
        dictUtil.translate(respList);
        return respList;
    }

    /**
     * 剪切（断开关系）
     * @param req
     * @author: 薛锦龙
     * @time: 2022/4/13
     * @return: {@link Boolean}
     */
    @Transactional
    default Boolean shear(ShearReq req) {
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        //获取关系表类型
        String relationShip = "";
        try {
            relationShip = dictUtil.getRelation(req.getLeftObjectType(), req.getRightObjectType());
        }catch (ServiceException e){
            throw new ServiceException(ErrorCode.FAIL, "无法断开"+e.getMessage().substring(4,e.getMessage().length()));
        }

        //关系校验
        if (EntityUtil.getEntityClass(relationShip).isAssignableFrom(MasterRLEntity.class)) {
            throw new ServiceException(ErrorCode.E_10, "不能断开零组件版本关系");
        }
        WorkspaceObjectEntity leftObject = EntityUtil.getById(req.getLeftObjectType(), req.getLeftObject());
        //获取左对象数据进行权限校验
        try{
            this.verifyAuthority(leftObject, OperatorEnum.Write);
        }catch (TipServiceException e){
            throw new ServiceException(ErrorCode.FAIL,e.getMessage()+"不支持剪切!");
        }
        //删除关系
        List<Pair<String, Object>> params = Arrays.asList(
                Pair.of(RelationEntity.LEFT_OBJECT, req.getLeftObject()),
                Pair.of(RelationEntity.LEFT_OBJECT_TYPE, req.getLeftObjectType()),
                Pair.of(RelationEntity.RIGHT_OBJECT, req.getRightObject()),
                Pair.of(RelationEntity.RIGHT_OBJECT_TYPE, req.getRightObjectType()),
                Pair.of(WorkspaceObjectEntity.OWNER_ID, userUtils.getCurrentUserId())
        );
        WorkspaceObjectEntity relation = EntityUtil.getDynamicEqQuery(relationShip, params).fetchFirst();
        if (Objects.isNull(relation)) {
            throw new ServiceException(ErrorCode.E_12, "关系数据不存在，请刷新后再试");
        }
        relation.setDelFlag(true);
        entityManager.merge(relation);
        return true;
    }
    /**
     * 剪切板获取数据
     * @param req
     * @author: 薛锦龙
     * @time: 2022/4/13
     * @return: {@link List< ClipboardDataResp>}
     */
    default List<ClipboardDataResp> clipboardData(List<LabelReq> req) {
        req = StreamEx.of(req).distinct(LabelReq::getUid).toList();
        List<ClipboardDataResp> list = new ArrayList<>();
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        req.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(LabelReq::getObjectType)).forEach((objectType, values) -> {
            List<String> uids = values.stream().map(LabelReq::getUid).collect(Collectors.toList());
            if (CollUtil.isEmpty(uids)) {
                return;
            }
            List<WorkspaceObjectEntity> fetch = EntityUtil.getDynamicQuery(objectType, Triple.of(BusinessObjectEntity.UID, Ops.IN, uids)).fetch();
            List<String> collect = fetch.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
            Map<String, WorkspaceObjectEntity> leftObjectMap = new HashMap<>();
            if(ItemRevisionEntity.class.isAssignableFrom(EntityUtil.getEntityClass(objectType))){
                leftObjectMap  = itemRevision.getLeftObjectMap(collect, objectType);
            }
            for (WorkspaceObjectEntity workspaceObject : fetch) {
                //查找零组件
                ClipboardDataResp clipboardDataResp = new ClipboardDataResp();
                BeanUtil.copyPropertiesIgnoreNull(workspaceObject, clipboardDataResp);
                //没有版本的数据
                if (ItemRevisionEntity.class.isAssignableFrom(EntityUtil.getEntityClass(objectType))){
                    WorkspaceObjectEntity workspaceObject1 = leftObjectMap.get(workspaceObject.getUid());
                    clipboardDataResp.setLeftObjectType(workspaceObject1.getObjectType());
                    clipboardDataResp.setLeftObject(workspaceObject1.getUid());
                }else {
                    clipboardDataResp.setLeftObjectType(workspaceObject.getObjectType());
                    clipboardDataResp.setLeftObject(workspaceObject.getUid());
                }
                list.add(clipboardDataResp);
            }
        });
        Map<String, ClipboardDataResp> dataRespMap = StreamEx.of(list).distinct(ClipboardDataResp::getUid).toMap(ClipboardDataResp::getUid, Function.identity());
        return req.stream().filter(data -> data != null && dataRespMap.containsKey(data.getUid()))
                .map(data -> dataRespMap.get(data.getUid())).collect(Collectors.toList());

    }
    /**
     * 粘贴（创建关系）
     * @param req
     * @author: 薛锦龙
     * @time: 2022/4/13
     * @return: {@link Boolean}
     */
    @Transactional
    default void paste(ClipboardReq req) {
        //如果是零组件或者是数据集不可以粘贴
        Class<?> entityClass = EntityUtil.getEntityClass(req.getLeftObjectType());
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        WorkspaceObjectEntity workspaceObject = EntityUtil.getDynamicQuery(req.getLeftObjectType(), Triple.of(BusinessObjectEntity.UID, Ops.EQ, req.getLeftObject())).fetchFirst();
        WorkspaceObjectResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(workspaceObject.getObjectType()));
        BeanUtil.copyPropertiesIgnoreNull(workspaceObject,resp);
        if (ItemEntity.class.isAssignableFrom(entityClass) || DatasetEntity.class.isAssignableFrom(entityClass)){
            IRelationDomainService.super.tipBack(new StringBuilder(resp.getDisplayName()),"pasteError");
        }
        //校验是否与父节点重复
        Map<String, PasteReq> collect2 = req.getRightObjects().stream().collect(Collectors.toMap(PasteReq::getUid, Function.identity()));
        if (collect2.containsKey(req.getLeftObject())){
            throw new ServiceException(ErrorCode.E_10,"源数据【"+resp.getDisplayName()+"】和目标数据【"+resp.getDisplayName()+"】相同，不支持粘贴！");
        }
        //校验当前数据是否有粘贴权限
        verifyAuthority(workspaceObject,OperatorEnum.Write,"pasteData",resp.getDisplayName());
        //校验是否重复
        MasterRL masterRL = SpringUtil.getBean(MasterRLFactory.class).create();
        //获取一级子节点
//        List<WorkspaceObjectEntity> childs = masterRL.getChilds(req.getLeftObject(), req.getLeftObjectType(), entity -> {
//            // 如果是版本，则只获取激活状态下的
//            if (entity instanceof ItemRevisionEntity) {
//                ItemRevisionEntity revisionEntity = (ItemRevisionEntity) entity;
//                return revisionEntity.getActive();
//            }
//            return true;
//        });
        List<IncludeOrMasterRLResp> rightObjects = IRelationDomainService.super.getRightObjects(new IdRequest(req.getLeftObject(), req.getLeftObjectType()));
        Map<String, IncludeOrMasterRLResp> collect = StreamEx.of(rightObjects).distinct(IncludeOrMasterRLResp::getUid).toMap(IncludeOrMasterRLResp::getUid, Function.identity());
        // 获取当前节点的父节点
        List<WorkspaceObjectEntity> parents = masterRL.getRecursionParent(req.getLeftObject(), req.getLeftObjectType(), null);
        //将自身放入集合
        parents.add(workspaceObject);
        //去重
        List<WorkspaceObjectEntity> collect1 = StreamEx.of(parents).distinct(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
        Map<String, WorkspaceObjectEntity> entityMap = collect1.stream().collect(Collectors.toMap(WorkspaceObjectEntity::getUid, Function.identity()));
        //收集被循环引用的数据
        StringBuilder list = new StringBuilder();
        StringBuilder child = new StringBuilder();
        StringBuilder relationsError = new StringBuilder();
        //检查要粘贴的对象是否存在循环引用
        req.getRightObjects().forEach(data->{
            WorkspaceObjectEntity byId = EntityUtil.getById(data.getObjectType(), data.getUid());
            WorkspaceObjectResp workspaceObjectResp = ReflectUtil.newInstance(EntityUtil.getRespPackage(byId.getObjectType()));
            BeanUtil.copyPropertiesIgnoreNull(byId,workspaceObjectResp);
            //校验父级
            if (entityMap.containsKey(data.getUid())) {
                list.append("源数据【" + workspaceObjectResp.getDisplayName() + "】是目标数据【" + resp.getDisplayName() + "】的父级，不支持粘贴!;");
            }
            //校验子集
            if (collect.containsKey(data.getUid())) {
                child.append("源数据【" +workspaceObjectResp.getDisplayName() + "】已经存在目标数据【" + resp.getDisplayName() + "】下，不支持粘贴!;");
            }
            //校验是否支持粘贴
            try {
                String relation = dictUtil.getRelation(workspaceObject.getObjectType(), data.getObjectType());
            }catch (ServiceException e){

                relationsError.append("【"+workspaceObjectResp.getDisplayName()+"】;");
            }
        });
        if (list.length()>0){
            throw new ServiceException(ErrorCode.FAIL,list.toString());
        }
        //校验子集是否存在相同的数据
        if (child.length()>0){
            throw new ServiceException(ErrorCode.FAIL,child.toString());
        }
        IRelationDomainService.super.tipBack(relationsError,"pasteRelationsError");
        //校验通过，创建关系
        Relation relation = SpringUtil.getBean(RelationFactory.class).create();
        req.getRightObjects().forEach(data->{
            //创建关系
            relation.saveRelation(req.getLeftObject(),req.getLeftObjectType(),data.getUid(),data.getObjectType());
        });
    }


    /**
     * 树结构处理虚拟节点
     * @param req
     * @author: 薛锦龙
     * @time: 2022/6/20
     * @return: {@link List< IncludeOrMasterRLResp>}
     */
    default List<IncludeOrMasterRLResp> getVirtualNode(IncludeOrMasterRlReq req){
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        List<DictItemVo> pseudoRel = dictUtil.listByObjectTypeAndDictRel(req.getObjectType(), DictRelEnum.PSEUDO_REL);
        List<IncludeOrMasterRLResp> resps = new ArrayList<>();
        pseudoRel.forEach(data->{
            IncludeOrMasterRLResp resp = new IncludeOrMasterRLResp();
            resp.setUid(req.getUid());
            resp.setObjectType(data.getCode());
            resp.setObjectName(data.getValue());
            resp.setLeftObject(req.getUid());
            resp.setLeftObjectType(req.getObjectType());
            resp.setFictitious(Boolean.TRUE);
            boolean hasChildren = false;
            //判断是否有子集
            //BomNode节点(BOMNode不属于关联关系)
            if (!RelationEntity.class.isAssignableFrom(EntityUtil.getEntityClass(resp.getObjectType()))){
                //工步
                if (Gte4MfgStepEntity.class.isAssignableFrom(EntityUtil.getEntityClass(req.getObjectType()))){
                    hasChildren = EntityUtil.getDynamicQuery(data.getCode(),Triple.of(BOMNodeEntity.PARENT_ITEM,Ops.EQ,req.getUid())).fetchCount()>0;
                }else {
                    hasChildren = this.findBomNode(resp.getLeftObject(), resp.getObjectType(), resp.getLeftObjectType(),true);
                }
            }else {
                 hasChildren =  EntityUtil.getDynamicQuery(resp.getObjectType(),Triple.of(MasterRLEntity.LEFT_OBJECT,Ops.EQ,resp.getUid())).fetchCount()>0;
            }
            resp.setRelationId(IdGeneratorUtil.generate());
            resp.setHasChildren(hasChildren);
            //如果有子集返回虚拟节点
            if (hasChildren){
                resps.add(resp);
            }
        });
        return resps;
    }


    /**
     *
     * @param uid 查找的零组件或版本的uid
     * @param objectType 中间表的类型
     * @param leftObjectType 零组件或版本类型
     * @param versionExists 表示零组件是否是存在版本：true表示存在
     * @author: 薛锦龙
     * @time: 2022/6/23
     * @return: {@link boolean}
     */
    default boolean findBomNode(String uid,String objectType,String leftObjectType,boolean versionExists){
        //表示零组件不存下版本
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        if (!versionExists){
            return EntityUtil.getDynamicQuery(objectType, Triple.of(BOMNodeEntity.PARENT_ITEM, Ops.EQ, uid)).fetchCount()>0;
        }
        //查询相同版本的所有版次
        List<WorkspaceObjectEntity> allSequences = itemRevision.getAllSequences(uid, leftObjectType);
        List<String> revisionUids = allSequences.stream().map(BusinessObjectEntity::getUid).collect(Collectors.toList());
        //查询BOMNode表，查看是否有子集
        if (!BOMNodeEntity.class.isAssignableFrom(EntityUtil.getEntityClass(objectType))){

        }
        return EntityUtil.getDynamicQuery(objectType, Triple.of(BOMNodeEntity.PARENT_ITEM_REV, Ops.IN, revisionUids)).fetchCount()>0;
    }

    /**
     * 零组件版本查询零组件(包括版本信息)
     * @param req
     * @author: 薛锦龙
     * @time: 2022/6/24
     * @return: {@link List< IncludeOrMasterRLResp>}
     */
    default List<IncludeOrMasterRLResp> getItem(IncludeOrMasterRlReq req){
        boolean flag = true;
        WorkspaceObjectEntity workspaceObject;
        List<WorkspaceObjectEntity> allRevisions = new ArrayList<>();
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        Item item = SpringUtil.getBean(ItemFactory.class).create();
        MasterRL masterRL = SpringUtil.getBean(MasterRLFactory.class).create();
        IncludeOrMasterRLResp includeOrMasterRLResp = new IncludeOrMasterRLResp();
        //如果是工步没有版本直接返回工步信息
        if (!ItemRevisionEntity.class.isAssignableFrom(EntityUtil.getEntityClass(req.getObjectType()))){
            workspaceObject = EntityUtil.getDynamicQuery(req.getObjectType(), Triple.of(BusinessObjectEntity.UID, Ops.EQ, req.getUid())).fetchFirst();
            //校验有没有子集
            flag = masterRL.bomNodeHasChildren( workspaceObject.getUid(), workspaceObject.getObjectType(), false, workspaceObject.getUid());
        }else {
            workspaceObject  = itemRevision.getLeftObjectByRightObject(req.getUid(), req.getObjectType());
            //获取一个零组件下所有激活的版本
            allRevisions = item.getAllRevisions(workspaceObject.getUid(), workspaceObject.getObjectType());
        }
        //获取零组件
        BeanUtil.copyPropertiesIgnoreNull(workspaceObject,includeOrMasterRLResp);
        List<WorkTreeResp> collect = new ArrayList<>();
        if(CollUtil.isNotEmpty(allRevisions)){
            collect = allRevisions.stream().map(dsta -> {
                WorkTreeResp resp = new WorkTreeResp();
                BeanUtil.copyPropertiesIgnoreNull(dsta, resp);
                resp.setRelationId(IdGeneratorUtil.generate());
                //判断是否存在子集
                boolean hasChildren = masterRL.bomNodeHasChildren(resp.getUid(), resp.getObjectType(), false,includeOrMasterRLResp.getUid());
                resp.setHasChildren(hasChildren);
                resp.setLeftObject(includeOrMasterRLResp.getUid());
                resp.setLeftObjectType(includeOrMasterRLResp.getObjectType());
                return resp;
            }).collect(Collectors.toList());
        }
        includeOrMasterRLResp.setRelationId(IdGeneratorUtil.generate());
        includeOrMasterRLResp.setHasChildren(flag);
        includeOrMasterRLResp.setChildren(collect);
        includeOrMasterRLResp.setOwnerGroupName("工艺技术处.型号组");
        //时间倒序排序
        if (CollUtil.isNotEmpty(includeOrMasterRLResp.getChildren())) {
            includeOrMasterRLResp.setChildren( includeOrMasterRLResp.getChildren().stream().sorted(Comparator.comparing(WorkTreeResp::getCreationDate).reversed()).collect(Collectors.toList()));
        }
        return CollUtil.newArrayList(includeOrMasterRLResp);
    }


}
