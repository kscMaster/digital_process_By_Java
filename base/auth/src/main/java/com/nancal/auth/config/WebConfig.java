package com.nancal.auth.config;

import cn.hutool.core.util.StrUtil;
import com.nancal.auth.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.boot.context.config.ConfigFileApplicationListener.ACTIVE_PROFILES_PROPERTY;

/***
 * 注册拦截器
 *
 * @author 徐鹏军
 * @date 2022/3/29 21:52
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;
    @Autowired
    private Environment environment;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/health", "/dependency-services", "/favicon.ico", "/error",
                        "/weboffice/editor/saveCallback","/quickReleaseCallback");
        String activeEnv = environment.getProperty(ACTIVE_PROFILES_PROPERTY);
        // 只在开发环境暴露swagger接口
        if (StrUtil.isNotBlank(activeEnv) && "dev".equalsIgnoreCase(activeEnv)) {
            interceptorRegistration.excludePathPatterns("/swagger-ui.html", "/webjars/**",
                    "/swagger-resources/**", "/swagger/**", "/csrf", "/"
            );
        }
    }
}
