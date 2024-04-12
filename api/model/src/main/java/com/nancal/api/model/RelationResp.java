package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@ApiModel(value = "Relation 关系 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RelationResp extends WorkspaceObjectResp implements Serializable{
    @ApiModelProperty(value="关系类型",required=false)
    private String relationType;
    @ApiModelProperty(value="左对象ID",required=false)
    private String leftObject;
    @ApiModelProperty(value="左对象类型",required=false)
    private String leftObjectType;
    @ApiModelProperty(value="右对象ID",required=false)
    private String rightObject;
    @ApiModelProperty(value="右对象类型",required=false)
    private String rightObjectType;
}