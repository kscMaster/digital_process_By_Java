package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ApiModel(value = "工艺规程结构编辑权限 的响应")
@ToString(callSuper = true)
public class MfgCheckResp {

    @ApiModelProperty(value = "对象属性编辑权限")
    private Boolean isObjectCheck;

    @ApiModelProperty(value = "对象bom属性编辑权限")
    private Boolean isBomCheck;

    @ApiModelProperty(value = "权限校验提示语")
    private String checkTip;

}