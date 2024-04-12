package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * @author： Wang Hui
 * @date： 2022/5/3 10:08
 * @description： 快速发布的回调（更新）：更新流程状态与业务对象的状态
 **/
@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "送审的回调的请求")
public class ProcessInstanceCallbackReq extends WorkspaceObjectReq {

    @NotEmpty(message = "流程实例id不能为空")
    @ApiModelProperty(value = "流程实例ID")
    private String processInstanceId;

    @NotEmpty(message = "流程状态不能为空")
    @ApiModelProperty(value = "流程状态")
    private String processState;
}
