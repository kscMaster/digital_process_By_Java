package com.nancal.followmsgbom.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.api.utils.TranslateUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.base.IdRequest;
import com.nancal.common.constants.BomConstant;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.DataTypeEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.OperatorEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.exception.TipServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.common.utils.MessageSourceUtil;
import com.nancal.model.entity.*;
import com.nancal.remote.vo.DictItemVo;
import com.nancal.service.bo.Item;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.service.IBOMNodeDomainService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class BOMNodeDomainServiceImpl implements IBOMNodeDomainService {

    @Autowired
    private JPAQueryFactory factory;
    @Autowired
    private DictUtil dictUtil;
    @Autowired
    private TranslateUtil translateUtil;

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
        //工步
        if (childItemType.equalsIgnoreCase(step)) {
            getDictList(DictConstant.GTE4_MFGSTEP_BOM, req,step);
            return;
        }
        //工序
        if (childItemType.equalsIgnoreCase(operation)) {
            getDictList(DictConstant.GTE4_MFGOPERATION_BOM, req,operation);
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

    @Override
    public <T extends BOMNodeResp> void fullChildNodeResp(List<T> childNodeList,boolean isPrecise) {
        Item item = SpringUtil.getBean(ItemFactory.class).create();
        childNodeList.stream().collect(Collectors.groupingBy(BOMNodeResp::getChildItemType)).forEach((objectType,values)->{
            List<String> uidList = values.stream().map(BOMNodeResp::getChildItem).collect(Collectors.toList());
            Map<String, WorkspaceObjectEntity> map = null;
            if(ItemEntity.class.isAssignableFrom(EntityUtil.getEntityClass(objectType))) {
                Map<String, String> uidMap = values.stream().collect(Collectors.toMap(BOMNodeResp::getChildItem, BOMNodeResp::getChildItemRevId, (key1, key2) -> key1));
                map = item.getAppointVersion(uidMap, objectType);
            }else{
                List<WorkspaceObjectEntity> list = EntityUtil.getByIds(objectType,uidList);
                if(CollUtil.isNotEmpty(list)){
                    map = list.stream().collect(
                            Collectors.toMap(WorkspaceObjectEntity::getUid, Function.identity()));
                }
            }
            if(CollUtil.isNotEmpty(map)) {
                for (T viewResp : values) {
                    WorkspaceObjectEntity entity = map.get(viewResp.getChildItem());
                    if(ObjectUtil.isNotNull(entity)) {
                        WorkspaceObjectResp resp = EntityUtil.entityToResp(entity);
                        processNodeProperty(viewResp, resp);
                    }
                }
            }
        });
    }


    @Override
    public <T extends BOMNodeResp> void fullRootNodeResp(T nodeResp) {
        nodeResp.setUid("0");
        fullParallelNum(nodeResp);
    }

    /**
     * 填充并行序号
     *
     * @param nodeResp
     */
    private void fullParallelNum(BOMNodeResp nodeResp) {
        List<BOMNodeResp> childrenList = nodeResp.getChildren();
        if (CollUtil.isEmpty(childrenList)) {
            return;
        }
        childrenList.stream().filter(resp->{
            Class<?> entityClass = EntityUtil.getEntityClass(resp.getChildItemType());
            return MfgOperationEntity.class.isAssignableFrom(entityClass);
        }).filter(resp->resp.getSequenceNo()!=null).collect(Collectors.groupingBy(BOMNodeResp::getFoundNo)).forEach((fondNo, values) -> {
            Set<String> sequenceSet = values.stream().map(resp -> resp.getSequenceNo() + "").collect(Collectors.toSet());
            if (!CollUtil.isEmpty(sequenceSet)) {
                values.forEach(resp -> {
                    BOPNodeViewResp viewResp = (BOPNodeViewResp) resp;
                    Set<String> cloneSequenceSet = ObjectUtil.cloneByStream(sequenceSet);
                    cloneSequenceSet.remove(viewResp.getSequenceNo() + "");
                    viewResp.setParallelNum(String.join(",", cloneSequenceSet));
                    fullParallelNum(resp);
                });
            }
        });
    }

//    /**
//     * 拷贝时间达0.5s
//     * @param viewResp
//     * @param resp
//     */
//    @Deprecated
//    private void processNodePropertyTO(BOPNodeViewResp viewResp,WorkspaceObjectResp resp){
//        String displayName = resp.getDisplayName();
//        String nodeUid = viewResp.getUid();
//        String remark = viewResp.getRemark();
//        BeanUtil.copyPropertiesIgnoreNull(resp, viewResp);
//        viewResp.setObjectName(displayName);
//        viewResp.setUid(nodeUid);
//        viewResp.setRemark(remark);
//    }

//    //工序/工步特殊处理合并字段处理
//    @Override
//    public void specialProcessNode(BOMNodeResp nodeResp,WorkspaceObjectResp resp){
//        BOPNodeViewResp viewResp = (BOPNodeViewResp)nodeResp;
//        translateUtil.translate(resp);
//        String typeValue = resp.getExtraPropertyDesc(viewResp.GTE4_TYPE);
//        String opTypeValue = resp.getExtraPropertyDesc(viewResp.OP_TYPE);
//        if(!StrUtil.isBlank(typeValue)){
//            viewResp.setOpOrGet4TypeDesc(typeValue);
//        }else{
//            viewResp.setOpOrGet4TypeDesc(opTypeValue);
//        }
//        //增加随机id用于前端显示结构
//        viewResp.setRandomUid(IdGeneratorUtil.generate());
//    }


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
        //根据父级所在bomId查询出当前行的父级对象，判断父级是否有编辑权限
        BOMNodeEntity bomNodeEntity = EntityUtil.getById(EntityUtil.getObjectType(), id.getUid());
        if (Objects.isNull(bomNodeEntity)) {
            throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("bomIsNull",id.getUid()));
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
    public void checkParentEdit(BOMNodeEntity bomNode) {
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
     * 校验工艺规程，工序，工步的升版权限
     * @param req
     * @author: 拓凯
     * @time: 2022/8/1
     * @return: {@link Boolean}
     */
    @Override
    public boolean upgradeCheck(MfgCheckReq req) {
        boolean isUpgrade ;
        //不管是何种对象都要校验的自身是否具有升版权限
        WorkspaceObjectEntity entity = EntityUtil.getById(req.getType(), req.getId());
        if (ObjectUtil.isNull(entity)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        verifyAuthority(entity, OperatorEnum.Upgrade, "upgradeData", entity.getObjectName());
        //工艺规程
        if (req.getBomId().equals("0")) {
            isUpgrade = true;
        } else {
            //工序+工步（校验父级有没有编辑权限）
            BOMNodeEntity bomNode = EntityUtil.getById(new BOMNodeEntity().getObjectType(), req.getBomId());
            if (ObjectUtil.isNull(bomNode)) {
                throw new TipServiceException(ErrorCode.FAIL,MessageSourceUtil.getMessage("bomIsNull",req.getBomId()));
            }
            WorkspaceObjectEntity object = EntityUtil.getById(EntityUtil.getRevision(bomNode.getParentItemType()), bomNode.getParentItemRev());
            if (ObjectUtil.isNull(object)) {
                throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("parentRevIsNull",bomNode.getParentItemRev()));
            }
            verifyAuthority(object, OperatorEnum.Write, DataTypeEnum.PARENT_DATA, "updateError", object.getObjectName());
            //校验父级的版本有没有进行升版
            Item item = SpringUtil.getBean(ItemFactory.class).create();
            ItemRevisionEntity lastVersion = (ItemRevisionEntity)item.getLastVersion(bomNode.getParentItem(), bomNode.getParentItemType());
            if (lastVersion.getRevisionId().equals("001")) {
                throw new TipServiceException(ErrorCode.FAIL,MessageSourceUtil.getMessage("parentRevIdTip",lastVersion.getObjectName()));
            }
            // 工步升版需要从现场生产端获取信息判断该随工工艺规程下的工步是否已经执行，如果已经执行，不允许对工步进行升版（解冻） isExecute
            if (req.getType().equals(new Gte4MfgStepEntity().getObjectType())) {
                Gte4MfgStepEntity step = EntityUtil.getById(req.getType(), req.getId());
                if (step.getIsExecute()) {
                    throw new TipServiceException(ErrorCode.FAIL,MessageSourceUtil.getMessage("stepIsExecuteTip",step.getObjectName()));
                }
            }
            isUpgrade = true;
        }
        return isUpgrade;
    }

    /**
     * 打包node节点（设置子版本号）
     *
     * @param req
     * @param appNameEnum
     * @return
     */
    @Override
    public  <T extends BOMNodeReq> BOMNodeEntity packageNode(T req, AppNameEnum appNameEnum) {
        BOMNodeEntity bomNodeEntity = IBOMNodeDomainService.super.packageNode(req, appNameEnum);
        //根据子版本id查询版本号
        if (EntityUtil.checkItem(req.getChildItemType())) {
            ItemRevisionEntity entity = EntityUtil.getById(EntityUtil.getRevision(req.getChildItemType()), req.getChildItemRevision());
            if (!Objects.isNull(entity)) {
                bomNodeEntity.setChildItemRevId(entity.getRevisionId());
            }
        }
        return bomNodeEntity;
    }
}
