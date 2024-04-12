package com.nancal.remote.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@ApiModel(value = "流程引擎任务处理的VO")
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskFlowableTo implements Serializable {

    @ApiModelProperty(value = "审批人员id", required = true)
    private String userId;

    @ApiModelProperty(value = "任务id", required = true)
    private String taskId;

    @ApiModelProperty("下个任务用户id")
    private String nextTaskUserId;

    @ApiModelProperty("流程结果")
    private String outcome;

    @ApiModelProperty("流程实例变量")
    private String processVariables;

    @ApiModelProperty("流程任务变量")
    private String taskLocalVariables;


}
