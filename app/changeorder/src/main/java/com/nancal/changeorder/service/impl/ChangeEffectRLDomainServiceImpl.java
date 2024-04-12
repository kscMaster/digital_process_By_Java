package com.nancal.changeorder.service.impl;

import com.nancal.api.model.ChangeEffectRLReq;
import com.nancal.api.model.CompareReq;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.OperatorEnum;
import com.nancal.common.exception.TipServiceException;
import com.nancal.common.utils.MessageSourceUtil;
import com.nancal.model.entity.*;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.service.IChangeEffectRLDomainService;
import com.querydsl.core.types.Ops;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class ChangeEffectRLDomainServiceImpl implements IChangeEffectRLDomainService {

    @Autowired
    private ItemRevisionFactory itemRevisionFactory;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(List<ChangeEffectRLReq> reqs) {
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
            //校验是重复添加数据
            List<Triple<String, Ops, Object>> paramsList = Arrays.asList(
                    Triple.of(ChangeEffectRLEntity.RIGHT_OBJECT, Ops.EQ, req.getRightObject()));
            long count = EntityUtil.getDynamicQuery(new ChangeEffectRLEntity().getObjectType(), paramsList).fetchCount();
            //处理版本号字段
            ItemRevisionEntity itemRevision = EntityUtil.getById(req.getRightObjectType(), req.getRightObject());
            if (count!=0) {
                throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("dataExist2", itemRevision.getObjectName(),entity.getObjectName()));
            }
            if (Objects.isNull(itemRevision)) {
                throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("objIsNull", req.getRightObject()));
            }
            req.setItemRevId(itemRevision.getRevisionId());
            //根据版本获取零件
            ItemEntity itemEntity = itemRevisionFactory.create().getLeftObjectByRightObject(req.getRightObject(), req.getRightObjectType());
            if (Objects.isNull(itemEntity)) {
                throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("objIsNull", req.getRightObject()));
            }
            req.setItemUid(itemEntity.getUid());
            req.setObjectName(entity.getObjectName());
            IChangeEffectRLDomainService.super.save(req);
        });
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteEffectRL(CompareReq req) {
        //根据左右id确定出一条关系数据的id
        List<Triple<String, Ops, Object>> paramsList = Arrays.asList(
                Triple.of(ChangeAfterRLEntity.LEFT_OBJECT, Ops.EQ, req.getLeftObject()),
                Triple.of(ChangeAfterRLEntity.RIGHT_OBJECT, Ops.EQ, req.getRightObject()));
        WorkspaceObjectEntity one = EntityUtil.getDynamicQuery(new ChangeEffectRLEntity().getObjectType(), paramsList).fetchOne();
        if (Objects.isNull(one)) {
            throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("dataExist"));
        }
        IChangeEffectRLDomainService.super.deleteObject(new IdRequest(one.getUid(),new ChangeEffectRLEntity().getObjectType()));
        return true;
    }


}
