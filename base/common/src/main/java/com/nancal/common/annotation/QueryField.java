package com.nancal.common.annotation;

import com.querydsl.core.types.Ops;

import java.lang.annotation.*;

@Target(value={ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueryField {

    /**
     * 需要查询的字段名称使用何种方式比较
     */
    Ops value() default Ops.EQ;

    /**
     * true 模糊查询时支持通配符，false 模糊查询时不支持通配符
     */
    boolean wildcard() default false;


}
