package com.nancal.common.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public class BeanUtil {

    /***
     * 获取对象中所有属性值为null的属性名称
     *
     * @param source 对象
     * @author 徐鹏军
     * @date 2022/3/31 17:40
     * @return {@link String[]}
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /***
     * 对象属性copy，忽略源对象属性值为null的属性
     *
     * @param src 源对象
     * @param target 目标对象
     * @author 徐鹏军
     * @date 2022/3/31 17:40
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        cn.hutool.core.bean.BeanUtil.copyProperties(src, target, getNullPropertyNames(src));
    }

    /**
     * 前缀拷贝，目前不支持驼峰
     * @param source 数据源
     * @param target 目标类
     * @param sourcePrefix 数据源前缀
     * @param targetPrefix 目标前缀
     * @return
     */
    public static Object copyPropertiesIgnoreNull(Object source,Object target,
                                                  String sourcePrefix,String targetPrefix){
        if (Objects.isNull(source)||Objects.isNull(target)) {
            return target;
        }
        if(Strings.isBlank(sourcePrefix) && Strings.isBlank(targetPrefix)){
            BeanUtil.copyPropertiesIgnoreNull(source,target);
            return target;
        }
        final BeanWrapper sourceBeanWrapper = new BeanWrapperImpl(source);
        final BeanWrapper targetBeanWrapper = new BeanWrapperImpl(target);
        java.beans.PropertyDescriptor[] sourcePropertys =sourceBeanWrapper.getPropertyDescriptors();
        for (java.beans.PropertyDescriptor sourcePd : sourcePropertys) {
            String sourceName = Strings.isBlank(sourcePrefix)?sourcePd.getName():sourcePd.getName().replace(sourcePrefix, StrUtil.EMPTY);
            String targetName = Strings.isBlank(targetPrefix)?sourceName:targetPrefix+sourceName;
            try{
                targetBeanWrapper.getPropertyValue(targetName);
                targetBeanWrapper.setPropertyValue(targetName, sourceBeanWrapper.getPropertyValue(sourcePd.getName()));
            }catch (Exception e){
            }
        }
        return target;
    }

    public static boolean matchAll(Object object,Predicate predicate){
        return  Arrays.stream(ReflectUtil.getFields(object.getClass())).allMatch(v ->
                {
                    try {
                        ReflectUtil.setAccessible(v);
                        return predicate.test(v.get(object));
                    }catch (Exception e){
                        return false;
                    }
                }
        );
    }

}
