package com.nancal.service.bo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.enums.LifeCycleStateEnum;
import com.nancal.model.entity.BusinessObjectEntity;
import com.nancal.model.entity.ItemRevisionEntity;
import com.nancal.model.entity.RelationEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.factory.ItemRevisionFactory;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@ApiModel(value = "Item 零组件 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Item extends WorkspaceObject {

    @ApiModelProperty(value = "零组件ID")
    private String itemId;


    /***
     * 根据uid和左对象类型，获取它最新的零组件版本
     * @param uid 组件的id
     * @param leftObjectType 做对象类型
     * @author 徐鹏军
     * @date 2022/4/12 17:29
     * @return {@link Item}
     */
    public WorkspaceObjectEntity getLastVersion(String uid, String leftObjectType) {
        Map<String, WorkspaceObjectEntity> lastVersionMap = getLastVersion(Collections.singletonList(uid), leftObjectType);
        return lastVersionMap.get(uid);
    }

    /**
     * 获取零组件指定的版本
     * @param uidMap
     * @param leftObjectType
     * @return
     */
    public Map<String, WorkspaceObjectEntity> getAppointVersion(Map<String,String> uidMap, String leftObjectType) {
        List<String> uids = new ArrayList<>(uidMap.keySet());
        Map<String, List<WorkspaceObjectEntity>> dataMap = getAllRevisionsMap(uids, leftObjectType);
        if (CollUtil.isEmpty(dataMap)) {
            return Collections.emptyMap();
        }
        Map<String, WorkspaceObjectEntity> resultMap = new HashMap<>();
        dataMap.forEach((id, revisions) -> {
            ItemRevisionEntity itemRevisionEntity = revisions.stream().map(ItemRevisionEntity.class::cast).
                    filter(re -> re.getRevisionId().equals(uidMap.get(id))).findFirst().orElse(null);
            resultMap.put(id, itemRevisionEntity);});
        return resultMap;
    }

    /***
     * 根据uid集合和左对象类型，获取它们最新的零组件版本
     *
     * @param uids 组件的id
     * @param leftObjectType 做对象类型
     * @author 徐鹏军
     * @date 2022/4/12 17:29
     * @return {@link Item}
     */

    public Map<String, WorkspaceObjectEntity> getLastVersion(List<String> uids, String leftObjectType) {
        Map<String, List<WorkspaceObjectEntity>> dataMap = getAllRevisionsMap(uids, leftObjectType);
        if (CollUtil.isEmpty(dataMap)) {
            return Collections.emptyMap();
        }
        Map<String, WorkspaceObjectEntity> resultMap = new HashMap<>();
        dataMap.forEach((id, revisions) -> {
            List<WorkspaceObjectEntity> list = revisions.stream().map(entity -> (ItemRevisionEntity) entity).
                    sorted(Comparator.comparing(ItemRevisionEntity::getRevisionId).reversed()).collect(Collectors.toList());
            resultMap.put(id, CollUtil.getFirst(list));});
        return resultMap;
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

    public Map<String,List<ItemRevisionEntity>> listByItemIds(List<String> itemIds, String objectType) {
        List<Triple<String, Ops, Object>> paramList = Arrays.asList(
                Triple.of(ItemRevisionEntity.ITEM_ID, Ops.IN, itemIds),
                // 获取active激活的
                Triple.of(ItemRevisionEntity.ACTIVE, Ops.EQ, true),
                Triple.of(ItemRevisionEntity.DEL_FLAG, Ops.EQ, false)
        );
        List<WorkspaceObjectEntity> entityList = EntityUtil.getDynamicQuery(objectType, paramList).fetch();
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyMap();
        }
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        List<String> collect = entityList.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
        Map<String, WorkspaceObjectEntity> leftObjectMap = itemRevision.getLeftObjectMap(collect, objectType);
        entityList.forEach(t->{
             WorkspaceObjectEntity workspaceObjectEntity = leftObjectMap.get(t.getUid());
            if (null != workspaceObjectEntity){
                t.setLeftObject(workspaceObjectEntity.getUid());
                t.setLeftObjectType(workspaceObjectEntity.getObjectType());
            }
        });
        return entityList.stream().map(ItemRevisionEntity.class::cast).collect(Collectors.groupingBy(ItemRevisionEntity::getItemId));
    }

//    /***
//     * 根据uid和左对象类型，获取它前2个零组件版本
//     * <p>
//     *     左：是最新的版本
//     *     右：是前一个激活的版本
//     * </p>
//     * @param uid 组件的id
//     * @param leftObjectType 做对象类型
//     * @author 徐鹏军
//     * @date 2022/4/12 17:29
//     * @return {@link Item}
//     */
//    public Pair<WorkspaceObjectEntity, WorkspaceObjectEntity> getTopTwoVersion(String uid, String leftObjectType) {
//        List<WorkspaceObjectEntity> items = getAllRevisions(uid, leftObjectType);
//        if (CollUtil.isEmpty(items)) {
//            return null;
//        }
//        return Pair.of(CollUtil.getFirst(items), CollUtil.get(items, 1));
//    }

//    /***
//     * 根据uid和左对象类型，获取它前2个零组件版本
//     * <p>
//     *     左：是最新的版本
//     *     右：是前一个激活的版本
//     * </p>
//     * @param uids 组件的id集合
//     * @param leftObjectType 做对象类型
//     * @author 徐鹏军
//     * @date 2022/4/12 17:29
//     * @return {@link Item}
//     */
//    public Pair<List<WorkspaceObjectEntity>, List<WorkspaceObjectEntity>> getTopTwoVersions(List<String> uids, String leftObjectType) {
//        Map<String, List<WorkspaceObjectEntity>> dataMap = getAllRevisionsMap(uids, leftObjectType);
//        if (CollUtil.isEmpty(dataMap)) {
//            return Pair.of(Collections.emptyList(), Collections.emptyList());
//        }
//        List<WorkspaceObjectEntity> topOnes = new ArrayList<>();
//        List<WorkspaceObjectEntity> topTwos = new ArrayList<>();
//        dataMap.forEach((id, revisions) -> {
//            topOnes.add(CollUtil.getFirst(revisions));
//            topTwos.add(CollUtil.get(revisions, 1));
//        });
//        return Pair.of(topOnes, topTwos);
//    }

    /***
     * 获取所有版本
     *
     * @param uid 组件的id
     * @param leftObjectType 类型
     * @author 徐鹏军
     * @date 2022/4/28 13:58
     * @return {@link List< WorkspaceObjectEntity>}
     */
    public List<WorkspaceObjectEntity> getAllRevisions(String uid, String leftObjectType) {
        return getAllRevisions(Collections.singletonList(uid), leftObjectType);
    }

    /***
     * 获取所有版本
     *
     * @param uids 组件的ids
     * @param leftObjectType 类型
     * @author 徐鹏军
     * @date 2022/4/28 13:58
     * @return {@link List< WorkspaceObjectEntity>}
     */
    public List<WorkspaceObjectEntity> getAllRevisions(List<String> uids, String leftObjectType) {
        Map<String, List<WorkspaceObjectEntity>> dataMap = getAllRevisionsMap(uids, leftObjectType);
        if (CollUtil.isEmpty(dataMap)) {
            return Collections.emptyList();
        }
        return dataMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    /***
     * 根据左对象获取右对象
     *
     * @param uids 左对象ids
     * @param leftObjectType 左对象类型
     * @param rightObjectType 右对象类型
     * @author 徐鹏军
     * @date 2022/4/28 14:04
     * @return {@link List<WorkspaceObjectEntity>}
     */
    public List<WorkspaceObjectEntity> getAllRightObjects(List<String> uids, String leftObjectType, String rightObjectType) {
        Map<String, List<WorkspaceObjectEntity>> dataMap = getRightObjectMap(uids, leftObjectType, rightObjectType);
        if (CollUtil.isEmpty(dataMap)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(dataMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList()));
    }

    /***
     * 根据左对象获取右对象
     *
     * @param uids 左对象ids
     * @param leftObjectType 左对象类型
     * @param rightObjectType 右对象类型
     * @author 徐鹏军
     * @date 2022/4/28 14:04
     * @return {@link List<WorkspaceObjectEntity>}
     */
    public List<WorkspaceObjectEntity> getActiveRightObjects(List<String> uids, String leftObjectType, String rightObjectType) {
        Map<String, List<WorkspaceObjectEntity>> dataMap = getRightObjectMap(uids, leftObjectType, rightObjectType);
        if (CollUtil.isEmpty(dataMap)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(dataMap.values().stream().flatMap(Collection::stream).filter(data -> {
            if (data instanceof ItemRevisionEntity) {
                ItemRevisionEntity revisionEntity = (ItemRevisionEntity) data;
                return revisionEntity.getActive();
            } else {
                return true;
            }
        }).collect(Collectors.toList()));
    }

    /***
     * 根据左对象获取右对象,key:是左对象id,value：是对应的右对象数据
     * <p>
     *     1:先根据左右对象类型，查询字典，获取他们的中间表
     *     2:左对象类型和id查询中间表数据
     *     3：根据找到的中间表，获取右对象id
     *     4:查询右对象数据
     *     5：按照入参对象封装成map返回
     * </p>
     *
     * @param uids 左对象ids
     * @param leftObjectType 左对象类型
     * @param rightObjectType 右对象类型
     * @author 徐鹏军
     * @date 2022/4/28 14:04
     * @return {@link Map<String, List<WorkspaceObjectEntity>>}
     */
    public Map<String, List<WorkspaceObjectEntity>> getRightObjectMap(List<String> uids, String leftObjectType, String rightObjectType) {
        String relation = SpringUtil.getBean(DictUtil.class).getRelation(leftObjectType, rightObjectType);
        if (StrUtil.isBlank(relation)) {
            return Collections.emptyMap();
        }
        List<Triple<String, Ops, Object>> params = Arrays.asList(
                Triple.of(RelationEntity.LEFT_OBJECT, Ops.IN, uids),
                Triple.of(RelationEntity.LEFT_OBJECT_TYPE, Ops.EQ, leftObjectType),
                Triple.of(RelationEntity.RIGHT_OBJECT_TYPE, Ops.EQ, rightObjectType)
        );
        // 查询中间表获取左对象数据
        List<WorkspaceObjectEntity> entityList = EntityUtil.getDynamicQuery(relation, params).fetch();
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyMap();
        }
        List<RelationEntity> relationList = entityList.stream().map(RelationEntity.class::cast).collect(Collectors.toList());
        // 获取右对象id
        List<String> rightObjects = relationList.stream().map(RelationEntity::getRightObject).collect(Collectors.toList());
        List<WorkspaceObjectEntity> itemRevisionList = EntityUtil.getByIds(rightObjectType, rightObjects);
        if (CollUtil.isEmpty(itemRevisionList)) {
            return Collections.emptyMap();
        }
        Map<String, WorkspaceObjectEntity> itemRevisionMap = itemRevisionList.stream().collect(Collectors.toMap(WorkspaceObjectEntity::getUid, Function.identity()));
        Map<String, List<WorkspaceObjectEntity>> dataMap = new HashMap<>();
        // 将左右对象id转成map
        relationList.stream().collect(Collectors.groupingBy(RelationEntity::getLeftObject)).forEach((key, values) -> {
            List<WorkspaceObjectEntity> entities = values.stream()
                    .filter(right -> itemRevisionMap.containsKey(right.getRightObject())).map(right -> {
                        // 将每一个右对象的左id和类型设置上
                        WorkspaceObjectEntity entity = itemRevisionMap.get(right.getRightObject());
                        entity.setLeftObject(key);
                        entity.setLeftObjectType(leftObjectType);
                        return entity;
                    }).sorted(Comparator.comparing(WorkspaceObjectEntity::getCreationDate).reversed())
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(entities)) {
                dataMap.put(key, entities);
            }
        });
        return dataMap;
    }

    /***
     * 获取所有版本,返回map
     *
     * @param uids 组件的ids
     * @param leftObjectType 类型
     * @author 徐鹏军
     * @date 2022/4/28 13:58
     * @return {@link List< WorkspaceObjectEntity>}
     */
    public Map<String, List<WorkspaceObjectEntity>> getAllRevisionsMap(List<String> uids, String leftObjectType) {
        String revisionType = EntityUtil.getRevision(leftObjectType);
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        String relation = dictUtil.getRelation(leftObjectType, revisionType);
        List<Triple<String, Ops, Object>> params = Arrays.asList(
                Triple.of(RelationEntity.LEFT_OBJECT_TYPE, Ops.EQ, leftObjectType),
                Triple.of(RelationEntity.LEFT_OBJECT, Ops.IN, uids),
                Triple.of(RelationEntity.RIGHT_OBJECT_TYPE, Ops.EQ, revisionType),
                Triple.of(RelationEntity.DEL_FLAG, Ops.EQ, Boolean.FALSE)
        );
        // 获取所有关系表数据
        List<WorkspaceObjectEntity> relations = EntityUtil.getDynamicQuery(relation, params).fetch();
        if (CollUtil.isEmpty(relations)) {
            return Collections.emptyMap();
        }
        // 获取所有右对象id
        List<String> rightUidList = relations.stream().map(RelationEntity.class::cast).map(RelationEntity::getRightObject).collect(Collectors.toList());
        // 动态查询版本数据
        List<Triple<String, Ops, Object>> paramList = Arrays.asList(
                Triple.of(BusinessObjectEntity.UID, Ops.IN, rightUidList),
                // 获取active激活的
                Triple.of(ItemRevisionEntity.ACTIVE, Ops.EQ, true),
                Triple.of(ItemRevisionEntity.DEL_FLAG, Ops.EQ, false)
        );
        OrderSpecifier order = new OrderSpecifier(Order.DESC, ExpressionUtils.path(EntityUtil.getEntityClass(revisionType), ItemRevisionEntity.REVISION_ID));
        List<WorkspaceObjectEntity> results = EntityUtil.getDynamicQuery(revisionType, paramList).orderBy(order).fetch();
        if (CollUtil.isEmpty(results)) {
            return Collections.emptyMap();
        }
        Map<String, WorkspaceObjectEntity> revisionMap = results.stream().collect(Collectors.toMap(WorkspaceObjectEntity::getUid, Function.identity()));
        // 组装数据
        Map<String, List<WorkspaceObjectEntity>> dataMap = new HashMap<>();
        // 将组件id和版本id转成map
        Map<String, List<String>> idMap = relations.stream().map(RelationEntity.class::cast)
                .collect(Collectors.groupingBy(RelationEntity::getLeftObject, Collectors.mapping(RelationEntity::getRightObject, Collectors.toList())));
        revisionMap.forEach((uid, revision) -> {
            idMap.forEach((id, revisionIds) -> {
                if (revisionIds.contains(revision.getUid())) {
                    List<WorkspaceObjectEntity> workspaceObjectEntities = dataMap.get(id);
                    if (CollUtil.isEmpty(workspaceObjectEntities)) {
                        workspaceObjectEntities = new ArrayList<>();
                        workspaceObjectEntities.add(revision);
                    } else {
                        workspaceObjectEntities.add(revision);
                    }
                    dataMap.put(id, workspaceObjectEntities);
                }
            });
        });
        return dataMap;
    }


    /***
     * 获取所有版本,返回map
     *
     * @param itemIds 组件的ids
     * @param leftObjectType 类型
     * @author 徐鹏军
     * @date 2022/4/28 13:58
     * @return {@link List< WorkspaceObjectEntity>}
     */
    public Map<String, List<WorkspaceObjectEntity>> getAllItemIdMap(List<String> itemIds, String leftObjectType) {
        String revisionType = EntityUtil.getRevision(leftObjectType);
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        String relation = dictUtil.getRelation(leftObjectType, revisionType);
        List<Triple<String, Ops, Object>> params = Arrays.asList(
                Triple.of(RelationEntity.LEFT_OBJECT_TYPE, Ops.EQ, leftObjectType),
                Triple.of(RelationEntity.LEFT_OBJECT, Ops.IN, itemIds),
                Triple.of(RelationEntity.RIGHT_OBJECT_TYPE, Ops.EQ, revisionType),
                Triple.of(RelationEntity.DEL_FLAG, Ops.EQ, Boolean.FALSE)
        );
        // 获取所有关系表数据
        List<WorkspaceObjectEntity> relations = EntityUtil.getDynamicQuery(relation, params).fetch();
        if (CollUtil.isEmpty(relations)) {
            return Collections.emptyMap();
        }
        // 获取所有右对象id
        List<String> rightUidList = relations.stream().map(RelationEntity.class::cast).map(RelationEntity::getRightObject).collect(Collectors.toList());
        // 动态查询版本数据
        List<Triple<String, Ops, Object>> paramList = Arrays.asList(
                Triple.of(BusinessObjectEntity.UID, Ops.IN, rightUidList),
                // 获取active激活的
                Triple.of(ItemRevisionEntity.ACTIVE, Ops.EQ, true),
                Triple.of(ItemRevisionEntity.DEL_FLAG, Ops.EQ, false)
        );
        OrderSpecifier order = new OrderSpecifier(Order.DESC, ExpressionUtils.path(EntityUtil.getEntityClass(revisionType), ItemRevisionEntity.REVISION_ID));
        List<WorkspaceObjectEntity> results = EntityUtil.getDynamicQuery(revisionType, paramList).orderBy(order).fetch();
        if (CollUtil.isEmpty(results)) {
            return Collections.emptyMap();
        }
        Map<String, WorkspaceObjectEntity> revisionMap = results.stream().collect(Collectors.toMap(WorkspaceObjectEntity::getUid, Function.identity()));
        // 组装数据
        Map<String, List<WorkspaceObjectEntity>> dataMap = new HashMap<>();
        // 将组件id和版本id转成map
        Map<String, List<String>> idMap = relations.stream().map(RelationEntity.class::cast)
                .collect(Collectors.groupingBy(RelationEntity::getLeftObject, Collectors.mapping(RelationEntity::getRightObject, Collectors.toList())));
        revisionMap.forEach((uid, revision) -> {
            idMap.forEach((id, revisionIds) -> {
                if (revisionIds.contains(revision.getUid())) {
                    List<WorkspaceObjectEntity> workspaceObjectEntities = dataMap.get(id);
                    if (CollUtil.isEmpty(workspaceObjectEntities)) {
                        workspaceObjectEntities = new ArrayList<>();
                        workspaceObjectEntities.add(revision);
                    } else {
                        workspaceObjectEntities.add(revision);
                    }
                    dataMap.put(id, workspaceObjectEntities);
                }
            });
        });
        return dataMap;
    }

//    /***
//     * 根据uid和左对象类型，获取它最新的零组件版本的文件
//     *
//     * @param uid 主键id
//     * @param leftObjectType 做对象类型
//     * @author 徐鹏军
//     * @date 2022/4/12 17:29
//     * @return {@link List< FileStorage >}
//     */
//    public List<FileStorage> getFileStorage(String uid, String leftObjectType) {
//        // 获取最新版本数据
//        WorkspaceObjectEntity workspaceObject = getLastVersion(uid, leftObjectType);
//        if (Objects.isNull(workspaceObject)) {
//            return null;
//        }
//        // 查询Revision的规范关系
//        ItemRevisionFactory revisionFactory = SpringUtil.getBean(ItemRevisionFactory.class);
//        return revisionFactory.create().getFileStorage(workspaceObject.getUid(), workspaceObject.getObjectType());
//    }

    /***
     * true有编辑零组件的图代号权限，false没有编辑零组件的图代号权限
     *
     * @param uid 主键id
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/12 17:29
     * @return {@link boolean}
     */
    public boolean editItemIdPermission(String uid, String objectType) {
        // 只要存在已发布的，则没有编辑权限
        String revisionObjectType = EntityUtil.getRevision(objectType);
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        String relation = dictUtil.getRelation(objectType, revisionObjectType);
        List<Pair<String, Object>> params = Arrays.asList(
                Pair.of(RelationEntity.LEFT_OBJECT_TYPE, objectType),
                Pair.of(RelationEntity.LEFT_OBJECT, uid),
                Pair.of(RelationEntity.RIGHT_OBJECT_TYPE, revisionObjectType)
        );
        // 获取所有关系表数据
        List<WorkspaceObjectEntity> relations = EntityUtil.getDynamicEqQuery(relation, params).fetch();
        if (CollUtil.isEmpty(relations)) {
            return true;
        }
        List<String> rightUids = relations.stream().map(RelationEntity.class::cast).map(RelationEntity::getRightObject).collect(Collectors.toList());
        List<Triple<String, Ops, Object>> paramList = Arrays.asList(
                Triple.of(BusinessObjectEntity.UID, Ops.IN, rightUids),
                Triple.of(WorkspaceObjectEntity.LIFE_CYCLE_STATE, Ops.EQ, LifeCycleStateEnum.Released.name())
        );
        return EntityUtil.getDynamicQuery(revisionObjectType, paramList).fetchCount() <= 0;
    }

}