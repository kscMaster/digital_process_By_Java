package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "封面节点 的响应")
public class CoverNodeResp {
    @ApiModelProperty(value = "节点")
    private String nodeName;
    @ApiModelProperty(value = "节点值")
    private String nodeValue;
}
