package com.nancal.api.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.nancal.remote.to.MoreDictEntryReq;
import com.nancal.common.annotation.Dictionary;
import com.nancal.common.annotation.TimeLog;
import com.nancal.common.base.Response;
import com.nancal.common.constants.CacheConstant;
import com.nancal.common.constants.Constant;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.DictRelEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.remote.service.RemoteLezaoDictService;
import com.nancal.remote.to.DictTypeListTo;
import com.nancal.remote.vo.DictItemVo;
import com.nancal.remote.vo.MoreDictEntryGroupVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/***
 * 操作字典工具类
 *
 * @author 徐鹏军
 * @date 2022/4/11 9:33
 */
@Slf4j
@Component
public class DictUtil {

    @Autowired
    private RemoteLezaoDictService remoteLezaoDictService;

    @Autowired
    private DictUtil dictUtil;

    /***
     * 根据字典类型获取类型下所有的字典明细
     *
     * @param dictType 字典类型
     * @author 徐鹏军
     * @date 2022/4/11 9:34
     */
    public Map<String, String> getCodeValueMap(String dictType) {
        List<DictItemVo> voList = dictUtil.getCodeValueList(dictType);
        if (CollUtil.isEmpty(voList)) {
            return Collections.emptyMap();
        }
        return voList.stream().collect(Collectors.toMap(DictItemVo::getCode, DictItemVo::getValue, (key1, key2) -> key2));
    }

    /***
     * 根据字典类型获取类型下所有的字典明细
     *
     * @param dictType 字典类型
     * @author 徐鹏军
     * @date 2022/4/11 9:34
     */
    @Cacheable(cacheNames = CacheConstant.DICT_KEY,key = "#root.methodName+'_'+#dictType")
    public List<DictItemVo> getCodeValueList(String dictType) {
        Response<List<DictItemVo>> response = remoteLezaoDictService.list(new DictTypeListTo(dictType));
        if (Objects.isNull(response)) {
            throw new ServiceException(ErrorCode.REMOTE_FAIL, "远程调用字典服务异常");
        }
        if (Boolean.TRUE.equals(response.isFail())) {
            log.error("远程调用字典服务异常,{}", response);
            throw new ServiceException(ErrorCode.REMOTE_FAIL, response.getMsg());
        }
        return response.getData();
    }


    /***
     * 根据字典类型获取类型下所有的字典明细
     *
     * @param moreDictEntryReq 字典类型
     * @author 徐鹏军
     * @date 2022/4/11 9:34
     */
    public List<MoreDictEntryGroupVo> getMoreDictEntryVo(MoreDictEntryReq moreDictEntryReq) {
        Response<List<MoreDictEntryGroupVo>> response = remoteLezaoDictService.getMoreDictEntryGroup(moreDictEntryReq);
        if (Objects.isNull(response)) {
            throw new ServiceException(ErrorCode.REMOTE_FAIL, "远程调用字典服务异常");
        }
        if (Boolean.TRUE.equals(response.isFail())) {
            log.error("远程调用字典服务异常,{}", response);
            throw new ServiceException(ErrorCode.REMOTE_FAIL, response.getMsg());
        }
        return response.getData();
    }

