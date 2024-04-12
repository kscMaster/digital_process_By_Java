package com.nancal.ebom.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.model.common.ValidList;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.common.constants.BomConstant;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.*;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.exception.TipServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.MessageSourceUtil;
import com.nancal.model.entity.BOMNodeEntity;
import com.nancal.model.entity.Gte4PartEntity;
import com.nancal.model.entity.ItemRevisionEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.remote.vo.DictItemVo;
import com.nancal.service.bo.Item;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.service.IBOMNodeDomainService;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class BOMNodeDomainServiceImpl implements IBOMNodeDomainService {

    @Autowired
    private JPAQueryFactory factory;
    @Autowired
    private DictUtil dictUtil;

    /**
     * 根据类型，调用字典服务赋值查找编号
     *
     * @param req
     * @author: 拓凯
     * @date: 2022/6/7
     * @return: {@link boolean}
     */
    @Override
    public void fullNodeReq(BOMNodeReq req) {
        String childItemType = req.getChildItemType();
//        String part = new Gte4PartEntity().getObjectType();
//        String objectType = new Gte4MaterialEntity().getObjectType();
//        if (!childItemType.equalsIgnoreCase(part)||!childItemType.equalsIgnoreCase(objectType)) {
//            throw new ServiceException(ErrorCode.E_12,"类型不支持!");
//        }
        getDictList(DictConstant.GTE4_PART_BOM, req);
    }

    private void getDictList(String dictType,BOMNodeReq req) {
        List<DictItemVo> codeValueList = dictUtil.getCodeValueList(dictType);
        Map<String, String> map = codeValueList.stream().collect(Collectors.toMap(DictItemVo::getCode, DictItemVo::getValue, (key1, key2) -> key2));
        processFoundNo(req, Integer.parseInt(map.get(BomConstant.FOUNDNO_START)), Integer.parseInt(map.get(BomConstant.FOUNDNO_STEP)));
    }

    /**
     * 新建设计零件后，设计零件所在的BOM行属性【查找编号】默认从10开始，按照步长为10自动递增。
     * 根据childItemType，查找编号排序 查询出最大的一条数据，
     * 要是此数据为空，则初始化查找编号为10，不为空，则+10再赋值
     *
     * @author: 拓凯
     * @time: 2022/7/5
     * @return: {@link}
     */
    private void processFoundNo(BOMNodeReq req, Integer foundNoStart, Integer foundNoStep) {
        List<Triple<String, Ops, Object>> paramList = Arrays.asList(
                Triple.of(BOMNodeEntity.DEL_FLAG, Ops.EQ, false),
                Triple.of(BOMNodeEntity.PARENT_ITEM, Ops.EQ, req.getParentItem()),
                Triple.of(BOMNodeEntity.CHILD_ITEM_TYPE, Ops.EQ, new Gte4PartEntity().getObjectType())
        );
        OrderSpecifier order = new OrderSpecifier(Order.DESC, ExpressionUtils.path(BOMNodeEntity.class, BOMNodeEntity.FOUND_NO));
        BOMNodeEntity first = (BOMNodeEntity)EntityUtil.getDynamicQuery(new BOMNodeEntity().getObjectType(), paramList).orderBy(order).fetchFirst();
        if (null == req.getFoundNo()){
            if (Objects.isNull(first)) {
                req.setFoundNo(foundNoStart);
            } else {
                req.setFoundNo(first.getFoundNo() + foundNoStep);
            }
        }
    }
    @Override
    public <T extends BOMNodeResp> void fullRootNodeResp(T nodeResp) {
        nodeResp.setUid("0");
        nodeResp.setLevel("1");
        setLevel(nodeResp);
    }

   public void setLevel(BOMNodeResp resp  ){
       if (CollUtil.isEmpty(resp.getChildren())){
           return;
       }
           for (int i = 0; i < resp.getChildren().size(); i++) {
               resp.getChildren().get(i).setLevel(resp.getLevel()+ StrUtil.DOT+(i+1));
               setLevel( (BOPNodeViewResp)resp.getChildren().get(i));
           }
   }


    @Override
    @Transactional
    public BusinessObjectResp deleteObject(IdRequest id) {
        String objectType = StrUtil.blankToDefault(id.getObjectType(), EntityUtil.getObjectType());
        //根据父级所在bomId查询出当前行的父级对象，判断父级是否有编辑权限
        BOMNodeEntity bomNodeEntity = EntityUtil.getById(objectType, id.getUid());
        if (Objects.isNull(bomNodeEntity)) {
            throw new ServiceException(ErrorCode.E_12, "根据此bomId,查询不到此bom行数据!");
        }
        checkParentEdit(bomNodeEntity);
        return IBOMNodeDomainService.super.deleteObject(id);
    }


    @Override
    @Transactional
    public BusinessObjectResp update(BusinessObjectReq req) {
        BOMNodeReq bomNodeReq = (BOMNodeReq) req;
        BOMNodeEntity bomNodeEntity = EntityUtil.getById(new BOMNodeEntity().getObjectType(), bomNodeReq.getUid());
        //能否编辑bomNode属性取决于其父类是否拥有编辑权限
        if (Objects.isNull(bomNodeEntity)) {
            throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("bomIsNull",bomNodeReq.getUid()));
        }
        checkParentEdit(bomNodeEntity);
        return IBOMNodeDomainService.super.update(bomNodeReq);
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

    /**
     * 1.校验粘贴权限，等同于编辑权限的校验（往A底下粘贴B,校验A的编辑权限）
     * 2.调用字典工具中的getRelation()判断是否支持粘贴关系  Gte4MfgProcess_bomStructure
     * 3.校验循环粘贴（避免当前子级的父级作为子级的子级）
     * 4.处理查找编号生成逻辑（主要处理工艺资源的查找编号）
     * A:当工艺资源与工步是同级时，查找编号继承工步
     * B:当工艺资源作为工步的子级时，查找编号等同于工步的规则
     *
     * @param reqs
     * @author: 拓凯
     * @time: 2022/6/21
     * @return: {@link List<BOMNodeResp>}
     */
    @Transactional
    public List<Object> parseData(ValidList<BOMNodeReq> reqs) {
        List<Object> msgList = new ArrayList<>();
        for (BOMNodeReq req : reqs) {
            WorkspaceObjectEntity entity;
            //工步
            if (req.getParentItemRev().equalsIgnoreCase("0")) {
                entity = EntityUtil.getById(req.getParentItemType(), req.getParentItem());
                //工序，工艺资源
            } else {
                entity = EntityUtil.getById(EntityUtil.getRevision(req.getParentItemType()), req.getParentItemRev());
            }
            //1.校验父级的编辑权限
            this.verifyAuthority(entity, OperatorEnum.Write);
            //2.校验自己粘贴自己
            if (req.getChildItem().equalsIgnoreCase(req.getParentItem())) {
                msgList.add("不允许粘贴到自己的子集里！！！");
            }
            //3.判断是否支持粘贴关系
            boolean b = this.checkRelation(req.getParentItemType(), req.getChildItemType());
            if (!b) {
                //转换字典值
                Map<String, String> map = SpringUtil.getBean(DictUtil.class).getCodeValueMap("ObjectTypeName");
                String msg = "目前不支持在[" + map.get(req.getParentItemType()) + "]下粘贴[" + map.get(req.getChildItemType()) + "]的操作!";
                msgList.add(msg);
            }
        }
        if (msgList.size() != 0) {
            return msgList;
        } else {
            reqs.stream().forEach(req -> IBOMNodeDomainService.super.createNode(req, AppNameEnum.EBOM));
            return CollUtil.newArrayList(true);
        }
    }

    /**
     * 校验是否支持粘贴关系
     * @param parentType
     * @param childType
     * @author: 拓凯
     * @time: 2022/6/24
     * @return: {@link boolean}
     */
    private boolean checkRelation(String parentType,String childType) {
        List<DictItemVo> codeValueList = dictUtil.getCodeValueList("Gte4Part_bomStructure");
        Set<String> collect = codeValueList.stream().map(dictItemVo -> dictItemVo.getValue() + dictItemVo.getCode()).collect(Collectors.toSet());
        return collect.stream().anyMatch(String -> String.equalsIgnoreCase(parentType + childType));
    }


}
