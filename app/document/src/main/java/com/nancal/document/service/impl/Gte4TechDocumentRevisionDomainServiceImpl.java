package com.nancal.document.service.impl;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.model.common.ValidList;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.annotation.TimeLog;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.DataTypeEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.OperatorEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.exception.TipServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.MessageSourceUtil;
import com.nancal.model.entity.*;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.service.IGte4TechDocumentRevisionDomainService;
import com.nancal.service.service.IWorkspaceObjectDomainService;
import com.querydsl.core.types.Ops;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class Gte4TechDocumentRevisionDomainServiceImpl implements IGte4TechDocumentRevisionDomainService {

    @Autowired
    private BOMNodeDomainServiceImpl bomNodeDomainService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ItemRevisionFactory itemRevisionFactory;


    @TimeLog
    @Override
    public BusinessObjectResp getObject(MfgCheckReq req) {
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        BOMNodeResp bomNodeResp = new BOMNodeResp();
        IdRequest idRequest = new IdRequest();
        if (!req.getBomId().equals("0")) {
            //子节点查询bom属性
            BOMNodeEntity bomNode = EntityUtil.getById(new BOMNodeEntity().getObjectType(), req.getBomId());
            if (Objects.isNull(bomNode)) {
                throw new ServiceException(ErrorCode.E_12);
            }
            BeanUtil.copyPropertiesIgnoreNull(bomNode, bomNodeResp);
            //根据零件uid获取最大版本最新激活的版本uid
            WorkspaceObjectEntity lastVersion = SpringUtil.getBean(ItemFactory.class).create().getLastVersion(bomNode.getChildItem(), bomNode.getChildItemType());
            if (Objects.isNull(lastVersion)) {
                throw new ServiceException(ErrorCode.E_12);
            }
            idRequest.setUid(lastVersion.getUid());
        } else {
            //根节点根据版本uid查询
            idRequest.setUid(req.getId());
        }
        Gte4TechDocumentRevisionResp objectResp = (Gte4TechDocumentRevisionResp) IGte4TechDocumentRevisionDomainService.super.getObject(idRequest);
        if (Objects.isNull(objectResp)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        objectResp.setIsAppoint(isAppoint(req));
        objBomResp.setObjectResp(objectResp);
        objBomResp.setBomNodeResp(bomNodeResp);
        return objBomResp;
    }

    /**
     * 判断当前文档有没有被其他文档所引用（及子类）
     * @param req
     * @author: 拓凯
     * @time: 2022/7/19
     * @return: {@link boolean}
     */
    private boolean isAppoint(MfgCheckReq req) {
        //根据右id和类型获取左id和类型
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        ItemEntity item = itemRevision.getLeftObjectByRightObject(req.getId(), req.getType());
        List<Triple<String, Ops, Object>> paramList = Arrays.asList(
                Triple.of(BOMNodeEntity.DEL_FLAG, Ops.EQ, false),
                Triple.of(BOMNodeEntity.CHILD_ITEM, Ops.EQ, item.getUid()),
                Triple.of(BOMNodeEntity.CHILD_ITEM_TYPE, Ops.EQ, item.getObjectType())
        );
        BOMNodeEntity first = (BOMNodeEntity)EntityUtil.getDynamicQuery(new BOMNodeEntity().getObjectType(), paramList).fetchFirst();
        if (Objects.isNull(first)) {
            return false;
        }
        return true;
    }


    @Override
    @Transactional
    public BusinessObjectResp update(BusinessObjectReq req) {
        Gte4TechDocumentBomReq documentBomReq = (Gte4TechDocumentBomReq) req;
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        if (!Objects.isNull(documentBomReq.getBomNodeReq())) {
            documentBomReq.getBomNodeReq().setObjectType(new BOMNodeEntity().getObjectType());
            //调用编辑bom属性方法
            BOMNodeResp bomNodeResp = (BOMNodeResp) bomNodeDomainService.update(documentBomReq.getBomNodeReq());
            objBomResp.setBomNodeResp(bomNodeResp);
        }
        if (!Objects.isNull(documentBomReq.getTechDocumentRevisionReq())) {
            //调用父类编辑属性方法
            WorkspaceObjectResp update = (WorkspaceObjectResp) IGte4TechDocumentRevisionDomainService.super.update(documentBomReq.getTechDocumentRevisionReq());
            objBomResp.setObjectResp(update);
        }
        return objBomResp;
    }

    @Transactional
    @Override
    public BusinessObjectResp updateRichText(Gte4MfgStepContentReq req) {
        WorkspaceObjectEntity entity = EntityUtil.getById(req.getObjectType(), req.getUid());
        //校验编辑权限
        IGte4TechDocumentRevisionDomainService.super.verifyAuthority(entity, OperatorEnum.Write,"updateError",entity.getObjectName());
        DocumentRevisionEntity documentRevisionEntity = (DocumentRevisionEntity)entity;
        documentRevisionEntity.setDocumentContent(req.getStepContent());
        entityManager.persist(documentRevisionEntity);
        Gte4TechDocumentRevisionResp resp = new Gte4TechDocumentRevisionResp();
        BeanUtil.copyPropertiesIgnoreNull(documentRevisionEntity,resp);
        return resp;
    }

    /**
     *  将所选的版本及零件的ownerId和ownerName替换成指定的用户
     * @param req
     * @return
     */
    @Transactional
    @Override
    public boolean taskAppoint(TaskAppointReq req) {
        req.getIds().forEach(id->{
            //版本
            WorkspaceObjectEntity revEntity = EntityUtil.getById(id.getObjectType(), id.getUid());
            changeOwner(revEntity, req);
            //零件
            ItemEntity itemEntity = itemRevisionFactory.create().getLeftObjectByRightObject(id.getUid(), id.getObjectType());
            changeOwner(itemEntity, req);
        });
        return true;
    }

    private void changeOwner(WorkspaceObjectEntity entity,TaskAppointReq req) {
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
    @Override
    public boolean checkItemAndRev(ValidList<IdRequest> ids) {
        StringBuilder tipBuilder = new StringBuilder();
        ids.stream().collect(Collectors.groupingBy(IdRequest::getObjectType))
                .forEach((objectType, values) -> {
                    //零件
                    Map<String, WorkspaceObjectEntity> map = itemRevisionFactory.create().getLeftObjectMap(values.stream().map(IdRequest::getUid).collect(Collectors.toList()), objectType);
                    try {
                        verifyAuthorityList(map.values().stream().collect(Collectors.toList()), OperatorEnum.Write, DataTypeEnum.CURRENT_DATA_ITEM);
                    }catch (TipServiceException e){
                        tipBuilder.append(e.getMessage());
                    }
                    //版本
                    List<WorkspaceObjectEntity> byIds = EntityUtil.getByIds(objectType, values.stream().map(IdRequest::getUid).collect(Collectors.toList()));
                    try {
                        verifyAuthorityList(byIds, OperatorEnum.Write,DataTypeEnum.CURRENT_DATA_ITEMREVISION);
                    }catch (TipServiceException e){
                        tipBuilder.append(e.getMessage());
                    }
                });
        this.tipBack(tipBuilder,"documentDispatchData");
        return true;
    }

    @Override
    public boolean upgradeCheck(IdRequest id) {
        WorkspaceObjectEntity entity = EntityUtil.getById(id.getObjectType(), id.getUid());
        if(ObjectUtil.isNull(entity)){
            throw new ServiceException(ErrorCode.E_12);
        }
        //校验创建人权限
        verifyAuthority(entity,OperatorEnum.Upgrade,"upgradeData",entity.getObjectName());
        return true;
    }


}
