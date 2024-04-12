package com.nancal.remote.config;

import cn.hutool.core.util.StrUtil;
import com.nancal.common.config.TokenContext;
import com.nancal.common.config.TokenInfo;
import com.nancal.common.constants.Constant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        TokenInfo tokenInfo = TokenContext.getToken();
        if (null != tokenInfo){
            requestTemplate.header(Constant.TENANT_ID, tokenInfo.getTenantId());
            requestTemplate.header(Constant.TOKEN, tokenInfo.getToken());
            return;
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //远程调用，统一在请求头上带上令牌标识
        HttpServletRequest request = attributes.getRequest();
        requestTemplate.header(Constant.TENANT_ID, request.getHeader(Constant.TENANT_ID));
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if(ObjectUtils.isEmpty(requestAttributes)||
                ObjectUtils.isEmpty(requestAttributes.getAttribute(Constant.TOKEN, RequestAttributes.SCOPE_REQUEST))){
            //token为空不处理
        }else{
            String token = requestAttributes.getAttribute(Constant.TOKEN,RequestAttributes.SCOPE_REQUEST).toString();
            requestTemplate.header(Constant.TOKEN, token);
        }
        if(StrUtil.isNotEmpty(request.getHeader(Constant.AUTHORIZATION))){
            requestTemplate.header(Constant.TOKEN, request.getHeader(Constant.AUTHORIZATION).substring(7));
        }
        requestTemplate.header(Constant.AUTHORIZATION, request.getHeader(Constant.AUTHORIZATION));
    }

}
