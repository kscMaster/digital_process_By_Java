package com.nancal.auth.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.nancal.common.base.Response;
import com.nancal.common.config.TokenContext;
import com.nancal.common.config.TokenInfo;
import com.nancal.common.constants.CacheConstant;
import com.nancal.common.constants.Constant;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.redis.service.RedisService;
import com.nancal.remote.service.RemoteLezaoPfmanageService;
import com.nancal.remote.vo.CurrentUserInfoVo;
import com.nancal.remote.vo.RoleResourceVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Nancal.com Inc.
 * Copyright (c) 2021- All Rights Reserved.
 *
 * @Author yangtz
 * @Date 2021/11/30 15:06
 * @Description
 */
@Component
@Slf4j
public class UserUtils {

    @Resource
    private RedisService redisService;
    @Resource
    private RemoteLezaoPfmanageService remoteLezaoPfmanageService;

    /***
     * 获取用户ID
     *
     * @author 徐鹏军
     * @date 2022/3/29 21:47
     * @return {@link String}
     */
    public String getCurrentUserId() {
        return getCurrentUser().getId();
    }

    /***
     * 获取用户登录名称
     *
     * @author 徐鹏军
     * @date 2022/3/29 21:47
     * @return {@link String}
     */
    public String getCurrentLoginName() {
        return getCurrentUser().getLoginName();
    }

    /***
     * 获取用户名称
     *
     * @author 徐鹏军
     * @date 2022/3/29 21:47
     * @return {@link String}
     */
    public String getCurrentUserName() {
        return getCurrentUser().getName();
    }

    /***
     * 获取租户ID
     *
     * @author 徐鹏军
     * @date 2022/3/29 21:47
     * @return {@link String}
     */
    public String getTenantId() {
        return getCurrentUser().getTenant().getId();
    }

    /***
     * 获取租户信息
     *
     * @author 徐鹏军
     * @date 2022/3/29 21:47
     * @return {@link String}
     */
    public CurrentUserInfoVo.Tenant getTenant() {
        return getCurrentUser().getTenant();
    }

    /***
     * 获取token
     *
     * @author 徐鹏军
     * @date 2022/3/29 21:47
     * @return {@link String}
     */
    public String getToken() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if(ObjectUtils.isEmpty(requestAttributes)||
                ObjectUtils.isEmpty(requestAttributes.getAttribute(Constant.TOKEN, RequestAttributes.SCOPE_REQUEST))){
            return StrUtil.EMPTY;
        }

        return  requestAttributes.getAttribute(Constant.TOKEN, RequestAttributes.SCOPE_REQUEST).toString();
    }

    /***
     * 获取当前登陆人基本信息
     *
     * @author 徐鹏军
     * @date 2022/3/29 21:49
     * @return {@link CurrentUserInfoVo}
     */
    public CurrentUserInfoVo getCurrentUser() {
        TokenInfo token = TokenContext.getToken();
        String accessToken = null;
        if (null != token){
            accessToken =  token.getToken();
        }else {
            accessToken = getToken();
        }
        if (StrUtil.isNotBlank(accessToken)) {
            Object cacheObject = redisService.getCacheObject(CacheConstant.LOGIN_TOKEN_KEY + accessToken);
            return JSONUtil.toBean(JSONUtil.parseObj(cacheObject), CurrentUserInfoVo.class);
        }
        return new CurrentUserInfoVo();

    }

    /***
     * 查询用户权限资源信息
     * @param appCode 应用code
     * @author 徐鹏军
     * @date 2022/3/29 21:50
     * @return {@link List<  RoleResourceVo >}
     */
    public List<RoleResourceVo> getUserResource(String appCode) {
        if (StrUtil.isEmpty(appCode)) {
            return Collections.emptyList();
        }
        String redisKey = getCurrentUserId() + "_resources:" + appCode;
        Object cacheObject = redisService.getCacheObject(redisKey);
        // 优先从redis中获取数据，如果获取到，则直接返回
        if (Objects.nonNull(cacheObject)) {
            return JSONUtil.toList(JSONUtil.parseArray(cacheObject), RoleResourceVo.class);
        }
        //Redis中取不到资源信息，调用iam接口查询，然后放到Redis
        Response<List<RoleResourceVo>> res = remoteLezaoPfmanageService.getUserRes(appCode,"Ui","Latest");
        if (Objects.isNull(res) || res.isFail()) {
            throw new ServiceException(ErrorCode.RESOURCE_GET_FAIL);
        }
        //资源集合
        if (CollUtil.isNotEmpty(res.getData())) {
            //保存当前用户应用资源到 redis
            redisService.setCacheObject(redisKey, res.getData(), Constant.TOKEN_EXPIRE, TimeUnit.SECONDS);
        }
        return res.getData();
    }

}
