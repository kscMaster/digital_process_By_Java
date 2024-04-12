package com.nancal.msgbom.service.impl;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.CoderSetUtil;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.base.IdRequest;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.exception.TipServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.common.utils.MessageSourceUtil;
import com.nancal.model.entity.BOMNodeEntity;
import com.nancal.model.entity.Gte4MfgStepEntity;
import com.nancal.service.service.IBOMNodeDomainService;
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
    private IBOMNodeDomainService ibomNodeDomainService;
    @Autowired
    private IItemDomainServiceDomainServiceImpl itemDomainService;
    @Autowired
    private IBOMNodeDomainService bomNodeDomainService;
    @Autowired
    private DictUtil dictUtil;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private EntityManager entityManager;

    /**
     *  创建工步对象，并创建关联的bom节点数据
     *  1、先将工步对象保存到数据库中 gte4mfg_step（工步表） 此时工步是一个最小的单元，没有版本
     *  2、再调用新增bom节点方法，新增bom节点  bomnode（bom节点表）
     *
     * @param req
     * @author: 拓凯
     * @time: 2022/6/7
     * @return: {@link BusinessObjectResp}
     */
    @Transactional
    @Override
    public WorkspaceObjectResp saveBomReq(BusinessObjectReq req) {
        Gte4MfgStepBomReq stepBomReq = (Gte4MfgStepBomReq) req;
        Gte4MfgStepReq stepReq = stepBomReq.getStepReq();
        BOMNodeReq nodeReq = stepBomReq.getBomNodeReq();
        String objectType = StrUtil.blankToDefault(stepReq.getObjectType(), EntityUtil.getObjectType());
        if(!ibomNodeDomainService.checkBomStructure(DictConstant.GTE4_MFGPLANTPR_BOMSTRUCTURE,nodeReq.getParentItemType(), objectType)){
            throw new ServiceException(ErrorCode.E_12, "工步只能在工序下新建！");
        }
        //判断是否可以添加，取决于父级的是否具有编辑权限
        itemDomainService.checkParentRevEidt(nodeReq);
        String itemId = SpringUtil.getBean(CoderSetUtil.class).getOneCoderByObjectType(objectType);
        stepReq.setItemId(itemId);
        WorkspaceObjectResp save = IGte4MfgStepDomainService.super.save(stepReq);
        nodeReq.setChildItem(save.getUid());
        nodeReq.setChildItemType(new Gte4MfgStepEntity().getObjectType());
        //维护手动工时
        nodeReq.setManualTaskTime(stepReq.getGte4TaskTime());
        ibomNodeDomainService.createNode(nodeReq, AppNameEnum.MSGBOM);
        //调用维护工时的方法
        bomNodeDomainService.fullTaskTime(1,save.getUid(),save.getObjectType());
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
//        List<BOMNodeEntity> childList = new ArrayList<>();
//        ibomNodeDomainService.childListSingle(req.getCloneId(), req.getCloneType(), AppNameEnum.MSGBOM,childList);
//        //3.调用字典配置判断该对象底下的对象是克隆还还是引用
//        List<DictItemVo> codeValueList = dictUtil.getCodeValueList(DictConstant.GTE4_MFGSTEP_CLONESTRUCTURE);
//        Map<String, String> map = codeValueList.stream().collect(Collectors.toMap(DictItemVo::getCode, DictItemVo::getValue, (key1, key2) -> key2));
//        //4.再创建新的bomNode数据，用uid替换parentItem
//        for (BOMNodeEntity bomNode : childList) {
//            //引用
//            if (map.get(bomNode.getChildItemType()).equals(CloneConstant.RELATION)) {
//                BOMNodeEntity entity = new BOMNodeEntity();
//                BeanUtil.copyPropertiesIgnoreNull(bomNode,entity);
//                installBomNode(entity, step.getUid(),null);
//            }
//        }
        return step;
    }

    /**
     * 克隆设置bom属性
     *
     * @param entity
     * @param parentItem
     * @author: 拓凯
     * @time: 2022/7/8
     * @return: {@link}
     */
    public void installBomNode(BOMNodeEntity entity,String parentItem,String parentItemRev) {
        entity.setUid(IdGeneratorUtil.generate());
        entity.setStateChangeDate(LocalDateTime.now());
        entity.setParentItem(parentItem);
        if (StringUtils.isNotBlank(parentItemRev)) {
            entity.setParentItemRev(parentItemRev);
        }
        entity.setOwnerId(userUtils.getCurrentUserId());
        entity.setOwnerName(userUtils.getCurrentUserName());
        entityManager.persist(entity);
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
        //调用维护工时的方法
        bomNodeDomainService.fullTaskTime(5,cloneResp.getUid(),new Gte4MfgStepEntity().getObjectType());
        return cloneResp;
    }


}
