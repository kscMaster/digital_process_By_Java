package com.nancal.msgbom.service.impl;


import cn.hutool.core.util.StrUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.model.entity.Gte4MfgOperationEntity;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IGte4MfgOperationDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;


@Service
public class Gte4MfgOperationDomainServiceImpl implements IGte4MfgOperationDomainService {

    @Autowired
    private IBOMNodeDomainService ibomNodeDomainService;
    @Autowired
    private Gte4MfgStepDomainServiceImpl stepDomainService;
    @Autowired
    private DictUtil dictUtil;


    /**
     * 创建工序对象，并创建关联的bom节点数据
     * 1、先将工序对象保存到数据库中 gte4mfg_operation（工序表） master_r_l (关系表) gte4mfg_operation_revision（工序版本表）
     * 2、再调用新增bom节点方法，新增bom节点  bomnode（bom节点表）
     *
     * @param req
     * @author: 拓凯
     * @time: 2022/6/7
     * @return: {@link BusinessObjectResp}
     */
    @Transactional
    @Override
    public WorkspaceObjectResp saveBomReq(BusinessObjectReq req) {
        Gte4MfgOperationBomReq operationBomReq = (Gte4MfgOperationBomReq) req;
        BOMNodeReq nodeReq = operationBomReq.getBomNodeReq();
        Gte4MfgOperationRevisionReq operationRevisionReq = operationBomReq.getOperationRevisionReq();
        String objectType = StrUtil.blankToDefault(operationRevisionReq.getObjectType(), EntityUtil.getObjectType());
//        TODO 优化后代码，未启用
        if(!ibomNodeDomainService.checkBomStructure(DictConstant.GTE4_MFGPLANTPR_BOMSTRUCTURE,nodeReq.getParentItemType(),objectType)){
            throw new ServiceException(ErrorCode.E_12, "工序类型不能挂在当前类型下");
        }
//        if (!(new Gte4MfgProcessEntity().getObjectType().equalsIgnoreCase(nodeReq.getParentItemType()))&&
//                !(new Gte4MfgOperationEntity().getObjectType().equalsIgnoreCase(nodeReq.getParentItemType()))) {
//            throw new ServiceException(ErrorCode.E_12, "工序类型只能在工艺规程下新建！");
//        }
        //判断是否可以添加，取决于父级的是否具有编辑权限
        IGte4MfgOperationDomainService.super.checkParentRevEidt(nodeReq);
        WorkspaceObjectResp save = IGte4MfgOperationDomainService.super.save(operationRevisionReq);
        nodeReq.setChildItem(save.getUid());
        nodeReq.setChildItemType(new Gte4MfgOperationEntity().getObjectType());
        //维护手动工时
        nodeReq.setManualTaskTime(operationBomReq.getOperationRevisionReq().getGte4TaskTime());
        ibomNodeDomainService.createNode(nodeReq, AppNameEnum.MSGBOM);
        //调用维护工时的方法
        ibomNodeDomainService.fullTaskTime(1,save.getRightObject(),save.getRightObjectType());
        return save;
    }


    /**
     * 克隆工序，以及克隆工序底下所有的工步对象
     *
     * @param req
     * @author: 拓凯
     * @time: 2022/7/11
     * @return: {@link BusinessObjectResp}
     */
    @Override
    @Transactional
    public BusinessObjectResp cloneOperation(Gte4MfgOperationCloneReq req) {
        //1.先根据入参新建一个新的工序，拿到工序uid
        WorkspaceObjectResp operation = IGte4MfgOperationDomainService.super.save(req.getOperationRevisionReq());
        //2.再根据cloneId查询出要克隆的工序在bomNode中所有的子级（工步+工艺资源）
        ibomNodeDomainService.cloneBomNode(req.getCloneType(),req.getCloneId(),operation.getObjectType(),
                operation.getUid(),operation.getRightObject(),AppNameEnum.MSGBOM,new HashMap<>());

//        List<BOMNodeEntity> childList = new ArrayList<>();
//        ibomNodeDomainService.childListSingle(req.getCloneId(), req.getCloneType(), AppNameEnum.MSGBOM,childList);
//        //3.调用字典配置判断该对象底下的对象是克隆还是引用
//        List<DictItemVo> codeValueList = dictUtil.getCodeValueList(DictConstant.GTE4_MFGOPERATION_CLONESTRUCTURE);
//        Map<String, String> map = codeValueList.stream().collect(Collectors.toMap(DictItemVo::getCode, DictItemVo::getValue, (key1, key2) -> key2));
//        //4.再创建新的bomNode数据，用工序uid替换parentItem
//        for (BOMNodeEntity bomNode : childList) {
//            BOMNodeEntity nodeEntity = new BOMNodeEntity();
//            //引用
//            if (map.get(bomNode.getChildItemType()).equals(CloneConstant.RELATION)) {
//                BeanUtil.copyPropertiesIgnoreNull(bomNode, nodeEntity);
//            }
//            //克隆
//            if (map.get(bomNode.getChildItemType()).equals(CloneConstant.CLONE)) {
//                Gte4MfgStepCloneReq stepCloneReq = new Gte4MfgStepCloneReq();
//                stepCloneReq.setCloneId(bomNode.getChildItem());
//                stepCloneReq.setCloneType(bomNode.getChildItemType());
//                WorkspaceObjectEntity entity = EntityUtil.getById(bomNode.getChildItemType(), bomNode.getChildItem());
//                Gte4MfgStepReq stepReq = new Gte4MfgStepReq();
//                BeanUtil.copyPropertiesIgnoreNull(entity, stepReq);
//                stepCloneReq.setStepReq(stepReq);
//                //调用克隆工步方法
//                WorkspaceObjectResp step = (WorkspaceObjectResp) stepDomainService.cloneStep(stepCloneReq);
//                BeanUtil.copyPropertiesIgnoreNull(bomNode, nodeEntity);
//                nodeEntity.setChildItem(step.getUid());
//                nodeEntity.setChildItemType(step.getObjectType());
//            }
//            stepDomainService.installBomNode(nodeEntity, operation.getUid(), operation.getRightObject());
//        }
        return operation;
    }

}
