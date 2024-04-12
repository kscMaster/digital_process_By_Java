package com.nancal.service.bo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.enums.DictRelEnum;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.*;
import com.nancal.remote.vo.DictItemVo;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.factory.ItemRevisionFactory;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "MasterRL 零组件和版本的关系 的请求")
public class MasterRL extends Relation {

    @Override
    public String getRelationType() {
        return "MasterRL";
    }

    /***
     * 获取指定节点下的所有数据(包括孙子节点)，得到的结果不包括当前的uid数据
     * 注意：拿到数据如果有需要，自己可根据UID进行去重(并非必须操作)
     * <p>
     *     不断层级的，比如
     *     A
     *     |
     *     -- B
     *        |
     *        -- C
     *
     *    如果要查询A下的所有子节点，那么如果B不满足要求，也会继续获取C的数据
     * </p>
     *
     * @param uid 主键id
     * @param objectType 对象类型
     * @param filter 自定义过滤条件
     * @author 徐鹏军
     * @date 2022/4/12 17:29
     * @return {@link List< WorkspaceObjectEntity >}
     */
    public List<WorkspaceObjectEntity> getRecursionChild(String uid, String objectType,
                                                         Predicate<WorkspaceObjectEntity> filter, DictRelEnum dictRelEnum) {
        return getRecursionChild(uid, objectType, filter,0,dictRelEnum);
    }

    /***
     * 获取指定节点下的所有数据(包括孙子节点)，得到的结果不包括当前的uid数据
     * 注意：拿到数据如果有需要，自己可根据UID进行去重(并非必须操作)
     * <p>
     *     不断层级的，比如
     *     A
     *     |
     *     -- B
     *        |
     *        -- C
     *
     *    如果要查询A下的所有子节点，那么如果B不满足要求，也会继续获取C的数据
     * </p>
     *
     * @param uid 主键id
     * @param objectType 对象类型
     * @param filter 自定义过滤条件
     * @param maxDeep 递归的最大深度
     * @author 徐鹏军
     * @date 2022/4/12 17:29
     * @return {@link List< WorkspaceObjectEntity >}
     */
    public List<WorkspaceObjectEntity> getRecursionChild(String uid, String objectType,
                                                         Predicate<WorkspaceObjectEntity> filter,int maxDeep,DictRelEnum dictRelEnum) {
        List<WorkspaceObjectEntity> datas = new ArrayList<>();
        recursionChild(uid, objectType, datas,filter, 0,maxDeep,false,dictRelEnum);
        SpringUtil.getBean(DictUtil.class).translate(datas);
        return datas;
    }

    /***
     * 获取到达指定节点的所有父数据(包括祖父节点，曾祖父节点...)，得到的结果不包括当前的uid数据
     * 注意：拿到数据如果有需要，自己可根据UID进行去重(并非必须操作)
     *     不断层级的，比如
     *     A
     *     |
     *     -- B
     *        |
     *        -- C
     *
     *    如果要查询C上的所有父节点，那么如果B不满足要求，也会继续获取A的数据
     * @param uid 主键id
     * @param objectType 对象类型
     * @param filter 自定义过滤条件
     * @author 徐鹏军
     * @date 2022/4/12 17:29
     * @return {@link List< WorkspaceObjectEntity >}
     */
    public List<WorkspaceObjectEntity> getRecursionParent(String uid, String objectType,
                                                          Predicate<WorkspaceObjectEntity> filter,DictRelEnum dictRelEnum) {
        List<WorkspaceObjectEntity> datas = new ArrayList<>();
        recursionParentBreak(uid, objectType, datas, filter,0,0,false,dictRelEnum);
        SpringUtil.getBean(DictUtil.class).translate(datas);
        return datas;
    }

    public List<WorkspaceObjectEntity> getRecursionParent(String uid, String objectType,
                                                          Predicate<WorkspaceObjectEntity> filter) {
        return getRecursionParent(uid,objectType,filter,DictRelEnum.PARENT_REL);
    }