    /***
     * 根据左右对象类型获取关系类型
     *
     * 假如左对象为R006Part，右对象类型为R006PartRevision。
     *
     * 1. 查找名称为R006Part_default_rel的字典, 无法找到
     * 2. 获取R006Part的父对象类型为R006Item，查找R006Item_deafult_rel,无法找到
     * 3. 获取R006Item的父对象类型为Item，查找字典Item_default_rel，返回字典列表
     * 4. 在字典中获取值为右对象类型R006PartRevision时的显示名称，不存在值R006PartRevision
     * 5. 获取R006PartRevision的父对象类型为R006ItemRevision，不存在该值
     * 6. 获取R006ItemRevision的父对象类型为ItemRevision，当值为ItemRevision时，显示名称为MasterRL
     * 7. 则可知当左对象为R006Part，右对象为R006PartRevision时，默认关系为MasterRL。
     *
     * @param leftObjectType 左对象类型
     * @param rightObjectType 右对象类型
     * @author 徐鹏军
     * @date 2022/4/12 11:35
     * @return {@link String}
     */
    @Cacheable(cacheNames = CacheConstant.DICT_KEY,key = "#root.methodName+'_'+#leftObjectType+'_'+#rightObjectType+'_default_rel'")
    public String getRelation(String leftObjectType, String rightObjectType) {
        // 获取左对象字典明细
        List<DictItemVo> itemVoList = getItemByObjectTypeAndDictType(leftObjectType, leftObjectType + "_default_rel");
        if (CollUtil.isEmpty(itemVoList)) {
            throw new ServiceException(ErrorCode.E_10, "查询不到左对象类型为[" + leftObjectType + "]的数据字典");
        }
        // 循环遍历字典明细数据，找出和右对象匹配的字典名称
        for (DictItemVo vo : itemVoList) {
            // 构建父类型
            Class<?> parentClazz = EntityUtil.getEntityClass(vo.getCode());
            // 构建子类型
            Class<?> childClazz = EntityUtil.getEntityClass(rightObjectType);
            if (parentClazz.isAssignableFrom(childClazz)) {
                return vo.getValue();
            }
        }
        // 转义类型，用于提示
        Map<String, String> valueMap = getCodeValueMap(DictConstant.OBJECT_TYPE_NAME);
        String left = MapUtil.isEmpty(valueMap) ? leftObjectType : valueMap.getOrDefault(leftObjectType, leftObjectType);
        String right = MapUtil.isEmpty(valueMap) ? rightObjectType : valueMap.getOrDefault(rightObjectType, rightObjectType);
        throw new ServiceException(ErrorCode.E_10, "无法关联" + left + "和" + right + "的关系");
    }

    /**
     * 查询对应的流程列表
     * @param objectType
     * @return
     */
    @Cacheable(cacheNames = CacheConstant.DICT_KEY,key = "#root.methodName+'_'+#objectType+'_processList'")
    public List<DictItemVo> getProcessListByObjectType(String objectType) {
        // 获取左对象字典明细
        List<DictItemVo> itemVoList = getItemByObjectTypeAndDictType(objectType, objectType + "_processList");
        if (CollUtil.isEmpty(itemVoList)) {
            throw new ServiceException(ErrorCode.E_10, "未匹配到配置的流程列表");
        }
        return  itemVoList;
    }

    public List<DictItemVo> listByObjectTypeAndDictRel(String objectType, DictRelEnum dictRelEnum){
        return dictUtil.listByObjectTypeAndDictRel(objectType,dictRelEnum.getCode());
    }

    @Cacheable(cacheNames = CacheConstant.DICT_KEY,key = "#root.methodName+'_'+#objectType+#dictRel")
    public List<DictItemVo> listByObjectTypeAndDictRel(String objectType,String dictRel) {
        // 获取左对象字典明细
        List<DictItemVo> itemVoList = getItemByObjectTypeAndDictType(objectType, objectType + dictRel);
        if (CollUtil.isEmpty(itemVoList)) {
            return Collections.emptyList();
        }
        return  itemVoList;
    }

    public List<String> listCodeByObjectTypeAndDictRel(String objectType,DictRelEnum dictRelEnum){
        return dictUtil.listCodeByObjectTypeAndDictRel(objectType,dictRelEnum.getCode());
    }

    @Cacheable(cacheNames = CacheConstant.DICT_KEY,key = "#root.methodName+'_'+#objectType+#dictRel")
    public List<String> listCodeByObjectTypeAndDictRel(String objectType,String dictRel) {
        // 获取左对象字典明细
        List<DictItemVo> itemVoList = getItemByObjectTypeAndDictType(objectType, objectType + dictRel);
        if (CollUtil.isEmpty(itemVoList)) {
            return Collections.emptyList();
        }
        return  itemVoList.stream().map(DictItemVo::getCode).collect(Collectors.toList());
    }


    /***
     * 根据左右对象类型获取关系Class
     *
     * @param leftObjectType 左对象类型
     * @param rightObjectType 右对象类型
     * @author 徐鹏军
     * @date 2022/4/12 11:35
     * @return {@link Class<?>}
     */
    public Class<?> getRelationClass(String leftObjectType, String rightObjectType) {
        String relation = dictUtil.getRelation(leftObjectType, rightObjectType);
        return EntityUtil.getEntityClass(relation);
    }

