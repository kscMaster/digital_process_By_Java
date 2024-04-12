package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@ApiModel(value = "Gte4PlantBomReq 工厂BOM行请求")
@Data

@NoArgsConstructor
@AllArgsConstructor
public class Gte4PlantBomReq <T extends WorkspaceObjectReq> extends BusinessObjectReq{
    @ApiModelProperty("bom行对象属性")
    private BOMNodeReq bomNodeReq;

    @Valid
    @ApiModelProperty("工厂版本对象属性")
    private Gte4PlantRevisionReq plantRevisionReq;
}
