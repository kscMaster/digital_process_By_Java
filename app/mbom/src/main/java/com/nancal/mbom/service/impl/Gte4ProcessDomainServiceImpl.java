package com.nancal.mbom.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.model.entity.Gte4PartEntity;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IGte4ProcessDomainService;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class Gte4ProcessDomainServiceImpl implements IGte4ProcessDomainService {

    @Autowired
    private IBOMNodeDomainService ibomNodeDomainService;

    /**
     * 创建设计零件对象，并创建关联的bom节点数据
     * 1、先将工序对象保存到数据库中 gte4part（工序表） master_r_l (关系表) gte4part_revision（工序版本表）
     * 2、再调用新增bom节点方法，新增bom节点  bomnode（bom节点表）
     *
     * @param req
     * @author: 拓凯
     * @time: 2022/7/5
     * @return: {@link BusinessObjectResp}
     */
    @Transactional
    @Override
    public WorkspaceObjectResp saveBomReq(BusinessObjectReq req,AppNameEnum appNameEnum) {
        Gte4ProcessBomReq processBomReq = (Gte4ProcessBomReq) req;
        BOMNodeReq nodeReq = processBomReq.getBomNodeReq();
        //校验是否是可以添加的关系
        String objectType = EntityUtil.getObjectType();
        if (ObjectUtil.isNotEmpty(nodeReq)){
            extracted(nodeReq.getParentItemType(), objectType);
        }
//        1、单独创建设计零件对象
        WorkspaceObjectResp save = IGte4ProcessDomainService.super.save(processBomReq.getProcessRevisionReq());
//        2、在父级底下添加设计零件对象
        if (!Objects.isNull(nodeReq)) {
            //判断是否可以添加，取决于父级的是否具有编辑权限
            IGte4ProcessDomainService.super.checkParentRevEidt(nodeReq);
            nodeReq.setChildItem(save.getUid());
            nodeReq.setChildItemType(save.getObjectType());
            ibomNodeDomainService.createNode(nodeReq, appNameEnum);
        }
        return save;
    }

    /**
     * 校验新建关系
     * @param parentItemType 父节点的类型
     * @param objectType 子节点的类型
     * @author: 薛锦龙
     * @time: 2022/10/10
     * @return: {@link}
     */
    private void extracted(String parentItemType, String objectType) {
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        Map<String, String> codeValueMap = dictUtil.getCodeValueMap(parentItemType + StrUtil.UNDERLINE + "bomStructure");
        //获取所有的key值
        List<String> child = new ArrayList<>();
        codeValueMap.forEach((key,value)->{
            if (value.equals(objectType)){
                child.add(value);
                return ;
            }
        });
        if (CollUtil.isEmpty(child)){
            Map<String, String> dictType = dictUtil.getCodeValueMap(DictConstant.OBJECT_TYPE_NAME);
            String beforeError = parentItemType;
            String afterError = objectType;
            if (dictType.containsKey(parentItemType)){
                beforeError = dictType.get(parentItemType);
            }
            if (dictType.containsKey(objectType)){
                afterError = dictType.get(objectType);
            }
            throw new ServiceException(ErrorCode.E_10,beforeError+"下不支持新建"+afterError);
        }
    }

}
