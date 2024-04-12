package com.nancal.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.huawei.innovation.rdm.bean.entity.BasicObject;
import com.huawei.innovation.rdm.coresdk.basic.dto.BasicObjectViewDTO;
import com.huawei.innovation.rdm.hwkeymodeltest.bean.entity.BusinessObjectEntity;
import com.nancal.api.model.common.BusinessMultiPropertyReq;
import com.nancal.api.model.common.BusinessSinglePropertyReq;
import com.nancal.common.enums.ActionEnum;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.xdm.XdmEntityUtil;
import com.nancal.xdm.builder.XdmRequest;

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
        Object fieldValue = ReflectUtil.getFieldValue(XdmEntityUtil.getEntityClass(XdmEntityUtil.getObjectType()), req.getPropertyName());
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
        Class<?> clazz = XdmEntityUtil.getEntityClass(XdmEntityUtil.getObjectType());
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
    default BasicObjectViewDTO saveAs(String uid) {
        BasicObject basicObject = XdmEntityUtil.getObject(XdmEntityUtil.getObjectType());
        basicObject.setId(Long.parseLong(uid));
        List<BasicObjectViewDTO> views = XdmRequest.postForObject(XdmEntityUtil.getObjectType(), ActionEnum.get, basicObject, BasicObjectViewDTO.class);
        if (CollUtil.isEmpty(views)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        return CollUtil.getFirst(views);
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
        String packageName = XdmEntityUtil.getEntityPackage(XdmEntityUtil.getObjectType());
        //2:反射创建对象
        BusinessObjectEntity baseEntity = ReflectUtil.newInstance(packageName);
        Field[] fields = ReflectUtil.getFields(baseEntity.getClass());
        if (ArrayUtil.isEmpty(fields)) {
            return Collections.emptyList();
        }
        return Arrays.stream(fields).map(Field::getName).collect(Collectors.toList());
    }
}
