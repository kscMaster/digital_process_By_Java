package com.nancal.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class AuxiliaryMaterialRevisionImportReq implements Serializable{

    @ApiModelProperty(value = "自定义字段输入框类型")
    private String type;

    @ApiModelProperty(value="自定义字段是否必填")
    private Boolean required;

    @ApiModelProperty(value = "自定义字段长度")
    private Integer length;

    @ApiModelProperty(value = "自定义字段值类型")
    private String dataType;

    @ApiModelProperty(value = "自定义字段值类型")
    private String dictType;



}