package com.nancal.api.utils.excel.analysis;

import com.nancal.api.model.*;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.FieldAlias;
import com.nancal.framework.common.annotations.JsonDict;
import com.nancal.remote.vo.MoreDictEntryVo;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.ConvertUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author hewei
 * @date 2022/9/27 15:51
 * @Description
 */
public class CustomFieldPoiWordAnalysis<T> extends ExcelPoiWordAnalysis<T>{

    private Map<String, MoreDictEntryVo> map;

    private DictUtil dictUtil;

    private Map<String, AuxiliaryMaterialRevisionImportReq> selectMap;

    private LibraryAttributeAuxiliaryReq libraryAttributeAuxiliaryReq;

    private LibraryAttributeEquipmentReq libraryAttributeEquipmentReq;

    private LibraryAttributeMeasureReq libraryAttributeMeasureReq;

    private LibraryAttributeToolReq libraryAttributeToolReq;

    @Override
    @SneakyThrows
    protected T getClassObject(Class<?> aClass, Map<String, Object> fieldMap, int currentIndex) {
        List<ExtraPropertyDataReq> arrayList = new ArrayList<>();
        Object o = aClass.getDeclaredConstructor().newInstance();
        if (o instanceof LibraryAttributeAuxiliaryReq){
            libraryAttributeAuxiliaryReq = (LibraryAttributeAuxiliaryReq)o;
            Class<AuxiliaryMaterialRevisionReq> reqClass = null;
            if (null != libraryAttributeAuxiliaryReq.getAuxiliaryMaterialRevisionReq()){
                reqClass = (Class<AuxiliaryMaterialRevisionReq>) libraryAttributeAuxiliaryReq.getAuxiliaryMaterialRevisionReq().getClass();
            }else {
                reqClass = AuxiliaryMaterialRevisionReq.class;
            }
            if (!verify(reqClass,fieldMap,currentIndex)){
                return null;
            }
            AuxiliaryMaterialRevisionReq materialRevisionReq = reqClass.getDeclaredConstructor().newInstance();
            Iterator<Map.Entry<String, Object>> iterator = fieldMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                Field field = getFieldAlias(reqClass, next.getKey());
                if (null != field) {
                    FieldAlias fieldAlias = field.getAnnotation(FieldAlias.class);
                    Object value = fieldMap.get(fieldAlias.name());
                    final JsonDict jsonDict = field.getAnnotation(JsonDict.class);
                    if (null != jsonDict && null != value){
                        value = paramDictionary(jsonDict,value);
                    }
                    Object convert = ConvertUtils.convert(value, field.getType());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), reqClass);

                    propertyDescriptor.getWriteMethod().invoke(materialRevisionReq, convert);
                    libraryAttributeAuxiliaryReq.setAuxiliaryMaterialRevisionReq(materialRevisionReq);
                }else {
                    if (map.containsKey(next.getKey()) && null != next.getValue()){
                        ExtraPropertyDataReq extraPropertyDataReq = new ExtraPropertyDataReq();
                        MoreDictEntryVo moreDictEntryVo = map.get(next.getKey());
                        extraPropertyDataReq.setProperty(moreDictEntryVo.getCode());
                        if (next.getKey().contains("*") && selectMap.containsKey(next.getKey()) && null != next.getValue()){
                            AuxiliaryMaterialRevisionImportReq reqList = selectMap.get(next.getKey());
                            String nameByDictCode =  dictUtil.getNameByDictValue(reqList.getDictType(), next.getValue().toString());
                            extraPropertyDataReq.setValue(nameByDictCode);
                        }else {
                            extraPropertyDataReq.setValue( next.getValue().toString());
                        }
                        arrayList.add(extraPropertyDataReq);
                        libraryAttributeAuxiliaryReq.setExtraPropertyDataReq(arrayList);
                    }
                }
            }
            return (T)libraryAttributeAuxiliaryReq;
        }
        else if (o instanceof LibraryAttributeEquipmentReq){
            libraryAttributeEquipmentReq = (LibraryAttributeEquipmentReq)o;
            Class<EquipmentRevisionReq> reqClass = null;
            if (null != libraryAttributeEquipmentReq.getEquipmentRevisionReq()){
                reqClass = (Class<EquipmentRevisionReq>) libraryAttributeEquipmentReq.getEquipmentRevisionReq().getClass();
            }else {
                reqClass = EquipmentRevisionReq.class;
            }
            if (!verify(reqClass,fieldMap,currentIndex)){
                return null;
            }
            EquipmentRevisionReq materialRevisionReq = reqClass.getDeclaredConstructor().newInstance();
            Iterator<Map.Entry<String, Object>> iterator = fieldMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                Field field = getFieldAlias(reqClass, next.getKey());
                if (null != field) {
                    FieldAlias fieldAlias = field.getAnnotation(FieldAlias.class);
                    Object value = fieldMap.get(fieldAlias.name());
                    final JsonDict jsonDict = field.getAnnotation(JsonDict.class);
                    if (null != jsonDict && null != value){
                        value = paramDictionary(jsonDict,value);
                    }
                    Object convert = ConvertUtils.convert(value, field.getType());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), reqClass);

                    propertyDescriptor.getWriteMethod().invoke(materialRevisionReq, convert);
                    libraryAttributeEquipmentReq.setEquipmentRevisionReq(materialRevisionReq);
                }else {
                    if (map.containsKey(next.getKey()) && null != next.getValue()){
                        ExtraPropertyDataReq extraPropertyDataReq = new ExtraPropertyDataReq();
                        MoreDictEntryVo moreDictEntryVo = map.get(next.getKey());
                        extraPropertyDataReq.setProperty(moreDictEntryVo.getCode());
                        if (next.getKey().contains("*") && selectMap.containsKey(next.getKey()) && null != next.getValue()){
                            AuxiliaryMaterialRevisionImportReq reqList = selectMap.get(next.getKey());
                            String nameByDictCode =  dictUtil.getNameByDictValue(reqList.getDictType(), next.getValue().toString());
                            extraPropertyDataReq.setValue(nameByDictCode);
                        }else {
                            extraPropertyDataReq.setValue( next.getValue().toString());
                        }
                        arrayList.add(extraPropertyDataReq);
                        libraryAttributeEquipmentReq.setExtraPropertyDataReq(arrayList);
                    }
                }
            }
                return (T)libraryAttributeEquipmentReq;
        }
        else if (o instanceof LibraryAttributeMeasureReq){
            libraryAttributeMeasureReq = (LibraryAttributeMeasureReq)o;
            Class<MeasureRevisionReq> reqClass = null;
            if (null != libraryAttributeMeasureReq.getMeasureRevisionReq()){
                reqClass = (Class<MeasureRevisionReq>) libraryAttributeMeasureReq.getMeasureRevisionReq().getClass();
            }else {
                reqClass = MeasureRevisionReq.class;
            }
            if (!verify(reqClass,fieldMap,currentIndex)){
                return null;
            }
            MeasureRevisionReq materialRevisionReq = reqClass.getDeclaredConstructor().newInstance();
            Iterator<Map.Entry<String, Object>> iterator = fieldMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                Field field = getFieldAlias(reqClass, next.getKey());
                if (null != field) {
                    FieldAlias fieldAlias = field.getAnnotation(FieldAlias.class);
                    Object value = fieldMap.get(fieldAlias.name());
                    final JsonDict jsonDict = field.getAnnotation(JsonDict.class);
                    if (null != jsonDict && null != value){
                        value = paramDictionary(jsonDict,value);
                    }
                    Object convert = ConvertUtils.convert(value, field.getType());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), reqClass);

                    propertyDescriptor.getWriteMethod().invoke(materialRevisionReq, convert);
                    libraryAttributeMeasureReq.setMeasureRevisionReq(materialRevisionReq);
                }else {
                    if (map.containsKey(next.getKey()) && null != next.getValue()){
                        ExtraPropertyDataReq extraPropertyDataReq = new ExtraPropertyDataReq();
                        MoreDictEntryVo moreDictEntryVo = map.get(next.getKey());
                        extraPropertyDataReq.setProperty(moreDictEntryVo.getCode());
                        if (next.getKey().contains("*") && selectMap.containsKey(next.getKey()) && null != next.getValue()){
                            AuxiliaryMaterialRevisionImportReq reqList = selectMap.get(next.getKey());
                            String nameByDictCode =  dictUtil.getNameByDictValue(reqList.getDictType(), next.getValue().toString());
                            extraPropertyDataReq.setValue(nameByDictCode);
                        }else {
                            extraPropertyDataReq.setValue( next.getValue().toString());
                        }

                        arrayList.add(extraPropertyDataReq);
                        libraryAttributeMeasureReq.setExtraPropertyDataReq(arrayList);
                    }
                }
            }
            return (T)libraryAttributeMeasureReq;
        }
        else {
            libraryAttributeToolReq = (LibraryAttributeToolReq)o;
            Class<ToolRevisionReq> reqClass = null;
            if (null != libraryAttributeToolReq.getToolRevisionReq()){
                reqClass = (Class<ToolRevisionReq>) libraryAttributeToolReq.getToolRevisionReq().getClass();
            }else {
                reqClass = ToolRevisionReq.class;
            }
            if (!verify(reqClass,fieldMap,currentIndex)){
                return null;
            }
            ToolRevisionReq materialRevisionReq = reqClass.getDeclaredConstructor().newInstance();
            Iterator<Map.Entry<String, Object>> iterator = fieldMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                Field field = getFieldAlias(reqClass, next.getKey());
                if (null != field) {
                    FieldAlias fieldAlias = field.getAnnotation(FieldAlias.class);
                    Object value = fieldMap.get(fieldAlias.name());
                    final JsonDict jsonDict = field.getAnnotation(JsonDict.class);
                    if (null != jsonDict && null != value){
                        value = paramDictionary(jsonDict,value);
                    }
                    Object convert = ConvertUtils.convert(value, field.getType());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), reqClass);

                    propertyDescriptor.getWriteMethod().invoke(materialRevisionReq, convert);
                    libraryAttributeToolReq.setToolRevisionReq(materialRevisionReq);
                }else {
                    if (map.containsKey(next.getKey()) && null != next.getValue()){
                        ExtraPropertyDataReq extraPropertyDataReq = new ExtraPropertyDataReq();
                        MoreDictEntryVo moreDictEntryVo = map.get(next.getKey());
                        extraPropertyDataReq.setProperty(moreDictEntryVo.getCode());
                        if (next.getKey().contains("*") && selectMap.containsKey(next.getKey()) && null != next.getValue()){
                            AuxiliaryMaterialRevisionImportReq reqList = selectMap.get(next.getKey());
                            String nameByDictCode =  dictUtil.getNameByDictValue(reqList.getDictType(), next.getValue().toString());
                            extraPropertyDataReq.setValue(nameByDictCode);
                        }else {
                            extraPropertyDataReq.setValue( next.getValue().toString());
                        }
                        arrayList.add(extraPropertyDataReq);
                        libraryAttributeToolReq.setExtraPropertyDataReq(arrayList);
                    }
                }
            }
            return (T)libraryAttributeToolReq;
        }
    }

    @Override
    public DictUtil getDictUtil() {
        return dictUtil;
    }

    @Override
    public void setDictUtil(DictUtil dictUtil) {
        this.dictUtil = dictUtil;
    }

    public Map<String, AuxiliaryMaterialRevisionImportReq> getSelectMap() {
        return selectMap;
    }

    public void setSelectMap(Map<String, AuxiliaryMaterialRevisionImportReq> selectMap) {
        this.selectMap = selectMap;
    }

    public Map<String, MoreDictEntryVo> getMap() {
        return map;
    }

    public void setMap(Map<String, MoreDictEntryVo> map) {
        this.map = map;
    }
}
