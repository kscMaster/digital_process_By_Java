package com.nancal.common.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/***
 * 系统应用code和名称配置类
 *
 * @author 徐鹏军
 * @date 2022/3/29 21:36
 */
@RefreshScope
@Component
@Data
public class AppConfig {

    @ApiModelProperty("应用名称")
    @Value("${app.name:}")
    private String appName;

    @ApiModelProperty("应用编码")
    @Value("${app.code:}")
    private String appCode;

    @ApiModelProperty("应用地址")
    @Value("${app.baseUrl:}")
    private String baseUrl;

    @ApiModelProperty("前端地址")
    @Value("${app.feBaseUrl:}")
    private String feBaseUrl;
}
