package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "PBom校验权限的响应")
public class CheckPomResp {

    @ApiModelProperty(value = "设计数量编辑权限")
    private Boolean quantityCheckPower;

    @ApiModelProperty(value = "bom节点数量编辑权限")
    private Boolean bomNodeCountCheckPower;

    @ApiModelProperty(value = "主材编辑权限")
    private Boolean priMaterialCheckPower;

    @ApiModelProperty(value = "辅材编辑权限")
    private Boolean auxMaterialCheckPower;

    @ApiModelProperty(value = "标准件编辑权限")
    private Boolean standardCheckPower;

    @ApiModelProperty(value = "工艺路线编辑权限")
    private Boolean routeCheckPower;

    @ApiModelProperty(value = "版本编辑权限")
    private Boolean revisionCheckPower;

    @ApiModelProperty("true有编辑图代号的权限，false没有权限")
    private boolean hasDrawingCode;

}