    /***
     * 根据对象类型，获取其下所有和右对象有关系的中间表
     *
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/12 12:58
     * @return {@link List< String>}
     */
    @Cacheable(cacheNames = CacheConstant.DICT_KEY,key = "#root.methodName+'_'+#objectType+'_child_rel'")
    public List<String> getRightObjectRelations(String objectType) {
        // 获取左对象字典明细
        List<DictItemVo> itemVoList = getItemByObjectTypeAndDictType(objectType, objectType + "_child_rel");
        if (CollUtil.isEmpty(itemVoList)) {
            return Collections.emptyList();
        }
        return itemVoList.stream().map(DictItemVo::getCode).collect(Collectors.toList());
    }

    /***
     * 根据对象类型，获取其下所有和右对象有关系的中间表
     *
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/12 12:58
     * @return {@link List<Class<?>>}
     */
//    public List<Class<?>> getRightObjectRelationClasses(String objectType) {
//        List<String> relations = dictUtil.getRightObjectRelations(objectType);
//        if (CollUtil.isEmpty(relations)) {
//            return Collections.emptyList();
//        }
//        return relations.stream().map(EntityUtil::getEntityClass).collect(Collectors.toList());
//    }

    /***
     * 根据对象类型，获取其上所有和我本身节点有关系的中间表
     *
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/12 12:58
     * @return {@link List<String>}
     */
//    @Cacheable(cacheNames = CacheConstant.DICT_KEY,key = "#root.methodName+'_'+#objectType+'_parent_rel'")
//    public List<String> getLeftObjectRelations(String objectType) {
//        // 获取左对象字典明细
//        List<DictItemVo> itemVoList = getItemByObjectTypeAndDictType(objectType, objectType + "_parent_rel");
//        if (CollUtil.isEmpty(itemVoList)) {
//            return Collections.emptyList();
//        }
//        return itemVoList.stream().map(DictItemVo::getCode).collect(Collectors.toList());
//    }

//    /**
//     * 获取伪文件夹
//     * @param objectType
//     * @return
//     */
//    @Cacheable(cacheNames = CacheConstant.DICT_KEY,key = "#root.methodName+'_'+#objectType+'_pseudo_rel'")
//    public List<DictItemVo> getPseudoRel(String objectType){
//        // 获取左对象字典明细
//        List<DictItemVo> itemVoList = getItemByObjectTypeAndDictType(objectType, objectType + "_pseudo_rel");
//        if (CollUtil.isEmpty(itemVoList)) {
//            return Collections.emptyList();
//        }
//        return itemVoList;
//    }


    /***
     * 根据对象类型，获取其上所有和我本身节点有关系的中间表
     *
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/12 12:58
     * @return {@link List<Class<?>>}
     */
//    public List<Class<?>> getLeftObjectRelationClasses(String objectType) {
//        List<String> relations = dictUtil.getLeftObjectRelations(objectType);
//        if (CollUtil.isEmpty(relations)) {
//            return Collections.emptyList();
//        }
//        return relations.stream().map(EntityUtil::getEntityClass).collect(Collectors.toList());
//    }

    /***
     * 根据对象类型，获取该对象类型所有需要校验的关系表
     *
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/13 18:01
     * @return {@link List< String>}
     */
    public List<String> getCheckAllRLs(String objectType) {
        List<DictItemVo> voList = getItemByObjectTypeAndDictType(objectType, objectType + "_delete_check_rel");
        if (CollUtil.isEmpty(voList)) {
            return Collections.emptyList();
        }
        return voList.stream().map(DictItemVo::getCode).collect(Collectors.toList());
    }

    /***
     * 根据对象类型，获取该对象类型所有需要校验的关系表
     *
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/13 18:01
     * @return {@link List<Class<?>>}
     */
    public List<Class<?>> getCheckAllRLClasses(String objectType) {
        List<String> voList = getCheckAllRLs(objectType);
        if (CollUtil.isEmpty(voList)) {
            return Collections.emptyList();
        }
        return voList.stream().map(EntityUtil::getEntityClass).collect(Collectors.toList());
    }

