package com.nancal.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dictionary {

    /**
     * 字典明细的dic_type, 该dic_type必须在 dic_item表中有记录
     */
    public String value() default "";

    public String defaultValue() default "";

    /**
     * 自动添加Desc后缀字典Label，要求继承<code>RespEntity</code>对象
     */
    public boolean dynamic() default true;

    public String valueField() default "";

}