    /***
     * 获取到达指定节点的所有父数据(包括祖父节点，曾祖父节点...)，得到的结果不包括当前的uid数据
     * 注意：拿到数据如果有需要，自己可根据UID进行去重(并非必须操作)
     *     不断层级的，比如
     *     A
     *     |
     *     -- B
     *        |
     *        -- C
     *
     *    如果要查询C上的所有父节点，那么如果B不满足要求，也会继续获取A的数据
     * @param uid 主键id
     * @param objectType 对象类型
     * @param maxDeep 最大递归深度
     * @param filter 自定义过滤条件
     * @author 徐鹏军
     * @date 2022/4/12 17:29
     * @return {@link List< WorkspaceObjectEntity >}
     */
    public List<WorkspaceObjectEntity> getRecursionParent(String uid, String objectType,int maxDeep,
                                                          Predicate<WorkspaceObjectEntity> filter,DictRelEnum dictRelEnum) {
        List<WorkspaceObjectEntity> datas = new ArrayList<>();
        recursionParentBreak(uid, objectType, datas, filter,0,maxDeep,false,dictRelEnum);
        SpringUtil.getBean(DictUtil.class).translate(datas);
        return datas;
    }

    /***
     * 获取指定节点下的所有子节点数据(包括孙子节点)，得到的结果不包括当前的uid数据
     * 注意：拿到数据如果有需要，自己可根据UID进行去重(并非必须操作)
     * <p>
     *     断层级的，比如
     *     A
     *     |
     *     -- B
     *        |
     *        -- C
     *
     *    如果要查询A下的所有子节点，那么如果B不满足要求，那么将不会继续获取C的数据
     * </p>
     *
     * @param uid 主键id
     * @param objectType 对象类型
     * @param filter 自定义过滤条件
     * @author 徐鹏军
     * @date 2022/4/12 17:29
     * @return {@link List< WorkspaceObjectEntity >}
     */
    public List<WorkspaceObjectEntity> recursionChildBreak(String uid, String objectType, Predicate<WorkspaceObjectEntity> filter) {
        return recursionChildBreak(uid, objectType, 0, filter,DictRelEnum.CHILD_REL);
    }

    /***
     * 获取指定节点下的所有直接子数据(不递归)，得到的结果不包括当前的uid数据
     * 注意：拿到数据如果有需要，自己可根据UID进行去重(并非必须操作)
     * @param uid 主键id
     * @param objectType 对象类型
     * @param filter 自定义过滤条件
     * @author 徐鹏军
     * @date 2022/4/12 17:29
     * @return {@link List< WorkspaceObjectEntity >}
     */
    public List<WorkspaceObjectEntity> getChilds(String uid, String objectType,
                                                 Predicate<WorkspaceObjectEntity> filter,DictRelEnum dictRelEnum) {
        return recursionChildBreak(uid, objectType, 1, filter,dictRelEnum);
    }

    /**
     * 默认查询子的方式
     * @param uid
     * @param objectType
     * @param filter
     * @return
     */
    public List<WorkspaceObjectEntity> getChilds(String uid, String objectType,
                                                 Predicate<WorkspaceObjectEntity> filter) {
        return getChilds(uid, objectType,filter,DictRelEnum.CHILD_REL);
    }

    /***
     * 获取指定节点下的所有子节点数据(包括孙子节点)，得到的结果不包括当前的uid数据
     * 注意：拿到数据如果有需要，自己可根据UID进行去重(并非必须操作)
     * <p>
     *     断层级的，比如
     *     A
     *     |
     *     -- B
     *        |
     *        -- C
     *
     *    如果要查询A下的所有子节点，那么如果B不满足要求，那么将不会继续获取C的数据
     * </p>
     *
     * @param uid 主键id
     * @param objectType 对象类型
     * @param maxDeep 递归的最大深度
     * @param filter 自定义过滤条件
     * @author 徐鹏军
     * @date 2022/4/12 17:29
     * @return {@link List< WorkspaceObjectEntity >}
     */
    public List<WorkspaceObjectEntity> recursionChildBreak(String uid, String objectType, int maxDeep,
                                                           Predicate<WorkspaceObjectEntity> filter,DictRelEnum dictRelEnum) {
        List<WorkspaceObjectEntity> datas = new ArrayList<>();
        recursionChild(uid, objectType, datas, filter, 0, maxDeep,true,dictRelEnum);
        SpringUtil.getBean(DictUtil.class).translate(datas);
        return datas;
    }
    /***
     * 获取到达指定节点的所有父数据(包括祖父节点，曾祖父节点...)，得到的结果不包括当前的uid数据
     * 注意：拿到数据如果有需要，自己可根据UID进行去重(并非必须操作)
     *     断层级的，比如
     *     A
     *     |
     *     -- B
     *        |
     *        -- C
     *
     *    如果要查询C上的所有父节点，那么如果B不满足要求，那么将不会继续获取A的数据
     * @param uid 主键id
     * @param objectType 对象类型
     * @param filter 自定义过滤条件
     * @author 徐鹏军
     * @date 2022/4/12 17:29
     * @return {@link List< WorkspaceObjectEntity >}
     */
    public List<WorkspaceObjectEntity> getRecursionParentBreak(String uid, String objectType,
                                                               Predicate<WorkspaceObjectEntity> filter,DictRelEnum dictRelEnum) {
        return  getRecursionParentBreak(uid, objectType, 0, filter,dictRelEnum);
    }

