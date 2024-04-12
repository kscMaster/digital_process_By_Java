package com.nancal.service.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.config.TokenContext;
import com.nancal.common.config.TokenInfo;
import com.nancal.common.constants.Constant;
import com.nancal.remote.config.FeignConfig;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class ServiceFeignConfig extends FeignConfig {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        TokenInfo tokenInfo = TokenContext.getToken();
        if (null != tokenInfo){
            requestTemplate.header(Constant.TENANT_ID, tokenInfo.getTenantId());
            requestTemplate.header(Constant.TOKEN, tokenInfo.getToken());
           return;
        }
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
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if (ObjectUtils.isEmpty(requestAttributes) ||
                ObjectUtils.isEmpty(requestAttributes.getAttribute(Constant.TOKEN, RequestAttributes.SCOPE_REQUEST))) {
            //token为空不处理
        } else {
            String token = requestAttributes.getAttribute(Constant.TOKEN, RequestAttributes.SCOPE_REQUEST).toString();
            requestTemplate.header(Constant.TOKEN, token);
        }
        requestTemplate.header(Constant.AUTHORIZATION, request.getHeader(Constant.AUTHORIZATION));

    }

}
