package com.nancal.home.config;

import com.nancal.common.config.WebOfficeConfig;
import com.nancal.gpaas.sdk.weboffice.client.DefaultEditorClient;
import com.nancal.gpaas.sdk.weboffice.client.EditorConfig;
import com.nancal.gpaas.sdk.weboffice.constants.WebOfficeEditorConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebOfficeClientConfig {

    @Autowired
    private WebOfficeConfig webOfficeConfig;
    /**
     * 获取客户端实例
     */
    @Bean
    public DefaultEditorClient defaultEditorClient() {
        EditorConfig config = new EditorConfig();
        config.setAppId(webOfficeConfig.getAppId());
        config.setApiUrl(webOfficeConfig.getAppUrl());
        config.setPrivateKey(webOfficeConfig.getAppKey());
        config.setSignType(WebOfficeEditorConstants.SignType.RSA2);
        DefaultEditorClient editorClient = new DefaultEditorClient(config);
        return editorClient;
    }

}
