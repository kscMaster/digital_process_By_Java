package com.nancal.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class XdmConfig {

    @Value("${xdm.appId:rdm_hwkeymodeltest_app}")
    private String appId;

    @Value("${xdm.domain:http://a0ec3992938846f9af25b4688b52c2ef.apic.cn-south-4.myhuaweicloudapis.com}")
    private String domain;

    @Value("${xdm.domainName:ruiyuan88}")
    private String domainName;

    @Value("${xdm.userName:xupj}")
    private String userName;

    @Value("${xdm.password:2wsx1qaz}")
    private String password;

    @Value("${xdm.endpoint:https://iam.cn-south-4.myhuaweicloud.com}")
    private String endpoint;

}
