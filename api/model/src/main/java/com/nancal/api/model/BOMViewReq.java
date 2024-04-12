package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "BOMView BOM视图 的请求")
public class BOMViewReq extends WorkspaceObjectReq {
    @ApiModelProperty(value = "是否精确")
    private Boolean precise;

    @ApiModelProperty(value = "视图类型")
    private String viewType;
}
