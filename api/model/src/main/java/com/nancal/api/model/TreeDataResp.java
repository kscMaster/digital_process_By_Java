package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
@ApiModel(value = "前端要的固定值")
@AllArgsConstructor
public class TreeDataResp  {
    @ApiModelProperty(value = "前端要的固定值")
    private String title;
}
