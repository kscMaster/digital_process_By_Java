package com.nancal.api.utils.excel.verify;

import com.nancal.api.model.MsgbImportReq;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.FieldAlias;
import com.nancal.api.utils.excel.Check;
import com.nancal.framework.common.annotations.JsonDict;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hewei
 * @date 2022/8/15 14:19
 * @Description
 */
public class BopVerify implements Check {


    private Map<String,Object> statisticsMap;

    private List<Map<String,Object>> param = new ArrayList<>();

    private String number = "层级";

    private String ID= "ID";

    private String edition= "版本";

    private String dataType= "数据类型";

    private String okNum= "okNum";

    private String failNum= "failNum";

    private String errorMsg= "errorMsg";

    private DictUtil dictUtil;

    private Object index;

    private Map<String, MsgbImportReq> objectMap;


    @Override
    @SneakyThrows
    public boolean verify(Class<?> aClass, Map<String, Object> fieldMap,int currentIndex) {
        if (null == aClass){
            return addErrorMsg(currentIndex,"必填项为空: 数据类型 未填写 ");
        }
        Object o = aClass.getDeclaredConstructor().newInstance();
        final Iterator<Map.Entry<String, Object>> iterator = fieldMap.entrySet().iterator();
        boolean record = true;
        Map<String,Object> map = new HashMap<>();
        this.index = currentIndex;
        if (null != fieldMap.get(number)){
            this.index =  fieldMap.get(number);
        }
        while (iterator.hasNext()) {
            final Map.Entry<String, Object> next = iterator.next();
            String name = next.getKey();
            try {
                Field field = getFieldAlias(aClass, next.getKey());
                if (null != field) {

                    FieldAlias fieldAlias = field.getAnnotation(FieldAlias.class);
                    Object value = fieldMap.get(fieldAlias.name());
                    final JsonDict jsonDict = field.getAnnotation(JsonDict.class);
                    if (null != jsonDict && null != value){
                        record = paramDictionary(fieldAlias,jsonDict,value,record);
                    }
                    record = isRequired(fieldAlias,value,index,record);
                    record = paramSize(fieldAlias, value,  index,next.getKey(),record);
                    record = isNumber( fieldAlias, record, index, value);
                    record = isCheck( fieldAlias, record, index, value);
                    IDAndEdition( fieldAlias, value, map);
                    // 判断字段类型是否正确
                    Object convert = ConvertUtils.convert(value, field.getType());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), aClass);
                    propertyDescriptor.getWriteMethod().invoke(o, convert);
                }
            } catch (ConversionException | ClassCastException | IllegalArgumentException exception) {
                record = paramType(index,name);
            }

        }
        record = IDAndEditionRepeat( index, record,  map);
        param.add(map);
        okAndFailNum( record );
        return record;
    }

    protected Field getFieldAlias(Class<?> aClass, String code){
        while (null != aClass){
            final Field[] declaredFields = aClass.getDeclaredFields();
            for (Field field:declaredFields){
                FieldAlias declaredAnnotation = field.getDeclaredAnnotation(FieldAlias.class);
                if (null != declaredAnnotation && code.equals(declaredAnnotation.name())){
                    return field;
                }
            }
            aClass =  aClass.getSuperclass();
        }
        return null;
    }


    /**
     * 数字判断
     */
    Pattern pattern = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    public Boolean isNumber(FieldAlias fieldAlias,Boolean record,Object index,Object value){
        if (fieldAlias.name().contains("#")){
            if (null != value ) {
                Matcher isNum = pattern.matcher(value.toString());
                if (!isNum.matches()) {
                    return addErrorMsg(index,"格式错误:" + fieldAlias.name() + " 必需是数字 ");
                }
            }
        }
        return record;
    }

    /**
     * 单选判断
     * @param fieldAlias
     * @param record
     * @param index
     * @param value
     * @return
     */
    public Boolean isCheck(FieldAlias fieldAlias,Boolean record,Object index,Object value){
        if (fieldAlias.name().contains("&")){
            if (null != value && !value.equals("是") && !value.equals("否")) {
                return addErrorMsg(index,"参数错误:" + fieldAlias.name() + " 必需是是或者否 ");


            }
        }
        return record;
    }

    /**
     *  添加行异常信息
     * @param name 描述
     * @return
     */
    public Boolean addErrorMsg(Object index,String name){
        Object msg = statisticsMap.get(errorMsg);
        Object currentIndex = statisticsMap.get("index");
        if ("".equals(msg)){
            statisticsMap.put("index",index);
            name = "第" + index + "行"+name;
        }else if (currentIndex != null && this.index != currentIndex){
            name = msg+"\n"+"第" + index + "行"+name;
            statisticsMap.put("index",index);
        }else {
            name = msg+"/"+name;
        }
        statisticsMap.put(errorMsg,name);
        return false;
    }


    /**
     * 判断 单行成功还是失败
     * @param record 当前行是否错误
     */
    public void okAndFailNum(Boolean record){
        if (record){
            int num = statisticsMap.get(okNum)==null?0:(int)statisticsMap.get(okNum);
            statisticsMap.put("okNum",num+1);
        }else {
            int num = statisticsMap.get(failNum)==null?0:(int)statisticsMap.get(failNum);
            statisticsMap.put("failNum",num+1);
        }
    }




    /**
     *  Excel里数据 ID和版本 重复
     * @param index 当前行
     * @param record 当前行是否错误
     * @param map 用于代号 id 版本
     * @return
     */
    public Boolean IDAndEditionRepeat(Object index,Boolean record, Map<String,Object> map){
        if (!"".equals(map.get(ID)) && "".equals(map.get(edition))){
            record = addErrorMsg(index,"参数错误: ID存在，未指定版本 ");
        }
        if (!"".equals(map.get(ID)) && !"".equals(map.get(edition))){
            String o = (String)map.get(edition);
            Matcher isNum = pattern.matcher(o);
            if (!isNum.matches()) {
                return addErrorMsg(index,"格式错误:" + "版本" + " 必需是数字 ");
            }
            if (o.length() != 3){
                return addErrorMsg(index,"格式错误:" + "版本" + " 长度为3位数 ");
            }
        }
        if (!"".equals(map.get(dataType))){
            if (!objectMap.containsKey(map.get(dataType)) && "".equals(map.get(ID))){
                record = addErrorMsg(index,"参数错误: 非工艺规划固定类型 ID和版本为必填项 ");
            }
        }
        if (!"".equals(map.get(number))) {
            final boolean idIsExist = param.stream().filter(parameter -> null !=  parameter.get(number)).anyMatch(parameter -> parameter.get(number).equals(map.get(number)));
            if (idIsExist) {
                record = addErrorMsg(index,"参数重复: 层级参数重复 ");
            }
        }
        if (!"".equals(map.get(ID))) {
            final boolean idIsExist = param.stream().filter(parameter -> null !=  parameter.get(edition) && null != parameter.get(ID))
                    .anyMatch(parameter -> parameter.get(edition).equals(map.get(edition)) && parameter.get(ID).equals(map.get(ID)));
            if (idIsExist) {
                record = addErrorMsg(index,"参数重复: ID和版本参数重复 ");
            }
        }

        return record;
    }

    /**
     * 是否必填校验
     * @param fieldAlias 注解
     * @param index 当前行
     * @param record 当前行是否错误
     * @param value 值
     * @return
     */
    public Boolean isRequired(FieldAlias fieldAlias,Object value, Object index,Boolean record){
        if (fieldAlias.required() && null == value){
            record = addErrorMsg(index,"必填项为空:"+fieldAlias.name()+" 未填写 ");
        }
        return record;
    }


    public Boolean paramDictionary(FieldAlias fieldAlias,JsonDict jsonDict,Object value, Boolean record){
        String nameByDictCode =  dictUtil.getNameByDictValue(jsonDict.value(), value.toString());
        if (!StringUtils.isNotBlank(nameByDictCode)){
            record = addErrorMsg(index,"数据字典:"+fieldAlias.name()+" 参数错误 ");
        }
        return record;
    }



    /**
     * 参数长度校验
     * @param index 当前行
     * @param name 列名
     * @param value 值
     * @param record 当前行是否错误
     * @param fieldAlias 校验注解
     * @return
     */
    public Boolean paramSize(FieldAlias fieldAlias,Object value, Object index,String name,Boolean record){
        if (null != value && 0 != fieldAlias.max() && fieldAlias.max() < value.toString().length()){
            record = addErrorMsg( index,"参数长度错误:超过最大限制 "+name+" 参数长度超过最大限制,最大长度 "+fieldAlias.max());
        }
        return record;
    }


    /**
     * 参数和版本规则校验
     * @param value 值
     * @param map 用于代号 id 版本
     * @param fieldAlias 校验注解
     * @return
     */
    public void IDAndEdition(FieldAlias fieldAlias,Object value, Map<String,Object> map){
        if ( fieldAlias.name().equals(ID) || fieldAlias.name().equals(edition) || fieldAlias.name().equals(dataType) || fieldAlias.name().equals(number)){
            if (null == value){
                map.put(fieldAlias.name(), "");
            }else {
                map.put(fieldAlias.name(), value);
            }
        }
    }

    /**
     * 参数类型校验
     * @return
     */
    public Boolean paramType(Object index,String name){
        return addErrorMsg(index,"类型错误: "+name+" 数据类型类型不匹配 ");
    }




    public Map<String, Object> getStatisticsMap() {
        return statisticsMap;
    }

    public void setStatisticsMap(Map<String, Object> statisticsMap) {
        this.statisticsMap = statisticsMap;
    }

    public DictUtil getDictUtil() {
        return dictUtil;
    }

    public void setDictUtil(DictUtil dictUtil) {
        this.dictUtil = dictUtil;
    }

    public Map<String, MsgbImportReq> getObjectMap() {
        return objectMap;
    }

    public void setObjectMap(Map<String, MsgbImportReq> objectMap) {
        this.objectMap = objectMap;
    }
}
