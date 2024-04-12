package com.nancal.service.service;


import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.HomeFolderResp;
import com.nancal.api.model.IncludeOrMasterRlReq;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.model.entity.HomeFolderEntity;
import com.nancal.model.entity.QHomeFolderEntity;
import com.nancal.service.dao.HomeFolderEntityRepository;
import com.nancal.service.factory.MasterRLFactory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Objects;

public interface IHomeFolderDomainService extends IWorkspaceObjectDomainService {

    /***
     * 获取个人工作台顶级HOME文件夹对象
     *
     * @author 徐鹏军
     * @date 2022/3/31 14:22
     * @return {@link HomeFolderResp}
     */
    @Transactional
    default HomeFolderResp getByOwnerId() {
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);

        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        QHomeFolderEntity homeFolder = QHomeFolderEntity.homeFolderEntity;
        BooleanBuilder where = new BooleanBuilder();
        where.and(homeFolder.delFlag.isFalse());
        where.and(homeFolder.ownerId.eq(userUtils.getCurrentUserId()));
        // 获取我的顶级HOME文件夹
        HomeFolderEntity homeFolderEntity = jpaQueryFactory.selectFrom(homeFolder).where(where).fetchFirst();
        HomeFolderResp homeFolderResp = new HomeFolderResp();
        if (Objects.isNull(homeFolderEntity)) {
            // 创建我的HOME顶级文件夹
            homeFolderEntity = new HomeFolderEntity();
            homeFolderEntity.setOwnerId(userUtils.getCurrentUserId());
            homeFolderEntity.setOwnerName(userUtils.getCurrentUserName());
            homeFolderEntity.setObjectName("Home");
            homeFolderEntity.setUid(IdGeneratorUtil.generate());
            entityManager.persist(homeFolderEntity);
            homeFolderResp.setHasChildren(false);
        } else {
            MasterRLFactory masterRLFactory = SpringUtil.getBean(MasterRLFactory.class);
            // 获取home下是否存在子节点
            boolean hasChildren = masterRLFactory.create().hasChildren(homeFolderEntity.getUid(), homeFolderEntity.getObjectType(), true);
            homeFolderResp.setHasChildren(hasChildren);
        }
        BeanUtil.copyPropertiesIgnoreNull(homeFolderEntity,homeFolderResp);
        homeFolderResp.setRelationId(IdGeneratorUtil.generate());
        if (homeFolderResp.isHasChildren()) {
            IncludeOrMasterRlReq req = new IncludeOrMasterRlReq();
            req.setObjectType(homeFolderResp.getObjectType());
            req.setUid(homeFolderResp.getUid());
            homeFolderResp.setChildren(SpringUtil.getBean(IIncludeRLDomainService.class).getIncludeByHomeId(req));
        }
        return homeFolderResp;
    }
}
