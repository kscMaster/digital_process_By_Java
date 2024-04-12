package com.nancal.service.bo;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.model.entity.DatasetEntity;
import com.nancal.model.entity.RelationEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Triple;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Data
@ApiModel(value = "Relation 关系 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Relation extends WorkspaceObject  implements Serializable{

    @ApiModelProperty(value="关系类型",required=false)
    private String relationType;

    @ApiModelProperty(value="左对象ID",required=false)
    private String leftObject;

    @ApiModelProperty(value="左对象类型",required=false)
    private String leftObjectType;

    @ApiModelProperty(value="右对象ID",required=false)
    private String rightObject;

    @ApiModelProperty(value="右对象类型",required=false)
    private String rightObjectType;


    /***
     * 保存关系
     *
     * @param leftObject 左对象id
     * @param leftObjectType 左对象类型
     * @param rightObject 右对象id
     * @param rightObjectType 右对象类型
     * @author 徐鹏军
     * @date 2022/5/5 14:21
     * @return {@link String}
     */
    public String saveRelation(String leftObject, String leftObjectType, String rightObject, String rightObjectType) {
        String relationTable = SpringUtil.getBean(DictUtil.class).getRelation(leftObjectType, rightObjectType);
        if (StrUtil.isBlank(relationTable)) {
            return StrUtil.EMPTY;
        }
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        RelationEntity relation = ReflectUtil.newInstance(EntityUtil.getEntityPackage(relationTable));
        relation.setUid(IdGeneratorUtil.generate());
        relation.setOwnerId(userUtils.getCurrentUserId());
        relation.setOwnerName(userUtils.getCurrentUserName());
        relation.setLeftObject(leftObject);
        relation.setLeftObjectType(leftObjectType);
        relation.setRightObject(rightObject);
        relation.setRightObjectType(rightObjectType);
        SpringUtil.getBean(EntityManager.class).persist(relation);
        return relation.getUid();
    }

    /***
     * 保存关系(类似组件和版本)
     * <p>
     *     只需要传一个组件的类型，例如左对象是R006Part,那么右对象类型自动的会变成R006PartRevision
     * </p>
     * @param leftObject 左对象id
     * @param leftObjectType 左对象类型 组件的类型
     * @param rightObject 右对象id
     * @author 徐鹏军
     * @date 2022/5/5 14:21
     * @return {@link String}
     */
    public String saveRelation(String leftObject, String leftObjectType, String rightObject) {
        String rightObjectType = EntityUtil.getRevision(leftObjectType);
        return saveRelation(leftObject, leftObjectType, rightObject, rightObjectType);
    }

    private void childList(String relationType,String leftObject,String leftObjectType,List<RelationEntity> entityList){
        List<Triple<String, Ops, Object>> list = new ArrayList<>();
        list.add(Triple.of(RelationEntity.LEFT_OBJECT, Ops.EQ, leftObject));
        list.add(Triple.of(RelationEntity.LEFT_OBJECT_TYPE, Ops.EQ, leftObjectType));

        List<WorkspaceObjectEntity> relationList = EntityUtil.getDynamicQuery(relationType, list).fetch();
        if(CollUtil.isEmpty(relationList)){
            return;
        }
        relationList.stream().map(RelationEntity.class::cast).forEach(relation->{
             relationList.add(relation);
            childList(relationType,relation.getRightObject(),relation.getRightObjectType(),entityList);
        });
    }

    /**
     * 根据版次查询关联数据
     * @param relationType
     * @param leftObjectUidList
     * @param leftObjectType
     * @return
     */
    public List<RelationEntity> childListSingle(String relationType,List<String> leftObjectUidList,String leftObjectType){
        List<Triple<String, Ops, Object>> list = new ArrayList<>();
        list.add(Triple.of(RelationEntity.LEFT_OBJECT, Ops.IN, leftObjectUidList));
        list.add(Triple.of(RelationEntity.LEFT_OBJECT_TYPE, Ops.EQ, leftObjectType));
        List<WorkspaceObjectEntity> relationList = EntityUtil.getDynamicQuery(relationType, list).fetch();
        if(CollUtil.isEmpty(relationList)){
            return Collections.emptyList();
        }
        return relationList.stream().map(RelationEntity.class::cast).collect(Collectors.toList());
    }

    public List<RelationEntity> childList(String relationType,String leftObject,String leftObjectType){
        List<RelationEntity> entityList = new ArrayList<>();
        childList(relationType,leftObject,leftObjectType,entityList);
        return entityList;
    }

    /**
     * 根据relation关系递归查询
     * @param leftObject
     * @param leftObjectType
     * @param resList
     * @param currentDeep
     * @param maxDeep
     */
    private void childRelationList(String leftObject,String leftObjectType,
                                List<RelationEntity> resList,int currentDeep,int maxDeep){
        if(resList == null){
            return;
        }
        if (maxDeep > 0 && currentDeep >= maxDeep) {
            return;
        }
        int newCurrentDeep = currentDeep + 1;
        List<Pair<String, Object>> params = Arrays.asList(
                Pair.of(RelationEntity.LEFT_OBJECT, leftObject),
                Pair.of(RelationEntity.LEFT_OBJECT_TYPE, leftObjectType)
        );
        List<String> relationList = getRelationByObjectType(leftObjectType);
        for (String relation: relationList) {
            List<WorkspaceObjectEntity> relationEntityList = EntityUtil.getDynamicEqQuery(relation, params).fetch();
            if(CollUtil.isEmpty(relationList)){
                continue;
            }
            relationEntityList.stream().map(RelationEntity.class::cast).forEach(relationEntity->{
                resList.add(relationEntity);
                // 如果是数据集的话，就不在次进行递归调用
                if (!DatasetEntity.class.isAssignableFrom(EntityUtil.getEntityClass(relationEntity.getRightObjectType()))) {
                    childRelationList(relationEntity.getRightObject(),
                            relationEntity.getRightObjectType(),resList,newCurrentDeep,maxDeep);
                }
            });
        }
    }

    /**
     * @param leftObject
     * @param leftObjectType
     * @param maxDeep
     */
    public List<RelationEntity> childRelationList(String leftObject,String leftObjectType,int maxDeep){
        List<RelationEntity> relationEntityList = new ArrayList<>();
        childRelationList(leftObject,leftObjectType,relationEntityList,0,maxDeep);
        return relationEntityList;
    }


    public List<String> getRelationByObjectType(String objectType){
        List<String> relationList;
        if(RelationEntity.class.isAssignableFrom(EntityUtil.getEntityClass(objectType))){
            relationList = Collections.singletonList(objectType);
        }else{
            DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
            relationList = dictUtil.getRightObjectRelations(leftObjectType);
            if(CollUtil.isEmpty(relationList)){
                return Collections.emptyList();
            }
        }
        return relationList;
    }

    /**
     * 将关联关系转换为右对象实体数据
     * @param leftObject
     * @param leftObjectType
     * @param maxDeep
     * @return
     */
    public List<WorkspaceObjectEntity> childList(String leftObject,String leftObjectType,int maxDeep,Predicate<WorkspaceObjectEntity> filter){
        List<RelationEntity> relationEntityList = childRelationList(leftObject, leftObjectType, maxDeep);
        return relationToWorkspaceList(relationEntityList,filter);
    }

    /**
     * relationToWorkspace
     * @param relationEntityList
     * @return
     */
    private List<WorkspaceObjectEntity> relationToWorkspaceList(List<RelationEntity> relationEntityList,Predicate<WorkspaceObjectEntity> filter){
        if(CollUtil.isEmpty(relationEntityList)){
            return Collections.emptyList();
        }
        List<WorkspaceObjectEntity> entityList = new ArrayList<>();
        relationEntityList.stream().collect(Collectors.groupingBy(RelationEntity::getRightObjectType)).forEach((objectType,values)->{
            Map<String, List<RelationEntity>> rightObjectMap = values.stream().collect(Collectors.groupingBy(RelationEntity::getRightObject));
            List<String> rightUidList = values.stream().map(RelationEntity::getRightObject).collect(Collectors.toList());
            // 防止id过多，in出现超出字符长度，此处进行切割分段获取
            List<WorkspaceObjectEntity> rightObjectList = CollUtil.split(rightUidList, 500).stream()
                    .flatMap(ids -> Optional.ofNullable(EntityUtil.getByIds(rightObjectType, ids)).orElse(Collections.emptyList()).stream())
                    .collect(Collectors.toList());
            List<WorkspaceObjectEntity> subEntityList = workspaceFillRelationInfo(rightObjectMap,rightObjectList,filter);
            entityList.addAll(subEntityList);
        });
        return entityList;
    }

    /**
     * 将workspace的relation信息进行填充
     * 当存在复制粘贴的情况，需要copy一份，防止前端重复id的问题
     * @param rightObjectMap
     * @param rightObjectList
     * @return
     */
    private List<WorkspaceObjectEntity> workspaceFillRelationInfo(Map<String, List<RelationEntity>> rightObjectMap,
                                                                  List<WorkspaceObjectEntity> rightObjectList,Predicate<WorkspaceObjectEntity> filter){
        if(CollUtil.isEmpty(rightObjectMap)){
            return Collections.emptyList();
        }
        if(CollUtil.isEmpty(rightObjectList)){
            return Collections.emptyList();
        }
        List<WorkspaceObjectEntity> dataList = new ArrayList<>();
        for (WorkspaceObjectEntity entity : rightObjectList) {
            if (!rightObjectMap.containsKey(entity.getUid())) {
                continue;
            }
            if (Objects.nonNull(filter) && !filter.test(entity)) {
                continue;
            }
            List<RelationEntity> entityList = rightObjectMap.get(entity.getUid());
            // 大多数情况下会只有一个，所以此处多做了一个判断，用于视情况而定的优化吧
            if (entityList.size() == 1) {
                RelationEntity relationEntity = CollUtil.getFirst(entityList);
                entity.setLeftObject(relationEntity.getLeftObject());
                entity.setLeftObjectType(relationEntity.getLeftObjectType());
                entity.setRelationId(relationEntity.getUid());
                // 将自身加入集合
                dataList.add(entity);
            } else {
                // 针对一个右对象存在多个左对象的时候，需要将当前右对象复制一份，将虚拟id修改即可，否则在构建树的时候会出现虚拟id一样的问题
                entityList.forEach(relationEntity -> {
                    WorkspaceObjectEntity newEntity = (WorkspaceObjectEntity) ReflectUtil.newInstance(EntityUtil.getEntityClass(rightObjectType));
                    BeanUtil.copyPropertiesIgnoreNull(entity, newEntity);
                    // 将当前对象的父节点id和类型设置
                    newEntity.setLeftObject(relationEntity.getLeftObject());
                    newEntity.setLeftObjectType(relationEntity.getLeftObjectType());
                    newEntity.setRelationId(relationEntity.getUid());
                    // 将自身加入集合
                    dataList.add(newEntity);
                });
            }
        }
        return dataList;
    }

}