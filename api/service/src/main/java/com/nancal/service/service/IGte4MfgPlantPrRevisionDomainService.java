package com.nancal.service.service;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.CoverNodeResp;
import com.nancal.api.model.FrontCoverResp;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.common.base.TableResponse;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.ItemRevisionEntity;
import com.nancal.model.entity.ProcessInstanceEntity;
import com.nancal.model.entity.RelationEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.remote.service.RemoteLezaoFlowableService;
import com.nancal.remote.to.ProcessPageQueryTo;
import com.nancal.remote.vo.NodeInfoVo;
import com.nancal.remote.vo.ProcessTaskNodeVo;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.bo.ProcessInstanceRL;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.factory.ProcessInstanceRLFactory;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModelProperty;
import lombok.SneakyThrows;
import one.util.streamex.StreamEx;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.tuple.Triple;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;


public interface IGte4MfgPlantPrRevisionDomainService<T> extends IItemRevisionDomainService {
    /**
     * 获取封面
     * @param id
     * @author: 薛锦龙
     * @time: 2022/9/19
     * @return: {@link WorkspaceObjectResp}
     */
    default WorkspaceObjectResp getCoverPage(IdRequest id){
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        ProcessInstanceRL processInstanceRL = SpringUtil.getBean(ProcessInstanceRLFactory.class).create();
        RemoteLezaoFlowableService remoteLezaoFlowableService = SpringUtil.getBean(RemoteLezaoFlowableService.class);
        WorkspaceObjectEntity activeRevision = itemRevision.getActiveRevision(id.getUid(), id.getObjectType());
        ItemRevisionEntity itemRevisionEntity = (ItemRevisionEntity)activeRevision;
        if (ObjectUtil.isEmpty(activeRevision)){
            throw new ServiceException(ErrorCode.E_12);
        }
        //获取当前版本的所有版次
        List<WorkspaceObjectEntity> allSequences = itemRevision.getAllSequences(id.getUid(), id.getObjectType());
        //获取流程数据
        List<String> uidList = StreamEx.of(allSequences).map(WorkspaceObjectEntity::getUid).toList();
        List<WorkspaceObjectEntity> rightType = processInstanceRL.getRightType(uidList, id.getObjectType());
        FrontCoverResp resp = new FrontCoverResp();
        if (CollUtil.isNotEmpty(rightType)){
            //获取最近一调数据
            RelationEntity firstRl = (RelationEntity)CollUtil.getFirst(rightType);
            WorkspaceObjectEntity entity = EntityUtil.getDynamicQuery(firstRl.getLeftObjectType(), Triple.of(WorkspaceObjectEntity.UID, Ops.EQ, firstRl.getLeftObject())).fetchFirst();
            ProcessInstanceEntity instanceEntity = (ProcessInstanceEntity)entity;
            ProcessPageQueryTo processPageQueryTo = new ProcessPageQueryTo(1,99999, null);
            TableResponse<ProcessTaskNodeVo> processNodePage = remoteLezaoFlowableService.getProcessNodePage(instanceEntity.getProcessDefinitionId(), processPageQueryTo);
            Response<List<NodeInfoVo>> processNode = remoteLezaoFlowableService.getProcessNode(instanceEntity.getProcessKey());//通过流程获取节点
            List<NodeInfoVo> data = processNode.getData();
            List<String> nodeInfoVo = StreamEx.of(data).map(NodeInfoVo::getName).toList();
            List<ProcessTaskNodeVo> listData = processNodePage.getData();
            if (CollUtil.isNotEmpty(listData)){
                Map<String, ProcessTaskNodeVo> stringProcessTaskNodeVoMap = StreamEx.of(listData).toMap(ProcessTaskNodeVo::getName, Function.identity());
                List<CoverNodeResp> resps = processData(stringProcessTaskNodeVoMap, nodeInfoVo);
                //送审的第一个日期
                String[] time = listData.get(0).getCreateTime().split(" ");
                LocalDate localDate = LocalDate.parse(time[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                resp.setDate(localDate);
                resp.setNode(resps);
            }
        }
        resp.setObjectName(itemRevisionEntity.getItemId()+StrUtil.SLASH+itemRevisionEntity.getRevisionId()+StrUtil.DASHED+itemRevisionEntity.getObjectName());
        return resp;
    }

    default List<CoverNodeResp> processData(Map<String, ProcessTaskNodeVo> stringProcessTaskNodeVoMap, List<String> stringNodeInfoVoMap){
        List<CoverNodeResp>  resps = new ArrayList<>();
        stringNodeInfoVoMap.forEach(key->{
            CoverNodeResp resp = new CoverNodeResp();
            if (stringProcessTaskNodeVoMap.containsKey(key)){
                ProcessTaskNodeVo processTaskNodeVo = stringProcessTaskNodeVoMap.get(key);
                String assignee = processTaskNodeVo.getAssignee();
                String endTime = processTaskNodeVo.getEndTime();
                if (StrUtil.isNotBlank(endTime)){
                    String[] time = endTime.split(" ");
                    assignee = assignee+StrUtil.DASHED+time[0];
                }
                resp.setNodeValue(assignee);
            }
            resp.setNodeName(key);
            resps.add(resp);
        });
        return resps;
    }

    @SneakyThrows
    default Object getFieldAlias(Class<?> aClass, Map<String, ProcessTaskNodeVo> stringProcessTaskNodeVoMap, Map<String, NodeInfoVo> stringNodeInfoVoMap){
        Object o = aClass.getDeclaredConstructor().newInstance();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            ApiModelProperty declaredAnnotation = declaredField.getDeclaredAnnotation(ApiModelProperty.class);
            if (null!=declaredAnnotation){
                if (stringProcessTaskNodeVoMap.containsKey(declaredAnnotation.name())){
                    String nodeKey = stringProcessTaskNodeVoMap.get(declaredAnnotation.name()).getAssignee();
                    String endTime = stringProcessTaskNodeVoMap.get(declaredAnnotation.name()).getEndTime();
                    if (StrUtil.isNotBlank(endTime)){
                        nodeKey = nodeKey+ StrUtil.DASHED+ endTime;
                    }
                    Object convert = ConvertUtils.convert(nodeKey, declaredField.getType());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(declaredField.getName(), aClass);
                    propertyDescriptor.getWriteMethod().invoke(o,convert);
                }
                continue;
            }
        }
        return o;
    }

}
