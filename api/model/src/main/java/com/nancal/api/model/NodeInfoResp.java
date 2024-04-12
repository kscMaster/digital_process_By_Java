package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author： Wang Hui
 * @date： 2022/5/3 12:43
 * @description： 节点信息
 **/
@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "获取流程节点数据的响应")
public class NodeInfoResp extends WorkspaceObjectResp {

    @ApiModelProperty(value = "流程名称")
    private String processName;

    @ApiModelProperty(value = "节点名称")
    private String name;

    @ApiModelProperty(value = "节点key")
    private String nodeKey;

    @ApiModelProperty(value = "子节点")
    private String child;
}
