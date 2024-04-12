package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Gte4PartToolRevision 设计工装版本 的响应")
@Data

@NoArgsConstructor
@AllArgsConstructor
public class Gte4PartToolRevisionResp extends ToolRevisionResp  implements Serializable{
    @ApiModelProperty(value="阶段标识",required=false)
    private String gte4Phase;
    @ApiModelProperty(value="设计者",required=false)
    private String gte4Designer;
}