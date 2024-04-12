package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author： Wang Hui
 * @date： 2022/5/3 12:07
 * @description： 送审：获取流程实例节点数据
 **/
@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "获取流程节点数据的响应")
public class ProcessNodeResp extends WorkspaceObjectResp {

    @ApiModelProperty(value = "流程标识")
    private String processKey;

    @ApiModelProperty(value = "节点信息")
    private List<NodeInfoResp> nodeList = new ArrayList<>();
}
