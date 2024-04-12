package com.nancal.service.bo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.IncludeOrMasterRLResp;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.LifeCycleStateEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.model.entity.*;
import com.nancal.service.factory.MasterRLFactory;
import com.nancal.service.factory.ObjectIndexFactory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "IncludeRL 包含关系 的BO")
public class IncludeRL extends Relation {

    @Override
    public String getRelationType() {
        return "IncludeRL";
    }



    /***
     * 删除
     * 1：查询要删除的对象
     * 2：校验状态和权限
     * 3：校验我是否有对父级的操作权限
     * 4：根据字典查询当前类型被哪些关系引用，去这些引用表中查询左对象是否存在
     * 5：删除当前对象
     * 6：删除对象的上下关系以及版本
     *
     * @param id id
     * @author: 王辉
     * @date: 2022/4/15 17:52
     * @return:  {@link IncludeOrMasterRLResp}
     */
    @Deprecated
    @Transactional
    public BusinessObjectResp delete(IdRequest id, AppNameEnum appName) {
        String objectType = EntityUtil.getObjectType();
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        //获取对象信息
        WorkspaceObjectEntity workspaceObjectEntity = EntityUtil.getById(objectType, id.getUid());
        if (Objects.isNull(workspaceObjectEntity)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        String currentUserId = userUtils.getCurrentUserId();
        if (appName == AppNameEnum.MY_WORKSPACE) {
            //校验当前用户是否有该对象的删除权限
            if (!workspaceObjectEntity.getOwnerId().equals(currentUserId)) {
                throw new ServiceException(ErrorCode.FORBIDDEN, "当前用户没有" + workspaceObjectEntity.getObjectName() + "的删除权限，操作无法完成");
            }
        }
        // 如果要删除的是组件，则判断他是否被BOM引用，如果被引用则不能为删除
        if (workspaceObjectEntity instanceof ItemEntity) {
            QBOMNodeEntity bomNode = QBOMNodeEntity.bOMNodeEntity;
            BooleanBuilder builder = new BooleanBuilder();
            builder.and(bomNode.childItem.eq(id.getUid()));
            builder.and(bomNode.delFlag.isFalse());
            if (jpaQueryFactory.selectFrom(bomNode).where(builder).fetchCount() > 0) {
                throw new ServiceException(ErrorCode.FORBIDDEN, "当前" + workspaceObjectEntity.getObjectName() + "被BOM引用，操作无法完成");
            }
        }
        //校验对象只有工作中，已驳回才可以删除
        if (!(workspaceObjectEntity.getLifeCycleState().equals(LifeCycleStateEnum.Working.name()) || workspaceObjectEntity.getLifeCycleState().equals(LifeCycleStateEnum.Obsolete.name()))) {
            throw new ServiceException(ErrorCode.E_10, "只有工作中、已驳回的对象才能删除");
        }
        // 校验我是否有对父级的操作权限
        MasterRL masterRL = SpringUtil.getBean(MasterRLFactory.class).create();
        masterRL.getParents(id.getUid(), objectType, null).forEach(entity -> {
            if (!entity.getOwnerId().equals(currentUserId)) {
                throw new ServiceException(ErrorCode.FORBIDDEN, "对象" + workspaceObjectEntity.getObjectName() + "被引用，无法执行删除");
            }
        });

        //根据对象类型，获取该对象类型所有需要校验的关系表
        List<String> checkAllRls = dictUtil.getCheckAllRLs(objectType);
        if (!CollUtil.isEmpty(checkAllRls)) {
            //校验关系关联的右对象是当前对象，是否存在左对象，如果存在，报错
            checkAllRlsLeftObjects(checkAllRls, workspaceObjectEntity);
        }
        //删除对象（逻辑删除）
        workspaceObjectEntity.setDelFlag(Boolean.TRUE);
        entityManager.merge(workspaceObjectEntity);
        //对象索引里只存入了版本
        if (workspaceObjectEntity instanceof ItemRevisionEntity) {
            //删除对象对应对象索引
            SpringUtil.getBean(ObjectIndexFactory.class).create(workspaceObjectEntity).deleteObjectIndex();
        }
        //删除对象的上下关系以及版本
        List<String> relations = dictUtil.getUpDownAllRLs(objectType);
        deleteUpDownAllRls(id.getUid(), relations);

        if (appName == AppNameEnum.LIBRARY) {
            //库文件夹数量递减
            LibraryIncludeRLEntity libraryIncludeRL = jpaQueryFactory.selectFrom(QLibraryIncludeRLEntity.libraryIncludeRLEntity)
                    .where(QLibraryIncludeRLEntity.libraryIncludeRLEntity.rightObject.eq(id.getUid()))
                    .fetchFirst();
            LibraryFolderEntity libraryFolder = jpaQueryFactory.selectFrom(QLibraryFolderEntity.libraryFolderEntity)
                    .where(QLibraryFolderEntity.libraryFolderEntity.uid.eq(libraryIncludeRL.getLeftObject())).fetchFirst();
            if (Objects.isNull(libraryFolder)) {
                throw new ServiceException(ErrorCode.E_12);
            }
            libraryFolder.setQuantity(libraryFolder.getQuantity() <= 0 ? libraryFolder.getQuantity() : libraryFolder.getQuantity() - 1);
            entityManager.merge(libraryFolder);
        }

        //创建响应对象
        Object instance = ReflectUtil.newInstance(EntityUtil.getRespPackage(objectType));
        workspaceObjectEntity.setDelFlag(Boolean.TRUE);
        cn.hutool.core.bean.BeanUtil.copyProperties(workspaceObjectEntity, instance);
        return (BusinessObjectResp) instance;
    }

    /***
     *检查关系关联的右对象是当前对象，是否存在左对象，如果存在，报错：对象{displayName}被{左对象displayName}引用，无法执行删除
     *
     * @param relations 此对象所有关联的关系表
     * @param workspaceObjectEntity 对象信息
     * @author: 王辉
     * @date: 2022/4/18 13:46
     * @return:  {void}
     */
    @Deprecated
    private void checkAllRlsLeftObjects(List<String> relations, WorkspaceObjectEntity workspaceObjectEntity) {
        for (String relation : relations) {
            List<WorkspaceObjectEntity> workspaceObjectEntities = EntityUtil.getDynamicQuery(relation, clazz -> {
                BooleanBuilder where = new BooleanBuilder();
                where.and(new BeanPath<Object>(clazz, BusinessObjectEntity.DEL_FLAG).eq(Boolean.FALSE));
                where.and(new BeanPath<Object>(clazz, RelationEntity.RIGHT_OBJECT).eq(workspaceObjectEntity.getUid()));
                return where;
            }).fetch();
            if (CollUtil.isEmpty(workspaceObjectEntities)) {
                continue;
            }
            // 按照左对象类型进行分组，这样就可以将查询同一张表的数据进行一次查询
            workspaceObjectEntities.stream().map(RelationEntity.class::cast).collect(Collectors.groupingBy(RelationEntity::getLeftObjectType)).forEach((leftObjectType, values) -> {
                // 获取左对象id列表
                List<String> leftUidList = values.stream().map(RelationEntity::getLeftObject).collect(Collectors.toList());
                // 分别去查询各自表中获取数据
                List<WorkspaceObjectEntity> leftObjects = EntityUtil.getDynamicQuery(leftObjectType, clazz -> {
                    BooleanBuilder where = new BooleanBuilder();
                    where.and(new BeanPath<Object>(clazz, BusinessObjectEntity.DEL_FLAG).eq(Boolean.FALSE));
                    where.and(new BeanPath<Object>(clazz, BusinessObjectEntity.UID).in(leftUidList));
                    return where;
                }).fetch();
                if (CollUtil.isEmpty(leftObjects)) {
                    return;
                }
                WorkspaceObjectEntity entity = CollUtil.getFirst(leftObjects);
                throw new ServiceException(ErrorCode.FORBIDDEN, "对象" + workspaceObjectEntity.getObjectName() + "被" + entity.getObjectName() + "引用，无法执行删除");
            });
        }
    }

    /***
     *删除上下关系以及右对象版本
     *
     * @param uid 当前对象的uid
     * @param relations 当前对象上下级的关系表
     * @author: 王辉
     * @date: 2022/4/18 17:14
     * @return:  {void}
     */
    public void deleteUpDownAllRls(String uid, List<String> relations) {
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
        for (String relation : relations) {
            List<WorkspaceObjectEntity> relationDataList = EntityUtil.getDynamicQuery(relation, clazz -> {
                BooleanBuilder where = new BooleanBuilder();
                where.and(new BeanPath<Object>(clazz, BusinessObjectEntity.DEL_FLAG).eq(Boolean.FALSE));
                where.and(new BeanPath<Object>(clazz, RelationEntity.RIGHT_OBJECT).eq(uid).or(new BeanPath<Object>(clazz, RelationEntity.LEFT_OBJECT).eq(uid)));
                return where;
            }).fetch();
            if (CollUtil.isEmpty(relationDataList)) {
                continue;
            }
            relationDataList.stream().map(RelationEntity.class::cast).collect(Collectors.groupingBy(RelationEntity::getRightObjectType)).forEach((rightObjectType, values) -> {
                // 获取右对象id列表
                List<String> rightUidList = values.stream().map(RelationEntity::getRightObject).collect(Collectors.toList());
                // 分别去查询各自表中获取数据
                List<WorkspaceObjectEntity> rightObjects = EntityUtil.getDynamicQuery(rightObjectType, clazz -> {
                    BooleanBuilder where = new BooleanBuilder();
                    where.and(new BeanPath<Object>(clazz, BusinessObjectEntity.DEL_FLAG).eq(Boolean.FALSE));
                    where.and(new BeanPath<Object>(clazz, BusinessObjectEntity.UID).in(rightUidList));
                    return where;
                }).fetch();
                if (CollUtil.isEmpty(rightObjects)) {
                    return;
                }
                for (WorkspaceObjectEntity rightObject : rightObjects) {
                    if (rightObject instanceof ItemRevisionEntity) {
                        rightObject.setDelFlag(Boolean.TRUE);
                        //删除版本
                        entityManager.merge(rightObject);
                        //删除版本的对象索引
                        SpringUtil.getBean(ObjectIndexFactory.class).create(rightObject).deleteObjectIndex();
                        // 如果是工艺路线版本,则删除对于的车间
//                        if (rightObject instanceof R006ProcessRouteRevisionEntity) {
//                            QR006RouteWorkshopRLEntity workshopRLEntity = QR006RouteWorkshopRLEntity.r006RouteWorkshopRLEntity;
//                            BooleanBuilder builder = new BooleanBuilder();
//                            builder.and(workshopRLEntity.leftObject.eq(rightObject.getUid()));
//                            List<R006RouteWorkshopRLEntity> rlEntitys = jpaQueryFactory.selectFrom(workshopRLEntity).where(builder).fetch();
//                            if (CollUtil.isEmpty(rlEntitys)) {
//                                continue;
//                            }
//                            rlEntitys.forEach(data -> {
//                                data.setDelFlag(Boolean.TRUE);
//                                entityManager.merge(data);
//                            });
//                            List<String> uids = rlEntitys.stream().map(R006RouteWorkshopRLEntity::getRightObject).collect(Collectors.toList());
//                            QR006WorkshopEntryEntity entryEntity = QR006WorkshopEntryEntity.r006WorkshopEntryEntity;
//                            builder = new BooleanBuilder();
//                            builder.and(entryEntity.uid.in(uids));
//                            List<R006WorkshopEntryEntity> entityList = jpaQueryFactory.selectFrom(entryEntity).where(builder).fetch();
//                            if (CollUtil.isEmpty(entityList)) {
//                                continue;
//                            }
//                            entityList.forEach(data -> {
//                                data.setDelFlag(Boolean.TRUE);
//                                entityManager.merge(data);
//                            });
//                        }
                    }
                }
            });
            //删除关系数据
            for (WorkspaceObjectEntity entity : relationDataList) {
                entity.setDelFlag(Boolean.TRUE);
                entityManager.merge(entity);
            }
        }
    }
}