    /***
     * 根据对象类型，获取该对象类型的上下相关联的所有关系表
     *
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/13 18:01
     * @return {@link List< String>}
     */
    public List<String> getUpDownAllRLs(String objectType) {
        List<DictItemVo> voList = getItemByObjectTypeAndDictType(objectType, objectType + "_sync_delete_rel");
        if (CollUtil.isEmpty(voList)) {
            return Collections.emptyList();
        }
        return voList.stream().map(DictItemVo::getCode).collect(Collectors.toList());
    }

    /***
     * 根据对象类型，获取该对象类型的上下相关联的所有关系表
     *
     * @param objectType 对象类型
     * @author 徐鹏军
     * @date 2022/4/13 18:01
     * @return {@link List<Class<?>>}
     */
    public List<Class<?>> getUpDownRLAllClasses(String objectType) {
        List<String> voList = getUpDownAllRLs(objectType);
        if (CollUtil.isEmpty(voList)) {
            return Collections.emptyList();
        }
        return voList.stream().map(EntityUtil::getEntityClass).collect(Collectors.toList());
    }

    /***
     * 根据对像类型和字典类型，获取其下的字典明细
     *
     * @param objectType 对像类型
     * @param dictType 字典类型
     * @author 徐鹏军
     * @date 2022/4/12 12:29
     * @return {@link List<DictItemVo>}
     */
    private List<DictItemVo> getItemByObjectTypeAndDictType(String objectType, String dictType) {
        if (StrUtil.isBlank(objectType) || StrUtil.isBlank(dictType)) {
            return Collections.emptyList();
        }
        List<DictItemVo> voList = dictUtil.getCodeValueList(dictType);
        if (CollUtil.isNotEmpty(voList)) {
            return voList;
        }
        Class<?> clazz = EntityUtil.getEntityClass(objectType);
        String simpleName = clazz.getSuperclass().getSimpleName();
        // 如果都找到了Object了，则直接放弃
        if (Object.class.getSimpleName().equals(simpleName)) {
            return Collections.emptyList();
        }
        // 将Entity字符串替换并递归调用本方法
        String newObjectType = simpleName.replaceAll(Constant.ENTITY, StrUtil.EMPTY);
        // 将字典类型改为当前对象的字典类型
        String newDictType = newObjectType + StrUtil.UNDERLINE + StrUtil.subAfter(dictType, StrUtil.UNDERLINE, false);
        // 递归查找父类的字典类型
        return getItemByObjectTypeAndDictType(newObjectType, newDictType);
    }

    /**
     * 模糊查询匹配
     * @param objectType
     * @param dictType
     * @param dictTypeLike
     * @return
     */
    private List<DictItemVo> getItemByObjectTypeAndDictType(String objectType, String dictType,String dictTypeLike) {
        if (StrUtil.isBlank(objectType) || StrUtil.isBlank(dictType)) {
            return Collections.emptyList();
        }
        Map<String, List<DictItemVo>> mapList = dictUtil.dictListLike(dictTypeLike, "left");
        if (CollUtil.isNotEmpty(mapList) && mapList.containsKey(dictType)) {
            return mapList.get(dictType);
        }
        Class<?> clazz = EntityUtil.getEntityClass(objectType);
        String simpleName = clazz.getSuperclass().getSimpleName();
        // 如果都找到了Object了，则直接放弃
        if (Object.class.getSimpleName().equals(simpleName)) {
            return Collections.emptyList();
        }
        // 将Entity字符串替换并递归调用本方法
        String newObjectType = simpleName.replaceAll(Constant.ENTITY, StrUtil.EMPTY);
        // 将字典类型改为当前对象的字典类型
        String newDictType = newObjectType + StrUtil.UNDERLINE + StrUtil.subAfter(dictType, StrUtil.UNDERLINE, false);
        // 递归查找父类的字典类型
        return getItemByObjectTypeAndDictType(newObjectType, newDictType,dictTypeLike);
    }

