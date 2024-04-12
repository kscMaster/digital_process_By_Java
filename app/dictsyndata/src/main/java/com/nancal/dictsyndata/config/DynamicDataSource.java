package com.nancal.dictsyndata.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;




/**
 * @author hewei
 * @date 2022/7/14 9:39
 * @Description
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
       return DynamicDataSourceContextHolder.getDataSourceType();
    }

}
