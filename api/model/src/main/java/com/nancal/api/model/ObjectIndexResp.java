package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "ObjectIndex 索引对象 的响应")
@Data

@NoArgsConstructor
@AllArgsConstructor
public class ObjectIndexResp extends WorkspaceObjectResp implements Serializable{
    @ApiModelProperty(value="对象UID",required=false)
    private String objectUid;
    @ApiModelProperty(value="编号",required=false)
    private String itemId;
}