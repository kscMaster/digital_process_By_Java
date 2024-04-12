package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "ProcessInstance 流程 的返回")
public class ProcessInstanceResp extends WorkspaceObjectResp{

    @ApiModelProperty(value = "流程定义ID")
    private String processDefinitionId;

    @ApiModelProperty(value = "流程标识")
    private String processKey;

    @ApiModelProperty(value = "业务标识")
    private String businessKey;

    @ApiModelProperty(value = "流程模板名称")
    private String processName;

    @ApiModelProperty(value = "流程状态")
    private String processState;

    @ApiModelProperty(value = "审核节点")
    private String assigneeJson;

    @ApiModelProperty(value = "零组件id")
    private String childItem;

    @ApiModelProperty(value = "零组件版本id")
    private String childItemRevision;

    @ApiModelProperty(value = "送审类型")
    private String approvalType;

    @ApiModelProperty(value = "流程标题")
    private String processTitle;

    @ApiModelProperty(value = "送审的零件")
    private List<WorkspaceObjectResp> workspaceObjectRespList;



}
