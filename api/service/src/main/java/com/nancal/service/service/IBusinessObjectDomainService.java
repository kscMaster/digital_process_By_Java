package com.nancal.service.service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.common.BusinessMultiPropertyReq;
import com.nancal.api.model.common.BusinessSinglePropertyReq;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.model.entity.BusinessObjectEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public interface IBusinessObjectDomainService {

    /***
     * 获取业务对象单属性
     *
     * @param req 请求参数
     * @author 徐鹏军
     * @date 2022/3/30 13:31
     * @return {@link String}
     */
    default String getProperty(BusinessSinglePropertyReq req) {
        Object fieldValue = ReflectUtil.getFieldValue(EntityUtil.getEntityClass(EntityUtil.getObjectType()), req.getPropertyName());
        return fieldValue.toString();
    }

    /***
     * 获取业务对象多属性
     *
     * @param req 请求参数
     * @author 徐鹏军
     * @date 2022/3/30 13:31
     * @return {@link Map<Object,Object>}
     */
    default Map<Object, Object> getProperties(BusinessMultiPropertyReq req) {
        Class<?> clazz = EntityUtil.getEntityClass(EntityUtil.getObjectType());
        HashMap<Object, Object> dataMap = MapUtil.newHashMap();
        req.getPropertyNames().forEach(name -> {
            Object fieldValue = ReflectUtil.getFieldValue(clazz, name);
            dataMap.put(name, fieldValue);
        });
        return dataMap;
    }

    /***
     * 复制对象
     *
     * @param uid 主键id
     * @author 徐鹏军
     * @date 2022/3/30 16:14
     * @return {@link Object}
     */
    default BusinessObjectResp saveAs(String uid) {
        WorkspaceObjectEntity entity = EntityUtil.getById(EntityUtil.getObjectType(), uid);
        if (Objects.isNull(entity)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        // 获取entity全报名
        String packageName = EntityUtil.getRespPackage(EntityUtil.getObjectType());
        // 创建响应对象
        BusinessObjectResp instance = ReflectUtil.newInstance(packageName);
        BeanUtils.copyProperties(entity, instance);
        return instance;
    }

    /***
     * 获取业务对象的所有属性名称
     *
     * @author 徐鹏军
     * @date 2022/3/30 13:54
     * @return {@link List<String>}
     */
    default List<String> getAllPropertyNames() {
        // 获取entity全报名
        String packageName = EntityUtil.getEntityPackage(EntityUtil.getObjectType());
        //2:反射创建对象
        BusinessObjectEntity baseEntity = ReflectUtil.newInstance(packageName);
        Field[] fields = ReflectUtil.getFields(baseEntity.getClass());
        if (ArrayUtil.isEmpty(fields)) {
            return Collections.emptyList();
        }
        return Arrays.stream(fields).map(Field::getName).collect(Collectors.toList());
    }
}