    /***
     * 获取到达指定节点的所有直接父数据(不递归)，得到的结果不包括当前的uid数据
     * 注意：拿到数据如果有需要，自己可根据UID进行去重(并非必须操作)
     *
     * @param uid 主键id
     * @param objectType 对象类型
     * @param filter 自定义过滤条件
     * @author 徐鹏军
     * @date 2022/4/12 17:29
     * @return {@link List< WorkspaceObjectEntity >}
     */
    public List<WorkspaceObjectEntity> getParents(String uid, String objectType,
                                                  Predicate<WorkspaceObjectEntity> filter,DictRelEnum dictRelEnum) {
        return getRecursionParentBreak(uid, objectType, 1,filter,dictRelEnum);
    }

    public List<WorkspaceObjectEntity> getParents(String uid, String objectType,
                                                  Predicate<WorkspaceObjectEntity> filter) {
        return getParents(uid, objectType, filter,DictRelEnum.PARENT_REL);
    }

    /***
     * 获取到达指定节点的所有父数据(包括祖父节点，曾祖父节点...)，得到的结果不包括当前的uid数据
     * 注意：拿到数据如果有需要，自己可根据UID进行去重(并非必须操作)
     *     断层级的，比如
     *     A
     *     |
     *     -- B
     *        |
     *        -- C
     *
     *    如果要查询C上的所有父节点，那么如果B不满足要求，那么将不会继续获取A的数据
     * @param uid 主键id
     * @param objectType 对象类型
     * @param maxDeep 递归的最大深度
     * @param filter 自定义过滤条件
     * @author 徐鹏军
     * @date 2022/4/12 17:29
     * @return {@link List< WorkspaceObjectEntity >}
     */
    public List<WorkspaceObjectEntity> getRecursionParentBreak(String uid, String objectType, int maxDeep,
                                                               Predicate<WorkspaceObjectEntity> filter,DictRelEnum dictRelEnum) {
        List<WorkspaceObjectEntity> datas = new ArrayList<>();
        recursionParentBreak(uid, objectType, datas, filter, 0,maxDeep,true,dictRelEnum);
        SpringUtil.getBean(DictUtil.class).translate(datas);
        return datas;
    }
    /***
     * 获取指定节点下的所有数据(包括孙子节点)，得到的结果不包括当前的uid数据
     * 注意：拿到数据如果有需要，自己可根据UID进行去重(并非必须操作)
     * <p>
     *     isBreak控制是否断层级的，比如
     *           A
     *           |
     *           -- B
     *              |
     *              -- C
     *     断层级：如果要查询A下的所有子节点，那么如果B不满足要求，那么将不会继续获取C的数据
     *     不断层级：如果要查询A下的所有子节点，那么如果B不满足要求，那么将会继续获取C的数据
     *
     *     1:从字典中获取该类型下所有直接子节点对象类型与当前节点关联的中间表
     *     2：循环每个中间表，查询数据
     *     3：递归调用自身
     * </p>
     *
     * @param uid 主键id
     * @param objectType 对象类型
     * @param datas 查询结果
     * @param filter 自定义过滤条件
     * @param currentDeep 当前的递归值，默认0
     * @param maxDeep 最大递归深度 <= 0表示不限制递归深度，>0 表示需要具体递归多少层
     * @param isBreak true:需要断层，false:不需要断层
     * @author 徐鹏军
     * @date 2022/4/12 17:29
     * @return {@link {Void}}
     */
    private void recursionChild(String uid, String objectType, List<WorkspaceObjectEntity> datas,
                                Predicate<WorkspaceObjectEntity> filter, int currentDeep, int maxDeep,
                                boolean isBreak,DictRelEnum dictRelEnum) {
        if (datas == null) {
            return;
        }
        if (maxDeep > 0 && currentDeep >= maxDeep) {
            return;
        }
        int newCurrentDeep = currentDeep + 1;
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        List<String> relations = null;
        List<Pair<String, Object>> params = null;
        // 判断objectType是否是关系类型，如果是，则不用再去字典中获取
        boolean assignableFrom = RelationEntity.class.isAssignableFrom(EntityUtil.getEntityClass(objectType));
        if (assignableFrom) {
            relations = Collections.singletonList(objectType);
            params = Collections.singletonList(Pair.of(RelationEntity.LEFT_OBJECT, uid));
        } else {
            // 获取该类型下所有我作为左对象类型的中间表名称
            relations = dictUtil.listCodeByObjectTypeAndDictRel(objectType,dictRelEnum);
            params = Arrays.asList(
                    Pair.of(RelationEntity.LEFT_OBJECT, uid),
                    Pair.of(RelationEntity.LEFT_OBJECT_TYPE, objectType)
            );
        }
        if (CollUtil.isEmpty(relations)) {
            return;
        }
        for (String relation : relations) {
            // 获取中间表数据
            List<WorkspaceObjectEntity> relationEntityList = EntityUtil.getDynamicEqQuery(relation, params).fetch();
            if (CollUtil.isEmpty(relationEntityList)) {
                continue;
            }
            // 将同一类型的数据进行分组查询
            relationEntityList.stream().map(RelationEntity.class::cast).collect(Collectors.groupingBy(RelationEntity::getRightObjectType)).forEach((rightObjectType, values) -> {
                // 获取右对象id列表
                List<String> rightUidList = values.stream().map(RelationEntity::getRightObject).collect(Collectors.toList());
                // 防止id过多，in出现超出字符长度，此处进行切割分段获取
                List<WorkspaceObjectEntity> rightObjects = CollUtil.split(rightUidList, 500).stream()
                        .flatMap(ids -> Optional.ofNullable(EntityUtil.getByIds(rightObjectType, ids)).orElse(Collections.emptyList()).stream())
                        .collect(Collectors.toList());
                if (CollUtil.isEmpty(rightObjects)) {
                    return;
                }
                // 将右对象的id和左对象的id进行map转换，用来构建父子结构数据,如果当前方法入参是关系类型，则右对象对应的左对象可能是多个不同的类型，所以此处需要做处理
                Map<String, List<RelationEntity>> rightLeftMap = values.stream().collect(Collectors.groupingBy(RelationEntity::getRightObject));
                for (WorkspaceObjectEntity entity : rightObjects) {
                    // 需要递归的类型
                    String recursionType = assignableFrom ? objectType : entity.getObjectType();
                    // 调用用户自定义的过滤条件，用来判断是否将该数据放入结果集中
                    if (Objects.nonNull(filter) && !filter.test(entity)) {
                        if (!isBreak) {
                            recursionChild(entity.getUid(), recursionType, datas, filter, newCurrentDeep, maxDeep, isBreak,dictRelEnum);
                        }
                        continue;
                    }
                    if (!rightLeftMap.containsKey(entity.getUid())) {
                        continue;
                    }
                    List<RelationEntity> entityList = rightLeftMap.get(entity.getUid());
                    // 大多数情况下会只有一个，所以此处多做了一个判断，用于视情况而定的优化吧
                    if (entityList.size() == 1) {
                        RelationEntity relationEntity = CollUtil.getFirst(entityList);
                        entity.setLeftObject(relationEntity.getLeftObject());
                        entity.setLeftObjectType(relationEntity.getLeftObjectType());
                        entity.setRelationId(relationEntity.getUid());
                        // 将自身加入集合
                        datas.add(entity);
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
                            datas.add(newEntity);
                        });
                    }
                    // 如果是数据集的话，就不在次进行递归调用
                    if (entity instanceof DatasetEntity) {
                        continue;
                    }
                    recursionChild(entity.getUid(), recursionType, datas, filter, newCurrentDeep, maxDeep, isBreak,dictRelEnum);
                }
            });
        }
    }
    /***
     * 获取到达指定节点的所有父数据(包括祖父节点，曾祖父节点...)，得到的结果不包括当前的uid数据
     * 注意：拿到数据如果有需要，自己可根据UID进行去重(并非必须操作)
     * <p>
     *     isBreak控制是否断层级的，比如
     *     A
     *     |
     *     -- B
     *        |
     *        -- C
     *
     *     断层级：如果要查询C上的所有父节点，那么如果B不满足要求，那么将不会继续获取A的数据
     *     不断层级：如果要查询C上的所有父节点，那么如果B不满足要求，那么将会继续获取A的数据
     *
     *     1:从字典中获取该类型的所有直接父节点对象类型与当前节点关联的中间表
     *     2：循环每个中间表，查询数据
     *     3：递归调用自身
     * </p>
     *
     * @param uid 主键id
     * @param objectType 对象类型
     * @param datas 查询结果
     * @param filter 自定义过滤条件
     * @param currentDeep 当前的递归值，默认0
     * @param maxDeep 最大递归深度 <= 0表示不限制递归深度，>0 表示需要具体递归多少层
     * @param isBreak true:需要断层，false:不需要断层
     * @author 徐鹏军
     * @date 2022/4/12 17:29
     * @return {@link {Void}}
     */
    private void recursionParentBreak(String uid, String objectType, List<WorkspaceObjectEntity> datas,
                                         Predicate<WorkspaceObjectEntity> filter, int currentDeep,
                                      int maxDeep, boolean isBreak,DictRelEnum dictRelEnum) {
        if (datas == null) {
            return;
        }
        if (maxDeep > 0 && currentDeep >= maxDeep) {
            return;
        }
        int newCurrentDeep = currentDeep + 1;
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        List<String> relations = null;
        List<Pair<String, Object>> params = null;
        // 判断objectType是否是关系类型，如果是，则不用再去字典中获取
        boolean assignableFrom = RelationEntity.class.isAssignableFrom(EntityUtil.getEntityClass(objectType));
        if (assignableFrom) {
            relations = Collections.singletonList(objectType);
            params = Collections.singletonList(Pair.of(RelationEntity.RIGHT_OBJECT, uid));
        } else {
            // 获取该类型下所有我作为左对象类型的中间表名称
            relations = dictUtil.listCodeByObjectTypeAndDictRel(objectType,dictRelEnum);
            params = Arrays.asList(
                    Pair.of(RelationEntity.RIGHT_OBJECT, uid),
                    Pair.of(RelationEntity.RIGHT_OBJECT_TYPE, objectType)
            );
        }
        if (CollUtil.isEmpty(relations)) {
            return;
        }
        for (String relation : relations) {
            // 获取数据
            List<WorkspaceObjectEntity> relationEntityList = EntityUtil.getDynamicEqQuery(relation, params).fetch();
            if (CollUtil.isEmpty(relationEntityList)) {
                continue;
            }
            // 将同一类型的数据进行分组查询
            relationEntityList.stream().map(RelationEntity.class::cast).collect(Collectors.groupingBy(RelationEntity::getLeftObjectType)).forEach((leftObjectType, values) -> {
                // 获取右对象id列表
                List<String> leftUidList = values.stream().map(RelationEntity::getLeftObject).collect(Collectors.toList());
                // 防止id过多，in出现超出字符长度，此处进行切割分段获取
                List<WorkspaceObjectEntity> leftObjects = CollUtil.split(leftUidList, 500).stream()
                        .flatMap(ids -> Optional.ofNullable(EntityUtil.getByIds(leftObjectType, ids)).orElse(Collections.emptyList()).stream())
                        .collect(Collectors.toList());
                if (CollUtil.isEmpty(leftObjects)) {
                    return;
                }
                for (WorkspaceObjectEntity entity : leftObjects) {
                    // 需要递归的类型
                    String recursionType = assignableFrom ? objectType : entity.getObjectType();
                    // 调用用户自定义的过滤条件，用来判断是否将该数据放入结果集中
                    if (Objects.nonNull(filter) && !filter.test(entity)) {
                        if (!isBreak) {
                            recursionParentBreak(entity.getUid(), recursionType, datas, filter, newCurrentDeep,maxDeep,isBreak,dictRelEnum);
                        }
                        continue;
                    }
                    // 将自身加入集合
                    datas.add(entity);
                    // 如果是数据集的话，就不在次进行递归调用
                    if (entity instanceof DatasetEntity) {
                        continue;
                    }
                    recursionParentBreak(entity.getUid(), recursionType, datas, filter, newCurrentDeep,maxDeep,isBreak,dictRelEnum);
                }
            });
        }
    }

    /***
     * 判断节点下是否存在子节点
     *
     * @param uid 节点id
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/14 13:56
     * @return {@link boolean}
     */
    public boolean hasChildren(String uid, String objectType, boolean isOwner) {
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        List<String> relations = dictUtil.getRightObjectRelations(objectType);
        if (CollUtil.isEmpty(relations)) {
            return false;
        }
        List<Pair<String, Object>> params = new ArrayList<>();
        params.add(Pair.of(RelationEntity.LEFT_OBJECT, uid));
        if (isOwner) {
            params.add(Pair.of(WorkspaceObjectEntity.OWNER_ID, userUtils.getCurrentUserId()));
        }
        return relations.stream().anyMatch(relation -> EntityUtil.getDynamicEqQuery(relation, params).fetchCount() > 0);
    }
    /**
     * 判断零组件（没有版本的零组件：工步）或版本下是否存下子集
     * @param uid 版本的uid
     * @param objectType 对象类型
     * @param isOwner 是否是拥有者
     * @param leftObject 零件的uid
     * @author: 薛锦龙
     * @time: 2022/6/21
     * @return: {@link boolean}
     */
    public boolean bomNodeHasChildren(String uid, String objectType, boolean isOwner,String leftObject) {
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        //通过字典获取关系
        List<DictItemVo> pseudoRel = dictUtil.listByObjectTypeAndDictRel(objectType,DictRelEnum.PSEUDO_REL);
        //为空表示没有子节点
        if (CollUtil.isEmpty(pseudoRel)) {
            return false;
        }
        return pseudoRel.stream().map(DictItemVo::getCode).anyMatch(type -> {
            List<Triple<String, Ops,Object>> params = new ArrayList<>();
            if (isOwner) {
                params.add(Triple.of(WorkspaceObjectEntity.OWNER_ID,Ops.EQ, userUtils.getCurrentUserId()));
            }
            //TODO 工步特殊处理
            if (!"BOMNode".equals(type)) {
                //数据集
                params.add(Triple.of(MasterRLEntity.LEFT_OBJECT,Ops.EQ,uid));
            }else {
                Class<?> entityClass = EntityUtil.getEntityClass(objectType);
                if(ItemEntity.class.isAssignableFrom(entityClass)){
                    //零组件
                }else if(ItemRevisionEntity.class.isAssignableFrom(entityClass)){
                   //版本(获取相同版本下的所有版次)
                    ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
                    List<WorkspaceObjectEntity> allSequences = itemRevision.getAllSequences(uid, objectType);
                    List<String> stringList = allSequences.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
                    params.add(Triple.of(BOMNodeEntity.PARENT_ITEM_REV, Ops.IN,stringList));
                }else {
                    //查找BomNode表
                    params.add(Triple.of(BOMNodeEntity.PARENT_ITEM, Ops.EQ,leftObject));
                }
            }
            return EntityUtil.getDynamicQuery(type,params).fetchCount() > 0;
        });
    }

    /***
     *判断节点上是否存在父节点
     *
     * @param uid 节点id
     * @param objectType 对象类型
     * @param isOwner 是否所属当前登录用户
     * @author: 王辉
     * @date: 2022/4/26 11:34
     * @return:  {@link boolean}
     */
    public boolean hasParents(String uid, String objectType, boolean isOwner) {
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        List<String> parentsRelations = dictUtil.listCodeByObjectTypeAndDictRel(objectType,DictRelEnum.PARENT_REL);
        if (CollUtil.isEmpty(parentsRelations)) {
            return false;
        }
        List<Pair<String, Object>> params = new ArrayList<>();
        params.add(Pair.of(RelationEntity.RIGHT_OBJECT, uid));
        if (isOwner) {
            params.add(Pair.of(WorkspaceObjectEntity.OWNER_ID, userUtils.getCurrentUserId()));
        }
        return parentsRelations.stream().anyMatch(relation -> EntityUtil.getDynamicEqQuery(relation, params).fetchCount() > 0);
    }
}
