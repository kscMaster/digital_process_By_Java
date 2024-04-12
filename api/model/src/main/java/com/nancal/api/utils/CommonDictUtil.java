package com.nancal.api.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.common.annotation.Dictionary;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.remote.vo.DictItemVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Data
public class CommonDictUtil {

    //字典值字段
    private Field valueField;
    //字典项名称字段
    private Field nameField;
    private List<DictItemVo> dicList;
    protected Dictionary dictAnno;

    public void init(Class<?> clz, Field field) {
        String dbDictionaryType = dictAnno.value();
        String valueFieldName = dictAnno.valueField();
        if (valueFieldName.trim().isEmpty()) {
            valueFieldName = dictAnno.dynamic() ? field.getName() : field.getName().replace("Desc", "");
        }
        valueField = ReflectUtil.getField(clz, valueFieldName);
        if (Objects.isNull(valueField)) {
            throw new ServiceException(ErrorCode.ERROR, "CommonDict valueField must not be null!");
        }
        // 字典的ItemValue字段必须和ItemName字段分开来 用Desc
        nameField = ReflectUtil.getField(clz, valueFieldName + "Desc");
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        List<DictItemVo> voList = dictUtil.getCodeValueList(dbDictionaryType);
        if (CollUtil.isEmpty(voList)) {
            log.error("CommonDict dictValues must not be null!");
            return;
        }
        dicList = voList;
    }

    public void invertTranslate(Object target, Field field) {
        if (nameField == null) {
            return;
        }
        String itemName = (String) ReflectUtil.getFieldValue(target, field);
        if (itemName == null) {
            Map<String, String> extraMap = ((BusinessObjectResp) target).getExtraMap();
            itemName = extraMap == null ? null : extraMap.get(nameField.getName());
        }
        if (itemName == null) {
            return;
        }
        for (DictItemVo item : dicList) {
            if (item.getValue().equalsIgnoreCase(itemName)) {
                ReflectUtil.setFieldValue(target,field,item.getCode());
                break;
            }
        }
    }

    public void translate(Object target, Field field) {
        Object dictValue = ReflectUtil.getFieldValue(target, field);
        if (dictValue == null) {
            return;
        }
        Object dictDisp = null;
        for (DictItemVo item : dicList) {
            if (item.getCode().equals(dictValue.toString())) {
                dictDisp = item.getValue();
                break;
            }
        }
        if (dictDisp == null) {
            dictDisp = dictAnno.defaultValue();
        }
        if (getDictAnno().dynamic() && target instanceof BusinessObjectResp) {
            ((BusinessObjectResp) target).putExtraProperty(valueField.getName() + "Desc", dictDisp);
        } else {
            ReflectUtil.setFieldValue(target,field,dictDisp);
        }
    }
}