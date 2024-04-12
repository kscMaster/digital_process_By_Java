package com.nancal.auth.annotation;

import java.lang.annotation.*;

/**
 * Nancal.com Inc.
 * Copyright (c) 2021- All Rights Reserved.
 *
 * @Author yangtz
 * @Date 2021/12/11 10:09
 * @Description 权限注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuthorize {

    /**
     * 用户有该权限
     *
     * @return 权限
     */
    String hasPermit() default "";

    /**
     * 用户有任意一个权限
     *
     * @return 权限
     */
    String[] hasAnyPermit() default {};

    /**
     * 用户有该角色
     *
     * @return 角色
     */
    String hasRole() default "";

    /**
     * 用户有任意一个角色
     *
     * @return 角色
     */
    String[] hasAnyRole() default {};


}
