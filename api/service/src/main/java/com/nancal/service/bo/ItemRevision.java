package com.nancal.service.bo;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.model.entity.*;
import com.nancal.service.factory.DatasetFileRLFactory;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.factory.SpecificationRLFactory;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Data
@ApiModel(value = "ItemRevision 零组件版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ItemRevision extends WorkspaceObject {

    @ApiModelProperty(value = "零组件号")
    private String itemId;

    @ApiModelProperty(value = "零组件版本号")
    private String revisionId;

    @ApiModelProperty(value = "单位")
    private String uom;

    @ApiModelProperty(value = "版次")
    private String sequence;

    @ApiModelProperty(value = "零组件版本状态")
    private String itemRevState;

    @ApiModelProperty(value = "是否激活")
    private Boolean active;

    /***
     * 根据版本获取对应的文件数据集
     *
     * @param uid 版本id
     * @param leftObjectType 左对象类型
     * @author 徐鹏军
     * @date 2022/4/12 18:20
     * @return {@link List< FileStorage >}
     */
    public List<FileStorage> getFileStorage(String uid, String leftObjectType) {
        List<WorkspaceObjectEntity> allSequences = getAllSequences(uid, leftObjectType);
        if(CollUtil.isEmpty(allSequences)){
            return Collections.emptyList();
        }
        SpecificationRL specificationRL= SpringUtil.getBean(SpecificationRLFactory.class).create();
        List<WorkspaceObjectEntity> entityList = specificationRL.getRightObjectList(
                allSequences.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList()), leftObjectType);
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        List<FileStorage> list = new ArrayList<>();
        DatasetFileRL datasetFileRL = SpringUtil.getBean(DatasetFileRLFactory.class).create();
        entityList.stream().collect(Collectors.groupingBy(WorkspaceObjectEntity::getObjectType)).forEach((objectType,values)->{
            List<String> uidList = values.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
            List<FileStorage> fileStorages = datasetFileRL.getFileStorages(uidList, objectType);
            list.addAll(Optional.ofNullable(fileStorages).orElse(new ArrayList<>()));
        });
        return list;
    }


    /**
     * 没有版本的数据获取数据集
     * @param uid
     * @param ObjectType
     * @author: 薛锦龙
     * @time: 2022/9/29
     * @return: {@link List< FileStorage>}
     */
    public List<FileStorage> getFileStorageNoRevision(String uid, String ObjectType) {
        SpecificationRL specificationRL= SpringUtil.getBean(SpecificationRLFactory.class).create();
        List<WorkspaceObjectEntity> entityList = specificationRL.getRightObjectList(
               CollUtil.newArrayList(uid) , ObjectType);
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        List<FileStorage> list = new ArrayList<>();
        DatasetFileRL datasetFileRL = SpringUtil.getBean(DatasetFileRLFactory.class).create();
        entityList.stream().collect(Collectors.groupingBy(WorkspaceObjectEntity::getObjectType)).forEach((objectType,values)->{
            List<String> uidList = values.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
            List<FileStorage> fileStorages = datasetFileRL.getFileStorages(uidList, objectType);
            list.addAll(Optional.ofNullable(fileStorages).orElse(new ArrayList<>()));
        });
        return list;
    }

    /***
     * 根据右对象获取左对象
     *
     * @param uid 版本id
     * @param objectType 版本类型
     * @author 徐鹏军
     * @date 2022/4/28 14:04
     * @return {@link ItemEntity}
     */
    public ItemEntity getLeftObjectByRightObject(String uid, String objectType) {
        Map<String, WorkspaceObjectEntity> dataMap = getLeftObjectMap(Collections.singletonList(uid), objectType);
        if (CollUtil.isEmpty(dataMap)) {
            return null;
        }
        WorkspaceObjectEntity objectEntity = dataMap.getOrDefault(uid, null);
        return Objects.isNull(objectEntity) ? null : (ItemEntity) objectEntity;
    }

    /***
     * 根据右对象获取左对象
     *
     * @param uid 版本id
     * @param objectType 版本类型
     * @author 徐鹏军
     * @date 2022/4/28 14:04
     * @return {@link ItemEntity}
     */
    public ItemEntity getLeftObjectByRightObject(String uid, String objectType,String leftObjectType) {
        Map<String, WorkspaceObjectEntity> dataMap = getLeftObjectMap(Collections.singletonList(uid), leftObjectType, objectType);
        if (CollUtil.isEmpty(dataMap)) {
            return null;
        }
        WorkspaceObjectEntity objectEntity = dataMap.getOrDefault(uid, null);
        return Objects.isNull(objectEntity) ? null : (ItemEntity) objectEntity;
    }

    /***
     * 根据右对象获取左对象,key:是版本id,value：是对应的左对象数据
     *
     * @param uids 版本ids
     * @param objectType 版本类型
     * @author 徐鹏军
     * @date 2022/4/28 14:04
     * @return {@link List<WorkspaceObjectEntity>}
     */
    public List<WorkspaceObjectEntity> getLeftObjects(List<String> uids, String objectType) {
        Map<String, WorkspaceObjectEntity> dataMap = getLeftObjectMap(uids, objectType);
        if (CollUtil.isEmpty(dataMap)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(dataMap.values());
    }

    /***
     * 根据右对象获取左对象,key:是版本id,value：是对应的左对象数据
     *
     * @param uids 版本ids
     * @param objectType 版本类型
     * @author 徐鹏军
     * @date 2022/4/28 14:04
     * @return {@link Map<String, WorkspaceObjectEntity>}
     */
    public Map<String, WorkspaceObjectEntity> getLeftObjectMap(List<String> uids, String objectType) {
        return getLeftObjectMap(uids, EntityUtil.getObjectTypeByRevisionType(objectType), objectType);
    }

    /***
     * 根据右对象获取左对象
     *
     * @param uids 版本ids
     * @param leftObjectType 左对象类型
     * @param rightObjectType 右对象类型
     * @author 徐鹏军
     * @date 2022/4/28 14:04
     * @return {@link List<WorkspaceObjectEntity>}
     */
    public List<WorkspaceObjectEntity> getLeftObjects(List<String> uids, String leftObjectType, String rightObjectType) {
        Map<String, WorkspaceObjectEntity> dataMap = getLeftObjectMap(uids, leftObjectType, rightObjectType);
        if (CollUtil.isEmpty(dataMap)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(dataMap.values());
    }

    /***
     * 根据ItemId集合和左对象类型，获取它们最新的零组件版本
     *
     * @param itemIds 组件的id
     * @param objectType 做对象类型
     * @author 徐鹏军
     * @date 2022/4/12 17:29
     * @return {@link Item}
     */

    public Map<String,List<Gte4MfgStepEntity>> listByItemIds(List<String> itemIds, String objectType) {
        List<Triple<String, Ops, Object>> paramList = Arrays.asList(
                Triple.of(ItemRevisionEntity.ITEM_ID, Ops.IN, itemIds),
                Triple.of(ItemRevisionEntity.DEL_FLAG, Ops.EQ, false)
        );
        List<WorkspaceObjectEntity> entityList = EntityUtil.getDynamicQuery(objectType, paramList).fetch();
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyMap();
        }
        return entityList.stream().map(Gte4MfgStepEntity.class::cast).collect(Collectors.groupingBy(Gte4MfgStepEntity::getItemId));
    }

    /***
     * 根据右对象获取左对象,key:是版本id,value：是对应的左对象数据
     * <p>
     *     1:先根据左右对象类型，查询字典，获取他们的中间表
     *     2:右对象类型和id查询中间表数据
     *     3：根据找到的中间表，获取左对象id
     *     4:查询做对象数据
     *     5：按照入参对象封装成map返回
     * </p>
     *
     * @param uids 版本ids
     * @param leftObjectType 左对象类型
     * @param rightObjectType 右对象类型
     * @author 徐鹏军
     * @date 2022/4/28 14:04
     * @return {@link Map<String, WorkspaceObjectEntity>}
     */
    public Map<String, WorkspaceObjectEntity> getLeftObjectMap(List<String> uids, String leftObjectType, String rightObjectType) {
        String relation = SpringUtil.getBean(DictUtil.class).getRelation(leftObjectType, rightObjectType);
        if (StrUtil.isBlank(relation)) {
            return Collections.emptyMap();
        }
        List<Triple<String, Ops, Object>> params = Arrays.asList(
                Triple.of(RelationEntity.RIGHT_OBJECT, Ops.IN, uids),
                Triple.of(RelationEntity.RIGHT_OBJECT_TYPE, Ops.EQ, rightObjectType),
                Triple.of(RelationEntity.LEFT_OBJECT_TYPE, Ops.EQ, leftObjectType)
        );
        // 查询中间表获取左对象数据
        List<WorkspaceObjectEntity> entityList = EntityUtil.getDynamicQuery(relation, params).fetch();
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyMap();
        }
        List<RelationEntity> relationList = entityList.stream().map(data -> (RelationEntity) data).collect(Collectors.toList());
        // 获取左对象id
        List<String> leftObjects = relationList.stream().map(RelationEntity::getLeftObject).collect(Collectors.toList());
        List<WorkspaceObjectEntity> itemList = EntityUtil.getDynamicQuery(leftObjectType, Triple.of(BusinessObjectEntity.UID, Ops.IN, leftObjects)).fetch();
        if (CollUtil.isEmpty(itemList)) {
            return Collections.emptyMap();
        }
        Map<String, WorkspaceObjectEntity> itemMap = itemList.stream().collect(Collectors.toMap(WorkspaceObjectEntity::getUid, Function.identity()));
        // 将左右对象id转成map
        Map<String, String> idMap = relationList.stream().collect(Collectors.toMap(RelationEntity::getRightObject, RelationEntity::getLeftObject, (key1, key2) -> key1));
        Map<String, WorkspaceObjectEntity> dataMap = new HashMap<>();
        idMap.forEach((rightObject, leftObject) -> {
            if (itemMap.containsKey(leftObject)) {
                dataMap.put(rightObject, itemMap.get(leftObject));
            }
        });
        return dataMap;
    }

    /***
     * 获取激活的版本
     *
     * @param uid 版本id
     * @param objectType 版本类型
     * @author 徐鹏军
     * @date 2022/4/28 14:04
     * @return {@link WorkspaceObjectEntity}
     */
    public WorkspaceObjectEntity getActiveRevision(String uid, String objectType) {
        ItemRevisionEntity revision = EntityUtil.getById(objectType, uid);
        if (Objects.isNull(revision)) {
            return null;
        }
        // 如果本身就是激活的，那就直接返回
        if (Boolean.TRUE.equals(revision.getActive())) {
            return revision;
        }
        // 由于同一个版本的版本号相同，并且同一组件图号也相同。根据此条件查询
        List<Pair<String, Object>> params = Arrays.asList(
                Pair.of(ItemRevisionEntity.REVISION_ID, revision.getRevisionId()),
                Pair.of(ItemRevisionEntity.ITEM_ID, revision.getItemId()),
                Pair.of(ItemRevisionEntity.ACTIVE, true)
        );
        return EntityUtil.getDynamicEqQuery(objectType, params).fetchFirst();
    }

    /**
     * 获取激活版本的数据
     * @param uids 版本uid集合
     * @param objectType 版本类型
     * @param lifeState 生命周期状态
     * @author: 薛锦龙
     * @time: 2022/9/30
     * @return: {@link List< WorkspaceObjectEntity>}
     */
    public List<WorkspaceObjectEntity> getActiveRevisions(List<String> uids, String objectType,List<String> lifeState) {
        List<WorkspaceObjectEntity> entities = new ArrayList<>();
        List<WorkspaceObjectEntity> revisions = EntityUtil.getByIds(objectType, uids);
        if (CollUtil.isEmpty(revisions)) {
            return null;
        }
        // 如果本身就是激活的，那就直接返回
        revisions.stream().map(ItemRevisionEntity.class::cast).forEach(data->{
            if (Boolean.TRUE.equals(data.getActive())) {
                entities.add(data);
                return ;
            }
            //获取激活的版次
            List<Triple<String,Ops,Object>> params = new ArrayList<>();
            params.add(Triple.of(ItemRevisionEntity.REVISION_ID,Ops.EQ ,data.getRevisionId()));
            params.add(Triple.of(ItemRevisionEntity.ITEM_ID,Ops.EQ ,data.getItemId()));
            params.add(Triple.of(ItemRevisionEntity.ACTIVE, Ops.EQ,true));
            if(CollUtil.isNotEmpty(lifeState)){
                params.add(Triple.of(ItemRevisionEntity.LIFE_CYCLE_STATE,Ops.IN,lifeState));
            }
            WorkspaceObjectEntity entity = EntityUtil.getDynamicQuery(data.getObjectType(), params).fetchFirst();
            entities.add(entity);
        });
        return entities;
    }


    /***
     * true有编辑零组件的图代号权限，false没有编辑零组件的图代号权限
     *
     * @param uid 主键id
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/12 17:29
     * @return {@link boolean}
     */
    public boolean editItemIdPermission(String uid,String objectType) {
        // 只要存在已发布的，则没有编辑权限
        ItemEntity itemEntity = getLeftObjectByRightObject(uid, objectType);
        if (Objects.isNull(itemEntity)) {
            return true;
        }
        Item item = SpringUtil.getBean(ItemFactory.class).create();
        return item.editItemIdPermission(itemEntity.getUid(),itemEntity.getObjectType());
    }
    /***
     * 获取相同版本的所有版次
     *
     * @param uid 版本id
     * @param objectType 版本类型
     * @author 徐鹏军
     * @date 2022/5/16 9:27
     * @return {@link List< WorkspaceObjectEntity>}
     */
    public List<WorkspaceObjectEntity> getAllSequences(String uid,String objectType) {
        WorkspaceObjectEntity entity = EntityUtil.getById(objectType, uid);
        if (Objects.isNull(entity)) {
            return Collections.emptyList();
        }
        ItemRevisionEntity revision = (ItemRevisionEntity) entity;
        List<Pair<String, Object>> params = Arrays.asList(
                Pair.of(ItemRevisionEntity.ITEM_ID, revision.getItemId()),
                Pair.of(ItemRevisionEntity.REVISION_ID, revision.getRevisionId())
        );
        return EntityUtil.getDynamicEqQuery(objectType,params).fetch();
    }

    /**
     * 获取同版本的激活版本
     * @param uid
     * @param objectType
     * @return
     */
    public WorkspaceObjectEntity getActiveSequence(String uid,String objectType) {
        WorkspaceObjectEntity entity = EntityUtil.getById(objectType, uid);
        if (Objects.isNull(entity)) {
            return null;
        }
        ItemRevisionEntity revision = (ItemRevisionEntity) entity;
        List<Pair<String, Object>> params = Arrays.asList(
                Pair.of(ItemRevisionEntity.ITEM_ID, revision.getItemId()),
                Pair.of(ItemRevisionEntity.ACTIVE, true),
                Pair.of(ItemRevisionEntity.REVISION_ID, revision.getRevisionId())
        );
        return EntityUtil.getDynamicEqQuery(objectType, params).fetchFirst();
    }


    /***
     * 获取所有的激活版本
     *
     * @param uid 版本id
     * @param objectType 版本类型
     * @author 徐鹏军
     * @date 2022/4/28 14:04
     * @return {@link WorkspaceObjectEntity}
     */
    public List<WorkspaceObjectEntity> getAllActiveRevision(String uid, String objectType) {
        ItemRevisionEntity revision = EntityUtil.getById(objectType, uid);
        if (Objects.isNull(revision)) {
            return null;
        }
        // 查询同一个Item的激活数据
        List<Pair<String, Object>> params = Arrays.asList(
                Pair.of(ItemRevisionEntity.ITEM_ID, revision.getItemId()),
                Pair.of(ItemRevisionEntity.ACTIVE, true)
        );
        return EntityUtil.getDynamicEqQuery(objectType, params).fetch();
    }

    public void deleteRelationshipVerification(IdRequest id){
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        List<String> leftObjectRelations = dictUtil.getCheckAllRLs(EntityUtil.getObjectType());
        if (CollectionUtil.isNotEmpty(leftObjectRelations)){
            leftObjectRelations.forEach(data->{
                    List<Triple<String, Ops,Object>> query = new ArrayList<>();
                    query.add(Triple.of(RelationEntity.RIGHT_OBJECT,Ops.EQ,id.getUid()));
                    query.add(Triple.of(RelationEntity.RIGHT_OBJECT_TYPE,Ops.EQ,EntityUtil.getObjectType()));
                    List<WorkspaceObjectEntity> entities = EntityUtil.getDynamicQuery(data, query).fetch();

                    if (CollectionUtil.isNotEmpty(entities)){
                        WorkspaceObjectEntity byId = EntityUtil.getById(EntityUtil.getObjectType(),id.getUid());
                        ItemRevisionEntity itemRevisionEntity  = (ItemRevisionEntity)byId;
                        throw new ServiceException(ErrorCode.E_10,itemRevisionEntity.getItemId()+"存在被"+data+"引用");
                }
            });
        }
    }



    /**
     * 根据版本获取当前版本的最新版次
     * @param objectType
     * @param uids
     * @author: 薛锦龙
     * @time: 2022/7/6
     * @return: {@link WorkspaceObjectEntity}
     */

    public List<WorkspaceObjectEntity> getItemRevision(String objectType,List<String> uids ){
        //获取零组件版本
        List<WorkspaceObjectEntity> byIds = EntityUtil.getByIds(objectType, uids);
        List<WorkspaceObjectEntity> list = new ArrayList<>();
        byIds.stream().forEach(data->{
            ItemRevisionEntity itemRevision = (ItemRevisionEntity) data;
            List<Triple<String,Ops,Object>> trip  = new ArrayList();
            trip.add(Triple.of(ItemRevisionEntity.ITEM_ID,Ops.EQ,itemRevision.getItemId()));
            trip.add(Triple.of(ItemRevisionEntity.REVISION_ID,Ops.EQ,itemRevision.getRevisionId()));
            trip.add(Triple.of(ItemRevisionEntity.ACTIVE,Ops.EQ,Boolean.TRUE));
            WorkspaceObjectEntity workspaceObject = EntityUtil.getDynamicQuery(objectType, trip).fetchFirst();
            list.add(workspaceObject);
        });
        return list;
    }

}