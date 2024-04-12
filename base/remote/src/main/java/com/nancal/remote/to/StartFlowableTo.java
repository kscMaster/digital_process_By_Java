package com.nancal.remote.to;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartFlowableTo implements Serializable {

    @ApiModelProperty(value = "业务标识", required = true)
    private String businessKey;

    @ApiModelProperty(value = "流程定义key", required = true)
    private String processKey;

    @ApiModelProperty(value = "流程实例变量")
    private Map<String, Object> processVariables;

    @ApiModelProperty(value = "租户id")
    private String tenantId;

    @ApiModelProperty(value = "人员id", required = true)
    private String userId;
}
