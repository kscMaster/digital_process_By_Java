package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@ApiModel(value = "WebOffice 在线编辑 的请求")
@Data
@NoArgsConstructor
public class WebOfficeReq {

    @ApiModelProperty(value="对象类型",required=true)
    @NotEmpty(message = "对象类型为必填项")
    private String objectType;

    @ApiModelProperty(value="对象uid",required=true)
    @NotEmpty(message = "对象uid为必填项")
    private String uid;


    @ApiModelProperty(value="请求类型：desktop，mobile，embedded",required=true)
    @NotEmpty(message = "请求类型为必填项")
    private String type;



}
