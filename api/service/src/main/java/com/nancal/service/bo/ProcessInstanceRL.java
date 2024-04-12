package com.nancal.service.bo;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.ProcessInstanceRLResp;
import com.nancal.api.model.ProcessInstanceResp;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.annotation.OrderField;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.model.entity.*;
import com.nancal.service.factory.ProcessInstanceRLFactory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Triple;

import javax.persistence.EntityManager;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "ProcessInstanceRL 流程实例和工作对象关系 的BO,左对象为流程实例ID，右对象为工作对象")
public class ProcessInstanceRL extends Relation {
    @Override
    public String getRelationType() {
        return "ProcessInstanceRL";
    }

    @Override
    public String getObjectType() {
        return "ProcessInstanceRL";
    }

    /***
     * 创建流程关联关系表
     *
     * @param processInstanceEntity 流程实例
     * @param entity 具体对象
     * @author 徐鹏军
     * @date 2022/5/25 16:45
     * @return
     */
    public <T extends WorkspaceObjectEntity> void create(ProcessInstanceEntity processInstanceEntity, T entity) {
        if (Objects.isNull(entity)) {
            return;
        }
        create(processInstanceEntity, Collections.singletonList(entity));
    }

    /***
     * 创建流程关联关系表
     *
     * @param processInstanceEntity 流程实例
     * @param entitys 具体对象
     * @author 徐鹏军
     * @date 2022/5/25 16:45
     * @return
     */
    public <T extends WorkspaceObjectEntity> void create(ProcessInstanceEntity processInstanceEntity, List<T> entitys) {
        if (CollUtil.isEmpty(entitys)) {
            return;
        }
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        for (WorkspaceObjectEntity entity : entitys) {
            // 创建流程实例关系
            ProcessInstanceRLEntity processInstanceRLEntityChild = new ProcessInstanceRLEntity();
            processInstanceRLEntityChild.setUid(IdGeneratorUtil.generate());
            processInstanceRLEntityChild.setObjectName(entity.getObjectName());
            processInstanceRLEntityChild.setLeftObject(processInstanceEntity.getUid());
            processInstanceRLEntityChild.setLeftObjectType(processInstanceEntity.getObjectType());
            processInstanceRLEntityChild.setOwnerId(userUtils.getCurrentUserId());
            processInstanceRLEntityChild.setOwnerName(userUtils.getCurrentUserName());
            processInstanceRLEntityChild.setRightObject(entity.getUid());
            processInstanceRLEntityChild.setRightObjectType(entity.getObjectType());
            processInstanceRLEntityChild.setLifeCycleState(processInstanceEntity.getLifeCycleState());
            entityManager.persist(processInstanceRLEntityChild);
            //更新当前零组件对象的状态
            entity.setLifeCycleState(processInstanceEntity.getLifeCycleState());
            entityManager.merge(entity);
        }
    }

    /***
     * 创建流程关联关系表
     *
     * @param processInstanceUid 流程实例id
     * @param entitys 具体对象
     * @author 徐鹏军
     * @date 2022/5/25 16:45
     * @return
     */
    public <T extends WorkspaceObjectEntity> void create(String processInstanceUid, List<T> entitys) {
        if (CollUtil.isEmpty(entitys)) {
            return;
        }
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        String objectType = EntityUtil.getObjectType(ProcessInstanceEntity.class);
        for (WorkspaceObjectEntity entity : entitys) {
            // 创建流程实例关系
            ProcessInstanceRLEntity processInstanceRLEntityChild = new ProcessInstanceRLEntity();
            processInstanceRLEntityChild.setUid(IdGeneratorUtil.generate());
            processInstanceRLEntityChild.setObjectName(entity.getObjectName());
            processInstanceRLEntityChild.setLeftObject(processInstanceUid);
            processInstanceRLEntityChild.setLeftObjectType(objectType);
            processInstanceRLEntityChild.setOwnerId(userUtils.getCurrentUserId());
            processInstanceRLEntityChild.setOwnerName(userUtils.getCurrentUserName());
            processInstanceRLEntityChild.setRightObject(entity.getUid());
            processInstanceRLEntityChild.setRightObjectType(entity.getObjectType());
            processInstanceRLEntityChild.setLifeCycleState(entity.getLifeCycleState());
            entityManager.persist(processInstanceRLEntityChild);
        }
    }

    /**
     * 通过左对象获取流程中间表信息
     * @param leftObject 左对象
     * @author: 薛锦龙
     * @time: 2022/7/4
     * @return: {@link List< ProcessInstanceRLResp>}
     */
    public List<ProcessInstanceRLResp> getLeftObjectType(String leftObject){
        QProcessInstanceRLEntity qProcessInstanceRLEntity = QProcessInstanceRLEntity.processInstanceRLEntity;
        BooleanBuilder where = new BooleanBuilder();
        where.and(qProcessInstanceRLEntity.delFlag.isFalse());
        where.and(qProcessInstanceRLEntity.leftObject.eq(leftObject));
        List<ProcessInstanceRLEntity> entities = SpringUtil.getBean(JPAQueryFactory.class).selectFrom(qProcessInstanceRLEntity).where(where).fetch();
        List<ProcessInstanceRLResp> resps = entities.stream().map(data -> {
            ProcessInstanceRLResp resp = new ProcessInstanceRLResp();
            BeanUtil.copyPropertiesIgnoreNull(data, resp);
            return resp;
        }).collect(Collectors.toList());
        return resps;
    }

    /**
     * 通过版本获取流程数据
     * @param rightUid 版本的uid集合
     * @param objectType 版本类型
     * @return
     */
    public List<WorkspaceObjectEntity> getRightType(List<String> rightUid,String objectType){
        String type = new ProcessInstanceRLEntity().getObjectType();
        List<Triple<String, Ops,Object>> triples = new ArrayList<>();
        triples.add(Triple.of(RelationEntity.RIGHT_OBJECT,Ops.IN,rightUid));
        triples.add(Triple.of(RelationEntity.RIGHT_OBJECT_TYPE,Ops.EQ,objectType));
        OrderSpecifier order = new OrderSpecifier(Order.DESC, ExpressionUtils.path(EntityUtil.getEntityClass(type),WorkspaceObjectEntity.CREATION_DATE));
        List<WorkspaceObjectEntity> entitiesRl = EntityUtil.getDynamicQuery(type, triples).orderBy(order).fetch();
        return entitiesRl;
    }


}
