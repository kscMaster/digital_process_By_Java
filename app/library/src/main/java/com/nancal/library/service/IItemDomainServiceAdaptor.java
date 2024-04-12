package com.nancal.library.service;


import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.BusinessObjectReq;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.ItemRevisionReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.model.entity.LibraryFolderEntity;
import com.nancal.model.entity.LibraryIncludeRLEntity;
import com.nancal.model.entity.QLibraryFolderEntity;
import com.nancal.model.entity.QLibraryIncludeRLEntity;
import com.nancal.service.service.IItemDomainService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Objects;

public interface IItemDomainServiceAdaptor extends IItemDomainService {

    @Override
    @Transactional
    default WorkspaceObjectResp save(BusinessObjectReq req) {
        ItemRevisionReq myReq = (ItemRevisionReq) req;
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        WorkspaceObjectResp resp = IItemDomainService.super.save(req);
        String objectType = StrUtil.blankToDefault(myReq.getLeftObjectType(),EntityUtil.getObjectType());
        LibraryFolderEntity libraryFolderEntity = EntityUtil.getById(objectType, myReq.getLeftObject());
        if (Objects.isNull(libraryFolderEntity)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        libraryFolderEntity.setQuantity(libraryFolderEntity.getQuantity() + 1);
        entityManager.merge(libraryFolderEntity);
        return resp;
    }

    @Transactional
    default WorkspaceObjectResp insert(BusinessObjectReq req) {
        return IItemDomainService.super.save(req);
    }


    @Transactional(rollbackFor = Exception.class)
    default void statistics(String objectType,String leftObject,Integer statisticsNumber){
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        LibraryFolderEntity libraryFolderEntity = EntityUtil.getById(objectType, leftObject);
        if (Objects.isNull(libraryFolderEntity)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        libraryFolderEntity.setQuantity(statisticsNumber);
        entityManager.merge(libraryFolderEntity);
    }


    @Transactional
    @Override
    default BusinessObjectResp deleteObject(IdRequest id, AppNameEnum appName) {
        BusinessObjectResp resp = IItemDomainService.super.deleteObject(id, appName);
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        //库文件夹数量递减
        LibraryIncludeRLEntity libraryIncludeRL = jpaQueryFactory.selectFrom(QLibraryIncludeRLEntity.libraryIncludeRLEntity)
                .where(QLibraryIncludeRLEntity.libraryIncludeRLEntity.rightObject.eq(id.getUid()))
                .fetchFirst();
        LibraryFolderEntity libraryFolder = jpaQueryFactory.selectFrom(QLibraryFolderEntity.libraryFolderEntity)
                .where(QLibraryFolderEntity.libraryFolderEntity.uid.eq(libraryIncludeRL.getLeftObject())).fetchFirst();
        if (Objects.isNull(libraryFolder)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        libraryFolder.setQuantity(libraryFolder.getQuantity() <= 0 ? libraryFolder.getQuantity(): libraryFolder.getQuantity() - 1);
        SpringUtil.getBean(EntityManager.class).merge(libraryFolder);
        return resp;
    }


}
