package com.nancal.auth.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.nancal.common.base.Response;
import com.nancal.common.constants.CacheConstant;
import com.nancal.common.constants.Constant;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.redis.service.RedisService;
import com.nancal.remote.service.RemoteLezaoPfmanageService;
import com.nancal.remote.vo.CurrentUserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Nancal.com Inc.
 * Copyright (c) 2021- All Rights Reserved.
 *
 * @Author yangtz
 * @Date 2021/11/30 14:33
 * @Description 拦截用户信息，并保存到redis
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private RedisService redisService;

    @Resource
    @Lazy
    private RemoteLezaoPfmanageService remoteLezaoPfmanageService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token=getToken(request);
        if (StrUtil.isBlank(token)) {
            log.info("url={}",request.getRequestURI());
            throw new ServiceException(ErrorCode.UNAUTHORIZED);
        }
        RequestContextHolder.currentRequestAttributes().setAttribute(Constant.TOKEN,token, RequestAttributes.SCOPE_REQUEST);
        String key = CacheConstant.LOGIN_TOKEN_KEY + token;
        // 判断缓存中是否存在当前用户信息，如果存在，直接放行
        if (redisService.hasKey(key)) {
            return true;
        }
        // 缓存中不存在，则远程获取用户基本信息，存入redis
        Response<CurrentUserInfoVo> res = remoteLezaoPfmanageService.getUserInfo();
        if (Objects.isNull(res) || res.isFail()) {
            log.error(JSONUtil.toJsonStr(res));
            throw new ServiceException(ErrorCode.UNAUTHORIZED,"远程调用IMA获取登陆人基本信息失败");
        }
        CurrentUserInfoVo userInfo = res.getData();
        // 保存当前用户信息到缓存 redis
        redisService.setCacheObject(CacheConstant.LOGIN_TOKEN_KEY + token, userInfo, Constant.TOKEN_EXPIRE, TimeUnit.SECONDS);
        return true;
    }

    private String getToken(HttpServletRequest request){
        String authToken = request.getHeader(Constant.AUTHORIZATION);
        String token = request.getHeader(Constant.TOKEN);
        return StrUtil.isNotBlank(token)?
                token:StrUtil.isNotBlank(authToken)?authToken.substring(7):getTokenParam(request);
    }

    private String getTokenParam(HttpServletRequest request){
        String queryString = request.getQueryString();
        if(StrUtil.isNotBlank(queryString) && queryString.indexOf(Constant.TOKEN)>0){
            for (String param : queryString.split("&")) {
                String[] arr = param.split("=");
                if(arr[0].equals(Constant.TOKEN)){
                    return param.split("=")[1];
                }
            }
        }
        return null;
    }

}
