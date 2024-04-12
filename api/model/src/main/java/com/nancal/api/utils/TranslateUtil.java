package com.nancal.api.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.common.annotation.TimeLog;
import com.nancal.common.config.TokenContext;
import com.nancal.common.config.TokenInfo;
import com.nancal.common.schedule.ThreadDictPool;
import com.nancal.framework.common.annotations.JsonDict;
import com.nancal.remote.vo.DictItemVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
public class TranslateUtil {

    public static final String USE_CACHE = "USE_CACHE";

//    @Autowired
//    private DictFrameworkStore dictFrameworkStore;

//    @TimeLog
//    public <T extends BusinessObjectResp> void translate(List<T> respList){
//        Map<T, Map<String, Object>> values = dictFrameworkStore.getValues(respList);
//        respList.stream().forEach(resp->{
//            Map<String, Object> fieldMap = values.get(resp);
//            if(CollUtil.isNotEmpty(fieldMap)){
//                fieldMap.keySet().stream().forEach(fieldName->{
//                    Object value = fieldMap.get(fieldName);
//                    resp.putExtraProperty(fieldName,value);
//                });
//            }
//        });
//    }
//
//    @TimeLog
//    public <T extends BusinessObjectResp> void translate(T resp) {
//        Map<String, Object> fieldMap = dictFrameworkStore.getValues(resp);
//        if(CollUtil.isEmpty(fieldMap)){
//            return;
//        }
//        fieldMap.keySet().stream().forEach(fieldName->{
//            Object value = fieldMap.get(fieldName);
//            resp.putExtraProperty(fieldName,value);
//        });
//    }


    private static final String DESC = "Desc";

    @Autowired
    private DictUtil dictUtil;

    private Map<String,Map<Field,JsonDict>> cacheMap = new HashMap<>();


    public void allField(List<Field> list,Class<?> clz){
        while (clz != Object.class) {
            list.addAll(Arrays.asList(clz.getDeclaredFields()));
            allField(list,clz = clz.getSuperclass());
        }
    }

    @TimeLog
    public <T extends BusinessObjectResp> void translate(List<T> respList){
        if(CollUtil.isEmpty(respList)){
            return;
        }
        respList.forEach(resp->translate(resp));
    }

    @TimeLog
    public <T extends BusinessObjectResp> void translate(T resp){
        if(ObjectUtil.isNull(resp)){
            return;
        }
        String simpleName = resp.getClass().getSimpleName();
        if(cacheMap.containsKey(simpleName)){
            translate(resp,cacheMap.get(simpleName));
            return;
        }
        Class<? extends BusinessObjectResp> clz = resp.getClass();
        List<Field> list = new ArrayList<>();
        allField(list,clz);
        if (CollUtil.isEmpty(list)) {
            return;
        }
        list.stream().forEach(field -> {
            if (!field.isAnnotationPresent(JsonDict.class)) {
                return;
            }
            field.setAccessible(true);
            JsonDict dict = field.getAnnotation(JsonDict.class);
            if(!cacheMap.containsKey(simpleName)){
                cacheMap.put(simpleName,new HashMap<>());
            }
            cacheMap.get(simpleName).put(field,dict);
        });
        translate(resp,cacheMap.get(simpleName));
    }

    public <T extends BusinessObjectResp> String getTranslateValue(T resp,String fieldName){
        Field field = ReflectUtil.getField(resp.getClass(), fieldName);
        if(ObjectUtil.isNull(field)){
            return null;
        }
        if (!field.isAnnotationPresent(JsonDict.class)) {
            return null;
        }
        field.setAccessible(true);
        JsonDict dict = field.getAnnotation(JsonDict.class);
        fieldName = StrUtil.isBlank(dict.fieldName())?fieldName+DESC:dict.fieldName();
        return resp.getExtraProperty(fieldName);
    }

    private <T extends BusinessObjectResp> void translate(T resp,Map<Field,JsonDict> jsonDictMap){
        if(CollUtil.isEmpty(jsonDictMap)){
            return;
        }
        Map<String, List<DictItemVo>> dictMap = dictList(jsonDictMap.values());
        for (Field field : jsonDictMap.keySet()) {
            JsonDict dict = jsonDictMap.get(field);
            List<DictItemVo> voList = dictMap.get(dict.value());
            if(CollUtil.isEmpty(voList)){
                String fieldName = StrUtil.isBlank(dict.fieldName())?field.getName()+DESC:dict.fieldName();
                resp.putExtraProperty(fieldName,dict.defaultValue());
                continue;
            }
            translate(resp,field,voList,dict);
        }
    }

    private <T extends BusinessObjectResp> void translate(T resp, Field field,List<DictItemVo> voList,JsonDict dict) {
        Object dictValue = ReflectUtil.getFieldValue(resp, field);
        if (dictValue == null) {
            return;
        }
        Object dictDisp = null;
        for (DictItemVo item : voList) {
            if (item.getCode().equals(dictValue.toString())) {
                dictDisp = item.getValue();
                break;
            }
        }
        if (dictDisp == null) {
            dictDisp = dict.defaultValue();
        }
        String fieldName = StrUtil.isBlank(dict.fieldName())?field.getName()+DESC:dict.fieldName();
        resp.putExtraProperty(fieldName,dictDisp);
    }

    private Map<String,List<DictItemVo>> dictList(Collection<JsonDict> jsonDictList){
        CountDownLatch countDownLatch = new CountDownLatch(jsonDictList.size());
        Map<String,List<DictItemVo>> map = new HashMap<>();
        TokenInfo token = TokenContext.getToken();
        RequestAttributes requestAttributes = null;
        if (Objects.isNull(token)){
            requestAttributes = RequestContextHolder.currentRequestAttributes();
        }
        final RequestAttributes finalRequestAttributes = requestAttributes;
        try {
            for (JsonDict dict : jsonDictList) {
                ThreadDictPool.exec(() -> {
                    try {
                        if (Objects.isNull(token)){
                            RequestContextHolder.setRequestAttributes(finalRequestAttributes);
                        }else {
                            TokenContext.setToken(token);
                        }
                        List<DictItemVo> codeValueList = dictUtil.getCodeValueList(dict.value());
                        map.put(dict.value(), codeValueList);
                    } catch (Exception e) {
                        log.warn("获取字典信息失败,", e);
                        log.error("获取字典信息失败,dictType={}", dict.value());
                    } finally {
                        countDownLatch.countDown();
                    }
                }, "获取字典信息" + dict.value());
            }
            countDownLatch.await();
        }catch (Exception e){
            log.warn("获取字典信息失败,", e);
            log.error("获取字典信息失败,dictList");
        }
        return map;
    }


}
