package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ApiModel(value = "Item 零组件 的响应")
@ToString(callSuper = true)
public class CheckResp {

    @ApiModelProperty(value = "设计数量编辑权限")
    private Boolean quantityCheckPower;

    @ApiModelProperty(value = "主材编辑权限")
    private Boolean priMaterialCheckPower;

    @ApiModelProperty(value = "辅材编辑权限")
    private Boolean auxMaterialCheckPower;

    @ApiModelProperty(value = "代号和型号编辑权限")
    private Boolean itemIdAndModelNoCheckPower;


}