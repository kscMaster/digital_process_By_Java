package com.nancal.imexport.service.impl;


import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.BOMNodeReq;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.constants.BomConstant;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.DataTypeEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.OperatorEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.exception.TipServiceException;
import com.nancal.common.utils.MessageSourceUtil;
import com.nancal.model.entity.*;
import com.nancal.remote.vo.DictItemVo;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.service.IBOMNodeDomainService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class BOMNodeDomainServiceImpl implements IBOMNodeDomainService {

    @Autowired
    private JPAQueryFactory factory;

    @Autowired
    private DictUtil dictUtil;

    /**
     * 根据类型，调用字典服务赋值查找编号和业务号（工序号，工步号）
     *
     * @param req
     * @author: 拓凯
     * @date: 2022/6/7
     * @return: {@link boolean}
     */
    @Override
    public void fullNodeReq(BOMNodeReq req) {
        String childItemType = req.getChildItemType();
        String step = new Gte4MfgStepEntity().getObjectType();
        String operation = new Gte4MfgOperationEntity().getObjectType();
        String stationPr = new Gte4MfgStationPrEntity().getObjectType();
        String linePr = new Gte4MfgLinePrEntity().getObjectType();
        if (childItemType.equalsIgnoreCase(step)) {
            getDictList(DictConstant.GTE4_MFGSTEP_BOM, req,step);
            return;
        }
        if (childItemType.equalsIgnoreCase(operation)) {
            getDictList(DictConstant.GTE4_MFGOPERATION_BOM, req,operation);
            return;
        }
        if (childItemType.equalsIgnoreCase(stationPr)) {
            getDictList(DictConstant.GTE4_MFGSTEP_BOM, req,stationPr);
            return;
        }
        if (childItemType.equalsIgnoreCase(linePr)) {
            getDictList(DictConstant.GTE4_MFGSTEP_BOM, req,linePr);
            return;
        }
        //工艺资源(查找编号同工步)
        getDictList(DictConstant.GTE4_MFGSTEP_BOM, req, BomConstant.PROCESS_RESOURCES);
    }

    private void getDictList(String dictType,BOMNodeReq req,String objType) {
        List<DictItemVo> codeValueList = dictUtil.getCodeValueList(dictType);
        Map<String, String> map = codeValueList.stream().collect(Collectors.toMap(DictItemVo::getCode, DictItemVo::getValue, (key1, key2) -> key2));
        processFoundNoAndSeqNo(objType, req, Integer.parseInt(map.get(BomConstant.FOUNDNO_START)), Integer.parseInt(map.get(BomConstant.FOUNDNO_STEP)),
                Integer.parseInt(map.get(BomConstant.SEQUENCE_START)), Integer.parseInt(map.get(BomConstant.SEQUENCE_STEP)));
    }

    /**
     * 新建工步过程中，查找编号默认从10开始，按照步长为10自动递增
     * 新建工步过程中，BOM属性【工步号】默认从1开始，按照步长为1自动递增
     *
     * 新建工序后，工序所在的BOM行属性【查找编号】默认从10开始，按照步长为10自动递增。
     * 新建工序后，BOM行属性【工序号】默认从0开始，按照步长为5自动递增；
     * 根据childItemType，查找编号排序 查询出最大的一条数据，
     * 要是此数据为空，则初始化查找编号为10，不为空，则+10再赋值
     *
     * @author: 拓凯
     * @time: 2022/6/8
     * @return: {@link}
     */
    private void processFoundNoAndSeqNo(String type, BOMNodeReq req, Integer foundNoStart, Integer foundNoStep, Integer sequenceStart, Integer sequenceStep) {
        //根据父id,查询是否在bomnode表中有子集，无则从零开始，有则查询出最大的递增
        QBOMNodeEntity node = QBOMNodeEntity.bOMNodeEntity;
        BooleanBuilder where = new BooleanBuilder();
        where.and(node.delFlag.isFalse());
        where.and(node.parentItem.eq(req.getParentItem()));
        if (type.equalsIgnoreCase(new Gte4MfgOperationEntity().getObjectType())) {
            where.and(node.childItemType.equalsIgnoreCase(new Gte4MfgOperationEntity().getObjectType()));
        }
        if (type.equalsIgnoreCase(new Gte4MfgStepEntity().getObjectType())) {
            where.and(node.childItemType.equalsIgnoreCase(new Gte4MfgStepEntity().getObjectType()));
        }
        if (type.equalsIgnoreCase(new Gte4MfgStationPrEntity().getObjectType())) {
            where.and(node.childItemType.equalsIgnoreCase(new Gte4MfgStationPrEntity().getObjectType()));
        }
        if (type.equalsIgnoreCase(new Gte4MfgLinePrEntity().getObjectType())) {
            where.and(node.childItemType.equalsIgnoreCase(new Gte4MfgLinePrEntity().getObjectType()));
        }
        //查找编号
        BOMNodeEntity foundNo = factory.selectFrom(node).where(where).orderBy(node.foundNo.desc()).fetchFirst();
        if (Objects.isNull(foundNo)) {
            req.setFoundNo(foundNoStart);
        } else {
            req.setFoundNo(foundNo.getFoundNo() + foundNoStep);
        }
        //业务号(工序和工步有)
        if (type.equalsIgnoreCase(new Gte4MfgOperationEntity().getObjectType()) || type.equalsIgnoreCase(new Gte4MfgStepEntity().getObjectType())) {
            BOMNodeEntity sequence = factory.selectFrom(node).where(where).orderBy(node.sequenceNo.desc()).fetchFirst();
            if (Objects.isNull(sequence)) {
                req.setSequenceNo(sequenceStart);
            } else {
                req.setSequenceNo(sequence.getSequenceNo() + sequenceStep);
            }
        } else {
            //数量（工艺资源有）
            req.setQuantity(1.0);
        }
    }

    @Override
    @Transactional
    public BusinessObjectResp deleteObject(IdRequest id) {
        String objectType;
        if (StringUtils.isNotBlank(id.getObjectType())){
            objectType = id.getObjectType();
        }else {
            objectType = EntityUtil.getObjectType();
        }
        //根据父级所在bomId查询出当前行的父级对象，判断父级是否有编辑权限
        BOMNodeEntity bomNodeEntity = EntityUtil.getById(objectType, id.getUid());
        if (Objects.isNull(bomNodeEntity)) {
            throw new ServiceException(ErrorCode.E_12, "根据此bomId,查询不到此bom行数据!");
        }
        checkParentEdit(bomNodeEntity);
        return IBOMNodeDomainService.super.deleteObject(id);
    }

    /**
     * 根据bom行uid校验父类是否拥有编辑权限
     *
     * @param bomNode
     * @author: 拓凯
     * @time: 2022/6/10
     * @return: {@link}
     */
    private void checkParentEdit(BOMNodeEntity bomNode) {
        WorkspaceObjectEntity entity ;
        //如果父类是没有版本的对象类型
        if ("0".equalsIgnoreCase(bomNode.getParentItemRev())) {
             entity = EntityUtil.getById(bomNode.getParentItemType(), bomNode.getParentItem());
        } else {
            //如果父类是有版本的对象类型
            ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
            //根据父版本uid获取同版本最新激活的版次
            entity = itemRevision.getActiveSequence(bomNode.getParentItemRev(), EntityUtil.getRevision(bomNode.getParentItemType()));
        }
        if (Objects.isNull(entity)) {
            throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("parentRevIsNull",bomNode.getParentItemRev()));
        }
        this.verifyAuthority(entity, OperatorEnum.Write, DataTypeEnum.PARENT_DATA,"updateError",entity.getObjectName());
    }
}
