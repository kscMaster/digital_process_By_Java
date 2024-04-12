package com.nancal.changeorder.service.impl;

import com.nancal.api.model.ChangeBeforeRLReq;
import com.nancal.api.model.CompareReq;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.LifeCycleStateEnum;
import com.nancal.common.enums.OperatorEnum;
import com.nancal.common.exception.TipServiceException;
import com.nancal.common.utils.MessageSourceUtil;
import com.nancal.model.entity.*;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.service.IChangeBeforeRLDomainService;
import com.querydsl.core.types.Ops;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Service
public class ChangeBeforeRLDomainServiceImpl implements IChangeBeforeRLDomainService {

    @Autowired
    private ItemRevisionFactory itemRevisionFactory;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(List<ChangeBeforeRLReq> reqs) {
        reqs.forEach(req->{
            //校验更改单是否是当前用户
            WorkspaceObjectEntity entity = EntityUtil.getById(req.getLeftObjectType(), req.getLeftObject());
            if (Objects.isNull(entity)) {
                throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("objIsNull", req.getLeftObject()));
            }
            this.verifyAuthority(entity, OperatorEnum.Write);
            //校验是否是可粘贴的对象类型
            if (!req.getRightObjectType().equals(new Gte4PartRevisionEntity().getObjectType())) {
                throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("changePasteTypeError", entity.getObjectName()));
            }
            //校验重复添加数据
            List<Triple<String, Ops, Object>> paramsList = Arrays.asList(
                    Triple.of(ChangeBeforeRLEntity.LEFT_OBJECT, Ops.EQ, req.getLeftObject()),
                    Triple.of(ChangeBeforeRLEntity.RIGHT_OBJECT, Ops.EQ, req.getRightObject()));
            WorkspaceObjectEntity fetchOne = EntityUtil.getDynamicQuery(new ChangeBeforeRLEntity().getObjectType(), paramsList).fetchOne();
            ItemRevisionEntity itemRevision = EntityUtil.getById(req.getRightObjectType(), req.getRightObject());
            if (Objects.isNull(itemRevision)) {
                throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("objIsNull", req.getRightObject()));
            }
            //在当前更改单底下粘贴重复数据
            if (!Objects.isNull(fetchOne) && fetchOne.getLeftObject()== entity.getUid()) {
                throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("dataExist2", itemRevision.getObjectName(), entity.getObjectName()));
            }
            //在其他更改单下粘贴已经粘贴过了的数据
            if (!Objects.isNull(fetchOne) && fetchOne.getLeftObject() != entity.getUid()) {
                WorkspaceObjectEntity change = EntityUtil.getById(new Gte4PartChangeEntity().getObjectType(), fetchOne.getLeftObject());
                throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("dataExist2", itemRevision.getObjectName(), change.getObjectName()));
            }
            //校验更改前的数据是否已发布的状态
            if (!itemRevision.getLifeCycleState().equals(LifeCycleStateEnum.Released.name())) {
                throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("UNReleased", itemRevision.getObjectName()));
            }
            //根据版本获取零件
            ItemEntity itemEntity = itemRevisionFactory.create().getLeftObjectByRightObject(req.getRightObject(), req.getRightObjectType());
            if (Objects.isNull(itemEntity)) {
                throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("objIsNull", req.getRightObject()));
            }
            //校验同一个更改单下只能存在一个零件的一个已发布版本
            List<Triple<String, Ops, Object>> paramsList2 = Arrays.asList(
                    Triple.of(ChangeBeforeRLEntity.LEFT_OBJECT, Ops.EQ, req.getLeftObject()),
                    Triple.of(ChangeBeforeRLEntity.ITEM_UID, Ops.EQ, itemEntity.getUid()));
            long number = EntityUtil.getDynamicQuery(new ChangeBeforeRLEntity().getObjectType(), paramsList2).fetchCount();
            if (number != 0L) {
                throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("existError"));
            }
            //处理版本号字段
            req.setItemRevId(itemRevision.getRevisionId());
            req.setItemUid(itemEntity.getUid());
            req.setObjectName(entity.getObjectName());
            IChangeBeforeRLDomainService.super.save(req);
            });
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBeforeRL(CompareReq req) {
        //根据左右id确定出一条关系数据的id
        List<Triple<String, Ops, Object>> paramsList = Arrays.asList(
                Triple.of(ChangeAfterRLEntity.LEFT_OBJECT, Ops.EQ, req.getLeftObject()),
                Triple.of(ChangeAfterRLEntity.RIGHT_OBJECT, Ops.EQ, req.getRightObject()));
        WorkspaceObjectEntity one = EntityUtil.getDynamicQuery(new ChangeBeforeRLEntity().getObjectType(), paramsList).fetchOne();
        if (Objects.isNull(one)) {
            throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("dataExist"));
        }
        IChangeBeforeRLDomainService.super.deleteObject(new IdRequest(one.getUid(),new ChangeBeforeRLEntity().getObjectType()));
        return true;
    }


}
