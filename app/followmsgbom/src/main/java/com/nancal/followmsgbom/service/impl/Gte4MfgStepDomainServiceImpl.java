package com.nancal.followmsgbom.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.CoderSetUtil;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.exception.TipServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.common.utils.MessageSourceUtil;
import com.nancal.model.entity.BOMNodeEntity;
import com.nancal.model.entity.Gte4MfgOperationEntity;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IGte4MfgOperationDomainService;
import com.nancal.service.service.IGte4MfgStepDomainService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

@Service
public class Gte4MfgStepDomainServiceImpl implements IGte4MfgStepDomainService {
    @Autowired
    private IGte4MfgOperationDomainService service;
    @Autowired
    private IBOMNodeDomainService ibomNodeDomainService;
    @Autowired
    private BOMNodeDomainServiceImpl bomNodeDomainService;
    @Autowired
    private DictUtil dictUtil;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private EntityManager entityManager;


    @Override
    public WorkspaceObjectResp saveBomReq(BusinessObjectReq req) {
        Gte4MfgStepBomReq gte4MfgStepBomReq= (Gte4MfgStepBomReq)req;
        if (!(new Gte4MfgOperationEntity().getObjectType().equalsIgnoreCase(gte4MfgStepBomReq.getBomNodeReq().getParentItemType()))) {
            throw new ServiceException(ErrorCode.E_12, "工步只能在工序下新建！");
        }
        //校验父级权限
        service.checkParentRevEidt(gte4MfgStepBomReq.getBomNodeReq());
        WorkspaceObjectResp save = IGte4MfgStepDomainService.super.save(gte4MfgStepBomReq.getStepReq());
        gte4MfgStepBomReq.getBomNodeReq().setChildItemType(save.getObjectType());
        gte4MfgStepBomReq.getBomNodeReq().setChildItem(save.getUid());
        gte4MfgStepBomReq.getBomNodeReq().setChildItemTypeRevision(save.getObjectType());
        gte4MfgStepBomReq.getBomNodeReq().setChildItemRevision(save.getUid());
        ibomNodeDomainService.createNode(gte4MfgStepBomReq.getBomNodeReq(), AppNameEnum.FOLLOW_MSGBOM);
        return save;
    }

    @Override
    public BusinessObjectResp getObject(IdRequest id) {
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        BOMNodeResp bomNodeResp = new BOMNodeResp();
//        查询bom属性
        BOMNodeEntity bomNode = EntityUtil.getById(new BOMNodeEntity().getObjectType(), id.getUid());
        if (Objects.isNull(bomNode)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        BeanUtil.copyPropertiesIgnoreNull( bomNode,bomNodeResp);
//        查询工步对象
        WorkspaceObjectResp objectResp = (WorkspaceObjectResp)IGte4MfgStepDomainService.super.getObject(new IdRequest(bomNode.getChildItem()));
        if (Objects.isNull(objectResp)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        objBomResp.setObjectResp(objectResp);
        objBomResp.setBomNodeResp(bomNodeResp);
        dictUtil.translate(objBomResp);
        return objBomResp;
    }

    @Override
    @Transactional
    public BusinessObjectResp update(BusinessObjectReq req) {
        Gte4MfgStepBomReq stepBomReq = (Gte4MfgStepBomReq)req;
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        if (!Objects.isNull(stepBomReq.getBomNodeReq())) {
            stepBomReq.getBomNodeReq().setObjectType(new BOMNodeEntity().getObjectType());
            //调用编辑bom属性方法
            BOMNodeResp bomNodeResp =(BOMNodeResp)bomNodeDomainService.update(stepBomReq.getBomNodeReq());
            objBomResp.setBomNodeResp(bomNodeResp);
        }
        if (!Objects.isNull(stepBomReq.getStepReq())) {
            //调用编辑工步属性方法
            WorkspaceObjectResp update = (WorkspaceObjectResp)IGte4MfgStepDomainService.super.update(stepBomReq.getStepReq());
            objBomResp.setObjectResp(update);
        }
        return objBomResp;
    }

    @Override
    public void check(IdRequest id) {
    }

    /**
     *  克隆工步
     *
     * @param req
     * @author: 拓凯
     * @time: 2022/7/8
     * @return: {@link BusinessObjectResp}
     */
    @Override
    @Transactional
    public BusinessObjectResp cloneStep(Gte4MfgStepCloneReq req) {
        //1.先根据入参新建一个新的工步，拿到uid
        String itemId = SpringUtil.getBean(CoderSetUtil.class).getOneCoderByObjectType(EntityUtil.getObjectType());
        req.getStepReq().setItemId(itemId);
        WorkspaceObjectResp step = IGte4MfgStepDomainService.super.save(req.getStepReq());
        //2.再根据cloneId查询出要克隆的工步在bomNode中所有的子级（工艺资源）
        ibomNodeDomainService.cloneBomNode(req.getCloneType(),req.getCloneId(),step.getObjectType(),
                step.getUid(),step.getRightObject(),AppNameEnum.MSGBOM,new HashMap<>());
        return step;
    }

    /**
     * 升版工步等同（克隆工步+替换当前bom行的工步id）
     * @param req
     * @author: 拓凯
     * @time: 2022/8/1
     * @return: {@link BusinessObjectResp}
     */
    @Transactional
    public BusinessObjectResp stepUpgrade(Gte4MfgStepUpgradeReq req){
        Gte4MfgStepCloneReq cloneReq = new Gte4MfgStepCloneReq();
        cloneReq.setCloneId(req.getCloneId());
        cloneReq.setCloneType(req.getCloneType());
        cloneReq.setStepReq(req.getStepReq());
        BusinessObjectResp cloneResp = this.cloneStep(cloneReq);
        //根据bomId替换当前的工步id
        BOMNodeEntity bomNode = EntityUtil.getById(new BOMNodeEntity().getObjectType(), req.getBomId());
        if (ObjectUtil.isNull(bomNode)) {
            throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("bomIsNull",req.getBomId()));
        }
        bomNode.setChildItem(cloneResp.getUid());
        entityManager.persist(bomNode);
        return cloneResp;
    }
}
