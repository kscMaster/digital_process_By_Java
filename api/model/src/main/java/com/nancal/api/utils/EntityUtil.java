package com.nancal.api.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.BusinessObjectReq;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.WorkspaceObjectReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.annotation.OrderField;
import com.nancal.common.annotation.QueryField;
import com.nancal.common.constants.Constant;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.SqlUtil;
import com.nancal.model.entity.BusinessObjectEntity;
import com.nancal.model.entity.RelationEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EntityUtil {

    /***
     * 获取对象类型的全包名
     *
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/12 12:53
     * @return {@link String}
     */
    public static String getEntityPackage(String objectType) {
        return ClassUtil.getPackage(BusinessObjectEntity.class) + StrUtil.DOT + objectType + Constant.ENTITY;
    }

    /***
     * 获取对象类型的全包名
     *
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/12 12:53
     * @return {@link Class<?>}
     */
    public static Class<?> getEntityClass(String objectType) {
        String packagePath = getEntityPackage(objectType);
        return ClassUtil.loadClass(packagePath);
    }

    /**
     * 判断是否是零组件,用来区分workspace，如：工步
     * @param objectType
     * @return
     */
    public static boolean checkItem(String objectType){
        try{
            EntityUtil.getEntityClass(EntityUtil.getRevision(objectType));
            return true;
        }catch (Exception e){
            return false;
        }catch (NoClassDefFoundError e){
            return false;
        }
    }

    /**
     * 判断是否关联表
     * @return
     */
    public static boolean checkRelation(String objectType){
        try{
            Class<?> entityClass = EntityUtil.getEntityClass(objectType);
            if(RelationEntity.class.isAssignableFrom(entityClass)){
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }catch (NoClassDefFoundError e){
            return false;
        }
    }

    /***
     * 获取指定参数的Req全包名
     *
     * @param objectType 指定类名称
     * @author 徐鹏军
     * @date 2022/4/2 13:13
     * @return {@link String}
     */
    public static String getReqPackage(String objectType) {
        String aPackage = ClassUtil.getPackage(BusinessObjectResp.class);
        return aPackage + StrUtil.DOT + objectType + "Req";
    }

    /***
     * 获取指定参数的Resp全包名
     *
     * @param objectType 指定类名称
     * @author 徐鹏军
     * @date 2022/4/2 13:13
     * @return {@link String}
     */
    public static String getRespPackage(String objectType) {
        String aPackage = ClassUtil.getPackage(BusinessObjectResp.class);
        return aPackage + StrUtil.DOT + objectType + "Resp";
    }


    /**
     * entity转resp
     * @param entity
     * @return
     */
    public static WorkspaceObjectResp entityToResp(WorkspaceObjectEntity entity) {
        String respType = getRespPackage(entity.getObjectType());
        WorkspaceObjectResp object = ReflectUtil.newInstance(respType);
        BeanUtil.copyPropertiesIgnoreNull(entity, object);
        return object;
    }

    /**
     * req转entity
     *
     * @param req
     * @return
     */
    public static WorkspaceObjectEntity reqToEnity(WorkspaceObjectReq req) {
        String objectType = StrUtil.isNotBlank(req.getObjectType())?req.getObjectType():EntityUtil.getObjectType();
        // 构建entity全包名
        String classPath = EntityUtil.getEntityPackage(objectType);
        WorkspaceObjectEntity entity = ReflectUtil.newInstance(classPath);
        BeanUtil.copyPropertiesIgnoreNull(req, entity);
        return entity;
    }

    /***
     * 拼接service实现bean名称
     *
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/12 16:57
     * @return {@link String}
     */
    public static String getBeanServiceImpl(String objectType) {
        return CharSequenceUtil.lowerFirst(objectType) + "DomainServiceImpl";
    }

    /***
     * 获取请求对象
     *
     * @author 徐鹏军
     * @date 2022/3/31 13:21
     * @return {@link String}
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /***
     * 获取对象类型
     *
     * @author 徐鹏军
     * @date 2022/3/31 13:21
     * @return {@link String}
     */
    public static String getObjectType() {
        return StrUtil.subBetween(getRequest().getRequestURI(), StrUtil.SLASH);
    }
    /***
     * 获取对象类型
     *
     * @author 徐鹏军
     * @date 2022/3/31 13:21
     * @return {@link String}
     */
    public static <T extends WorkspaceObjectEntity> String getObjectType(Class<T> clazz) {
        return StrUtil.removeSuffix(clazz.getSimpleName(),Constant.ENTITY);
    }
    /***
     * 获取对象类型
     *
     * @author 徐鹏军
     * @date 2022/3/31 13:21
     * @return {@link String}
     */
    public static <T extends WorkspaceObjectEntity> String getObjectType(T t) {
        return getObjectType(t.getClass());
    }
    /***
     * 拼接零组件版本类型名称
     *
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/12 16:57
     * @return {@link String}
     */
    public static String getRevision(String objectType) {
        return StrUtil.addSuffixIfNot(objectType, "Revision");
    }
    /***
     * 根据版本类型转成组件类型
     *
     * @param revisionType 版本类型
     * @author 徐鹏军
     * @date 2022/4/12 16:57
     * @return {@link String}
     */
    public static String getObjectTypeByRevisionType(String revisionType) {
        return StrUtil.removeSuffix(getRevision(revisionType), "Revision");
    }

    /***
     * 获取动态Query
     *
     * @param objectType 对象类型
     * @param function 条件函数
     * @author 徐鹏军
     * @date 2022/4/12 17:08
     * @return {@link JPAQuery<T extends WorkspaceObjectEntity>}
     */
    @Deprecated
    public static <T extends WorkspaceObjectEntity> JPAQuery<T> getDynamicQuery(String objectType, Function<Class<?>, Predicate> function) {
        // 加载需要处理的类型
        Class<?> clazz = EntityUtil.getEntityClass(objectType);
        // 创建需要查询的是哪张表
        EntityPathBase<T> table = new EntityPathBase(clazz, objectType);
        JPAQuery<T> jpaQuery = SpringUtil.getBean(JPAQueryFactory.class).selectFrom(table);
        if (Objects.nonNull(function)) {
            Predicate where = function.apply(clazz);
            return jpaQuery.where(where);
        }
        // 如过没有查询条件，默认带上delFlag=false
        return jpaQuery.where(new BeanPath<Object>(clazz, BusinessObjectEntity.DEL_FLAG).eq(Boolean.FALSE));
    }

    /***
     * 获取动态Query
     *
     * @param objectType 对象类型
     * @param pair 条件函数
     * @author 徐鹏军
     * @date 2022/4/12 17:08
     * @return {@link JPAQuery< WorkspaceObjectEntity>}
     */
    public static <T extends WorkspaceObjectEntity> JPAQuery<T> getDynamicEqQuery(String objectType, Pair<String, Object> pair) {
        return getDynamicQuery(objectType, Triple.of(pair.getKey(), Ops.EQ, pair.getValue()));
    }
    /***
     * 获取动态Query
     *
     * @param objectType 对象类型
     * @param pairs 条件函数
     * @author 徐鹏军
     * @date 2022/4/12 17:08
     * @return {@link JPAQuery< WorkspaceObjectEntity>}
     */
    public static <T extends WorkspaceObjectEntity> JPAQuery<T> getDynamicEqQuery(String objectType, List<Pair<String, Object>> pairs) {
        List<Triple<String, Ops, Object>> triples = pairs.stream().map(pair -> {
            return Triple.of(pair.getKey(), Ops.EQ, pair.getValue());
        }).collect(Collectors.toList());
        return getDynamicQuery(objectType,triples);
    }

    public static <T extends WorkspaceObjectEntity> JPAQuery<T> getDynamicEqQuery(String objectType, List<Pair<String, Object>> pairs,boolean includeDel) {
        List<Triple<String, Ops, Object>> triples = pairs.stream().map(pair -> {
            return Triple.of(pair.getKey(), Ops.EQ, pair.getValue());
        }).collect(Collectors.toList());
        return getDynamicQuery(objectType,triples,includeDel);
    }

    /***
     * 获取动态Query
     *
     * @param objectType 对象类型
     * @param triple 条件函数
     * @author 徐鹏军
     * @date 2022/4/12 17:08
     * @return {@link JPAQuery< WorkspaceObjectEntity>}
     */
    public static <T extends WorkspaceObjectEntity> JPAQuery<T> getDynamicQuery(String objectType, Triple<String, Ops, Object> triple) {
        return getDynamicQuery(objectType, Collections.singletonList(triple));
    }
    public static <T extends WorkspaceObjectEntity> JPAQuery<T> getDynamicQuery(String objectType, List<Triple<String, Ops, Object>> triples) {
        return getDynamicQuery(objectType,triples,false);
    }



    /***
     * 获取动态Query
     *
     * @param objectType 对象类型
     * @param triples 条件函数
     * @author 徐鹏军
     * @date 2022/4/12 17:08
     * @return {@link JPAQuery< WorkspaceObjectEntity>}
     */
    public static <T extends WorkspaceObjectEntity> JPAQuery<T> getDynamicQuery(String objectType, List<Triple<String, Ops, Object>> triples,boolean includeDel) {
        // 加载需要处理的类型
        Class<?> clazz = EntityUtil.getEntityClass(objectType);
        // 创建需要查询的是哪张表
        EntityPathBase<T> table = new EntityPathBase(clazz, objectType);
        JPAQuery<T> jpaQuery = SpringUtil.getBean(JPAQueryFactory.class).selectFrom(table);
        // 创建查询条件
        BooleanBuilder where = new BooleanBuilder();
        triples.forEach(triple -> {
            PredicateOperation predicate = ExpressionUtils.predicate(
                    triple.getMiddle(),
                    ExpressionUtils.path(clazz, triple.getLeft()),
                    ExpressionUtils.toExpression(triple.getRight())
            );
            where.and(predicate);
        });
        if(!includeDel) {
            PredicateOperation delFlag = ExpressionUtils.predicate(Ops.EQ,
                    ExpressionUtils.path(clazz, BusinessObjectEntity.DEL_FLAG),
                    ExpressionUtils.toExpression(Boolean.FALSE)
            );
            where.and(delFlag);
        }
        return jpaQuery.where(where);
    }

    /***
     * 获取动态Query
     *
     * @param objectType 对象类型
     * @param triples 条件函数
     * @author 徐鹏军
     * @date 2022/4/12 17:08
     * @return {@link JPAQuery< WorkspaceObjectEntity>}
     */
    public static <T extends WorkspaceObjectEntity> JPAQuery<T> getDynamicOrQuery(String objectType, List<Triple<String, Ops, Object>> triples,boolean includeDel) {
        // 加载需要处理的类型
        Class<?> clazz = EntityUtil.getEntityClass(objectType);
        // 创建需要查询的是哪张表
        EntityPathBase<T> table = new EntityPathBase(clazz, objectType);
        JPAQuery<T> jpaQuery = SpringUtil.getBean(JPAQueryFactory.class).selectFrom(table);
        // 创建查询条件
        BooleanBuilder where = new BooleanBuilder();
        triples.forEach(triple -> {
            PredicateOperation predicate = ExpressionUtils.predicate(
                    triple.getMiddle(),
                    ExpressionUtils.path(clazz, triple.getLeft()),
                    ExpressionUtils.toExpression(triple.getRight())
            );
            where.and(predicate);
        });
        if(!includeDel) {
            PredicateOperation delFlag = ExpressionUtils.predicate(Ops.EQ,
                    ExpressionUtils.path(clazz, BusinessObjectEntity.DEL_FLAG),
                    ExpressionUtils.toExpression(Boolean.FALSE)
            );
            where.and(delFlag);
        }
        return jpaQuery.where(where);
    }
    /***
     * 根据主键查询数据
     *
     * @param objectType 对象类型
     * @param uid 主键id
     * @author 徐鹏军
     * @date 2022/4/12 17:08
     * @return {@link JPAQuery< WorkspaceObjectEntity>}
     */
    public static <T extends WorkspaceObjectEntity> T getById(String objectType, String uid) {
        WorkspaceObjectEntity workspaceObjectEntity = getDynamicEqQuery(objectType, Pair.of(BusinessObjectEntity.UID, uid)).fetchFirst();
        if (Objects.isNull(workspaceObjectEntity)) {
            return null;
        }
        return (T)workspaceObjectEntity;
    }
    /***
     * 根据主键查询数据
     *
     * @param objectType 对象类型
     * @param uids 主键ids
     * @author 徐鹏军
     * @date 2022/4/12 17:08
     * @return {@link JPAQuery< WorkspaceObjectEntity>}
     */
    public static <T extends WorkspaceObjectEntity> List<T> getByIds(String objectType, List<String> uids) {
        return (List<T>) getDynamicQuery(objectType, Triple.of(BusinessObjectEntity.UID, Ops.IN, uids)).fetch();
    }


    /***
     * 获取查询条件
     * <P>
     *     根据自定义注解组装查询条件
     *     返回左是查询条件，右是排序条件
     * </P>
     *
     * @param req 请求对象
     * @author 徐鹏军
     * @date 2022/4/12 17:08
     * @return {@link JPAQuery< WorkspaceObjectEntity>}
     */
    public static <T extends BusinessObjectReq> Pair<List<Triple<String, Ops, Object>>, OrderSpecifier[]> getAnnotationQueryParam(T req,List<Triple<String, Ops, Object>> of) {
        Pair<List<Triple<String, Ops, Object>>, OrderSpecifier[]> annotationQueryParam = getAnnotationQueryParam(req);
        if (null != annotationQueryParam){
            List<Triple<String, Ops, Object>> key = annotationQueryParam.getKey();
            key.addAll(of);
            return annotationQueryParam;
        }else {
            // 获取排序
            OrderSpecifier[] specifiers = FieldUtils.getFieldsListWithAnnotation(req.getClass(), OrderField.class).stream().map(field -> {
                OrderField orderField = field.getAnnotation(OrderField.class);
                OrderSpecifier orderSpecifier = new OrderSpecifier(orderField.value(), ExpressionUtils.path(req.getClass(), field.getName()));
                return Pair.of(orderField.priority(),orderSpecifier);
            }).sorted(Comparator.comparing(Pair::getKey)).map(Pair::getValue).collect(Collectors.toList()).toArray(new OrderSpecifier[]{});
            List<Triple<String, Ops, Object>> arrayList = new ArrayList<>(of);
            arrayList.add(Triple.of(BusinessObjectEntity.DEL_FLAG, Ops.EQ, Boolean.FALSE));
            return Pair.of(arrayList,specifiers);

        }
    }

    /***
     * 获取查询条件
     * <P>
     *     根据自定义注解组装查询条件
     *     返回左是查询条件，右是排序条件
     * </P>
     *
     * @param req 请求对象
     * @author 徐鹏军
     * @date 2022/4/12 17:08
     * @return {@link JPAQuery< WorkspaceObjectEntity>}
     */
    public static <T extends BusinessObjectReq> Pair<List<Triple<String, Ops, Object>>, OrderSpecifier[]> getAnnotationQueryParam(T req) {
        // 1：获取请求对象所有标注需要查询的属性，即属性中带有@QueryField注解的
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(req.getClass(), QueryField.class);
        if (CollUtil.isEmpty(fields)) {
            return null;
        }
        // 2：将属性值是空的过滤,3：根据@QueryField的value组合查询条件
        List<Triple<String, Ops, Object>> params = fields.stream().map(field -> {
            Object fieldValue = ReflectUtil.getFieldValue(req, field);
            if (Objects.isNull(fieldValue) || StrUtil.isBlank(fieldValue.toString())) {
                return null;
            }
            QueryField queryField = field.getAnnotation(QueryField.class);
            if (queryField.value() == Ops.LIKE) {
                fieldValue = queryField.wildcard() ? SqlUtil.likeStar(fieldValue.toString()) : SqlUtil.like(fieldValue.toString());
            }
            return Triple.of(field.getName(), queryField.value(), fieldValue);
        }).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollUtil.isEmpty(params)) {
            return null;
        }
        // 获取排序
        OrderSpecifier[] specifiers = FieldUtils.getFieldsListWithAnnotation(req.getClass(), OrderField.class).stream().map(field -> {
            OrderField orderField = field.getAnnotation(OrderField.class);
            OrderSpecifier orderSpecifier = new OrderSpecifier(orderField.value(), ExpressionUtils.path(req.getClass(), field.getName()));
            return Pair.of(orderField.priority(),orderSpecifier);
        }).sorted(Comparator.comparing(Pair::getKey)).map(Pair::getValue).collect(Collectors.toList()).toArray(new OrderSpecifier[]{});
        params.add(Triple.of(BusinessObjectEntity.DEL_FLAG, Ops.EQ, Boolean.FALSE));
        return Pair.of(params,specifiers);
    }


    /***
     * 获取动态Query
     *
     * @param clazz 对象
     * @param pair 条件函数
     * @author 徐鹏军
     * @date 2022/4/12 17:08
     * @return {@link JPAQuery< WorkspaceObjectEntity>}
     */
    public static <T extends WorkspaceObjectEntity> JPAQuery<T> getDynamicEqQuery(Class<T> clazz, Pair<String, Object> pair) {
        return getDynamicQuery(clazz, Objects.nonNull(pair) ? Triple.of(pair.getKey(), Ops.EQ, pair.getValue()) : null);
    }

    /***
     * 获取动态Query
     *
     * @param clazz 对象
     * @param triple 条件函数
     * @author 徐鹏军
     * @date 2022/4/12 17:08
     * @return {@link JPAQuery< WorkspaceObjectEntity>}
     */
    public static <T extends WorkspaceObjectEntity> JPAQuery<T> getDynamicQuery(Class<T> clazz, Triple<String, Ops, Object> triple) {
        return getDynamicQuery(clazz, Objects.nonNull(triple) ? Collections.singletonList(triple) : null);
    }

    /***
     * 获取动态Query
     *
     * @param clazz 对象
     * @param pairs 条件函数
     * @author 徐鹏军
     * @date 2022/4/12 17:08
     * @return {@link JPAQuery< WorkspaceObjectEntity>}
     */
    public static <T extends WorkspaceObjectEntity> JPAQuery<T> getDynamicEqQuery(Class<T> clazz, List<Pair<String, Object>> pairs) {
        if (CollUtil.isEmpty(pairs)) {
            return getDynamicQuery(clazz, Collections.emptyList());
        }
        List<Triple<String, Ops, Object>> triples = pairs.stream().map(pair -> {
            return Triple.of(pair.getKey(), Ops.EQ, pair.getValue());
        }).collect(Collectors.toList());
        return getDynamicQuery(clazz, triples);
    }


    /***
     * 获取动态Query
     *
     * @param clazz 对象
     * @param triples 条件函数
     * @author 徐鹏军
     * @date 2022/4/12 17:08
     * @return {@link JPAQuery< WorkspaceObjectEntity>}
     */
    public static <T extends WorkspaceObjectEntity> JPAQuery<T> getDynamicQuery(Class<T> clazz, List<Triple<String, Ops, Object>> triples) {
        // 创建需要查询的是哪张表
        EntityPathBase<T> table = new EntityPathBase(clazz, getObjectType(clazz));
        JPAQuery<T> jpaQuery = SpringUtil.getBean(JPAQueryFactory.class).selectFrom(table);
        // 创建查询条件
        BooleanBuilder where = new BooleanBuilder();
        if (CollUtil.isNotEmpty(triples)) {
            triples.forEach(triple -> {
                PredicateOperation predicate = ExpressionUtils.predicate(
                        triple.getMiddle(),
                        ExpressionUtils.path(clazz, triple.getLeft()),
                        ExpressionUtils.toExpression(triple.getRight())
                );
                where.and(predicate);
            });
        }
        PredicateOperation delFlag = ExpressionUtils.predicate(Ops.EQ,
                ExpressionUtils.path(clazz, BusinessObjectEntity.DEL_FLAG),
                ExpressionUtils.toExpression(Boolean.FALSE)
        );
        where.and(delFlag);
        return jpaQuery.where(where);
    }

}
