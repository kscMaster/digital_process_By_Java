package com.nancal.api.utils.excel.analysis;


import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.FieldAlias;
import com.nancal.api.utils.excel.Check;
import com.nancal.api.utils.excel.Word;
import com.nancal.api.utils.excel.exception.RowNullExcel;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.framework.common.annotations.JsonDict;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author HeWei123
 */
@Slf4j
public class ExcelPoiWordAnalysis<T> extends PoiAnalysis implements Word{

    private DictUtil dictUtil;

    private Check check;

    private String number;

    private int rowNum = 2;

    @Override
    public List<T> analysis(MultipartFile file) {
        int index = file.getOriginalFilename().lastIndexOf(".");
        if (file.getOriginalFilename().substring(index).equals(".xls") || file.getOriginalFilename().substring(index).equals(".xlsx")){
            try (InputStream inputStream = file.getInputStream()) {
                // 不区分版本
                Workbook workBook = WorkbookFactory.create(inputStream);
                List<T> tList = taxWorkBook(workBook);
                workBook.close();
                return tList;
            }catch (Exception e){
                if (e instanceof ServiceException ){
                    throw new ServiceException(((ServiceException) e).getCode(),e.getMessage());
                }else {
                    e.printStackTrace();
                    throw new ServiceException(ErrorCode.E_10,"文件内容格式错误，解析失败");
                }
            }
        }else {
            throw new ServiceException(ErrorCode.E_10,"文件格式错误，解析失败");
        }
    }



    /**
     * 解析workBook
     * 获取Row对象获取行级数据
     * 使用poi空值策略对空格进行处理  MissingCellPolicy.RETURN_BLANK_AS_NULL 返回空值或者null
     * 存储第一行表头数据 去除第一个单元格
     * 添加返回的对象进数组中解析成对象
     *
     * @param workBook
     */
    protected List<T> taxWorkBook(Workbook workBook) {
        // 获取第一个sheet
        Sheet sheet = workBook.getSheetAt(0);
        // 总行数
        int lastRowNum = sheet.getLastRowNum();
        List<T> dataList = new ArrayList<>();
        if (lastRowNum == 0){
            throw new ServiceException(ErrorCode.E_12,"导入模板格式不能为空");
        }
        else if (lastRowNum -2 == 0){
            throw new ServiceException(ErrorCode.E_12,"导入模板数据不能为空");
        }
        else if (lastRowNum -2 < 0){
            throw new ServiceException(ErrorCode.E_12,"导入模板格式异常");
        }
        final List<String> tableHeader = getTableHeader(sheet);
        // 用于存储所有数据的集合
        for (int i = rowNum+1; i <= lastRowNum; i++) {
            try {
                final Row row = sheet.getRow(i);
                Map<String, Object> fieldMap = new LinkedHashMap<>();
                for (int k = 0; k < sheet.getRow(2).getLastCellNum(); k++){
                    final Cell cell = row.getCell(k, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    Object fieldValue = getFieldValue(cell);
                    if (k == 0){
                        fieldMap.put(number,fieldValue);
                        continue;
                    }
                    fieldMap.put(tableHeader.get(k), fieldValue);
                }
                T classObject = getClassObject(currentModelClass(getClass()), fieldMap, i + 1);
                if (null != classObject){
                    dataList.add(classObject);

                }
            }catch (NullPointerException e) {
                e.printStackTrace();
                throw new RowNullExcel("Excel has options for blank lines");
            }
        }
        return dataList;
    }



    @SneakyThrows
    public boolean verify(Class<?> aClass, Map<String, Object> fieldMap, int currentIndex) {
        if (null == check){
            return true;
        }
       return check.verify(aClass,fieldMap,currentIndex);
    }


    @SneakyThrows
    protected T getClassObject(Class<?> aClass, Map<String, Object> fieldMap,int currentIndex){
        if (!verify(aClass,fieldMap,currentIndex)){
            return null;
        }
        Object o = aClass.getDeclaredConstructor().newInstance();
        final Iterator<Map.Entry<String, Object>> iterator = fieldMap.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, Object> next = iterator.next();
            Field field = getFieldAlias(aClass, next.getKey());
            if (null != field) {
                FieldAlias fieldAlias = field.getAnnotation(FieldAlias.class);
                Object value = fieldMap.get(fieldAlias.name());
                final JsonDict jsonDict = field.getAnnotation(JsonDict.class);
                if (null != jsonDict && null != value){
                    value = paramDictionary(jsonDict,value);
                }
                Object convert = ConvertUtils.convert(value, field.getType());
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), aClass);
                propertyDescriptor.getWriteMethod().invoke(o, convert);
            }
        }
        return (T)o;
    }

    public Object paramDictionary(JsonDict jsonDict,Object value){
        String nameByDictCode =  dictUtil.getNameByDictValue(jsonDict.value(), value.toString());
        value = (null == nameByDictCode ? "" : nameByDictCode);
        return value;
    }

    public List<String> getTableHeader( Sheet sheet){
        List<String> header = new ArrayList<>();
        Row row = sheet.getRow(rowNum);
        Iterator<Cell> cellIterator = row.cellIterator();
        int i = 0;
        while (cellIterator.hasNext()){
            Cell next = cellIterator.next();
            if (i == 0){
                header.add(number);
                i++;
                continue;
            }
            header.add(next.getStringCellValue());

        }
        return header;
    }





    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public DictUtil getDictUtil() {
        return dictUtil;
    }

    public void setDictUtil(DictUtil dictUtil) {
        this.dictUtil = dictUtil;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Check getCheck() {
        return check;
    }

    public void setCheck(Check check) {
        this.check = check;
    }
}
