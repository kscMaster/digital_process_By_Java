package com.nancal.common.annotation;

import com.querydsl.core.types.Order;

import java.lang.annotation.*;

@Target(value={ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OrderField {

    /**
     * 排序优先级，值越小越优先
     */
    int priority() default 0;

    /**
     * 升序还是降序
     */
    Order value() default Order.ASC;


}