    /**
     * 模糊查询
     * @param dictTypeLike
     * @param likeType
     * @return
     */
    @Cacheable(cacheNames = CacheConstant.DICT_KEY,key = "#root.methodName+'_'+#dictTypeLike+'_'+#likeType")
    public Map<String, List<DictItemVo>> dictListLike(String dictTypeLike,String likeType){
        Response<Map<String, List<DictItemVo>>> response = remoteLezaoDictService.like(new DictTypeListTo(dictTypeLike, likeType));
        if (Objects.isNull(response)) {
            throw new ServiceException(ErrorCode.REMOTE_FAIL, "远程调用字典服务异常");
        }
        if (Boolean.TRUE.equals(response.isFail())) {
            log.error("远程调用字典服务异常,{}", response);
            throw new ServiceException(ErrorCode.REMOTE_FAIL, response.getMsg());
        }
        return response.getData();
    }

    public String getNameByDictCode(String dictType, String objectType) {
        Map<String, String> codeValueMap = getCodeValueMap(dictType);
        if (MapUtil.isEmpty(codeValueMap)) {
            return StrUtil.EMPTY;
        }
        return codeValueMap.getOrDefault(objectType, StrUtil.EMPTY);
    }

    public  Map<String, String> getNameByDictCode(String dictType) {
        Map<String, String> codeValueMap = getCodeValueMap(dictType);
        if (MapUtil.isEmpty(codeValueMap)) {
            return null;
        }
        return codeValueMap;
    }


    public String getNameByDictValue(String dictType, String objectType) {
        Map<String, String> codeValueMap = getCodeValueMap(dictType);
        if (MapUtil.isEmpty(codeValueMap)) {
            return StrUtil.EMPTY;
        }
        Map<String, String> valueCodeMap = getCodeValueMap(dictType);
        final Iterator<Map.Entry<String, String>> iterator = codeValueMap.entrySet().iterator();
        while (iterator.hasNext()){
            final Map.Entry<String, String> next = iterator.next();
            valueCodeMap.put(next.getValue(),next.getKey());
        }

        return valueCodeMap.getOrDefault(objectType, StrUtil.EMPTY);
    }


    /***
     * 根据对像类型获取显示名称字典值
     *
     * @param objectType 对像类型
     * @author 徐鹏军
     * @date 2022/4/12 12:29
     * @return {@link String}
     */
    @TimeLog
    @Cacheable(cacheNames = CacheConstant.DICT_KEY,key = "#root.methodName+'_'+#objectType+'_displayName'")
    public String getDisplayNameDict(String objectType) {
        long current = DateUtil.current();
        List<DictItemVo> voList = getItemByObjectTypeAndDictType(objectType, objectType + "_displayName","displayName");
        if (CollUtil.isEmpty(voList)) {
            return StrUtil.EMPTY;
        }
        return CollUtil.getFirst(voList).getValue();
    }

    /***
     * 转换字典
     *
     * @param t 转换对象
     * @author 徐鹏军
     * @date 2022/4/12 14:04
     */
    public void invertTranslate(Object t) {
        if (t == null) {
            return;
        }
        invertTranslate(Arrays.asList(t));
    }

    /***
     * 转换字典
     *
     * @param list 转换对象
     * @author 徐鹏军
     * @date 2022/4/12 14:04
     */
    public void invertTranslate(List<?> list) {
//        if (list == null || list.isEmpty()) {
//            return;
//        }
//        _translate(list, true);
    }

    /***
     * 转换字典
     *
     * @param t 转换对象
     * @author 徐鹏军
     * @date 2022/4/12 14:04
     */
//    @TimeLog
    public void translate(Object t) {
//        if (Objects.isNull(t)) {
//            return;
//        }
//        if (t instanceof List<?>) {
//            _translate((List<?>) t, false);
//        } else {
//            _translate(Arrays.asList(t), false);
//        }
    }

    private void _translate(List<?> list, boolean invert) {
        Map<Field, CommonDictUtil> cache = buildCache(list);
        if (cache == null) {
            return;
        }
        Set<Map.Entry<Field, CommonDictUtil>> needTranslateFields = cache.entrySet();
        if (needTranslateFields.isEmpty()) {
            return;
        }
        for (Object target : list) {
            for (Map.Entry<Field, CommonDictUtil> e : needTranslateFields) {
                Field f = e.getKey();
                CommonDictUtil c = e.getValue();
                try {
                    if (invert) {
                        c.invertTranslate(target, f);
                    } else {
                        c.translate(target, f);
                    }
                } catch (Exception e1) {
                }
            }
        }
    }

