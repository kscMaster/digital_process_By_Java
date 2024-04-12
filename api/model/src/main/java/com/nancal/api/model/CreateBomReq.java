package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@ApiModel(value = "创建bom行 的请求")
@Data
@NoArgsConstructor
public class CreateBomReq<T extends WorkspaceObjectReq> extends BusinessObjectReq {

    @Valid
    @ApiModelProperty("bom行对象属性")
    private BOMNodeReq bomNodeReq;

    @Valid
    @ApiModelProperty("创建bom行对象属性")
    private T req;

}
