package com.nancal.service.service;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.annotation.TimeLog;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.LifeCycleStateEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.VersionUtil;
import com.nancal.model.entity.*;
import com.nancal.service.bo.Item;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.bo.Relation;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.factory.RelationFactory;
import com.querydsl.core.types.Ops;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public interface IGte4PartRevisionDomainService extends IItemRevisionDomainService{

    @TimeLog
    @Transactional
    default WorkspaceObjectResp generateMidBOM(FindReq id){
        String gte4MaterialRl = new Gte4MaterialRlEntity().getObjectType();
        String materialRevision = new Gte4MaterialRevisionEntity().getObjectType();
        String material = new Gte4MaterialEntity().getObjectType();
        ItemRevision revision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        Relation relation = SpringUtil.getBean(RelationFactory.class).create();
        Item item = SpringUtil.getBean(ItemFactory.class).create();
        IItemDomainService itemDomainService = SpringUtil.getBean(IItemDomainService.class);
        //获取零组件数据
        IBOMNodeDomainService ibomNodeDomainService = SpringUtil.getBean(IBOMNodeDomainService.class);
        id.setDeep(-1);
        BOPNodeViewResp viewResp = ibomNodeDomainService.find(BOPNodeViewResp.class, id, AppNameEnum.EBOM);
        //校验数据状态
        if (!viewResp.getExtraMap().get("resp_lifeCycleState").equals(LifeCycleStateEnum.Released.name())){
            throw new ServiceException(ErrorCode.FAIL,"【"+viewResp.getExtraMap().get("resp_objectName")+"】不是已发布状态！");
        }
        List<WorkspaceObjectEntity> allSequences = revision.getAllSequences(viewResp.getRevUid(), viewResp.getRevObjectType());
        long count = EntityUtil.getDynamicQuery(gte4MaterialRl, Triple.of(RenderingRLEntity.LEFT_OBJECT, Ops.IN, allSequences.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList()))).fetchCount();
        String number = count +"";
        ItemRevisionReq itemRevision = (ItemRevisionReq) ReflectUtil.newInstance(EntityUtil.getReqPackage(materialRevision));
        itemRevision.setItemId((String) viewResp.getExtraMap().get("resp_itemId")+viewResp.getExtraMap().get("resp_revisionId")+ VersionUtil.createRevisionId(number));
        itemRevision.setObjectType(material);
        itemRevision.setObjectName((String)viewResp.getExtraMap().get("resp_objectName"));
        WorkspaceObjectResp save = itemDomainService.save(itemRevision);
        WorkspaceObjectEntity lastVersion = item.getLastVersion(save.getUid(), save.getObjectType());
        WorkspaceObjectResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(lastVersion.getObjectType()));
        BeanUtil.copyPropertiesIgnoreNull(lastVersion,resp);
        //创建关系
        relation.saveRelation(viewResp.getRevUid(), viewResp.getRevObjectType(),save.getRightObject(),save.getRightObjectType());
        //递归创建子集数据
        List<CompletableFuture<Void>> all = new ArrayList<>();
        if (CollUtil.isNotEmpty(viewResp.getChildren())){
            generateMidBOMChild(viewResp.getChildren(),save);
        }
        return resp;
    }

    //当前零组件版本，父实物信息
    @Transactional
    default void generateMidBOMChild(List<BOMNodeResp> child,WorkspaceObjectResp resp ){
        String gte4MaterialRl = new Gte4MaterialRlEntity().getObjectType();
        String materialRevision = new Gte4MaterialRevisionEntity().getObjectType();
        String material = new Gte4MaterialEntity().getObjectType();
        ItemRevision revision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        IBOMNodeDomainService ibomNodeDomainService = SpringUtil.getBean(IBOMNodeDomainService.class);
        Relation relation = SpringUtil.getBean(RelationFactory.class).create();
        IItemDomainService itemDomainService = SpringUtil.getBean(IItemDomainService.class);
        for (BOMNodeResp bomNodeResp : child) {
            List<WorkspaceObjectEntity> allSequences = revision.getAllSequences(bomNodeResp.getRevUid(), bomNodeResp.getRevObjectType());
            long count = EntityUtil.getDynamicQuery(gte4MaterialRl, Triple.of(RenderingRLEntity.LEFT_OBJECT, Ops.IN, allSequences.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList()))).fetchCount();
            String number =count + "";
            ItemRevisionReq itemRevision = (ItemRevisionReq) ReflectUtil.newInstance(EntityUtil.getReqPackage(materialRevision));
            itemRevision.setItemId((String) bomNodeResp.getExtraMap().get("resp_itemId")+bomNodeResp.getExtraMap().get("resp_revisionId")+ VersionUtil.createRevisionId(number));
            itemRevision.setObjectType(material);
            itemRevision.setObjectName((String)bomNodeResp.getExtraMap().get("resp_objectName"));
            WorkspaceObjectResp save = itemDomainService.save(itemRevision);
            //创建关系
            relation.saveRelation(bomNodeResp.getRevUid(), bomNodeResp.getRevObjectType(),save.getRightObject(),save.getRightObjectType());
            //创建BOM行关联关系
            BOMNodeReq req = new BOMNodeReq();
            req.setParentItem(resp.getUid());
            req.setParentItemRev(resp.getRightObject());
            req.setParentItemType(resp.getObjectType());
            req.setChildItem(save.getUid());
            req.setChildItemType(save.getObjectType());
            BOMNodeResp node = ibomNodeDomainService.createNode(req, AppNameEnum.MIDBOM);
            if (CollUtil.isNotEmpty(bomNodeResp.getChildren())){
                //递归调用
                generateMidBOMChild(bomNodeResp.getChildren(),save);

            }
        }

    }

    /**
     * 关联板子
     * @param req
     * @author: 薛锦龙
     * @time: 2022/8/30
     * @return: {@link boolean}
     */
    @Transactional
    default boolean associatedBoards(AssociatedBoardsReq req){
        String objectType = new BOMNodeEntity().getObjectType();
        Item item = SpringUtil.getBean(ItemFactory.class).create();
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        IBOMNodeDomainService ibomNodeDomainService = SpringUtil.getBean(IBOMNodeDomainService.class);
        ibomNodeDomainService.existParent(req.getUid(), objectType,true);
        WorkspaceObjectEntity activeSequence = item.getLastVersion(req.getUid(), req.getObjectType());
        if (ObjectUtil.isEmpty(activeSequence)){
            throw new ServiceException(ErrorCode.E_12);
        }
        Gte4PartRevisionEntity partRevisionEntity = (Gte4PartRevisionEntity)activeSequence;
        partRevisionEntity.setBoardKey(req.getBoardKey());
        entityManager.merge(partRevisionEntity);
        return true;
    }


}
