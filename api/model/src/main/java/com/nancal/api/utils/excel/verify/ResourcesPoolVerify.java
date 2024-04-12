package com.nancal.api.utils.excel.verify;

import com.nancal.api.model.AuxiliaryMaterialRevisionImportReq;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.FieldAlias;
import com.nancal.api.utils.excel.Check;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hewei
 * @date 2022/9/7 13:48
 * @Description
 */
public class ResourcesPoolVerify implements Check {

    private Map<String,Object> statisticsMap;

    private Map<String,AuxiliaryMaterialRevisionImportReq> map;

    private List<Map<String,Object>> param = new ArrayList<>();

    private String number = "序号";

    private String okNum= "okNum";

    private String failNum= "failNum";

    private String errorMsg= "errorMsg";

    private int index;

    private DictUtil dictUtil;

    private String filePath;

    private Boolean isExistFile = false;


    @Override
    @SneakyThrows
    public boolean verify(Class<?> aClass, Map<String, Object> fieldMap, int currentIndex) {
        Object o = aClass.getDeclaredConstructor().newInstance();
        final Iterator<Map.Entry<String, Object>> iterator = fieldMap.entrySet().iterator();
        boolean record = true;
        Map<String,Object> map = new HashMap<>();
        this.index = currentIndex;
        if (null != fieldMap.get(number)){
            this.index = (int)fieldMap.get(number);
        }
        while (iterator.hasNext()) {
            final Map.Entry<String, Object> next = iterator.next();
            String name = next.getKey();
            try {
                Field field = getFieldAlias(aClass, next.getKey());
                if (null != field || this.map.containsKey(next.getKey())) {
                    String keyName = "";
                    FieldAlias fieldAlias = null;
                    if (null != field){
                         fieldAlias = field.getAnnotation(FieldAlias.class);
                        keyName = fieldAlias.name();
                    }else if (this.map.containsKey(next.getKey())){
                        keyName = next.getKey();
                    }

                    Object value = fieldMap.get(keyName);
                    if (isExistFile){
                        record = isFilePostfix(keyName, value, record, index);
                    }
                    record = isRequired(fieldAlias,keyName,value,index,record);
                    record = paramSize(fieldAlias,keyName, value,  index,record);
                    record = isNumber( fieldAlias,keyName, record, index, value);
                    record = isCheck( fieldAlias,keyName, record, index, value);
                    record = isSelect( keyName, record, index, value);
                    // 判断字段类型是否正确
                    if (null != field) {
                        Object convert = ConvertUtils.convert(value, field.getType());
                        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), aClass);
                        propertyDescriptor.getWriteMethod().invoke(o, convert);
                    }
                }
            } catch (ConversionException | ClassCastException | IllegalArgumentException exception) {
                record = paramType(index,name);
            }
        }
        param.add(map);
        okAndFailNum( record );
        return record;
    }

    /**
     * 数字判断
     */
    Pattern pattern = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    public Boolean isNumber(FieldAlias fieldAlias,String name,Boolean record,int index,Object value){
        if (null != fieldAlias && fieldAlias.name().contains("#")){
            if (null != value ) {
                Matcher isNum = pattern.matcher(value.toString());
                if (!isNum.matches()) {
                    return addErrorMsg(index,"格式错误:" + fieldAlias.name() + " 必需是数字 ");
                }
            }
        }else if (name.contains("#")){
            if (null != value ) {
                Matcher isNum = pattern.matcher(value.toString());
                if (!isNum.matches()) {
                    return addErrorMsg(index,"格式错误:" + name + " 必需是数字 ");
                }
            }
        }
        return record;
    }

    /**
     * 下拉判断
     * @param name
     * @param record
     * @param index
     * @param value
     * @return
     */
    public Boolean isSelect(String name,Boolean record,int index,Object value){
        if (StringUtils.isNotBlank(name) && name.contains("*") && map.containsKey(name) && null != value){
            AuxiliaryMaterialRevisionImportReq reqList = map.get(name);
            String nameByDictCode =  dictUtil.getNameByDictValue(reqList.getDictType(), value.toString());
            if (!StringUtils.isNotBlank(nameByDictCode)){
                record = addErrorMsg(index,"数据字典:"+name+" 参数错误 ");
            }
            return record;
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
    public Boolean isCheck(FieldAlias fieldAlias,String name,Boolean record,int index,Object value){
        if (null != fieldAlias && fieldAlias.name().contains("&")){
            String check = value.toString().toLowerCase();
            if (null != value && !check.equals("true") && !check.equals("false")) {
                return addErrorMsg(index,"参数错误:" + fieldAlias.name() + " 必需是true或者false ");
            }
        }else if (name.contains("&")){
            String check = value.toString().toLowerCase();
            if (null != value && !check.equals("true") && !check.equals("false")) {
                return addErrorMsg(index,"参数错误:" + name + " 必需是true或者false ");
            }
        }
        return record;
    }

    /**
     *  添加行异常信息
     * @param name 描述
     * @return
     */
    public Boolean addErrorMsg(int index,String name){
        Object msg = statisticsMap.get(errorMsg);
        Object currentIndex = statisticsMap.get("index");
        if ("".equals(msg)){
            statisticsMap.put("index",index);
            name = "第" + index + "行"+name;
        }else if (currentIndex != null && this.index != (int)currentIndex){
            name = msg+"\n"+"第" + index + "行"+name;
            statisticsMap.put("index",index);
        }else {
            name = msg+"/"+name;
        }
        statisticsMap.put(errorMsg,name);
        return false;
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

    public Boolean getFile(Object value,int index,Boolean record) {
        File gte4Attaches = new File(filePath+ File.separator + value);
        if (!gte4Attaches.exists()){
            return addErrorMsg( index,"附件错误:"+value+" 附件不存在 ");
        }
        return record;
    }

    /**
     *  后缀规则校验
     * @param name
     * @param value 后缀
     * @param record 当前行是否错误
     * @param index 当前行
     * @return
     */
    public Boolean isFilePostfix(String name,Object value,Boolean record,int index){
        if (name.equals("附件") && null != value){
            final int postfixIndex = value.toString().lastIndexOf(".");
            final String postfix = value.toString().substring(postfixIndex);
            final Map<String, String> dataSetType = dictUtil.getNameByDictCode("DataSetType");
            final Iterator<Map.Entry<String, String>> iterator = dataSetType.entrySet().iterator();
            boolean boo = false;
            while (iterator.hasNext()){
                if ( iterator.next().getKey().indexOf(postfix) != -1 ){
                    boo = true;
                }
            }
            if (boo){
                record  = getFile(value,index,record);
            }
            if (!boo) {
                record  = addErrorMsg(index," 规则错误:附件后缀规则错误 ");
            }
        }
        return record;
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
     * 是否必填校验
     * @param fieldAlias 注解
     * @param index 当前行
     * @param record 当前行是否错误
     * @param value 值
     * @return
     */
    public Boolean isRequired(FieldAlias fieldAlias,String name,Object value, int index,Boolean record){
        if (null != fieldAlias && fieldAlias.required() && null == value){
            record = addErrorMsg(index,"必填项为空:"+fieldAlias.name()+" 未填写 ");
        }
        if (StringUtils.isNotBlank(name) && map.containsKey(name)){
            AuxiliaryMaterialRevisionImportReq importReq = map.get(name);
            if (importReq.getRequired() && null == value){
                record = addErrorMsg(index,"必填项为空:"+name+" 未填写 ");
            }
        }
        return record;
    }

    /**
     * 参数长度校验
     * @param index 当前行
     * @param value 值
     * @param record 当前行是否错误
     * @param fieldAlias 校验注解
     * @return
     */
    public Boolean paramSize(FieldAlias fieldAlias,String name,Object value, int index,Boolean record){
        if (null != fieldAlias && null != value && 0 != fieldAlias.max() && fieldAlias.max() < value.toString().length()){
            record = addErrorMsg( index,"参数长度错误:超过最大限制 "+fieldAlias.name()+" 参数长度超过最大限制,最大长度 "+fieldAlias.max());
        }
        if (StringUtils.isNotBlank(name) && map.containsKey(name)){
            AuxiliaryMaterialRevisionImportReq importReq = map.get(name);
            if (null != value && null != importReq.getLength() && importReq.getLength() < value.toString().length()){
                record = addErrorMsg( index,"参数长度错误:超过最大限制 "+name+" 参数长度超过最大限制,最大长度 "+importReq.getLength());
            }
        }
        return record;
    }

    /**
     * 参数类型校验
     * @return
     */
    public Boolean paramType(int index,String name){
        return addErrorMsg(index,"类型错误: "+name+" 数据类型类型不匹配 ");
    }


    public Map<String, Object> getStatisticsMap() {
        return statisticsMap;
    }

    public void setStatisticsMap(Map<String, Object> statisticsMap) {
        this.statisticsMap = statisticsMap;
    }

    public List<Map<String, Object>> getParam() {
        return param;
    }

    public void setParam(List<Map<String, Object>> param) {
        this.param = param;
    }

    public DictUtil getDictUtil() {
        return dictUtil;
    }

    public void setDictUtil(DictUtil dictUtil) {
        this.dictUtil = dictUtil;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Boolean getExistFile() {
        return isExistFile;
    }

    public void setExistFile(Boolean existFile) {
        isExistFile = existFile;
    }

    public Map<String, AuxiliaryMaterialRevisionImportReq> getMap() {
        return map;
    }

    public void setMap(Map<String, AuxiliaryMaterialRevisionImportReq> map) {
        this.map = map;
    }
}
