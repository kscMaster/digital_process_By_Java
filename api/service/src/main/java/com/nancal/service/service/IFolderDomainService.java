package com.nancal.service.service;

import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.FolderReq;
import com.nancal.api.model.BusinessObjectReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.api.utils.EntityUtil;
import com.nancal.service.factory.IncludeRLFactory;
import org.springframework.transaction.annotation.Transactional;

public interface IFolderDomainService extends IWorkspaceObjectDomainService {

    @Transactional
    @Override
    default WorkspaceObjectResp save(BusinessObjectReq req) {
        FolderReq folderReq = (FolderReq) req;
        //创建文件夹
        WorkspaceObjectResp save = IWorkspaceObjectDomainService.super.save(folderReq);
        IncludeRLFactory includeRLFactory = SpringUtil.getBean(IncludeRLFactory.class);
        //创建关系
        includeRLFactory.create().saveRelation(folderReq.getLeftObject(),folderReq.getLeftObjectType(),save.getUid(), EntityUtil.getObjectType());
        save.setLeftObject(folderReq.getLeftObject());
        save.setLeftObjectType(folderReq.getLeftObjectType());
        return save;
    }
}