    private Map<Field, CommonDictUtil> buildCache(List<?> list) {
        Object first = null;
        for (int i = 0; i < list.size(); i++) {
            first = list.get(i);
            if (first != null) {
                break;
            }
        }
        if (first == null) {
            return null;
        }
        Map<Field, CommonDictUtil> cache = new HashMap<>();
        Class<?> clz = first.getClass();
        while (clz != Object.class) {
            buildCache(cache, clz);
            clz = clz.getSuperclass();
        }

        return cache;
    }

    private void buildCache(Map<Field, CommonDictUtil> cache, Class<?> clz) {
        Field[] fields = clz.getDeclaredFields();
        if (fields.length == 0) {
            return;
        }
        Arrays.stream(fields).forEach(field -> {
            if (!field.isAnnotationPresent(Dictionary.class)) {
                return;
            }
            field.setAccessible(true);
            Dictionary dictAnno = field.getAnnotation(Dictionary.class);
            CommonDictUtil commonDictUtil = new CommonDictUtil();
            commonDictUtil.setDictAnno(dictAnno);
            commonDictUtil.init(clz, field);
            cache.put(field, commonDictUtil);
        });
    }

    /**
     * 获取工作台的Tab页签配置
     * @param objectType
     * @return
     */
    @Cacheable(cacheNames = CacheConstant.DICT_KEY,key = "#root.methodName+'_'+#objectType+'_Tab'")
    public List<DictItemVo> getTabConfigByObjectType(String objectType) {
        // 获取左对象字典明细
        List<DictItemVo> itemVoList = getItemByObjectTypeAndDictType(objectType, objectType + "_Tab");
        if (CollUtil.isEmpty(itemVoList)) {
            throw new ServiceException(ErrorCode.E_10, "未匹配到配置的页签");
        }
        return  itemVoList;
    }

    /**
     * 比对属性
     * @param src  更改前
     * @param target 更改后
     * @param dictType
     * @param <T>
     * @return
     */
    public <T extends Object> List<Triple<String, String, String>> compareField(T src, T target,String dictType) {
        List<Triple<String, String, String>> compareList = new ArrayList<>();
        List<DictItemVo> codeValueList = this.getCodeValueList(dictType);
        if(CollUtil.isEmpty(codeValueList)){
            return compareList;
        }
        Map srcMap = JSONUtil.toBean(JSONUtil.toJsonStr(src), Map.class);
        Map targetMap = JSONUtil.toBean(JSONUtil.toJsonStr(target), Map.class);

        //更改后为空，不做不处理
        if(ObjectUtil.isNull(target)){
            return compareList;
        }
        //更改前为空
        if(ObjectUtil.isNull(src)){
            //处理更改后数据
            codeValueList.forEach(dict->{
                if(targetMap.containsKey(dict.getCode())){
                    Object value = targetMap.get(dict.getCode());
                    compareList.add(Triple.of(dict.getValue(),"",ObjectUtil.isNull(value)?"":value.toString()));
                }
            });
            return compareList;
        }
        //更改前、更改后都不为空
        codeValueList.forEach(dict->{
            //更改前数据
            Object srcValue = null;
            if(srcMap.containsKey(dict.getCode())){
                srcValue = srcMap.get(dict.getCode());

            }
            //更改后数据
            Object targetValue = null;
            if(targetMap.containsKey(dict.getCode())){
                targetValue = targetMap.get(dict.getCode());
            }
            //更改前后均为空
            if(ObjectUtil.isNull(srcValue) && ObjectUtil.isNull(targetValue)){
                return;
            }
            //更改前后均不为空
            if(ObjectUtil.isNotNull(srcValue) && ObjectUtil.isNotNull(targetValue)){
                if(srcValue.toString().equals(targetValue.toString())){
                    return;
                }
            }
            //比对数据
            compareList.add(Triple.of(dict.getValue(),ObjectUtil.isNull(srcValue)?"":srcValue.toString(),
                    ObjectUtil.isNull(targetValue)?"":targetValue.toString()));
        });
        return compareList;
    }


}
