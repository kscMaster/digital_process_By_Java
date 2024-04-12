package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@ApiModel(value = "工艺零件版本对象与BOM关系 的请求")
@Data
@NoArgsConstructor
public class Gte4ProcessBomReq<T extends WorkspaceObjectReq> extends BusinessObjectReq {

    @Valid
    @ApiModelProperty("bom行对象属性")
    private BOMNodeReq bomNodeReq;

    @Valid
    @ApiModelProperty("工艺零件版本对象属性")
    private Gte4ProcessRevisionReq processRevisionReq;

}
