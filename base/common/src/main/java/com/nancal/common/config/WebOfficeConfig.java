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
public class WebOfficeConfig {

    @ApiModelProperty("AppId")
    @Value("${weboffice.appId:}")
    private String appId;

    @ApiModelProperty("App密钥")
    @Value("${weboffice.appKey:}")
    private String appKey;

    @ApiModelProperty("AppUrl")
    @Value("${weboffice.appUrl:}")
    private String appUrl;

    @ApiModelProperty("回调地址")
    @Value("${weboffice.callBackUrl:}")
    private String callBackUrl;

}
