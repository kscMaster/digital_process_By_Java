package com.nancal.service.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nancal.api.model.*;
import com.nancal.api.model.common.ValidList;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.api.utils.TranslateUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.annotation.TimeLog;
import com.nancal.common.base.IdRequest;
import com.nancal.common.config.AppConfig;
import com.nancal.common.config.TokenContext;
import com.nancal.common.config.TokenInfo;
import com.nancal.common.constants.CloneConstant;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.constants.MsgConstant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.DataTypeEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.OperatorEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.exception.TipServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.common.utils.MessageSourceUtil;
import com.nancal.model.entity.*;
import com.nancal.remote.vo.DictItemVo;
import com.nancal.service.bo.BOMView;
import com.nancal.service.bo.Item;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.bo.MasterRL;
import com.nancal.service.factory.BOMViewFactory;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.factory.MasterRLFactory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public interface IBOMNodeDomainService extends IWorkspaceObjectDomainService {

    Logger logger = LoggerFactory.getLogger(BusinessObjectEntity.class);


    @TimeLog
    default <T extends BOMNodeResp> T find(Class<T> nodeRespType, FindReq req, AppNameEnum appNameEnum,
                                           List<T> childNodeList) {
        BOMView bomView = SpringUtil.getBean(BOMViewFactory.class).create();
        boolean isPrecise = bomView.checkPrecise(appNameEnum);
        return find(nodeRespType,req,appNameEnum,childNodeList,isPrecise);
    }

    /**
     *  1、返回树结构的列表
     *  2、入参为返回列表
     * @param nodeRespType
     * @param req
     * @param appNameEnum
     * @param childNodeList
     * @param <T>
     * @return
     */
    @TimeLog
    default <T extends BOMNodeResp> T find(Class<T> nodeRespType, FindReq req, AppNameEnum appNameEnum,
                                           List<T> childNodeList,boolean isPrecise) {
        String bomSnapshotUid = req.getBomSnapshotUid();
        if(ObjectUtil.isNull(childNodeList)){
            logger.error("请创建list容器使用");
            return null;
        }
        WorkspaceObjectEntity objectEntity = EntityUtil.getById(req.getObjectType(), req.getUid());
        if(ObjectUtil.isNull(objectEntity)){
            logger.error("数据不存在");
            return null;
        }
        Class<?> entityClass = EntityUtil.getEntityClass(req.getObjectType());
        T rootNodeResp = ReflectUtil.newInstance(nodeRespType);
        ItemEntity itemEntity = null;
        if (ItemRevisionEntity.class.isAssignableFrom(entityClass)){
            ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
            itemEntity = itemRevision.getLeftObjectByRightObject(req.getUid(), req.getObjectType());
            if(ObjectUtil.isNull(itemEntity)){
                return null;
            }
            req = new FindReq(itemEntity.getUid(),itemEntity.getObjectType(),req.getDeep());
        }else if (ItemEntity.class.isAssignableFrom(entityClass)){
            //如果使用Item查询，默认为非精确bom
            isPrecise = false;
            itemEntity = (ItemEntity)objectEntity;
        }
        if(ObjectUtil.isNotEmpty(itemEntity)) {
            //精确bom
            if (isPrecise) {
                ItemRevisionEntity itemRevisionEntity = (ItemRevisionEntity) objectEntity;
                rootNodeResp.setChildItemRevId(itemRevisionEntity.getRevisionId());
            } else {
                //非精确bom，Item获取最新的版本
                Item item = SpringUtil.getBean(ItemFactory.class).create();
                WorkspaceObjectEntity lastVersion = item.getLastVersion(itemEntity.getUid(), itemEntity.getObjectType());
                rootNodeResp.setChildItemRevId(((ItemRevisionEntity) lastVersion).getRevisionId());
            }
        }else{
            WorkspaceObjectResp resp = EntityUtil.entityToResp(objectEntity);
            processNodeProperty(rootNodeResp, resp);
        }
        rootNodeResp.setParentItem("0");
        rootNodeResp.setBomView(appNameEnum.name());
        rootNodeResp.setChildItem(req.getUid());
        rootNodeResp.setChildItemType(req.getObjectType());

        //精确bom
        if(isPrecise){
            childList(rootNodeResp, appNameEnum, childNodeList,req.getDeep(),rootNodeResp.getChildItemRevId(),bomSnapshotUid);
        }else{
            //非精确bom，父子都查询最新版本
            childList(rootNodeResp, appNameEnum, childNodeList,req.getDeep());
        }
        fullRootNodeResp(rootNodeResp);
        childNodeList.add(rootNodeResp);
        fullChildNodeResp(childNodeList,isPrecise);
        afterFullRootNodeResp(rootNodeResp);
        return rootNodeResp;
    }

    /**
     * 节点渲染结束处理数据
     * @param nodeResp
     * @param <T>
     */
    default <T extends BOMNodeResp> void afterFullRootNodeResp(T nodeResp) {
    }

    /**
     * 返回树结构
     * @param nodeRespType
     * @param req
     * @param appNameEnum
     * @param <T>
     * @return
     */
    @TimeLog
    default <T extends BOMNodeResp> T find(Class<T> nodeRespType, FindReq req, AppNameEnum appNameEnum,boolean isPrecise) {
        //bom行获取关联view，查询对应的子数据
        if(StrUtil.isNotBlank(req.getBomId())){
            BOMNodeEntity bomNode = (BOMNodeEntity)EntityUtil.getById(new BOMNodeEntity().getObjectType(), req.getBomId());
            appNameEnum = ObjectUtil.isEmpty(bomNode)?appNameEnum:StrUtil.isBlank(bomNode.getBomRelationView())?
                    appNameEnum: EnumUtil.fromString(AppNameEnum.class,bomNode.getBomRelationView());
        }

        return find(nodeRespType,req,appNameEnum,new ArrayList<>(),isPrecise);
    }

    /**
     * 返回树结构
     * @param nodeRespType
     * @param req
     * @param appNameEnum
     * @param <T>
     * @return
     */
    @TimeLog
    default <T extends BOMNodeResp> T find(Class<T> nodeRespType, FindReq req, AppNameEnum appNameEnum) {
        BOMView bomView = SpringUtil.getBean(BOMViewFactory.class).create();
        boolean isPrecise = bomView.checkPrecise(appNameEnum);
        return find(nodeRespType,req,appNameEnum,isPrecise);

    }

    /**
     * 填充根节点，可覆盖进行处理
     * @param nodeResp
     * @param <T>
     */
    default <T extends BOMNodeResp> void fullRootNodeResp(T nodeResp) {
        nodeResp.setUid("0");
    }

    /**
     * 非精确bom
     * 用来填充node节点的额外信息
     * map的key值为uid_type
     *
     * @param childNodeList
     * @param <T>
     */
    default <T extends BOMNodeResp> void fullChildNodeResp(List<T> childNodeList,boolean isPrecise) {
        Item item = SpringUtil.getBean(ItemFactory.class).create();
        if(!isPrecise) {
            childNodeList.stream().collect(Collectors.groupingBy(BOMNodeResp::getChildItemType)).forEach((objectType, values) -> {
                List<String> uidList = values.stream().map(BOMNodeResp::getChildItem).collect(Collectors.toList());
                Map<String, WorkspaceObjectEntity> map = null;
                if (ItemEntity.class.isAssignableFrom(EntityUtil.getEntityClass(objectType))) {
                    map = item.getLastVersion(uidList, objectType);
                } else {
                    List<WorkspaceObjectEntity> list = EntityUtil.getByIds(objectType, uidList);
                    if (CollUtil.isNotEmpty(list)) {
                        map = list.stream().collect(
                                Collectors.toMap(WorkspaceObjectEntity::getUid, Function.identity()));
                    }
                }
                if (CollUtil.isNotEmpty(map)) {
                    for (T viewResp : values) {
                        WorkspaceObjectEntity entity = map.get(viewResp.getChildItem());
                        if (ObjectUtil.isNotNull(entity)) {
                            WorkspaceObjectResp resp = EntityUtil.entityToResp(entity);
                            processNodeProperty(viewResp, resp);
                        }
                    }
                }
            });
        }else{
            childNodeList.stream().collect(Collectors.groupingBy(BOMNodeResp::getChildItemType)).forEach((objectType, values) -> {
                List<String> uidList = values.stream().map(BOMNodeResp::getChildItem).collect(Collectors.toList());
                Map<String, WorkspaceObjectEntity> map = null;
                if (ItemEntity.class.isAssignableFrom(EntityUtil.getEntityClass(objectType))) {
                    Map<String, List<WorkspaceObjectEntity>> allRevisionsMap = item.getAllRevisionsMap(uidList, objectType);
                    Map<String, WorkspaceObjectEntity> resultMap = new HashMap<>();
                    if (CollUtil.isNotEmpty(allRevisionsMap)) {
                        values.forEach(nodeResp -> {
                            List<WorkspaceObjectEntity> objectEntityList = allRevisionsMap.get(nodeResp.getChildItem());
                            if (CollUtil.isNotEmpty(objectEntityList)) {
                                ItemRevisionEntity resRevision = objectEntityList.stream().map(ItemRevisionEntity.class::cast).
                                        filter(resp -> resp.getRevisionId().equals(nodeResp.getChildItemRevId())).findFirst().orElse(null);
                                resultMap.put(nodeResp.getChildItem(), resRevision);
                            }
                        });
                    }
                    map = resultMap;
                } else {
                    List<WorkspaceObjectEntity> list = EntityUtil.getByIds(objectType, uidList);
                    if (CollUtil.isNotEmpty(list)) {
                        map = list.stream().collect(
                                Collectors.toMap(WorkspaceObjectEntity::getUid, Function.identity()));
                    }
                }
                if (CollUtil.isNotEmpty(map)) {
                    for (T viewResp : values) {
                        WorkspaceObjectEntity entity = map.get(viewResp.getChildItem());
                        if (ObjectUtil.isNotNull(entity)) {
                            WorkspaceObjectResp resp = EntityUtil.entityToResp(entity);
                            processNodeProperty(viewResp, resp);
                        }
                    }
                }
            });
        }
    }

    default void processNodeProperty(BOMNodeResp nodeResp,WorkspaceObjectResp resp){
        this.processNodeProperty(nodeResp,resp,"resp_");
    }


    /**
     * 处理节点属性
     * @param nodeResp
     * @param resp
     */
    default void processNodeProperty(BOMNodeResp nodeResp,WorkspaceObjectResp resp,String prefix){
        if(ObjectUtil.isNull(resp)){
            return;
        }
        nodeResp.setObjectName(resp.getDisplayName());
        nodeResp.setLifeCycleState(resp.getLifeCycleState());
        nodeResp.setOwnerName(resp.getOwnerName());
        if(resp instanceof ItemRevisionResp){
            nodeResp.setRevUid(resp.getUid());
            nodeResp.setRevObjectType(resp.getObjectType());
        }
        specialProcessNode(nodeResp,resp);
        ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);
        Map<String,Object> mapData = objectMapper.convertValue(resp, Map.class);
        Map<String, Object> newMapData = new HashMap<>();
        mapData.keySet().forEach(key->newMapData.put(prefix+key,mapData.get(key)));
        if(CollUtil.isEmpty(nodeResp.getExtraMap())){
            nodeResp.setExtraMap(newMapData);
        }else{
            nodeResp.getExtraMap().putAll(newMapData);
        }
    }

    default void specialProcessNode(BOMNodeResp nodeResp,WorkspaceObjectResp resp){
        TranslateUtil translateUtil = SpringUtil.getBean(TranslateUtil.class);
        translateUtil.translate(resp);
        //增加随机id用于前端显示结构
        nodeResp.setRandomUid(IdGeneratorUtil.generate());
    }

    /**
     * 打包node节点信息
     *
     * @param reqs
     * @param appNameEnum
     * @return
     */
    default <T extends BOMNodeReq> List<BOMNodeEntity> packageNode(ValidList<T> reqs, AppNameEnum appNameEnum) {
        if (CollUtil.isEmpty(reqs)) {
            throw new ServiceException(ErrorCode.PARAM_INFO_LOSE, "参数不能为空");
        }
        List<BOMNodeEntity> nodeList = new ArrayList<>();
        for (T req : reqs) {
            BOMNodeEntity nodeEntity = packageNode(req, appNameEnum);
            nodeList.add(nodeEntity);
        }
        return nodeList;
    }


    /**
     * 打包node节点
     *
     * @param req
     * @param appNameEnum
     * @return
     */
    default <T extends BOMNodeReq> BOMNodeEntity packageNode(T req, AppNameEnum appNameEnum) {
        this.checkNode(req, appNameEnum);
        fullNodeReq(req);
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        BOMNodeEntity bomNodeEntity = new BOMNodeEntity();
        BeanUtil.copyPropertiesIgnoreNull(req, bomNodeEntity);
        bomNodeEntity.setBomView(appNameEnum.name());
        bomNodeEntity.setUid(IdGeneratorUtil.generate());
        bomNodeEntity.setOwnerId(userUtils.getCurrentUserId());
        bomNodeEntity.setOwnerName(userUtils.getCurrentUserName());
        bomNodeEntity.setBomRelationView(req.getSourceAppName());
        bomNodeEntity.setDelFlag(false);
        if(ObjectUtil.isNull(req.getQuantity())){
            bomNodeEntity.setQuantity(1d);
        }
        if(ObjectUtil.isNull(bomNodeEntity.getTaskTime())){
            bomNodeEntity.setTaskTime(0);
        }
        if(ObjectUtil.isNull(bomNodeEntity.getManualTaskTime())){
            bomNodeEntity.setManualTaskTime(0);
        }
        //粘贴工序或者工步时给位号赋值---同时给手动工时和总工时赋值
        if (Strings.isNotEmpty(req.getBomId())) {
//            BOMNodeEntity bomNode = EntityUtil.getById(new BOMNodeEntity().getObjectType(), req.getBomId());
            //这里根据bomId查询数据，不能限制delflag,配合剪切粘贴总工时赋值
            JPAQueryFactory factory = SpringUtil.getBean(JPAQueryFactory.class);
            QBOMNodeEntity entity = QBOMNodeEntity.bOMNodeEntity;
            BooleanBuilder where = new BooleanBuilder();
            where.and(entity.uid.eq(req.getBomId()));
            BOMNodeEntity bomNode = factory.selectFrom(entity).where(where).fetchFirst();
            if (!Objects.isNull(bomNode)) {
                bomNodeEntity.setTagNo(bomNode.getTagNo());
                bomNodeEntity.setTaskTime(bomNode.getTaskTime());
                bomNodeEntity.setManualTaskTime(bomNode.getManualTaskTime());
            }
        }
        //根据父版本id查询版本号
        if (EntityUtil.checkItem(req.getParentItemType())) {
            ItemRevisionEntity entity = EntityUtil.getById(EntityUtil.getRevision(req.getParentItemType()), req.getParentItemRev());
            if (!Objects.isNull(entity)) {
                bomNodeEntity.setParentItemRevId(entity.getRevisionId());
            }
        }
        return bomNodeEntity;
    }



    /**
     * 可对外进行服务
     * 创建node节点
     * @param nodeRespType
     * @param reqs
     * @param appNameEnum
     * @param <T>
     * @return
     */
    @Transactional
    default <T extends BOMNodeResp> List<T> createNode(Class<T> nodeRespType, ValidList<BOMNodeReq> reqs, AppNameEnum appNameEnum) {
        List<T> respList = new ArrayList<>();
        List<BOMNodeEntity> list = packageNode(reqs, appNameEnum);
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        for (BOMNodeEntity nodeEntity : list) {
            nodeEntity.setObjectType(EntityUtil.getObjectType(BOMNodeEntity.class));
            entityManager.persist(nodeEntity);
            respList.add(ReflectUtil.newInstance(nodeRespType));
        }
        return respList;
    }

    default <T extends BOMNodeReq> void fullNodeReq(T req) {
    }

    /**
     * 对外进行服务，返回指定类型
     * 保存单个node节点
     * @param nodeRespType
     * @param req
     * @param appNameEnum
     * @param <T>
     * @return
     */
    @Transactional
    default <T extends BOMNodeResp> BOMNodeResp createNode(Class<T> nodeRespType, BOMNodeReq req, AppNameEnum appNameEnum) {
        return createNode(nodeRespType, new ValidList<>() {{
            add(req);
        }}, appNameEnum).get(0);
    }

    /**
     * 对外进行服务，返回NodeResp
     * 保存多个node节点
     * @param reqs
     * @param appNameEnum
     * @return
     */
    @Transactional
    default List<BOMNodeResp> createNode(ValidList<BOMNodeReq> reqs, AppNameEnum appNameEnum) {
        return createNode(BOMNodeResp.class, reqs, appNameEnum);
    }

    /**
     * 对外进行服务，返回NodeResp
     * 保存单个node节点
     * @param req
     * @param appNameEnum
     * @return
     */
    @Transactional
    default BOMNodeResp createNode(BOMNodeReq req, AppNameEnum appNameEnum) {
        return createNode(BOMNodeResp.class, req, appNameEnum);
    }

    /**
     * 检查节点循环依赖关系
     *
     * @param bomNodeReq
     * @param appNameEnum
     */
    default void checkNode(BOMNodeReq bomNodeReq, AppNameEnum appNameEnum) {
        if (bomNodeReq.getChildItem().equalsIgnoreCase(bomNodeReq.getParentItem())) {
            throw new ServiceException(ErrorCode.E_11, "不允许粘贴到自己的子集里！！！");
        }
        //查询所有的子
        List<BOMNodeEntity> childList = new ArrayList<>();
        childList(EntityUtil.getObjectType(BOMNodeEntity.class),bomNodeReq.getChildItem(), bomNodeReq.getChildItemType(), appNameEnum, childList); //2048下所有的子
        Set<String> childSet = childList.stream().map(node -> node.getChildItem() + "_" + node.getChildItemType()).collect(Collectors.toSet());
        childSet.add(bomNodeReq.getChildItem()+ "_" + bomNodeReq.getChildItemType());
        //查询所有的父
        List<BOMNodeEntity> parentList = new ArrayList<>();
        parentList(EntityUtil.getObjectType(BOMNodeEntity.class),bomNodeReq.getParentItem(), bomNodeReq.getParentItemType(), appNameEnum, parentList);//0448的所有的父
        Set<String> parentSet = parentList.stream().map(node -> node.getParentItem() + "_" + node.getParentItemType()).collect(Collectors.toSet());
        for (String pUid : parentSet) {
            if (childSet.contains(pUid)) {
                String[] itemArr = pUid.split("_");
                WorkspaceObjectEntity entity = EntityUtil.getById(itemArr[1], itemArr[0]);
                throw new ServiceException(ErrorCode.E_11, "零组件【" + entity.getObjectName() + "】在父项中已经存在，添加BOM失败!!!");
            }
        }
    }


    /**
     * BOM对比
     *
     * @author: 郑复明
     * @date: 11:50  2022/5/11
     * @return: {@link Boolean}
     */
    default CompareResp compare(CompareReq req,AppNameEnum appNameEnum) {
        CompareResp resp = new CompareResp();
        if(req.isLeftIsSnapshot()){
            FindReq left = new FindReq(req.getLeftObject(), req.getLeftObjectType(), -1);
            left.setBomSnapshotUid(req.getLeftBomSnapshotUid());
            BOMNodeSnapshotResp leftResp = this.find(BOMNodeSnapshotResp.class, left,AppNameEnum.MSGBOM,true);
            List<BOMNodeResp> leftList = ObjectUtil.isNull(leftResp) ? null : Collections.singletonList(leftResp);
            resp.setLeftObjects(leftList);
        }else{
            FindReq left = new FindReq(req.getLeftObject(), req.getLeftObjectType(), -1);
            BOPNodeViewResp leftResp = processData(left,appNameEnum);
            List<BOMNodeResp> leftList = ObjectUtil.isNull(leftResp) ? null : Collections.singletonList(leftResp);
            resp.setLeftObjects(leftList);
        }
        if(req.isRightIsSnapshot()) {
            FindReq right = new FindReq(req.getRightObject(), req.getRightObjectType(), -1);
            right.setBomSnapshotUid(req.getRightBomSnapshotUid());
            BOMNodeSnapshotResp rightResp = this.find(BOMNodeSnapshotResp.class, right,AppNameEnum.MSGBOM,true);
            List<BOMNodeResp> rightList = ObjectUtil.isNull(rightResp) ? null : Collections.singletonList(rightResp);
            resp.setRightObjects(rightList);
        }else{
            FindReq right = new FindReq(req.getRightObject(), req.getRightObjectType(), -1);
            BOPNodeViewResp rightResp = processData(right,appNameEnum);
            List<BOMNodeResp> rightList = ObjectUtil.isNull(rightResp) ? null : Collections.singletonList(rightResp);
            resp.setRightObjects(rightList);
        }
        return resp;
    }

    default BOPNodeViewResp processData(FindReq req,AppNameEnum appNameEnum){
        WorkspaceObjectEntity entity = EntityUtil.getById(req.getObjectType(),req.getUid());
        if(ObjectUtil.isEmpty(entity)){
            return null;
        }
        String revisionId = null;
        if(EntityUtil.checkItem(req.getObjectType())){
            revisionId = ((ItemRevisionEntity)entity).getRevisionId();
        }
        BOPNodeViewResp leftResp = this.find(BOPNodeViewResp.class, req, appNameEnum,true);
        leftResp.setChildren(null);
        this.processNodeProperty(leftResp,EntityUtil.entityToResp(entity),"resp_");
        List<BOMNodeEntity> leftList = new ArrayList<>();
        childList(EntityUtil.getObjectType(BOMNodeEntity.class),leftResp.getChildItem(),leftResp.getChildItemType(), revisionId,appNameEnum,leftList,0,1);
        if(CollUtil.isNotEmpty(leftList)){
            List<BOMNodeResp> leftListResp = new ArrayList<>();
            leftList.forEach(le->{
                BOPNodeViewResp re = new BOPNodeViewResp();
                BeanUtil.copyPropertiesIgnoreNull(le,re);
                leftListResp.add(re);
            });
            fullChildNodeResp(leftListResp,false);
            leftResp.setChildren(leftListResp);
            leftListResp.forEach(resp->{
                FindReq nLeft = new FindReq(resp.getChildItem(), resp.getChildItemType(), -1);
                BOPNodeViewResp childResp = find(BOPNodeViewResp.class, nLeft, appNameEnum);
                if(ObjectUtil.isNotEmpty(nLeft)){
                    resp.setChildren(childResp.getChildren());
                }
            });
        }
        return leftResp;
    }

    default List<WorkspaceObjectEntity> parentList(String cid, String cType, AppNameEnum appNameEnum) {
        return parentList(cid,cType,null,appNameEnum);
    }

    /**
     * 父级单层查询
     * @param cid
     * @param cType
     * @param appNameEnum
     * @param
     */
    default List<WorkspaceObjectEntity> parentList(String cid, String cType,String cRevId, AppNameEnum appNameEnum) {
        List<Pair<String, Object>> params = Arrays.asList(Pair.of(
                BOMNodeEntity.CHILD_ITEM, cid),
                Pair.of(BOMNodeEntity.CHILD_ITEM_TYPE, cType)
//                ,Pair.of(BOMNodeEntity.BOM_VIEW, appNameEnum.name()
        );
        List<WorkspaceObjectEntity> nodeList = EntityUtil.getDynamicEqQuery(new BOMNodeEntity().getObjectType(), params).fetch();
        if (CollUtil.isEmpty(nodeList)) {
            return null;
        }
        if(!StrUtil.isBlank(cRevId)){
            nodeList = nodeList.stream().map(BOMNodeEntity.class::cast).filter(node->node.getChildItemRevId().equals(cRevId)).collect(Collectors.toList());
        }
        return nodeList;
    }

    /**
     * 递归获取所有父节点
     *
     * @param cid
     * @param cType
     * @param appNameEnum
     * @param list
     */
    default void parentList(String nodeType,String cid, String cType, AppNameEnum appNameEnum, List<BOMNodeEntity> list) {
        List<Pair<String, Object>> params = Arrays.asList(Pair.of(
                BOMNodeEntity.CHILD_ITEM, cid),
                Pair.of(BOMNodeEntity.CHILD_ITEM_TYPE, cType)
//                ,Pair.of(BOMNodeEntity.BOM_VIEW, appNameEnum.name())
        );
        List<WorkspaceObjectEntity> nodeList = EntityUtil.getDynamicEqQuery(new BOMNodeEntity().getObjectType(), params).fetch();
        if (CollUtil.isEmpty(nodeList)) {
            return;
        }
        nodeList.stream().map(BOMNodeEntity.class::cast).forEach(data -> {
            list.add(data);
            parentList(nodeType,data.getParentItem(), data.getParentItemType(), appNameEnum, list);
        });
    }


    /**
     * 获取所有层级bom结构-非精确bom
     * @param pid
     * @param pType
     * @param appNameEnum
     * @param list
     */
    default void childList(String nodeType,String pid, String pType, AppNameEnum appNameEnum, List<BOMNodeEntity> list){
        childList(nodeType,pid,pType,null,appNameEnum,list,0,-1);
    }

    default void childList(String pid, String pType, AppNameEnum appNameEnum, List<BOMNodeEntity> list){
        childList(EntityUtil.getObjectType(BOMNodeEntity.class),pid,pType,null,appNameEnum,list,0,-1);
    }

    /**
     * 获取所有层级bom结构-精确bom
     * @param pid
     * @param pType
     * @param pRevId
     * @param appNameEnum
     * @param list
     */
    default void childList(String nodeType,String pid, String pType,String pRevId, AppNameEnum appNameEnum, List<BOMNodeEntity> list){
        childList(nodeType,pid,pType,pRevId,appNameEnum,list,0,-1);
    }

    /**
     * 获取指定层级bom结构-非精确bom
     * @param pid
     * @param pType
     * @param appNameEnum
     * @param list
     */
    default void childList(String nodeType,String pid, String pType, AppNameEnum appNameEnum, List<BOMNodeEntity> list,Integer deep){
        childList(nodeType,pid,pType,null,appNameEnum,list,0,deep);
    }


    /**
     * 获取指定层级bom结构-精确bom
     * @param pid
     * @param pType
     * @param pRevId
     * @param appNameEnum
     * @param deep
     * @return
     */
    default void childList(String nodeType,String pid, String pType,String pRevId, AppNameEnum appNameEnum, List<BOMNodeEntity> list,Integer deep){
        childList(nodeType,pid,pType,pRevId,appNameEnum,list,0,deep);
    }

    /**
     * 获取单层级-非精确bom
     * @param pid
     * @param pType
     * @param appNameEnum
     */
    default List<BOMNodeEntity> childListSingle(String nodeType,String pid, String pType,String pRevId, AppNameEnum appNameEnum){
        List<BOMNodeEntity> list = new ArrayList<>();
        childList(nodeType,pid,pType,pRevId,appNameEnum,list,0,1);
        return list;
    }

    /**
     * 获取单层级-精确bom
     * @param pid
     * @param pType
     * @param appNameEnum
     * @return
     */
    default List<BOMNodeEntity> childListSingle(String nodeType,String pid, String pType,AppNameEnum appNameEnum){
        List<BOMNodeEntity> list = new ArrayList<>();
        childList(nodeType,pid,pType,null,appNameEnum,list,0,1);
        return list;
    }

    /**
     * 递归获取子的BOM节点
     *
     * @param pid
     * @param pType
     * @param appNameEnum
     * @param list
     */
    default void childList(String nodeType,String pid, String pType,String pRevId, AppNameEnum appNameEnum, List<BOMNodeEntity> list,
                           Integer deep,Integer maxDeep) {
        //根据子零组件id作为父零组件id,查询出子零组件id的集合
        List<Pair<String, Object>> params = Arrays.asList(Pair.of(
                BOMNodeEntity.PARENT_ITEM, pid),
                Pair.of(BOMNodeEntity.PARENT_ITEM_TYPE, pType),
                Pair.of(BOMNodeEntity.OBJECT_TYPE, nodeType)
//                ,Pair.of(BOMNodeEntity.BOM_VIEW, appNameEnum.name())
        );
        List<WorkspaceObjectEntity> nodeList = EntityUtil.getDynamicEqQuery(
                new BOMNodeEntity().getObjectType(), params).fetch();
        if (CollUtil.isEmpty(nodeList)) {
            return;
        }
        deep=deep+1;
        List<BOMNodeEntity> nodeEntityList = nodeList.stream().map(BOMNodeEntity.class::cast).collect(Collectors.toList());
        Map<String, List<BOMNodeEntity>>  versionGroupMap = nodeEntityList.stream().
                collect(Collectors.groupingBy(BOMNodeEntity::getParentItemRevId));
        //精确bom标记，如果传了父版本，认为是精确bom
        boolean accurateFlag = true;
        if(StrUtil.isBlank(pRevId)) {
            accurateFlag = false;
            pRevId = versionGroupMap.keySet().stream().sorted(Comparator.reverseOrder()).findFirst().orElse("0");
        }
        nodeEntityList = versionGroupMap.get(pRevId);
        if(CollUtil.isEmpty(nodeEntityList)){
            return;
        }
        nodeEntityList = nodeEntityList.stream().filter(work->!work.getDelFlag()).collect(Collectors.toList());
        for (WorkspaceObjectEntity entity : nodeEntityList) {
            BOMNodeEntity data = (BOMNodeEntity)entity;
            list.add(data);
            if(maxDeep>deep || maxDeep == -1){
                childList(nodeType,data.getChildItem(), data.getChildItemType(),accurateFlag?data.getChildItemRevId():null, appNameEnum, list,deep,maxDeep);
            }
        }
    }


    /**
     * 单层级结构-非精确bom
     * @param nodeResp
     * @param appNameEnum
     * @param childList
     * @param <T>
     */
    default <T extends BOMNodeResp> void childListSingle(T nodeResp, AppNameEnum appNameEnum, List<T> childList){
        childList(nodeResp,appNameEnum,childList,0,1,null,null);
    }

    /**
     * 单层级结构-精确bom
     * @param nodeResp
     * @param appNameEnum
     * @param childList
     * @param <T>
     */
    default <T extends BOMNodeResp> void childListSingle(T nodeResp, AppNameEnum appNameEnum, List<T> childList,String revId){
        childList(nodeResp,appNameEnum,childList,0,1,revId,null);
    }

    /**
     * 所有结构-非精确bom
     * @param nodeResp
     * @param appNameEnum
     * @param childList
     * @param <T>
     */
    default <T extends BOMNodeResp> void childList(T nodeResp, AppNameEnum appNameEnum, List<T> childList){
        childList(nodeResp,appNameEnum,childList,0,-1,null,null);
    }

    /**
     * 所有结构-精确bom
     * @param nodeResp
     * @param appNameEnum
     * @param childList
     * @param <T>
     */
    default <T extends BOMNodeResp> void childList(T nodeResp, AppNameEnum appNameEnum, List<T> childList,String revId,String snapshotUid){
        childList(nodeResp,appNameEnum,childList,0,-1,revId,snapshotUid);
    }

    /**
     * 指定层级-非精确bom
     * @param nodeResp
     * @param appNameEnum
     * @param childList
     * @param maxDeep
     * @param <T>
     */
    default <T extends BOMNodeResp> void childList(T nodeResp, AppNameEnum appNameEnum, List<T> childList,Integer maxDeep){
        childList(nodeResp,appNameEnum,childList,0,maxDeep,null,null);
    }

    /**
     * 指定层级-精确bom
     * @param nodeResp
     * @param appNameEnum
     * @param childList
     * @param maxDeep
     * @param <T>
     */
    default <T extends BOMNodeResp> void childList(T nodeResp, AppNameEnum appNameEnum, List<T> childList,Integer maxDeep,String revId,String snapshotUid){
        childList(nodeResp,appNameEnum,childList,0,maxDeep,revId,snapshotUid);
    }

    /**
     * 层级结构
     *
     * @param nodeResp
     * @param appNameEnum
     * @param <T>
     */
    default <T extends BOMNodeResp> void childList(T nodeResp, AppNameEnum appNameEnum, List<T> childList,
                                                   Integer deep,Integer maxDeep,String revId,String snapshotUid) {
        List<Pair<String, Object>> params = new ArrayList<>();
        params.add(Pair.of(BOMNodeEntity.PARENT_ITEM, nodeResp.getChildItem()));
        params.add(Pair.of(BOMNodeEntity.PARENT_ITEM_TYPE, nodeResp.getChildItemType()));
        params.add(Pair.of(BOMNodeEntity.OBJECT_TYPE, nodeResp.getObjectType()));
        if(StrUtil.isNotEmpty(snapshotUid)){
            params.add(Pair.of(BOMNodeSnapshotEntity.SNAPSHOT_ID, snapshotUid));
        }
        String objectType = new BOMNodeEntity().getObjectType();
        OrderSpecifier orderSpecifier = new OrderSpecifier(Order.ASC,
                ExpressionUtils.path(EntityUtil.getEntityClass(objectType), BOMNodeEntity.FOUND_NO));
        //查询删除的bom，目的，获取非精确bom的最新版本
        List<WorkspaceObjectEntity> nodeList = EntityUtil.getDynamicEqQuery(objectType, params,true).orderBy(orderSpecifier).fetch();
        if (CollUtil.isEmpty(nodeList)) {
            return;
        }
        deep=deep+1;
        List<BOMNodeEntity> nodeEntityList = nodeList.stream().map(BOMNodeEntity.class::cast).collect(Collectors.toList());
        Map<String, List<BOMNodeEntity>> versionGroupMap = nodeEntityList.stream().
                collect(Collectors.groupingBy(BOMNodeEntity::getParentItemRevId));
        //精确bom标记，如果传了父版本，认为是精确bom
        boolean accurateFlag = true;
        if(StrUtil.isBlank(revId)) {
            accurateFlag = false;
            revId = versionGroupMap.keySet().stream().sorted(Comparator.reverseOrder()).findFirst().orElse("0");
        }
        nodeEntityList = versionGroupMap.get(revId);
        if(CollUtil.isEmpty(nodeEntityList)){
            return;
        }
        //过滤移除的bom
        nodeEntityList = nodeEntityList.stream().filter(work->!work.getDelFlag()).collect(Collectors.toList());
//        final AppNameEnum finalEnum = appNameEnum;
        for (BOMNodeEntity nodeEntity : nodeEntityList) {
            T bomNodeResp = (T) ReflectUtil.newInstance(nodeResp.getClass());
            BeanUtil.copyPropertiesIgnoreNull(nodeEntity, bomNodeResp);
            childList.add(bomNodeResp);
            if (CollUtil.isEmpty(nodeResp.getChildren())) {
                nodeResp.setChildren(new ArrayList<>());
            }
            nodeResp.getChildren().add(bomNodeResp);
            nodeResp.setHasChildren(true);
            if(maxDeep>deep || maxDeep == -1){
//                appNameEnum = StrUtil.isBlank(nodeEntity.getBomRelationView())?
//                        appNameEnum: EnumUtil.fromString(AppNameEnum.class,nodeEntity.getBomRelationView());
                childList(bomNodeResp, appNameEnum, childList,deep,maxDeep,accurateFlag?nodeEntity.getChildItemRevId():null,snapshotUid);
            }else{
                bomNodeResp.setHasChildren(hasChildren(bomNodeResp.getChildItemType(),bomNodeResp.getChildItem()));
            }
        }
    }



    /**
     * 编辑权限校验（bom行属性+当前对象的属性）
     * 1、bom行属性的校验规则：
     * a.工艺规程没有父级，没有bom行属性
     * b.工序和工步的bom属性编辑权限取决于其父级的当前对象属性是不是当前用户创建，且状态为工作中
     * 2、当前对象的属性编辑权限规则：
     * a.工艺规程与工序和工步相同
     * b.都取决于当前对象属性是不是当前用户创建，且状态为工作中
     *
     * @param req
     * @author: 拓凯
     * @time: 2022/6/9
     * @return: {@link MfgCheckResp}
     */
    default MfgCheckResp check(MfgCheckReq req) {
        StringBuilder tipBuilder = new StringBuilder();
        MfgCheckResp checkResp = new MfgCheckResp();
        checkResp.setIsBomCheck(false);
        //不管啥样的类型都要走的校验逻辑
        WorkspaceObjectEntity entity = EntityUtil.getById(req.getType(), req.getId());
        if (Objects.isNull(entity)) {
            throw new ServiceException(ErrorCode.E_12);
        }
//        checkResp.setIsObjectCheck(this.verifyAuthorityReturn(entity, OperatorEnum.Write));
        checkResp.setIsObjectCheck(true);
        try {
            this.verifyAuthority(entity, OperatorEnum.Write,"updateError",entity.getObjectName());
        }catch (TipServiceException e){
            tipBuilder.append(e.getMessage());
            checkResp.setIsObjectCheck(false);
        }
        //如果bomId不为0则代表次对象有父级
        if (!"0".equals(req.getBomId())) {
            BOMNodeEntity bomNode = EntityUtil.getById(new BOMNodeEntity().getObjectType(), req.getBomId());
            if (Objects.isNull(bomNode)) {
                throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("bomIsNull",req.getBomId()));
            }
            //工步作为父级
            if ("0".equals(bomNode.getParentItemRev())) {
                WorkspaceObjectEntity stepEntity = EntityUtil.getById(bomNode.getParentItemType(), bomNode.getParentItem());
//                checkResp.setIsBomCheck(this.verifyAuthorityReturn(stepEntity, OperatorEnum.Write));
                checkResp.setIsBomCheck(true);
                try {
                    this.verifyAuthority(stepEntity, OperatorEnum.Write,DataTypeEnum.PARENT_DATA);
                }catch (TipServiceException e){
                    tipBuilder.append(e.getMessage());
                    checkResp.setIsBomCheck(false);
                }
            } else {
                ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
                //根据父版本uid获取同版本最新激活的版次
                //根据parentItemRev，parentItemRevType判断bom属性的编辑权限
                ItemRevisionEntity activeSequence = (ItemRevisionEntity) itemRevision.getActiveSequence(bomNode.getParentItemRev(), EntityUtil.getRevision(bomNode.getParentItemType()));
                if (Objects.isNull(activeSequence)) {
                    throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("parentRevIsNull",bomNode.getChildItem()));
                }
                if (!activeSequence.getActive()) {
                    throw new ServiceException(ErrorCode.E_12, "查询不到最新的版本，请刷新后重试！");
                }
//                checkResp.setIsBomCheck(this.verifyAuthorityReturn(activeSequence, OperatorEnum.Write));
                checkResp.setIsBomCheck(true);
                try {
                    this.verifyAuthority(activeSequence, OperatorEnum.Write,DataTypeEnum.PARENT_DATA);
                }catch (TipServiceException e){
                    tipBuilder.append(e.getMessage());
                    checkResp.setIsBomCheck(false);
                }
            }
        }
        checkResp.setCheckTip(tipBuilder.toString());
        return checkResp;
    }

    default BusinessObjectResp getObjectTile(IdRequest id) {
        return null;
    }

    default CheckPomResp checkPBom(IdRequest idRequest) {
        return null;

    }

    @Transactional
    default void cloneBomNode(String objectType,String uid,String cloneObjectType,String cloneUid,String cloneRevId,
                              AppNameEnum appNameEnum,Map<String,Triple<String,String,String>> repeatCheckMap){
        cloneBomNode(objectType,uid,cloneObjectType,cloneUid,cloneRevId,appNameEnum,appNameEnum,repeatCheckMap,null);
    }

    @Transactional
    default void cloneBomNode(String objectType,String uid,String cloneObjectType,String cloneUid,String cloneRevId,
                              AppNameEnum appNameEnum,AppNameEnum saveAppNameEnum,List<String> bomIdList){
        cloneBomNode(objectType,uid,cloneObjectType,cloneUid,cloneRevId,appNameEnum,saveAppNameEnum,new HashMap<>(),bomIdList);
    }

    @Transactional
    default void cloneBomNode(String objectType,String uid,String cloneObjectType,String cloneUid,String cloneRevId,
                              AppNameEnum appNameEnum,AppNameEnum saveAppNameEnum,Map<String,Triple<String,String,String>> repeatCheckMap,List<String> bomIdList){
        //查询当前类型的克隆结构
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        Map<String, String> codeValueMap = dictUtil.getCodeValueMap(cloneObjectType + "_cloneStructure");
        if(CollUtil.isEmpty(codeValueMap)){
            return;
        }
        //特殊关联关系拷贝
        codeValueMap.keySet().forEach(code->{
            String relationRl = codeValueMap.get(code);
            if(EntityUtil.checkRelation(relationRl)){
                Item item = SpringUtil.getBean(ItemFactory.class).create();
                WorkspaceObjectEntity lastVersion = item.getLastVersion(uid, objectType);
                if(ObjectUtil.isNotNull(lastVersion)){
                    ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
                    List<WorkspaceObjectEntity> allSequences = itemRevision.getAllSequences(lastVersion.getUid(), lastVersion.getObjectType());
                    List<String> uidList = allSequences.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
                    MasterRL masterRL = SpringUtil.getBean(MasterRLFactory.class).create();
                    List<RelationEntity> relationList = masterRL.childListSingle(relationRl, uidList, lastVersion.getObjectType());
                    relationList.forEach(relation->{
                        masterRL.saveRelation(cloneRevId,EntityUtil.getRevision(cloneObjectType),relation.getRightObject(),relation.getRightObjectType());
                    });
                }
            }
        });
        //bomNode克隆
        List<BOMNodeEntity> bomNodeList = childListSingle(EntityUtil.getObjectType(BOMNodeEntity.class),uid, objectType, appNameEnum);
        if(CollUtil.isEmpty(bomNodeList)){
            return;
        }
        BOMView bomView = SpringUtil.getBean(BOMViewFactory.class).create();
        boolean checkPrecise = bomView.checkPrecise(saveAppNameEnum);
        for (BOMNodeEntity bomNode : bomNodeList) {
            String cloneType = codeValueMap.get(bomNode.getChildItemType());
            if(!CloneConstant.CLONE.equals(cloneType) && !CloneConstant.RELATION.equals(cloneType)) {
                continue;
            }
            if(CollUtil.isNotEmpty(bomIdList) && !bomIdList.contains(bomNode.getUid())){
                continue;
            }
            String childItemType = bomNode.getChildItemType();
            String childItem = bomNode.getChildItem();
            String newCloneObjectType = "";
            String newCloneUid = "";
            String newCloneRev = "";
            String newCloneRevId = "";
            String newCloneRevType = "";
            //判断克隆类型
            Item item = SpringUtil.getBean(ItemFactory.class).create();
            if(CloneConstant.CLONE.equals(cloneType)){
                if(!repeatCheckMap.containsKey(childItemType+"_"+childItem)){
                    IWorkspaceObjectDomainService workspaceObjectDomainService = SpringUtil.getBean(EntityUtil.getBeanServiceImpl(childItemType));
                    WorkspaceObjectReq workspaceObjectReq;
                    //无版本，如工步
                    if(!EntityUtil.checkItem(childItemType)){
                        WorkspaceObjectEntity entity = EntityUtil.getById(childItemType,childItem);
                        workspaceObjectReq = ReflectUtil.newInstance(EntityUtil.getReqPackage(childItemType));
                        BeanUtil.copyPropertiesIgnoreNull(entity,workspaceObjectReq);
                    }else{
                        WorkspaceObjectEntity lastVersion = item.getLastVersion(childItem, childItemType);
                        workspaceObjectReq = ReflectUtil.newInstance(EntityUtil.getReqPackage(EntityUtil.getRevision(childItemType)));
                        BeanUtil.copyPropertiesIgnoreNull(lastVersion,workspaceObjectReq);
                    }
                    try {
                        ReflectUtil.setFieldValue(workspaceObjectReq, ItemEntity.ITEM_ID, "");
                    }catch (Exception e){
                        log.info("当前对象没有{}字段",ItemEntity.ITEM_ID);
                    }
                    workspaceObjectReq.setObjectName(workspaceObjectReq.getObjectName());
                    workspaceObjectReq.setObjectType(childItemType);
                    WorkspaceObjectResp resp = workspaceObjectDomainService.save(workspaceObjectReq);
                    newCloneObjectType = resp.getObjectType();
                    newCloneUid = resp.getUid();
                    newCloneRev = resp.getRightObject();
                    newCloneRevType = resp.getRightObjectType();
                    newCloneRevId = resp.getRightObjectRevId();
                    if (Objects.isNull(newCloneRev)) {
                        newCloneRev = "0";
                    }
                    //保存当前克隆的数据，以下可能存在重复的数据，针对重复数据第一次克隆，第二次关联
                    repeatCheckMap.put(childItemType+"_"+childItem, Triple.of(newCloneUid,newCloneObjectType,newCloneRev));
                }else{
                    Triple<String, String, String> triple = repeatCheckMap.get(childItemType + "_" + childItem);
                    newCloneUid = triple.getLeft();
                    newCloneObjectType = triple.getMiddle();
                    newCloneRev = triple.getRight();
                    newCloneRevType = EntityUtil.getRevision(newCloneObjectType);
                }
            }else if(CloneConstant.RELATION.equals(cloneType)){
                newCloneObjectType = childItemType;
                newCloneUid = childItem;
                WorkspaceObjectEntity lastVersion = item.getLastVersion(childItem, childItemType);
                newCloneRev = lastVersion.getUid();
                newCloneRevType = EntityUtil.getRevision(childItemType);
            }
            BOMNodeReq bomNodeReq = new BOMNodeReq();
            BeanUtil.copyPropertiesIgnoreNull(bomNode,bomNodeReq);
            bomNodeReq.setParentItemType(cloneObjectType);
            bomNodeReq.setParentItem(cloneUid);
            bomNodeReq.setParentItemRev(cloneRevId);
            bomNodeReq.setChildItemType(newCloneObjectType);
            bomNodeReq.setChildItem(newCloneUid);
            bomNodeReq.setBomRelationView(bomNode.getBomRelationView());
            if(checkPrecise){
                if (EntityUtil.checkItem(newCloneObjectType)) {
                    bomNodeReq.setChildItemRevision(newCloneRev);
                    bomNodeReq.setChildItemTypeRevision(newCloneRevType);
                    bomNodeReq.setChildItemRevId(newCloneRevId);
                }else {
                    bomNodeReq.setChildItemRevision(newCloneUid);
                    bomNodeReq.setChildItemTypeRevision(newCloneObjectType);
                    bomNodeReq.setChildItemRevId(newCloneRevId);
                }
            }
            this.createNode(bomNodeReq,saveAppNameEnum);
            cloneBomNode(childItemType,childItem,newCloneObjectType,newCloneUid,newCloneRev,appNameEnum,saveAppNameEnum,repeatCheckMap,bomIdList);
        }
    }


    /**
     * 1.校验粘贴权限，等同于编辑权限的校验（往A底下粘贴B,校验A的编辑权限）
     * 2.调用字典工具中的getRelation()判断是否支持粘贴关系  Gte4MfgProcess_bomStructure
     * 3.校验循环粘贴（避免当前子级的父级作为子级的子级）
     * 4.处理查找编号生成逻辑（主要处理工艺资源的查找编号）
     * A:当工艺资源与工步是同级时，查找编号继承工步
     * B:当工艺资源作为工步的子级时，查找编号等同于工步的规则
     *
     * @param reqs
     * @author: 拓凯
     * @time: 2022/6/21
     * @return: {@link List<BOMNodeResp>}
     */
    @Transactional
    default List<Object> parseData(ValidList<BOMNodeReq> reqs,AppNameEnum appNameEnum,String bomStructureName) {
        StringBuilder stringBuilder = new StringBuilder();
        for (BOMNodeReq req : reqs) {
            WorkspaceObjectEntity entity;
            //无版本类型
            if (!EntityUtil.checkItem(req.getParentItemType())) {
                entity = EntityUtil.getById(req.getParentItemType(), req.getParentItem());
                //有版本类型
            } else {
                entity = EntityUtil.getById(EntityUtil.getRevision(req.getParentItemType()), req.getParentItemRev());
            }
            //1.校验父级的编辑权限
            try {
                this.verifyAuthority(entity, OperatorEnum.Write, DataTypeEnum.PARENT_DATA);
            }catch (TipServiceException e){
                stringBuilder.append(e.getMessage());
                continue;
            }
            //2.校验自己粘贴自己
            if (req.getChildItem().equalsIgnoreCase(req.getParentItem())) {
                stringBuilder.append(MessageSourceUtil.getMessage("pasteOwnChild",entity.getObjectName()));
                continue;
            }
            //3.判断是否支持粘贴关系
            boolean checkRes = this.checkBomStructureRelation(req.getParentItemType(), req.getChildItemType(),bomStructureName);
            if (!checkRes) {
                //转换字典值
                Map<String, String> map = SpringUtil.getBean(DictUtil.class).getCodeValueMap(DictConstant.OBJECT_TYPE_NAME);
                stringBuilder.append(MessageSourceUtil.getMessage("pasteTypeCheck",map.get(req.getParentItemType()),map.get(req.getChildItemType())));
            }
            // 随工工艺规程--判断要粘贴的工序有没有被其他随工工艺规程或者工艺规程引用 判断要粘贴的工步有没有别其他工序引用
            if (appNameEnum.equals(AppNameEnum.FOLLOW_MSGBOM)) {
                List<Pair<String, Object>> params = Arrays.asList(
                        Pair.of(BOMNodeEntity.DEL_FLAG, false),
                        Pair.of(BOMNodeEntity.CHILD_ITEM, req.getChildItem())
                );
                List<WorkspaceObjectEntity> list = EntityUtil.getDynamicEqQuery(new BOMNodeEntity().getObjectType(), params).fetch();
                if (list.size()>0) {
                    stringBuilder.append(MessageSourceUtil.getMessage("quoteTip",entity.getObjectName()));
                    continue;
                }
            }
        }
        if (stringBuilder.length() > 0) {
            throw new TipServiceException(ErrorCode.FAIL,MessageSourceUtil.getMessage("pasteDataTip",stringBuilder.toString()));
        } else {
            reqs.stream().forEach(req -> {
                //调用维护工时的方法
                this.createNode(req, appNameEnum);
                fullTaskTime(2,req.getChildItem(),req.getChildItemType());
            });
            return CollUtil.newArrayList(true);
        }
    }


    /**
     * 判断BOMNode是否存在子集
     * @param
     * @author: 薛锦龙
     * @time: 2022/7/21
     * @return: {@link boolean}
     */
    default boolean hasChildren(String objectType,String uid){
        List<Triple<String,Ops,Object>> params = new ArrayList<>();
        Class<?> entityClass = EntityUtil.getEntityClass(objectType);
        if(ItemRevisionEntity.class.isAssignableFrom(entityClass)){
            //版本(获取相同版本下的所有版次)
            ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
            List<WorkspaceObjectEntity> allSequences = itemRevision.getAllSequences(uid, objectType);
            List<String> stringList = allSequences.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
            params.add(Triple.of(BOMNodeEntity.PARENT_ITEM_REV, Ops.IN,stringList));
        }else {
            //查找BomNode表
            params.add(Triple.of(BOMNodeEntity.PARENT_ITEM, Ops.EQ,uid));
        }
        return EntityUtil.getDynamicQuery(ReflectUtil.newInstance(BOMNodeEntity.class).getObjectType(),params).fetchCount()>0;

    }


    /**
     * 升版后往BOMNODE表中添加数据
     * @param id
     * @author: 拓凯
     * @time: 2022/7/25
     * @return: {@link boolean}
     */
    @Transactional
    default boolean insertBomNode(IdRequest id,ItemRevisionResp resp,AppNameEnum appNameEnum) {
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        //1.先根据版本id和类型查询出在bomNode中所有的子级
        List<BOMNodeEntity> childList = childListSingle(EntityUtil.getObjectType(BOMNodeEntity.class),id.getUid(), id.getObjectType(), appNameEnum);
        //2.去重集合，将数据中的父级版本id跟换，再添加到表中
        List<BOMNodeEntity> collect = StreamEx.of(childList).distinct(BOMNodeEntity::getChildItem).collect(Collectors.toList());
        collect.forEach(data->{
                    BOMNodeEntity entity = new BOMNodeEntity();
                    BeanUtil.copyPropertiesIgnoreNull(data, entity);
                    entity.setParentItemRev(resp.getUid());
                    entity.setUid(IdGeneratorUtil.generate());
                    entity.setParentItemRevId(resp.getRevisionId());
                    entityManager.persist(entity);
        });
        return true;
    }

    default boolean checkBomStructure(String bomStructure,String parentItemType,String childItemType){
        if(StrUtil.isBlank(bomStructure) || StrUtil.isBlank(childItemType)){
            return false;
        }
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        List<DictItemVo> codeValueList = dictUtil.getCodeValueList(bomStructure);
        if(CollUtil.isEmpty(codeValueList)){
            return false;
        }
        Map<String, List<DictItemVo>> codeMap = codeValueList.stream().collect(Collectors.groupingBy(DictItemVo::getCode));
        List<DictItemVo> voList = codeMap.get(childItemType);
        if(CollUtil.isEmpty(voList)){
            return false;
        }
        return voList.stream().anyMatch(vo -> parentItemType.equalsIgnoreCase(vo.getValue()));
    }


    default boolean upgradeCheck(MfgCheckReq req){
        return false;}


    /**
     * 校验是否存在父级
     * @param uid
     * @param objectType
     * @author: 薛锦龙
     * @time: 2022/8/30
     * @return: {@link boolean}
     */
    default List<WorkspaceObjectEntity> existParent(String uid,String objectType,boolean isThroe){
        List<WorkspaceObjectEntity> entities = EntityUtil.getDynamicQuery(objectType, Triple.of(BOMNodeEntity.CHILD_ITEM, Ops.EQ, uid)).fetch();
        if (CollUtil.isNotEmpty(entities) && isThroe){
            throw new ServiceException(ErrorCode.E_10,"数据不是根节点");
        }
        return entities;
    }

    /**
     * 检查是否支持bom结构关系
     * @param parentType
     * @param childType
     * @param bomStructureName
     * @author: 拓凯
     * @time: 2022/6/24
     * @return: {@link boolean}
     */
    default boolean checkBomStructureRelation(String parentType,String childType,String bomStructureName) {
        List<DictItemVo> codeValueList = SpringUtil.getBean(DictUtil.class).getCodeValueList(bomStructureName);
        Set<String> collect = codeValueList.stream().map(dictItemVo -> dictItemVo.getValue() + dictItemVo.getCode()).collect(Collectors.toSet());
        return collect.stream().anyMatch(String -> String.equalsIgnoreCase(parentType + childType));
    }

    /**
     * 升版权限校验通用
     * @author: 拓凯
     * @time: 2022/6/24
     * @return: {@link boolean}
     */
    default boolean upgradeCheck(IdRequest id) {
        WorkspaceObjectEntity entity = EntityUtil.getById(id.getObjectType(), id.getUid());
        if(ObjectUtil.isNull(entity)){
            throw new ServiceException(ErrorCode.E_12);
        }
        //校验创建人权限
        verifyAuthority(entity, OperatorEnum.Upgrade,"upgradeData",entity.getObjectName());
        return true;
    }

    /**
     *  将所选的版本及零件的ownerId和ownerName替换成指定的用户
     * @param req
     * @return
     */
    @Transactional
    default boolean taskAppoint(TaskAppointReq req) {
        ItemRevisionFactory itemRevisionFactory = SpringUtil.getBean(ItemRevisionFactory.class);
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        List<String> childNameList = new ArrayList<>();
        req.getIds().forEach(id->{
            //版本
            WorkspaceObjectEntity revEntity = EntityUtil.getById(id.getObjectType(), id.getUid());
            changeOwner(revEntity, req,entityManager);
            //零件（除无版本类型，工步)
            if (EntityUtil.checkItem(id.getObjectType())) {
                ItemEntity itemEntity = itemRevisionFactory.create().getLeftObjectByRightObject(id.getUid(), id.getObjectType());
                changeOwner(itemEntity, req,entityManager);
            }
            childNameList.add(revEntity.getObjectName());
        });
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("childNameList",childNameList);
        processParam(paramMap,req);
        sendMsg(MsgConstant.ASSIGN_PERSON, Collections.singletonList(req.getOwnerId()),paramMap);
        return true;
    }

    default void processParam(Map<String,Object> paramMap,TaskAppointReq req){
        if(StrUtil.isEmpty(req.getRootUid())){
            paramMap.put("rootUrl","#");
            paramMap.put("rootObjectName","");
            return;
        }
        AppConfig appConfig = SpringUtil.getBean(AppConfig.class);
        StringBuilder rootUrl = new StringBuilder();
        rootUrl.append(appConfig.getFeBaseUrl());
        rootUrl.append("/?").append("lang=cn");
        rootUrl.append("&tagName=").append(appConfig.getAppName());
        rootUrl.append("&appCode=").append(appConfig.getAppCode());
        rootUrl.append("&uid=").append(req.getRootUid());
        rootUrl.append("&objectType=").append(req.getRootObjectType());
        paramMap.put("rootUrl",rootUrl.toString());
        paramMap.put("rootObjectName",req.getRootObjectName());

    }

    @Transactional
    default void changeOwner(WorkspaceObjectEntity entity,TaskAppointReq req,EntityManager entityManager) {
        entity.setOwnerId(req.getOwnerId());
        entity.setOwnerName(req.getOwnerName());
        entityManager.persist(entity);
    }

    /**
     *  同时校验零件与版本的编辑权限
     * @param ids
     * @author: 拓凯
     * @time: 2022/7/20
     * @return: {@link boolean}
     */
    default boolean checkItemAndRev(ValidList<IdRequest> ids) {
        StringBuilder tipBuilder = new StringBuilder();
        ItemRevisionFactory itemRevisionFactory = SpringUtil.getBean(ItemRevisionFactory.class);
        ids.stream().collect(Collectors.groupingBy(IdRequest::getObjectType))
                .forEach((objectType, values) -> {
                    //零件（除无版本类型，工步)
                    if (EntityUtil.checkItem(objectType)) {
                        Map<String, WorkspaceObjectEntity> map = itemRevisionFactory.create().getLeftObjectMap(values.stream().map(IdRequest::getUid).collect(Collectors.toList()), objectType);
                        try {
                            verifyAuthorityList(map.values().stream().collect(Collectors.toList()), OperatorEnum.Write,DataTypeEnum.CURRENT_DATA_ITEM);
                        } catch (TipServiceException e) {
                            tipBuilder.append(e.getMessage());
                        }
                    }
                    //版本
                    List<WorkspaceObjectEntity> byIds = EntityUtil.getByIds(objectType, values.stream().map(IdRequest::getUid).collect(Collectors.toList()));
                    try {
                        verifyAuthorityList(byIds, OperatorEnum.Write,DataTypeEnum.CURRENT_DATA_ITEMREVISION);
                    } catch (TipServiceException e) {
                        tipBuilder.append(e.getMessage());
                    }
                });
        this.tipBack(tipBuilder,"dispatchData");
        return true;
    }

    @TimeLog
    default <T extends BOMNodeResp> T findOne(Class<T> nodeRespType, FindReq req, AppNameEnum appNameEnum){
        WorkspaceObjectEntity objectEntity = EntityUtil.getById(req.getObjectType(), req.getUid());
        if(ObjectUtil.isNull(objectEntity)){
            logger.error("数据不存在");
            return null;
        }
        BOMView bomView = SpringUtil.getBean(BOMViewFactory.class).create();
        boolean isPrecise = bomView.checkPrecise(appNameEnum);
        Class<?> entityClass = EntityUtil.getEntityClass(req.getObjectType());
        ItemEntity itemEntity = null;
        if (ItemRevisionEntity.class.isAssignableFrom(entityClass)){
            ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
            itemEntity = itemRevision.getLeftObjectByRightObject(req.getUid(), req.getObjectType());
            if(ObjectUtil.isNull(itemEntity)){
                logger.error("数据不存在");
                return null;
            }
        }else if (ItemEntity.class.isAssignableFrom(entityClass)){
            //如果使用Item查询，默认为非精确bom
            isPrecise = false;
            itemEntity = (ItemEntity)objectEntity;
        }
        T bomNodeResp = ReflectUtil.newInstance(nodeRespType);
        if(StrUtil.isNotBlank(req.getBomId())){
            WorkspaceObjectEntity nodeEntity = EntityUtil.getById(new BOMNodeEntity().getObjectType(), req.getBomId());
            if(ObjectUtil.isNotEmpty(nodeEntity)) {
                BeanUtil.copyPropertiesIgnoreNull(nodeEntity, bomNodeResp);
            }else{
                bomNodeResp.setParentItem("0");
                bomNodeResp.setBomView(appNameEnum.name());
                bomNodeResp.setChildItem(req.getUid());
                bomNodeResp.setChildItemType(req.getObjectType());
            }
        }else{
            bomNodeResp.setParentItem("0");
            bomNodeResp.setBomView(appNameEnum.name());
            bomNodeResp.setChildItem(req.getUid());
            bomNodeResp.setChildItemType(req.getObjectType());
        }
        if(ObjectUtil.isNotEmpty(itemEntity)) {
            //精确bom
            if (isPrecise) {
                ItemRevisionEntity itemRevisionEntity = (ItemRevisionEntity) objectEntity;
                bomNodeResp.setChildItemRevId(itemRevisionEntity.getRevisionId());
                WorkspaceObjectResp resp = EntityUtil.entityToResp(itemRevisionEntity);
                processNodeProperty(bomNodeResp, resp);
            } else {
                //非精确bom，Item获取最新的版本
                Item item = SpringUtil.getBean(ItemFactory.class).create();
                WorkspaceObjectEntity lastVersion = item.getLastVersion(itemEntity.getUid(), itemEntity.getObjectType());
                bomNodeResp.setChildItemRevId(((ItemRevisionEntity) lastVersion).getRevisionId());
                WorkspaceObjectResp resp = EntityUtil.entityToResp(lastVersion);
                processNodeProperty(bomNodeResp, resp);
            }
        }else{
            WorkspaceObjectResp resp = EntityUtil.entityToResp(objectEntity);
            processNodeProperty(bomNodeResp, resp);
        }
        boolean child = findChild(bomNodeResp.getChildItem(), bomNodeResp.getChildItemType());
        bomNodeResp.setHasChildren(child);
        return bomNodeResp;
    }

    //同步父数据
    default void fullTaskTime(int flag,String uid,String objectType) {
        try {
            List<BOMNodeEntity>  nodeList= findChildByObjectType(uid, objectType);
            if(CollUtil.isEmpty(nodeList)){
                return;
            }
            for (BOMNodeEntity bomNode : nodeList) {
                updateTaskTime(bomNode.getParentItem(), bomNode.getParentItemType());
            }
        }catch (Exception e){
            log.warn("工时更新失败",e);
            log.error("fullTaskTime 工时更新失败，uid={},objectType={}",uid,objectType);
        }
    }

    /**
     * 处理当前数据
     * @param uid
     * @param objectType
     */
    default void fullSelfTaskTime(String uid,String objectType) {
        ItemRevisionEntity entity = EntityUtil.getById(objectType, uid);
        if(ObjectUtil.isNull(entity)){
            return;
        }
        ItemEntity item = SpringUtil.getBean(ItemRevisionFactory.class).create().getLeftObjectByRightObject(uid, objectType);
        if (ObjectUtil.isNull(item)) {
            return;
        }
        updateSelfTaskTime(item.getUid(),item.getObjectType());
        updateTaskTime(item.getUid(),item.getObjectType());
    }

    default void updateSelfTaskTime(String uid,String objectType){
        List<BOMNodeEntity> childNodeList = childListSingle(EntityUtil.getObjectType(BOMNodeEntity.class),uid, objectType, null);
        int taskTime = 0;
        if(CollUtil.isNotEmpty(childNodeList)){
            taskTime = childNodeList.stream().mapToInt(node ->
                    (ObjectUtil.isNull(node.getTaskTime()) ? 0 : node.getTaskTime())
                            * (ObjectUtil.isNull(node.getQuantity()) ? 1 : node.getQuantity().intValue())).sum();
        }
        log.info("总工时计算，uid={},objectType={},taskTime={}",uid,objectType,taskTime);
        List<BOMNodeEntity>  nodeList= findChildByObjectType(uid, objectType);
        if(CollUtil.isEmpty(nodeList)){
            return;
        }
        for (BOMNodeEntity bomNode : nodeList) {
            int nowTaskTime = ObjectUtil.isNull(bomNode.getManualTaskTime())?0:bomNode.getManualTaskTime()+taskTime;
            if(Integer.valueOf(nowTaskTime).equals(bomNode.getTaskTime())){
                continue;
            }
            bomNode.setTaskTime(Integer.valueOf(nowTaskTime));
            EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
            entityManager.merge(bomNode);
        }
    }

    default void updateTaskTime(String uid,String objectType){
        List<BOMNodeEntity>  nodeList= findChildByObjectType(uid, objectType);
        if(CollUtil.isEmpty(nodeList)){
            return;
        }
        for (BOMNodeEntity bomNode : nodeList) {
            updateSelfTaskTime(bomNode.getChildItem(),bomNode.getChildItemType());
            updateTaskTime(bomNode.getParentItem(),bomNode.getParentItemType());
        }
    }



    default List<BOMNodeEntity> findChildByObjectType(String uid,String objectType){
        List<Pair<String, Object>> params = Arrays.asList(Pair.of(
                BOMNodeEntity.CHILD_ITEM, uid),
                Pair.of(BOMNodeEntity.CHILD_ITEM_TYPE, objectType)
//                ,Pair.of(BOMNodeEntity.BOM_VIEW, appNameEnum.name())
        );
        List<BOMNodeEntity> nodeList = EntityUtil.getDynamicEqQuery(
                BOMNodeEntity.class, params).fetch();
        return nodeList;
    }

    /**
     * 获取BOM行是否存在子集
     * @param
     * @author: 薛锦龙
     * @time: 2022/10/21
     * @return: {@link boolean}
     */
    default boolean findChild(String childItem,String childItemType){
        List<Triple<String,Ops,Object>> triples = Arrays.asList(
                Triple.of(BOMNodeEntity.PARENT_ITEM,Ops.EQ,childItem),
                Triple.of(BOMNodeEntity.PARENT_ITEM_TYPE,Ops.EQ,childItemType)
        );
        List<WorkspaceObjectEntity> fetch = EntityUtil.getDynamicQuery(new BOMNodeEntity().getObjectType(), triples).fetch();
        if (CollUtil.isEmpty(fetch)){
            return false;
        }
        return true;
    }

}
