package com.nancal.api.utils.excel.analysis;


import com.nancal.api.utils.FieldAlias;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaError;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author HeWei123
 */
@Slf4j
public class PoiAnalysis{


    /**
     * 获取泛型对象
     *
     * @param clazz
     * @return
     */
    public static Class<?> currentModelClass(Class<?> clazz) {
        // 获取超类
        Type type = clazz.getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) {
            throw new RuntimeException("超类没有参数化类型");
        }
        // 获取超累泛型上设置的类
        Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];
        if (!(trueType instanceof Class)) {
            throw new RuntimeException("没有在超累泛型上设置实际的类");
        }

        return (Class<?>) trueType;
    }


    protected Object getFieldValue(Cell cell){
        switch (cell.getCellType()){
            case ERROR:
                return FormulaError.forInt(cell.getErrorCellValue()).getString();
            case STRING:
               return  getString(cell);
            case BOOLEAN:
               return cell.getBooleanCellValue();
            case FORMULA:
               return cell.getCellFormula();
            case NUMERIC:
               return getNumeric(cell);
            case _NONE:
            case BLANK:
            default:
                return null;
        }
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

    protected Object  getNumeric(Cell cell){
        double numericCellValue = cell.getNumericCellValue();
        if (DateUtil.isCellDateFormatted(cell)){
            Date numericDate = DateUtil.getJavaDate(numericCellValue);
            return numericDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        String value = Double.toString(numericCellValue);
        if (Pattern.compile("^[+-]?\\d+\\.?\\d*[Ee][+-]?\\d+$").matcher(value).find()){
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(numericCellValue);
        }
        if (numericCellValue == ((Double)numericCellValue).intValue()){
            return ((Double)numericCellValue).intValue();
        }
        return numericCellValue;
    }

    protected Object getString(Cell cell){
        try{
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
           return LocalDateTime.parse(cell.getStringCellValue(), df);
        } catch (Exception e){
          return cell.getStringCellValue();
        }
    }


}
