package com.nancal.api.utils.excel.verify;

import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.FieldAlias;
import com.nancal.api.utils.excel.Check;
import com.nancal.framework.common.annotations.JsonDict;
import com.nancal.model.entity.Gte4InspectionRevisionEntity;
import com.nancal.model.entity.QGte4InspectionRevisionEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
 * @date 2022/8/15 14:19
 * @Description
 */
public class BaseLibraryVerify implements Check {


    private Map<String,Object> statisticsMap;

    private List<Map<String,Object>> param = new ArrayList<>();

    private String leftObject;

    private String filePath;

    private String fileName;

    private Boolean fileIsExist;

    private String number = "序号";

    private String code = "代号";

    private String ID= "ID";

    private String edition= "版本";

    private String okNum= "okNum";

    private String failNum= "failNum";

    private String errorMsg= "errorMsg";

    private DictUtil dictUtil;

    private int index;

    private JPAQueryFactory jpaQueryFactory;

    @Override
    @SneakyThrows
    public boolean verify(Class<?> aClass, Map<String, Object> fieldMap,int currentIndex) {
        Object o = aClass.getDeclaredConstructor().newInstance();
        final Iterator<Map.Entry<String, Object>> iterator = fieldMap.entrySet().iterator();
        boolean record = true;
        Map<String,Object> map = new HashMap<>();
        this.index = currentIndex;
        if (null != fieldMap.get(number)){
            this.index =  (int)fieldMap.get(number);
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
                    record = isFilePostfix(fieldAlias, value, record, index);
                    record = isRequired(fieldAlias,value,index,record);
                    record = paramSize(fieldAlias, value,  index,next.getKey(),record);
                    IDAndEdition( fieldAlias, value, map);
                    record = isNumber( fieldAlias, record, index, value);
                    record = isCodeIsExist(fieldAlias,value,index,name,map,record);
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
     *  添加行异常信息
     * @param name 描述
     * @return
     */
    public Boolean addErrorMsg(int index,String name){
        Object msg = statisticsMap.get(errorMsg);
        Object currentIndex = statisticsMap.get("index");
        if ("".equals(msg)){
            statisticsMap.put("index",index);
            name = fileName+"第" + index + "行"+name;
        }else if (currentIndex != null && this.index != (int)currentIndex){
            name = msg+"\n"+fileName+"第" + index + "行"+name;
            statisticsMap.put("index",index);
        }else {
            name = msg+"/"+name;
        }
        statisticsMap.put(errorMsg,name);
        return false;
    }

    public Boolean isNumber(FieldAlias fieldAlias,Boolean record,int index,Object value){
        if (fieldAlias.name().equals("设计许可值上限") || fieldAlias.name().equals("设计许可值下限")) {
            if (null != value ) {
                Pattern pattern = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
                Matcher isNum = pattern.matcher(value.toString());
                if (!isNum.matches()) {
                    return addErrorMsg(index,"格式错误:" + fieldAlias.name() + " 必需是数字 ");
                }
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
     * 代号重复校验
     * @param gte4ID 代号
     * @param record 当前行是否错误
     * @param index 当前行
     * @return
     */
    public Boolean isGte4IDIsExist(String gte4ID,Boolean record,int index, Map<String,Object> map){
        QGte4InspectionRevisionEntity qGte4InspectionRevisionEntity = QGte4InspectionRevisionEntity.gte4InspectionRevisionEntity;
        BooleanBuilder where = new BooleanBuilder();
        if (StringUtils.isNotBlank(gte4ID)){
            where.and(qGte4InspectionRevisionEntity.gte4ID.eq(gte4ID));
            where.and(qGte4InspectionRevisionEntity.delFlag.isFalse());
            List<Gte4InspectionRevisionEntity> fetch = jpaQueryFactory.selectFrom(qGte4InspectionRevisionEntity).where(where).fetch();
            if (!fetch.isEmpty() && fetch.get(0).getItemId().equals(map.get(ID)) &&  !"Working".equals(fetch.get(0).getLifeCycleState())){
                record = addErrorMsg(index,"工作状态:数据处于流程中或者是已发布状态 不允许更新 ");
            }
            else if (!fetch.isEmpty() && !fetch.get(0).getItemId().equals(map.get(ID))){
                record = addErrorMsg(index,"参数重复: 代号"+gte4ID+" 数据库已存在 ");
            }
        }
        return record;
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
     * @param fieldAlias
     * @param value 后缀
     * @param record 当前行是否错误
     * @param index 当前行
     * @return
     */
    public Boolean isFilePostfix(FieldAlias fieldAlias,Object value,Boolean record,int index){
        if (fieldAlias.name().equals("附件") && null != value){
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
            if (boo && fileIsExist){
                record  = getFile(value,index,record);
            }
            if (!boo) {
                record  = addErrorMsg(index," 规则错误:附件后缀规则错误 ");
            }
        }
        return record;
    }


    /**
     *  Excel里数据 ID和版本 重复
     * @param index 当前行
     * @param record 当前行是否错误
     * @param map 用于代号 id 版本
     * @return
     */
    public Boolean IDAndEditionRepeat(int index,Boolean record, Map<String,Object> map){
        if (!"".equals(map.get(ID)) && "".equals(map.get(edition))){
            record = addErrorMsg(index,"参数错误: ID存在，未指定版本 ");
        }
        if (!"".equals(map.get(ID)) && !"".equals(map.get(edition))){
            if ( !"001".equals(map.get(edition))){
                record = addErrorMsg(index,"规则错误: "+edition+" 规则错误 版本只能为001");
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
    public Boolean isRequired(FieldAlias fieldAlias,Object value, int index,Boolean record){
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
    public Boolean paramSize(FieldAlias fieldAlias,Object value, int index,String name,Boolean record){
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
        if ( fieldAlias.name().equals(ID) || fieldAlias.name().equals(edition) ){
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
    public Boolean paramType(int index,String name){
        return addErrorMsg(index,"类型错误: "+name+" 数据类型类型不匹配 ");
    }

    /**
     * 代号excel重复校验
     * 代号数据库重复校验
     * @param fieldAlias 校验注解
     * @param value 值
     * @param index 当前行
     * @param name 列名
     * @param map 用于代号 id 版本
     * @param record 当前行是否错误
     * @return
     */
    public Boolean isCodeIsExist(FieldAlias fieldAlias,Object value,int index,String name, Map<String,Object> map ,Boolean record){
        if (null == value ||  !fieldAlias.name().equals(code) ) {
            return record;
        }
        Object finalValue = value;
        boolean codeIsExist = param.stream().filter(parameter -> null != parameter.get(code)).anyMatch(parameter ->parameter.get(code).equals(finalValue));
        if (codeIsExist){
            record = addErrorMsg(index,"参数重复: "+name+" 参数重复 ");
        }
        map.put(fieldAlias.name(),value);
        record = isGte4IDIsExist(value.toString(),record,index,map);

        return record;
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

    public JPAQueryFactory getJpaQueryFactory() {
        return jpaQueryFactory;
    }

    public void setJpaQueryFactory(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public String getLeftObject() {
        return leftObject;
    }

    public void setLeftObject(String leftObject) {
        this.leftObject = leftObject;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Boolean getFileIsExist() {
        return fileIsExist;
    }

    public void setFileIsExist(Boolean fileIsExist) {
        this.fileIsExist = fileIsExist;
    }
}
