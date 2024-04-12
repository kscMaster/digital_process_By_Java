package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


@ApiModel(value = "Gte4PartToolRevision 设计工装版本 的请求")
@Data

@NoArgsConstructor
@AllArgsConstructor
public class Gte4PartToolRevisionReq extends ToolRevisionReq  implements Serializable{

    @ApiModelProperty(value="阶段标识",required=false)
    @Length(max = 64)
    private String gte4Phase;

    @ApiModelProperty(value="设计者",required=false)
    @Length(max = 128)
    private String gte4Designer;
}