package com.nancal.service.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.ClipboardReq;
import com.nancal.api.model.Gte4ResponsibleRlReq;
import com.nancal.api.model.HomeFolderResp;
import com.nancal.api.model.PasteReq;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.config.AppConfig;
import com.nancal.common.constants.MsgConstant;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.IncludeRLEntity;
import com.nancal.model.entity.RelationEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.querydsl.core.types.Ops;
import org.apache.commons.lang3.tuple.Triple;
import com.nancal.model.entity.WorkspaceObjectEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface ISynergismDomainService extends IWorkspaceObjectDomainService{
    /**
     * 指派负责人（协同编辑）
     * @param
     * @author: 薛锦龙
     * @time: 2022/9/27
     * @return: {@link }
     */
    @Transactional
    default void assignPersons(Gte4ResponsibleRlReq req){
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        Map<String, String> assignmentExclusion = dictUtil.getCodeValueMap("AssignmentExclusion");
        if (assignmentExclusion.containsKey(req.getGte4LObjectType())){
            throw new ServiceException(ErrorCode.E_10,"当前数据不支持指派");
        }
        WorkspaceObjectEntity entity = EntityUtil.getById(req.getGte4LObjectType(), req.getGte4LeftObject());
        if(ObjectUtils.isEmpty(entity)){
            throw new ServiceException(ErrorCode.E_12);
        }
        List<String> userIdList = new ArrayList<>();
        req.getResponsibleReqs().forEach(data->{
            Gte4ResponsibleRlReq responsibleRlReq = new Gte4ResponsibleRlReq();
            BeanUtil.copyPropertiesIgnoreNull(req,responsibleRlReq);
            responsibleRlReq.setGte4ResponsibleId(data.getGte4ResponsibleId());
            responsibleRlReq.setGte4ResponsibleName(data.getGte4ResponsibleName());
            IWorkspaceObjectDomainService.super.save(responsibleRlReq);
            userIdList.add(data.getGte4ResponsibleId());
        });
        sendMsg(MsgConstant.WORK_ASSIGN_PERSON,userIdList,processParam(entity));
    }

    default Map<String,Object> processParam( WorkspaceObjectEntity entity){
        Map<String,Object> paramMap = new HashMap<>();
        AppConfig appConfig = SpringUtil.getBean(AppConfig.class);
        StringBuilder rootUrl = new StringBuilder();
        rootUrl.append(appConfig.getFeBaseUrl());
        rootUrl.append("/?");
        rootUrl.append("openFlag=").append("associatedFile");
        rootUrl.append("&uid=").append(entity.getUid());
        rootUrl.append("&objectType=").append(entity.getObjectType());
        paramMap.put("rootUrl",rootUrl.toString());
        paramMap.put("objectName",entity.getObjectName());
        return paramMap;
    }

    /**
     * 指派文件
     * @param id
     * @author: 薛锦龙
     * @time: 2022/9/28
     * @return: {@link }
     */
    @Transactional
    default void associatedFile(IdRequest id){
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        Map<String, String> assignmentExclusion = dictUtil.getCodeValueMap("AssignmentExclusion");
        if (assignmentExclusion.containsKey(id.getObjectType())){
            throw new ServiceException(ErrorCode.E_10,"当前数据不支持指派");
        }
        IHomeFolderDomainService homeFolderDomainService = SpringUtil.getBean(IHomeFolderDomainService.class);
        IIncludeRLDomainService includeRLDomainService = SpringUtil.getBean(IIncludeRLDomainService.class);
        HomeFolderResp byOwnerId = homeFolderDomainService.getByOwnerId();
        List<Triple<String, Ops,Object>> triples = Arrays.asList(
                Triple.of(RelationEntity.LEFT_OBJECT,Ops.EQ,byOwnerId.getUid()),
                Triple.of(RelationEntity.LEFT_OBJECT_TYPE,Ops.EQ,byOwnerId.getObjectType()),
                Triple.of(RelationEntity.RIGHT_OBJECT,Ops.EQ,id.getUid()),
                Triple.of(RelationEntity.RIGHT_OBJECT_TYPE,Ops.EQ,id.getObjectType())
        );
        WorkspaceObjectEntity entity = EntityUtil.getDynamicQuery(new IncludeRLEntity().getObjectType(), triples).fetchFirst();
        if (ObjectUtil.isNotEmpty(entity)){
            return;
        }
        ClipboardReq req = new ClipboardReq();
        req.setLeftObject(byOwnerId.getUid());
        req.setLeftObjectType(byOwnerId.getObjectType());
        PasteReq pasteReq = new PasteReq();
        pasteReq.setUid(id.getUid());
        pasteReq.setObjectType(id.getObjectType());
        List<PasteReq> pasteReqs = new ArrayList<>();
        pasteReqs.add(pasteReq);
        req.setRightObjects(pasteReqs);
        includeRLDomainService.paste(req);
    }
}
