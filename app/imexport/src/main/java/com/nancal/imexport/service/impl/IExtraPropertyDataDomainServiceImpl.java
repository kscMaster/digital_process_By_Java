package com.nancal.imexport.service.impl;

import cn.hutool.core.lang.Pair;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.nancal.api.model.*;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.annotation.OrderField;
import com.nancal.common.base.TableResponse;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.SqlUtil;
import com.nancal.framework.common.annotations.JsonDict;
import com.nancal.model.entity.BusinessObjectEntity;
import com.nancal.model.entity.ExtraPropertyDataEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.remote.to.MoreDictEntryReq;
import com.nancal.remote.vo.MoreDictEntryGroupVo;
import com.nancal.service.bo.Item;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.service.IExtraPropertyDataDomainService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author hewei
 * @date 2022/9/16 14:08
 * @Description
 */
@Service
public class IExtraPropertyDataDomainServiceImpl implements IExtraPropertyDataDomainService{

    @Autowired
    private DictUtil dictUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(List<ExtraPropertyDataReq> extraPropertyDataReqs,String leftObject,String leftObjectType){
        if (null != extraPropertyDataReqs && !extraPropertyDataReqs.isEmpty()) {
            Item item = SpringUtil.getBean(ItemFactory.class).create();
            final WorkspaceObjectEntity lastVersion = item.getLastVersion(leftObject, leftObjectType);
            List<Pair<String, Object>> leftObjectParams = Arrays.asList(
                    Pair.of(ExtraPropertyDataEntity.LEFT_OBJECT, lastVersion.getUid()));
            List<WorkspaceObjectEntity> fetchs = EntityUtil.getDynamicEqQuery(new ExtraPropertyDataResp().getObjectType(), leftObjectParams).fetch();
            extraPropertyDataReqs.forEach(extraPropertyDataReq -> {
                if(fetchs.isEmpty()){
                    // 添加新的关联
                    extraPropertyDataReq.setLeftObject(lastVersion.getUid());
                    extraPropertyDataReq.setLeftObjectType(lastVersion.getObjectType());
                    IExtraPropertyDataDomainService.super.save(extraPropertyDataReq);
                }else {
                    List<Pair<String, Object>> pairs = Arrays.asList(
                            Pair.of(ExtraPropertyDataEntity.UID, extraPropertyDataReq.getUid())
                    );
                    List<WorkspaceObjectEntity> entities = EntityUtil.getDynamicEqQuery(new ExtraPropertyDataResp().getObjectType(), pairs).fetch();
                    if (!entities.isEmpty()){
                        ExtraPropertyDataEntity extraPropertyDataEntity = (ExtraPropertyDataEntity)entities.get(0);
                        extraPropertyDataEntity.setValue(extraPropertyDataReq.getValue());
                        extraPropertyDataEntity.setProperty(extraPropertyDataReq.getProperty());
                        SpringUtil.getBean(EntityManager.class).merge(extraPropertyDataEntity);
                    }

                }

            });
        }
    }

