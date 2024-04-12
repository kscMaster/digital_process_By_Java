package com.nancal.auth.aop;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.nancal.auth.annotation.PreAuthorize;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.config.AppConfig;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.remote.vo.RoleResourceVo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Nancal.com Inc.
 * Copyright (c) 2021- All Rights Reserved.
 *
 * @Author yangtz
 * @Date 2021/12/11 10:20
 * @Description 权限拦截aop
 */
@Aspect
@Component
public class PreAuthorizeAspect {

    @Autowired
    private UserUtils userUtils;
    @Autowired
    private AppConfig appConfig;

    /**
     * 所有权限标识
     */
    private static final String ALL_PERMISSION = "*:*:*";

    @Pointcut("@annotation(com.nancal.auth.annotation.PreAuthorize)")
    public void cut() {
    }

    @Around("cut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        PreAuthorize annotation = method.getAnnotation(PreAuthorize.class);
        if (annotation == null) {
            return point.proceed();
        }
        if (StrUtil.isNotBlank(annotation.hasPermit())) {
            if (hasPermit(annotation.hasPermit())) {
                return point.proceed();
            }
            throw new ServiceException(ErrorCode.FORBIDDEN);
        } else if (StrUtil.isNotBlank(annotation.hasRole())) {
            if (hasRole(annotation.hasRole())) {
                return point.proceed();
            }
            throw new ServiceException(ErrorCode.FORBIDDEN);
        } else if (ObjectUtil.isNotEmpty(annotation.hasAnyPermit())) {
            if (hasAnyPermit(annotation.hasAnyPermit())) {
                return point.proceed();
            }
            throw new ServiceException(ErrorCode.FORBIDDEN);
        } else if (ObjectUtil.isNotEmpty(annotation.hasAnyRole())) {
            if (hasAnyRole(annotation.hasAnyRole())) {
                return point.proceed();
            }
            throw new ServiceException(ErrorCode.FORBIDDEN);
        }
        return point.proceed();
    }

    /**
     * 判断当前用户是否拥有该角色指
     *
     * @param hasRole 角色
     * @return 是否包含该角色信息
     */
    private boolean hasRole(String hasRole) {
        // 获取当前登录用户的角色信息
        return false;
    }

    private boolean hasAnyRole(String[] hasAnyRole) {
        // 获取当前登录用户的任意任意一个角色信息
        return false;
    }

    /**
     * 当前登录用户是否有该权限信息
     *
     * @param permission 权限信息
     * @return true/false
     */
    private boolean hasPermit(String permission) {
        List<RoleResourceVo> userResources = userUtils.getUserResource(appConfig.getAppCode());
        if (CollUtil.isNotEmpty(userResources)) {
            List<String> resources = userResources.stream().map(RoleResourceVo::getCode).collect(Collectors.toList());
            return hasPermissions(resources, permission);
        }
        return false;
    }

    /**
     * 当前登录用户是否有任何一种权限信息和当前权限信息匹配
     *
     * @param permissions 权限信息集合
     * @return true/false
     */
    private boolean hasAnyPermit(String[] permissions) {
        // 获取当前登录用户的任意权限信息
        List<RoleResourceVo> userResources = userUtils.getUserResource(appConfig.getAppCode());
        if (CollectionUtil.isEmpty(userResources)) {
            return false;
        }
        List<String> resources = userResources.stream().map(RoleResourceVo::getCode).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(resources)) {
            return false;
        }
        return Stream.of(permissions).anyMatch(permission -> hasPermissions(resources, permission));
    }

    /**
     * 判断是否包含权限
     *
     * @param authorities 权限资源列表
     * @param permission  权限资源字符串
     * @return 用户是否具备某权限资源
     */
    private boolean hasPermissions(Collection<String> authorities, String permission) {
        return authorities.stream().filter(StringUtils::hasText)
                .anyMatch(x -> ALL_PERMISSION.contains(x) || PatternMatchUtils.simpleMatch(x, permission));
    }
}