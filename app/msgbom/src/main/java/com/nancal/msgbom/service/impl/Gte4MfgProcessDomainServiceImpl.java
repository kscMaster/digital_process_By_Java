package com.nancal.msgbom.service.impl;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.CoderSetUtil;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.annotation.TimeLog;
import com.nancal.common.base.IdRequest;
import com.nancal.common.constants.CloneConstant;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.BOMNodeEntity;
import com.nancal.model.entity.MfgTargetRLEntity;
import com.nancal.model.entity.RelationEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.remote.vo.DictItemVo;
import com.nancal.service.bo.Item;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IGte4MfgProcessDomainService;
import com.querydsl.core.types.Ops;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class Gte4MfgProcessDomainServiceImpl implements IGte4MfgProcessDomainService {

    @Autowired
    private IBOMNodeDomainService ibomNodeDomainService;
    @Autowired
    private Gte4MfgOperationDomainServiceImpl operationDomainService;
    @Autowired
    private Gte4MfgStepDomainServiceImpl stepDomainService;
    @Autowired
    private DictUtil dictUtil;


    @Override
    public WorkspaceObjectResp save(BusinessObjectReq req) {
        WorkspaceObjectResp save = IGte4MfgProcessDomainService.super.save(req);
        //根据版本uid和版本类型查询版本对象
        WorkspaceObjectEntity entity = EntityUtil.getById(save.getRightObjectType(), save.getRightObject());
        BomEditRevisionResp objBomResp = new BomEditRevisionResp();
        WorkspaceObjectResp objectResp = new WorkspaceObjectResp();
        BOMNodeResp bomNodeResp = new BOMNodeResp();
        BeanUtil.copyPropertiesIgnoreNull(entity,objectResp);
        objectResp.setLeftObject(save.getUid());
        objectResp.setLeftObjectType(save.getObjectType());
        //添加返回根节点属性
        bomNodeResp.setParentItem("0");
        bomNodeResp.setChildItem(save.getUid());
        bomNodeResp.setChildItemType(save.getObjectType());
        bomNodeResp.setBomView(AppNameEnum.MSGBOM.name());
        objBomResp.setObjectResp(objectResp);
        objBomResp.setBomNodeResp(bomNodeResp);
        return objBomResp;
    }

    /**
     * 克隆工艺规程，以及克隆工艺规程底下所有的工序以及工步对象
     *
     * @param req
     * @author: 拓凯
     * @time: 2022/7/12
     * @return: {@link BusinessObjectResp}
     */
    @Override
    @Transactional
    @TimeLog
    public BusinessObjectResp cloneProcess(Gte4MfgProcessCloneReq req) {
        //1.先根据入参新建一个新的工艺规程，拿到工艺规程uid
        WorkspaceObjectResp process = IGte4MfgProcessDomainService.super.save(req.getProcessRevisionReq());
        //2.再根据cloneId查询出要克隆的工艺规程在bomNode中所有的子级（工序+工步+工艺资源）
        ibomNodeDomainService.cloneBomNode(req.getCloneType(),req.getCloneId(),process.getObjectType(),
                process.getUid(),process.getRightObject(),AppNameEnum.MSGBOM,new HashMap<>());
//        List<BOMNodeEntity> childList = new ArrayList<>();
//        ibomNodeDomainService.childListSingle(req.getCloneId(), req.getCloneType(), AppNameEnum.MSGBOM,childList);
//        //3.调用字典配置判断该对象底下的对象是克隆还是引用
//        List<DictItemVo> codeValueList = dictUtil.getCodeValueList(DictConstant.GTE4_MFGPROCESS_CLONESTRUCTURE);
//        Map<String, String> map = codeValueList.stream().collect(Collectors.toMap(DictItemVo::getCode, DictItemVo::getValue, (key1, key2) -> key2));
//        //4.再创建新的bomNode数据，用工艺规程uid替换parentItem
//        for (BOMNodeEntity bomNode : childList) {
//            //克隆
//            if (map.get(bomNode.getChildItemType()).equals(CloneConstant.CLONE)) {
//                //调用克隆工序方法，工艺规程下只有工序，所以都支持克隆
//                Gte4MfgOperationCloneReq operationCloneReq = new Gte4MfgOperationCloneReq();
//                operationCloneReq.setCloneId(bomNode.getChildItem());
//                operationCloneReq.setCloneType(bomNode.getChildItemType());
//                //根据零件id和类型获取最新的版本信息
//                Item item = SpringUtil.getBean(ItemFactory.class).create();
//                WorkspaceObjectEntity entity = item.getLastVersion(bomNode.getChildItem(), bomNode.getChildItemType());
//                Gte4MfgOperationRevisionReq operationReq = new Gte4MfgOperationRevisionReq();
//                BeanUtil.copyPropertiesIgnoreNull(entity, operationReq);
//                operationReq.setObjectType(EntityUtil.getObjectTypeByRevisionType(entity.getObjectType()));
//                operationCloneReq.setOperationRevisionReq(operationReq);
//                WorkspaceObjectResp operation = (WorkspaceObjectResp) operationDomainService.cloneOperation(operationCloneReq);
//                //创建并修改bomNode节点数据
//                BOMNodeEntity nodeEntity = new BOMNodeEntity();
//                BeanUtil.copyPropertiesIgnoreNull(bomNode, nodeEntity);
//                nodeEntity.setChildItem(operation.getUid());
//                nodeEntity.setChildItemType(operation.getObjectType());
//                stepDomainService.installBomNode(nodeEntity, process.getUid(), process.getRightObject());
//            }
//            //引用
//            if (map.get(bomNode.getChildItemType()).equals(CloneConstant.RELATION)) {
//
//            }
//        }
        return process;
    }


}