   @Override
   @Transactional(rollbackFor = Exception.class)
   public void insert(List<ExtraPropertyDataReq> extraPropertyDataReqs, WorkspaceObjectResp save){
       if (null != extraPropertyDataReqs && !extraPropertyDataReqs.isEmpty()) {
           Item item = SpringUtil.getBean(ItemFactory.class).create();
           final WorkspaceObjectEntity lastVersion = item.getLastVersion(save.getUid(), save.getObjectType());
           extraPropertyDataReqs.forEach(extraPropertyDataReq -> {
               extraPropertyDataReq.setLeftObject(lastVersion.getUid());
               extraPropertyDataReq.setLeftObjectType(lastVersion.getObjectType());
               IExtraPropertyDataDomainService.super.save(extraPropertyDataReq);
           });


       }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ExtraPropertyDataResp> getLikeList(List<ExtraPropertyDataReq> list,String uid){
        // 拼接SQL
        // 创建需要查询的是哪张表
        EntityPathBase<ExtraPropertyDataEntity> table = new EntityPathBase(ExtraPropertyDataEntity.class, new ExtraPropertyDataResp().getObjectType());
        JPAQuery<ExtraPropertyDataEntity> jpaQuery = SpringUtil.getBean(JPAQueryFactory.class).selectFrom(table);
        // 创建查询条件
        BooleanBuilder where = new BooleanBuilder();
        where.and(ExpressionUtils.predicate(Ops.EQ, ExpressionUtils.path(ExtraPropertyDataEntity.class,ExtraPropertyDataEntity.LEFT_OBJECT_TYPE), ExpressionUtils.toExpression(EntityUtil.getObjectType())));
        AtomicInteger i = new AtomicInteger();
        list.forEach(extraPropertyDataResp -> {
            if (i.get() == 0) {
                where.and(ExpressionUtils.predicate(Ops.EQ, ExpressionUtils.path(ExtraPropertyDataEntity.class, ExtraPropertyDataEntity.PROPERTY), ExpressionUtils.toExpression(extraPropertyDataResp.getProperty())));
                where.and(ExpressionUtils.predicate(Ops.LIKE, ExpressionUtils.path(ExtraPropertyDataEntity.class, ExtraPropertyDataEntity.VALUE), ExpressionUtils.toExpression(SqlUtil.likeStar(extraPropertyDataResp.getValue()))));
            }else {
                where.or(ExpressionUtils.predicate(Ops.EQ, ExpressionUtils.path(ExtraPropertyDataEntity.class, ExtraPropertyDataEntity.PROPERTY), ExpressionUtils.toExpression(extraPropertyDataResp.getProperty())));
                where.and(ExpressionUtils.predicate(Ops.LIKE, ExpressionUtils.path(ExtraPropertyDataEntity.class, ExtraPropertyDataEntity.VALUE), ExpressionUtils.toExpression(SqlUtil.likeStar(extraPropertyDataResp.getValue()))));
            }
            i.getAndIncrement();
        });
        List<ExtraPropertyDataResp> respList = new ArrayList<>();
        if (!list.isEmpty()) {
            where.and(ExpressionUtils.predicate(Ops.EQ, ExpressionUtils.path(ExtraPropertyDataEntity.class, BusinessObjectEntity.DEL_FLAG), ExpressionUtils.toExpression(Boolean.FALSE)));
            // 获取排序
            OrderSpecifier[] specifiers = FieldUtils.getFieldsListWithAnnotation(getClass(), OrderField.class).stream().map(field -> {
                OrderField orderField = field.getAnnotation(OrderField.class);
                OrderSpecifier orderSpecifier = new OrderSpecifier(orderField.value(), ExpressionUtils.path(getClass(), field.getName()));
                return Pair.of(orderField.priority(),orderSpecifier);
            }).sorted(Comparator.comparing(Pair::getKey)).map(Pair::getValue).collect(Collectors.toList()).toArray(new OrderSpecifier[]{});
            //获取对象的上一级的关系
            List<ExtraPropertyDataEntity> fetchs = jpaQuery.where(where).orderBy(specifiers).fetch();
            fetchs.forEach(fetch -> {
                ExtraPropertyDataResp extraPropertyDataResp = new ExtraPropertyDataResp();
                BeanUtil.copyPropertiesIgnoreNull(fetch, extraPropertyDataResp);
                respList.add(extraPropertyDataResp);
            });
        }
        return respList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
   public List<ExtraPropertyDataResp> getList(WorkspaceObjectResp workspaceObjectResp){
       List<Pair<String, Object>> leftObjectParams = Arrays.asList(
               Pair.of(ExtraPropertyDataEntity.LEFT_OBJECT, workspaceObjectResp.getUid()),
               Pair.of(ExtraPropertyDataEntity.LEFT_OBJECT_TYPE, workspaceObjectResp.getObjectType())
       );
       //获取对象的上一级的关系
       List<WorkspaceObjectEntity> fetchs = EntityUtil.getDynamicEqQuery(new ExtraPropertyDataResp().getObjectType(), leftObjectParams).fetch();
       List<ExtraPropertyDataResp> respList = new ArrayList<>();
       fetchs.forEach(fetch ->{
           ExtraPropertyDataResp extraPropertyDataResp = new ExtraPropertyDataResp();
           BeanUtil.copyPropertiesIgnoreNull(fetch,extraPropertyDataResp);
           respList.add(extraPropertyDataResp);
       });
       return respList;
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upgradeRelation(WorkspaceObjectReq req,List<ExtraPropertyDataReq> list){
        // 获取上一个版本关联的自定义属性
        ItemRevisionResp itemRevisionResp = new ItemRevisionResp();
        itemRevisionResp.setObjectType(EntityUtil.getRevision(req.getLeftObjectType()));
        itemRevisionResp.setUid(req.getUid());
        // 转换
        List<ExtraPropertyDataReq> reqList = new ArrayList<>();
        list.forEach(extraPropertyDataResp -> {
            ExtraPropertyDataReq extraPropertyDataReq = new ExtraPropertyDataReq();
            extraPropertyDataReq.setProperty(extraPropertyDataResp.getProperty());
            extraPropertyDataReq.setValue(extraPropertyDataResp.getValue());
            reqList.add(extraPropertyDataReq);
        });
        // 插入需要关联的自定义属性
        WorkspaceObjectResp workspaceObjectResp = new WorkspaceObjectResp();
        workspaceObjectResp.setUid(req.getLeftObject());
        workspaceObjectResp.setObjectType(req.getLeftObjectType());
        insert(reqList,workspaceObjectResp);
    }

    @Override
    public TableResponse<List<Map<String,Object>>> getCustomObject(TableResponse<WorkspaceObjectResp> workspaceObjectRespTableResponse,String dictType){
        List<WorkspaceObjectResp> listData = workspaceObjectRespTableResponse.getData();
        List<Map<String,Object>> mapList = new ArrayList<>();
        listData.forEach(data ->{
            Map map = JSON.parseObject(JSON.toJSONString(data), Map.class);
            Class<? extends WorkspaceObjectResp> aClass = data.getClass();
            getClassObject(aClass, map);
            List<ExtraPropertyDataResp> list = getList(data);
            MoreDictEntryReq moreDictEntryReq = new MoreDictEntryReq();
            List<String> appCodes = new ArrayList<>(3);
            appCodes.add("lz624-library");
            List<String> dictTypes = new ArrayList<>();
            dictTypes.add(dictType);
            moreDictEntryReq.setAppCodes(appCodes);
            moreDictEntryReq.setDictTypes(dictTypes);
            List<MoreDictEntryGroupVo> codeValueList = dictUtil.getMoreDictEntryVo(moreDictEntryReq);
            codeValueList.forEach(codeValue ->{
                codeValue.getDictList().forEach(code ->{
                    List<ExtraPropertyDataResp> collect = list.stream().filter(t -> t.getProperty().equals(code.getCode())).collect(Collectors.toList());
                    if (!collect.isEmpty()){
                        AuxiliaryMaterialRevisionImportReq parseObject = JSON.parseObject(code.getRemark(), AuxiliaryMaterialRevisionImportReq.class);
                        ExtraPropertyDataResp extraPropertyDataResp = collect.get(0);
                        if ("select".equals(parseObject.getType())){
                            String nameByDictValue = dictUtil.getNameByDictValue(parseObject.getDictType(), extraPropertyDataResp.getValue());
                            map.put(code.getCode(),nameByDictValue);
                        }else if("radio".equals(parseObject.getType())){
                            map.put(code.getCode(),extraPropertyDataResp.getValue().equals("true")?"是":"否");
                        }else {
                            map.put(code.getCode(),extraPropertyDataResp.getValue());
                        }

                    }
                });
            });
            mapList.add(map);
        });
       return new TableResponse(workspaceObjectRespTableResponse.getTotal(),mapList);
    }

    @SneakyThrows
    protected Map<String,Object> getClassObject(Class<?> aClass,Map<String,Object> map){
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            Object o = map.get(field.getName());
            JsonDict annotation = field.getAnnotation(JsonDict.class);
            if (null != annotation) {
                Object object = paramDictionary(annotation, o == null ? "" : o);
                map.put(field.getName() + "Desc", object);
            } else {
                field.setAccessible(true);
                map.put(field.getName(), o == null ? "" : o);
            }
        }
        Class<?> superclass = aClass.getSuperclass();
        if (null != superclass) {
            getClassObject(superclass, map);
        }
        return map;
    }

    public Object paramDictionary(JsonDict jsonDict,Object value){
        String nameByDictCode =  dictUtil.getNameByDictValue(jsonDict.value(), value.toString());
        value = (null == nameByDictCode ? "" : nameByDictCode);
        return value;
    }
}
