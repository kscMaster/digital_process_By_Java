package com.nancal.changeorder.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nancal.api.model.*;
import com.nancal.api.model.common.WorkBaseReq;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.Response;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.LifeCycleStateEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.*;
import com.nancal.remote.service.RemoteLezaoFlowableService;
import com.nancal.remote.service.RemoteMpTenantAdminService;
import com.nancal.remote.vo.DeptVo;
import com.nancal.remote.vo.DictItemVo;
import com.nancal.remote.vo.ManagerVo;
import com.nancal.remote.vo.NodeInfoVo;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.bo.ProcessInstance;
import com.nancal.service.factory.ItemRevisionFactory;
import com.nancal.service.factory.ProcessInstanceFactory;
import com.nancal.service.service.IChangeAfterRLDomainService;
import com.nancal.service.service.IChangeDomainService;
import com.nancal.service.service.IProcessInstanceDomainService;
import com.querydsl.core.types.Ops;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import javax.persistence.EntityManager;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface IProcessInstanceDomainServiceAdaptor extends IProcessInstanceDomainService {
    /**
     * 变更单提交
     * @param req
     * @author: 薛锦龙
     * @time: 2022/9/27
     * @return: {@link BusinessObjectResp}
     */
    @Transactional
    default void submit(ProcessInstanceReq req){
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        RemoteLezaoFlowableService lezaoFlowableService = SpringUtil.getBean(RemoteLezaoFlowableService.class);
        RemoteMpTenantAdminService remoteMpTenantAdminService = SpringUtil.getBean(RemoteMpTenantAdminService.class);
        String noticeType = new Gte4NoticeOrgEntryEntity().getObjectType();
        String noticeRLType = new Gte4NoticeOrgRLEntity().getObjectType();
        String distRLType = new Gte4DistOrgRLEntity().getObjectType();
        String distType = new Gte4DistOrgEntryEntity().getObjectType();
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        IChangeAfterRLDomainService afterRLDomainService = SpringUtil.getBean(IChangeAfterRLDomainService.class);
        String changeAfterType = new ChangeAfterRLEntity().getObjectType();
        LocalDateTime localDate = LocalDateTime.now();
        //启动多条送审流程
        List<WorkspaceObjectResp> afters = new ArrayList<>();
        req.getReqList().forEach(data->{
            WorkspaceObjectEntity chengeEntity = EntityUtil.getById(data.getObjectType(), data.getUid());
            //生成数据集
            try {
                ChangeEntity changeEntity = (ChangeEntity) chengeEntity;
                SpringUtil.getBean(IChangeDomainService.class).uploadChangePDF(changeEntity, DictConstant.PART_CHANGE_COMPARE,"files/工艺更改单模板v2.docx",Gte4RunTimeChangeAttachResp.class);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("生成附件失败");
            }
            //校验送审（变更后数据必须存在否则无法送审）
            List<WorkspaceObjectResp> afterObject = afterRLDomainService.getAfterObject(new IdRequest(data.getUid(), data.getObjectType()));
            if (CollUtil.isEmpty(afterObject)){
                WorkspaceObjectEntity entity = EntityUtil.getById(data.getObjectType(),data.getUid());
                ChangeEntity changeEntity = (ChangeEntity)entity;
                throw new ServiceException(ErrorCode.E_12,changeEntity.getObjectName()+"更改单数据不完善");
            }
            ProcessInstanceReq processInstanceReq = new ProcessInstanceReq();
            BeanUtil.copyPropertiesIgnoreNull(req,processInstanceReq);
            List<WorkBaseReq> workBaseReqs = new ArrayList<>();
            workBaseReqs.add(data);
            processInstanceReq.setReqList(workBaseReqs);
            //启动变更单的送审流程
            IProcessInstanceDomainService.super.release(req, AppNameEnum.CHANGE_ORDER);
            afters.addAll(afterObject);
        });
        //通知单位送审流程
        //从流程获取通知单位的节点
        List<DictItemVo> noticeProcess = dictUtil.getProcessListByObjectType(noticeType);
        String noticeProcessKey = CollUtil.getFirst(noticeProcess).getCode();
        List<NodeInfoVo> noticeProcessNode = getProcessNode(lezaoFlowableService, noticeProcessKey);
        String noticeKey = "";
        String name = "";
        if (noticeProcessNode.size()>1){
            noticeKey = noticeProcessNode.get(1).getNodeKey();
            name = noticeProcessNode.get(1).getProcessName();
        }
        String processName = name;
        String noticeKeys = noticeKey;
        List<WorkspaceObjectEntity> all = new ArrayList<>();
        afters.forEach(data->{
            List<WorkspaceObjectEntity> allSequences = itemRevision.getAllSequences(data.getUid(), data.getObjectType());
            all.addAll(allSequences);
        });
        List<String> afterUids = StreamEx.of(all).map(WorkspaceObjectEntity::getUid).toList();
        List<WorkspaceObjectEntity> changeAfters = EntityUtil.getDynamicQuery(changeAfterType, Triple.of(RelationEntity.RIGHT_OBJECT, Ops.IN, afterUids)).fetch();
        List<String> afterRlUids = StreamEx.of(changeAfters).map(WorkspaceObjectEntity::getUid).toList();
        List<WorkspaceObjectEntity> noticeExtracted = extracted(noticeRLType, afterRlUids);//通知单位
        if (CollUtil.isNotEmpty(noticeExtracted)){
            noticeExtracted.stream().map(Gte4NoticeOrgEntryEntity.class::cast).collect(Collectors.groupingBy(Gte4NoticeOrgEntryEntity::getGte4NoticeOrgId)).forEach((org,values)->{
                //TODO 启动送审流程（每个数据启动一条流程）
                // 根据部门获取下面的人员信息
                Gte4NoticeOrgEntryEntity entity = CollUtil.getFirst(values);
                String userUid = getPrincipal(remoteMpTenantAdminService, org);
                log.info("通知单位：" + org + ",名称：" + entity.getGte4NoticeOrg() + ",下面人员id:" + userUid);
                if (userUid == null) return;
                values.forEach(notice->{
                    extracted(noticeProcessKey,noticeKeys,notice,userUid,notice.getGte4NoticeOrg(), processName);
                    //更新通知单位的时间
                    notice.setGte4NoticeDate(localDate);
                    notice.setLifeCycleState(LifeCycleStateEnum.InJob.name());
                    entityManager.merge(notice);
                });
            });
        }
        LocalDateTime localDateTim = LocalDateTime.now();
        //发布单位送审流程
        List<WorkspaceObjectEntity> distExtracted = extracted(distRLType, afterRlUids);//发放单位
        List<DictItemVo> dictItemVos= dictUtil.getProcessListByObjectType(distType);
        String distProcessKey =  CollUtil.getFirst(dictItemVos).getCode();
        List<NodeInfoVo> dictProcessNode = getProcessNode(lezaoFlowableService, noticeProcessKey);
        String distKey = "";
        String distName = "";
        if (dictProcessNode.size()>1){
            distKey = dictProcessNode.get(1).getNodeKey();
            distName = dictProcessNode.get(1).getProcessName();
        }
        String distKeys = distKey;
        String distProcessName = distName;
        if (CollUtil.isNotEmpty(distExtracted)){
            distExtracted.stream().map(Gte4DistOrgEntryEntity.class::cast).collect(Collectors.groupingBy(Gte4DistOrgEntryEntity::getGte4DistriOrgId)).forEach((org,values)->{
                //TODO 启动送审流程（每个数据启动一条流程）
                // 根据部门获取下面的人员信息
                Gte4DistOrgEntryEntity entity = CollUtil.getFirst(values);
                String userUid = getPrincipal(remoteMpTenantAdminService, org);
                log.info("通知单位：" + org + ",名称：" + entity.getGte4DistriOrg() + ",下面人员id:" + userUid);
                if (userUid == null) return;
                values.forEach(dist->{
                    extracted(distProcessKey,distKeys,dist,userUid,dist.getGte4DistriOrg(),distProcessName);
//                    //更新通知单位的时间
                    dist.setGte4NoticeDate(localDateTim);
                    dist.setLifeCycleState(LifeCycleStateEnum.InJob.name());
                    entityManager.merge(dist);
                });
            });
        }
    }

    /**
     * 获取部门负责人
     * @param remoteMpTenantAdminService
     * @param departmentId 部门的id
     * @return
     */
    private String getPrincipal(RemoteMpTenantAdminService remoteMpTenantAdminService, String departmentId) {
        Response<DeptVo> response = remoteMpTenantAdminService.getDeptManagers(departmentId);
        if (Boolean.TRUE.equals(response.isFail())) {
            log.error("调用IAM获取部门下责任人失败，" + JSONUtil.toJsonStr(response));
            throw new ServiceException(ErrorCode.REMOTE_FAIL, "调用IAM获取部门下责任人失败");
        }
        if (CollUtil.isEmpty(response.getData().getManager())) {
            return null;
        }
        String userUid = response.getData().getManager().stream().map(ManagerVo::getId).collect(Collectors.joining(StrUtil.COMMA));
        return userUid;
    }

    /**
     * 获取指定的流程节点的key
     * @param lezaoFlowableService
     * @param processKey
     * @author: 薛锦龙
     * @time: 2022/9/29
     * @return: {@link String}
     */
    private List<NodeInfoVo> getProcessNode(RemoteLezaoFlowableService lezaoFlowableService, String processKey) {
        Response<List<NodeInfoVo>> processNode = lezaoFlowableService.getProcessNode(processKey);
        if (Boolean.TRUE.equals(processNode.isFail())) {
            log.error(JSONUtil.toJsonStr(processNode));
            throw new ServiceException(ErrorCode.REMOTE_FAIL, "远程获取流程节点异常");
        }
        List<NodeInfoVo> processNodeData = processNode.getData();
        //获取通知单位的所有节点
        return processNodeData;
    }

    private List<WorkspaceObjectEntity> extracted(String relationType, List<String> uids) {
        List<WorkspaceObjectEntity> entities = EntityUtil.getDynamicQuery(relationType, Triple.of(RelationEntity.LEFT_OBJECT, Ops.IN, uids)).fetch();
        List<WorkspaceObjectEntity> workspaceObjectEntities = new ArrayList<>();
        entities.stream().map(RelationEntity.class::cast).collect(Collectors.groupingBy(RelationEntity::getRightObjectType)).forEach((key,values)->{
            List<String> stringList = values.stream().map(RelationEntity::getRightObject).collect(Collectors.toList());
            List<WorkspaceObjectEntity> objectEntities = EntityUtil.getDynamicQuery(key, Triple.of(WorkspaceObjectEntity.UID, Ops.IN, stringList)).fetch();
            workspaceObjectEntities.addAll(objectEntities);
        });
        return workspaceObjectEntities;
    }

    /**
     * 启动通知单位或变更单位的流程
     * @param processKey 流程的key
     * @param noticeKey 指定的流程节点
     * @param entity 数据
     * @param userUid 发送的用户
     * @param title 流程标题
     * @author: 薛锦龙
     * @time: 2022/9/29
     * @return: {@link }
     */
    private void extracted( String processKey, String noticeKey, WorkspaceObjectEntity entity, String userUid,String title,String processName) {
        ProcessInstance processInstance = SpringUtil.getBean(ProcessInstanceFactory.class).create();
        ProcessInstanceReq noticeProcess = new ProcessInstanceReq();
        noticeProcess.setProcessTitle(title);
        if (StrUtil.isNotBlank(noticeKey)){
            Map<String,String> map = new HashMap<>();
            map.put(noticeKey, userUid);
            noticeProcess.setAssigneeJson(JSONUtil.toJsonStr(map));
        }
        noticeProcess.setApprovalType("release");
        noticeProcess.setProcessName(processName);
        noticeProcess.setBusinessKey(entity.getUid());
        noticeProcess.setProcessKey(processKey);//需要从流程获取
        List<WorkBaseReq> baseReqs = new ArrayList<>();
        WorkBaseReq workBaseReq = new WorkBaseReq();
        workBaseReq.setUid(entity.getUid());
        workBaseReq.setObjectType(entity.getObjectType());
        baseReqs.add(workBaseReq);
        noticeProcess.setReqList(baseReqs);
        //启动流程
        processInstance.createInstance(noticeProcess);
    }
}
