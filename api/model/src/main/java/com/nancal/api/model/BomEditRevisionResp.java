package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@ApiModel(value = "版本对象与BOM关系 的响应")
@Data
@NoArgsConstructor
public class BomEditRevisionResp<T extends WorkspaceObjectResp> extends WorkspaceObjectResp {

    @ApiModelProperty("bom行对象属性")
    private BOMNodeResp bomNodeResp;

    private T objectResp;

    @Override
    public String getObjectType() {
        return "BOMNode";
    }
}
