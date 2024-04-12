package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * @author： Wang Hui
 * @date： 2022/5/3 12:16
 * @description： 送审：获取流程实例节点数据
 **/
@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "获取流程节点数据的请求")
public class ProcessNodeReq extends WorkspaceObjectReq {

    @NotEmpty(message = "审核流程不能为空")
    @ApiModelProperty(value = "流程标识")
    private String processKey;

}
