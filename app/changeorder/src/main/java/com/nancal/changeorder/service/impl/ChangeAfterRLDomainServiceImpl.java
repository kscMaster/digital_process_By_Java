package com.nancal.changeorder.service.impl;

import com.nancal.api.model.ChangeAfterRLReq;
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
import com.nancal.service.service.IChangeAfterRLDomainService;
import com.querydsl.core.types.Ops;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Service
public class ChangeAfterRLDomainServiceImpl implements IChangeAfterRLDomainService {

    @Autowired
    private ItemRevisionFactory itemRevisionFactory;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(List<ChangeAfterRLReq> reqs) {
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
            //校验重复添加数据(同一个版本数据，工作中，仅能粘贴到一个更改单)
            List<Triple<String, Ops, Object>> paramsList = Arrays.asList(
                    Triple.of(ChangeAfterRLEntity.RIGHT_OBJECT, Ops.EQ, req.getRightObject()));
            WorkspaceObjectEntity fetchOne = EntityUtil.getDynamicQuery(new ChangeAfterRLEntity().getObjectType(), paramsList).fetchOne();

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
            //校验更改前的数据是否工作中的状态
            if (!itemRevision.getLifeCycleState().equals(LifeCycleStateEnum.Working.name())) {
                throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("UNWorking", itemRevision.getObjectName()));
            }
            //处理版本号字段
            req.setItemRevId(itemRevision.getRevisionId());
            //根据版本获取零件
            ItemEntity itemEntity = itemRevisionFactory.create().getLeftObjectByRightObject(req.getRightObject(), req.getRightObjectType());
            if (Objects.isNull(itemEntity)) {
                throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("objIsNull", req.getRightObject()));
            }
            req.setItemUid(itemEntity.getUid());
            req.setObjectName(entity.getObjectName());
            IChangeAfterRLDomainService.super.save(req);
        });
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAfterRL(CompareReq req) {
        //根据左右id确定出一条关系数据的id
        List<Triple<String, Ops, Object>> paramsList = Arrays.asList(
                Triple.of(ChangeAfterRLEntity.LEFT_OBJECT, Ops.EQ, req.getLeftObject()),
                Triple.of(ChangeAfterRLEntity.RIGHT_OBJECT, Ops.EQ, req.getRightObject()));
        WorkspaceObjectEntity one = EntityUtil.getDynamicQuery(new ChangeAfterRLEntity().getObjectType(), paramsList).fetchOne();
        if (Objects.isNull(one)) {
            throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("dataExist"));
        }
        IChangeAfterRLDomainService.super.deleteObject(new IdRequest(one.getUid(),new ChangeAfterRLEntity().getObjectType()));
        return true;
    }


}
