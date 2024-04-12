package com.nancal.service.bo;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.model.entity.ObjectIndexEntity;
import com.nancal.model.entity.QObjectIndexEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import java.util.Objects;

@Data
@NoArgsConstructor
public class ObjectIndex extends WorkspaceObject {

    @Column(name = "object_uid", length = 64)
    private String objectUid;

    @ApiModelProperty("编号")
    private String itemId;

    /***
     * 保存或更新
     *
     * @author 徐鹏军
     * @date 2022/4/14 9:50
     * @return {@link Void}
     */
    public void saveOrUpdate() {
        if (StrUtil.isBlank(getObjectName())) {
            return;
        }
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        // 先查询数据，是否存在
        QObjectIndexEntity objectIndex = QObjectIndexEntity.objectIndexEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(objectIndex.objectUid.eq(objectUid));
        builder.and(objectIndex.delFlag.isFalse());
        builder.and(objectIndex.objectType.eq(getObjectType()));
        ObjectIndexEntity entity = jpaQueryFactory.selectFrom(objectIndex).where(builder).fetchFirst();
        if (Objects.nonNull(entity)) {
            entity.setObjectName(getObjectName());
            entity.setItemId(itemId);
            entityManager.merge(entity);
            return;
        }
        entity = new ObjectIndexEntity();
        entity.setUid(IdGeneratorUtil.generate());
        entity.setObjectUid(objectUid);
        entity.setObjectName(getObjectName());
        entity.setObjectType(getObjectType());
        entity.setItemId(itemId);
        entity.setOwnerId(userUtils.getCurrentUserId());
        entity.setOwnerName(userUtils.getCurrentUserName());
        entityManager.persist(entity);
    }

    /***
     * 删除对象索引数据
     *
     * @author 徐鹏军
     * @date 2022/4/21 9:48
     * @return
     */
    public void deleteObjectIndex() {
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        QObjectIndexEntity objectIndex = QObjectIndexEntity.objectIndexEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(objectIndex.objectUid.eq(objectUid));
        builder.and(objectIndex.delFlag.isFalse());
        builder.and(objectIndex.objectType.eq(getObjectType()));
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        ObjectIndexEntity entity = jpaQueryFactory.selectFrom(objectIndex).where(builder).fetchFirst();
        if (Objects.isNull(entity)) {
            return;
        }
        entity.setDelFlag(Boolean.TRUE);
        entityManager.merge(entity);
    }
}
