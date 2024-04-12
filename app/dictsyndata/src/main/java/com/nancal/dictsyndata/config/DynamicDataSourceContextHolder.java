package com.nancal.dictsyndata.config;

/**
 * @author hewei
 * @date 2022/7/14 9:40
 * @Description
 */
public class DynamicDataSourceContextHolder {
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();


    public static void setDataSourceType(String dataSourceType){
        CONTEXT_HOLDER.set(dataSourceType);
    }


    public static String getDataSourceType(){
        return CONTEXT_HOLDER.get();
    }

    public static void clearDataSourceType(){
        CONTEXT_HOLDER.remove();
    }
}
