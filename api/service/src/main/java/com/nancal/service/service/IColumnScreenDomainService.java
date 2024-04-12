package com.nancal.service.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.base.Response;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.model.entity.ColumnScreenEntity;
import com.nancal.model.entity.QColumnScreenEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;

/**
 * @author： Wang Hui
 * @date： 2022/4/25 19:07
 * @description： 列设置service
 **/
public interface IColumnScreenDomainService extends IWorkspaceObjectDomainService {

    /***
     * 查询列的信息
     *
     * @author: 王辉
     * @date: 2022/4/26 12:42
     * @return:  {@link Response<ColumnScreenResp>}
     */
    @Deprecated
    default ColumnScreenResp find(AppNameEnum appName,String objectType) {
        ColumnFindReq columnFindReq = new ColumnFindReq();
        columnFindReq.setAppCode(appName.getCode());
        columnFindReq.setObjectType(objectType);
        return find(columnFindReq);
    }

    /**
     * 查询列设置
     * @param columnFindReq
     * @return
     */
    default ColumnScreenResp find(ColumnFindReq columnFindReq) {
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        QColumnScreenEntity qColumnScreenEntity = QColumnScreenEntity.columnScreenEntity;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qColumnScreenEntity.appName.eq(columnFindReq.getAppCode()));
        booleanBuilder.and( qColumnScreenEntity.ownerId.eq(userUtils.getCurrentUserId()));
        if (StrUtil.isNotBlank(columnFindReq.getObjectType())){
            booleanBuilder.and(qColumnScreenEntity.objectType.eq(columnFindReq.getObjectType()));
        }
        ColumnScreenEntity entity = SpringUtil.getBean(JPAQueryFactory.class)
                .selectFrom(qColumnScreenEntity)
                .where(booleanBuilder).fetchFirst();
        if (ObjectUtil.isEmpty(entity)) {
            return null;
        }
        ColumnScreenResp columnScreenResp = new ColumnScreenResp();
        BeanUtil.copyPropertiesIgnoreNull(entity, columnScreenResp);
        return columnScreenResp;

    }


    @Transactional
    @Override
    @Deprecated
    default BusinessObjectResp update(BusinessObjectReq req) {
        ColumnScreenReq myReq = (ColumnScreenReq) req;
        ColumnPropReq realReq = new ColumnPropReq();
        BeanUtil.copyPropertiesIgnoreNull(myReq,realReq);
        realReq.setAppCode(myReq.getAppName().getCode());
        return update(realReq);
    }

    @Transactional
    default BusinessObjectResp update(ColumnPropReq myReq) {
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);

        QColumnScreenEntity qColumnScreenEntity = QColumnScreenEntity.columnScreenEntity;
        ColumnScreenEntity entity = SpringUtil.getBean(JPAQueryFactory.class)
                .selectFrom(qColumnScreenEntity)
                .where(qColumnScreenEntity.appName.eq(myReq.getAppCode()),
                        qColumnScreenEntity.ownerId.eq(userUtils.getCurrentUserId())).fetchFirst();
        ColumnScreenResp columnScreenResp = new ColumnScreenResp();
        if (ObjectUtil.isEmpty(entity)) {
            ColumnScreenEntity columnScreenEntity = new ColumnScreenEntity();
            columnScreenEntity.setOwnerId(userUtils.getCurrentUserId());
            columnScreenEntity.setOwnerName(userUtils.getCurrentUserName());
            columnScreenEntity.setUid(IdGeneratorUtil.generate());
            columnScreenEntity.setAppName(myReq.getAppCode());
            columnScreenEntity.setDisplayProperties(myReq.getObjectType());
            columnScreenEntity.setObjectType(columnScreenEntity.getObjectType());
            entityManager.persist(columnScreenEntity);
            //组装返回数据
            BeanUtil.copyPropertiesIgnoreNull(columnScreenEntity, columnScreenResp);
            return columnScreenResp;
        }
        entity.setDisplayProperties(myReq.getDisplayProperties());
        entity.setLastUpdate(LocalDateTime.now());
        entityManager.merge(entity);
        //返回数据
        BeanUtil.copyPropertiesIgnoreNull(entity, columnScreenResp);
        return columnScreenResp;
    }

}
