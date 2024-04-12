package com.nancal.remote.vo;

import lombok.Data;

@Data
public class IntegrationFileVo {

    private String name;
    private String[] tags;
    private String ext;

    private Long size;
    private Long imgWidth;
    private Long imgHeight;
    private Long videoTIme;
    private String videoLogoUrl;
    private String storageType;
    private String url;
    private String group;
    private String corpId;
    private String errorMsg;
    private String id;

}
