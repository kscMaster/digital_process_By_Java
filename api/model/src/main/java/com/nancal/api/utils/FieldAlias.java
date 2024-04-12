package com.nancal.api.utils;

import java.lang.annotation.*;

/**
 * excel导入判断
 * @author hewei
 * @date 2022/4/24 16:08
 * @Description
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldAlias {
    String name() default "";

    boolean required() default false;

    int max() default 0;

}
