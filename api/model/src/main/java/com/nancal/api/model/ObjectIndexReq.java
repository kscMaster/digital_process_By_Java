package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


@ApiModel(value = "ObjectIndex 索引对象 的请求")
@Data

@NoArgsConstructor
@AllArgsConstructor
public class ObjectIndexReq extends WorkspaceObjectReq implements Serializable{

    @ApiModelProperty(value="对象UID",required=false)
    @Length(max = 64)
    private String objectUid;

    @ApiModelProperty(value="编号",required=false)
    @Length(max = 64)
    private String itemId;
}