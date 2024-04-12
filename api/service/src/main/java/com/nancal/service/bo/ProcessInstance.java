package com.nancal.service.bo;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.nancal.api.model.ProcessInstanceReq;
import com.nancal.api.model.ProcessInstanceResp;
import com.nancal.api.model.common.WorkBaseReq;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.base.Response;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.LifeCycleStateEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.model.entity.ProcessInstanceEntity;
import com.nancal.model.entity.QProcessInstanceEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.remote.service.RemoteLezaoFlowableService;
import com.nancal.remote.to.StartFlowableTo;
import com.nancal.service.factory.ProcessInstanceRLFactory;
import com.nancal.service.factory.RelationFactory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Slf4j
@ToString
@NoArgsConstructor
@ApiModel(value = "ProcessInstance 流程 的BO")
public class ProcessInstance extends WorkspaceObject {

    @ApiModelProperty(value = "流程定义ID")
    private String processDefinitionId;
    @ApiModelProperty(value = "流程标识")
    private String processKey;
    @ApiModelProperty(value = "流程定义ID")
    private String businessKey;

    /**
     * 通过流程Id获取流程数据
     * @param processDefinitionId 流程id
     * @author: 薛锦龙
     * @time: 2022/7/4
     * @return: {@link List< ProcessInstanceResp>}
     */
    public ProcessInstanceResp getByProcessDefinitionId(String processDefinitionId){
        QProcessInstanceEntity qProcessInstanceEntity = QProcessInstanceEntity.processInstanceEntity;
        BooleanBuilder where = new BooleanBuilder();
        where.and(qProcessInstanceEntity.delFlag.isFalse());
        where.and(qProcessInstanceEntity.processDefinitionId.eq(processDefinitionId));
        ProcessInstanceEntity processInstanceEntity = SpringUtil.getBean(JPAQueryFactory.class).selectFrom(qProcessInstanceEntity).where(where).fetchFirst();
        ProcessInstanceResp resp = new ProcessInstanceResp();
        BeanUtil.copyPropertiesIgnoreNull(processInstanceEntity,resp);
        return resp;
    }

    /**
     * 启动流程实例
     * @param req
     * @author: 薛锦龙
     * @time: 2022/9/29
     * @return: {@link}
     */
    public void createInstance(ProcessInstanceReq req){
        ProcessInstanceRL instanceRL = SpringUtil.getBean(ProcessInstanceRLFactory.class).create();
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        RemoteLezaoFlowableService flowableService = SpringUtil.getBean(RemoteLezaoFlowableService.class);
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        //流程实例变量
        Map<String, Object> objectMap = new HashMap<>(16);
        objectMap.put("processTitle", req.getProcessName());
        //是否是在流程启动是指派办理人  true/false
        objectMap.put("isStartAssignee", Boolean.TRUE);
        if (StrUtil.isNotBlank(req.getAssigneeJson())) {
            // 指派的办理人  例子："assigneeJson":{"节点id1":"办理人1,办理人2","节点id2":"办理人1,办理人2"}
            objectMap.put("assigneeJson", JSONUtil.toBean(req.getAssigneeJson(),Map.class));
        }
        objectMap.put("assignee", userUtils.getCurrentUserId());
        //组装启动流程请求
        StartFlowableTo startFlowableTo = new StartFlowableTo();
        startFlowableTo.setBusinessKey(req.getBusinessKey());
        startFlowableTo.setProcessKey(req.getProcessKey());
        startFlowableTo.setTenantId(userUtils.getTenantId());
        startFlowableTo.setUserId(userUtils.getCurrentUserId());
        startFlowableTo.setProcessVariables(objectMap);
        log.info("提交流程参数：" + JSONUtil.toJsonStr(startFlowableTo));
        Response<String> flowable = flowableService.startFlowableAndDealTask(startFlowableTo);
        if (Boolean.TRUE.equals(flowable.isFail())) {
            log.error("远程调用发起流程失败",JSONUtil.toJsonStr(flowable));
            throw new ServiceException(ErrorCode.ERROR, flowable.getMsg());
        }
        log.info("流程响应：" + JSONUtil.toJsonStr(flowable));
        //保存实体
        String processInstanceUid = IdGeneratorUtil.generate();
        ProcessInstanceEntity processInstanceEntity = new ProcessInstanceEntity();
        processInstanceEntity.setUid(processInstanceUid);
        processInstanceEntity.setProcessKey(startFlowableTo.getProcessKey());
        processInstanceEntity.setBusinessKey(StrUtil.blankToDefault(req.getBusinessKey(),processInstanceUid));
        //添加返回的流程实例id
        processInstanceEntity.setProcessDefinitionId(flowable.getData());
        processInstanceEntity.setObjectName(req.getProcessName());
        processInstanceEntity.setOwnerId(userUtils.getCurrentUserId());
        processInstanceEntity.setOwnerName(userUtils.getCurrentUserName());
        processInstanceEntity.setRemark(req.getRemark());
        processInstanceEntity.setLifeCycleState(LifeCycleStateEnum.InJob.name());
        processInstanceEntity.setProcessTitle(req.getProcessTitle());
        entityManager.persist(processInstanceEntity);
        //创建流程与变更单的关系
        Map<String, List<WorkBaseReq>> map = req.getReqList().stream().collect(Collectors.groupingBy(WorkBaseReq::getObjectType));
        List<WorkspaceObjectEntity> entities = new ArrayList<>();
        map.forEach((key,values)->{
            List<String> listUids = StreamEx.of(values).map(WorkBaseReq::getUid).toList();
            entities.addAll(EntityUtil.getByIds(key, listUids));

        });
        instanceRL.create(processInstanceEntity.getUid(),entities);
    }

}
