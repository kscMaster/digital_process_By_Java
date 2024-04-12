package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "Gte4PartToolRevision 设计工装版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4PartToolRevision extends ToolRevision  implements Serializable{

    @ApiModelProperty(value="阶段标识",required=false)
    private String gte4Phase;

    @ApiModelProperty(value="设计者",required=false)
    private String gte4Designer;

}