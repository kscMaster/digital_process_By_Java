package com.nancal.common.annotation;

import java.lang.annotation.*;

@Target(value={ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TimeLog {

    /**
     * 操作名称
     * @return
     */
    String handleName() default "";


}
