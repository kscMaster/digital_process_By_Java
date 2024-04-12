package com.nancal.dictsyndata.config;


import java.lang.annotation.*;

/**
 * @author hewei
 * @date 2022/7/14 9:43
 * @Description
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DataSourceSwitch {
    DataSourceType value() default DataSourceType.DEV;
}
