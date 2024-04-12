package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@ApiModel(value = "Gte4WorklineBOMReq 线体BOM行请求")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gte4WorklineBOMReq<T extends WorkspaceObjectReq> extends BusinessObjectReq {
    @Valid
    @ApiModelProperty("bom行对象属性")
    private BOMNodeReq bomNodeReq;

    @Valid
    @ApiModelProperty("线体版本对象属性")
    private Gte4WorklineRevisionReq worklineRevisionReq;

}
