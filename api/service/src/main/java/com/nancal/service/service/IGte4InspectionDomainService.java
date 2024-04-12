package com.nancal.service.service;


import com.google.common.base.Strings;
import com.nancal.api.model.*;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.ExportExcelUtil;
import com.nancal.common.base.Response;
import com.nancal.common.base.TableRequest;
import com.nancal.framework.common.annotations.JsonDict;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public interface IGte4InspectionDomainService extends IItemDomainService{



    Response<Gte4ImportRevisionResp> importExcelVerify(MultipartFile file);

    Response<Gte4ImportRevisionResp> importExcel(MultipartFile file,String leftObject) throws ZipException, UnsupportedEncodingException;

    void treeExport(TableRequest<ItemRevisionReq> req, HttpServletResponse response);

    void likeExport(TableRequest<Gte4InspectionRevisionReq> req, HttpServletResponse response);

    default void export(List<ColumnReq> libraryList , List<? extends WorkspaceObjectResp> dataList, DictUtil dictUtil, String fileName, HttpServletResponse response){
        List<String> tableHost = new ArrayList<>();
        tableHost.add("序号");
        libraryList.forEach(library ->{
            tableHost.add(library.getValue());
        });
        List<Map<String,Object>> mapList = new ArrayList<>();

        dataList.forEach(data ->{
            Map<String,Object> map = new LinkedHashMap();
            for (ColumnReq library : libraryList) {
                try {
                    String code = library.getCode();
                    Object value = null;
                    if (code.equals("files")){
                        map.put(code, value);
                        continue;
                    }
                    if (code.contains("Desc")){
                        // 截取 数据库存储没有Desc
                        code = code.substring(0,code.indexOf("Desc"));
                    }
                    final Field lifeCycleState = getDeclaredField(Gte4InspectionRevisionResp.class, code);
                    Objects.requireNonNull(lifeCycleState).setAccessible(true);
                    final JsonDict annotation = lifeCycleState.getAnnotation(JsonDict.class);
                    if (null != annotation){
                        value =lifeCycleState.get(data);
                        String nameByDictCode = dictUtil.getNameByDictCode(annotation.value(),value.toString());
                        value = null ==  nameByDictCode ? "" : nameByDictCode;
                    }else {
                        value = lifeCycleState.get(data);
                        if (value instanceof LocalDateTime) {
                            final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            value = ((LocalDateTime) value).format(dateTimeFormatter);
                        }
                    }
                    map.put(code, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            mapList.add(map);
        });
        ExportExcelUtil.ExportExcels(response,mapList,tableHost,fileName);
    }

    /**
     * 获取属性
     * @param clazz
     * @param fieldName
     * @return
     */
     static Field getDeclaredField(Class clazz, String fieldName) {
        if (Strings.isNullOrEmpty(fieldName)) {
            return null;
        }
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {

            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

}
