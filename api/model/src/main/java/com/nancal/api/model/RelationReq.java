package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


@ApiModel(value = "Relation 关系 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RelationReq extends WorkspaceObjectReq implements Serializable{

    @ApiModelProperty(value="关系类型",required=false)
    @Length(max = 64)
    private String relationType;

    @ApiModelProperty(value="左对象ID",required=false)
    @Length(max = 64)
    private String leftObject;

    @ApiModelProperty(value="左对象类型",required=false)
    @Length(max = 64)
    private String leftObjectType;

    @ApiModelProperty(value="右对象ID",required=false)
    @Length(max = 64)
    private String rightObject;

    @ApiModelProperty(value="右对象类型",required=false)
    @Length(max = 64)
    private String rightObjectType;
}