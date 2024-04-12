package com.nancal.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.constants.Constant;
import com.nancal.remote.config.FeignConfig;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class ServiceFeignConfig extends FeignConfig {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        //远程调用，统一在请求头上带上令牌标识
        HttpServletRequest request = attributes.getRequest();
        String tenantId = request.getHeader(Constant.TENANT_ID);
        if (!ObjectUtil.isEmpty(userUtils.getCurrentUser()) && StrUtil.isEmpty(tenantId)) {
            if (!ObjectUtil.isEmpty(userUtils.getCurrentUser().getId()) && !ObjectUtil.isEmpty(userUtils.getCurrentUser().getTenant())) {
                tenantId = userUtils.getTenantId();
            }
        }
        requestTemplate.header(Constant.TENANT_ID, tenantId);
        String token = request.getHeader(Constant.AUTHORIZATION);
        if (StrUtil.isBlank(request.getHeader(Constant.TOKEN))) {
            requestTemplate.header(Constant.TOKEN, token.substring(7));
        } else {
            requestTemplate.header(Constant.TOKEN, request.getHeader(Constant.TOKEN));
        }
        requestTemplate.header(Constant.AUTHORIZATION, request.getHeader(Constant.AUTHORIZATION));
    }

}
