package com.nancal.api.model;

import com.nancal.api.model.common.WorkBaseReq;
import com.nancal.common.enums.AppNameEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "ProcessInstance 流程 的请求")
public class ProcessInstanceReq extends WorkspaceObjectReq {
    @ApiModelProperty(value = "流程标题")
    @Length(max = 128,message = "流程标题超过了最大长度限制")
    private String processTitle;
    @ApiModelProperty(value = "流程定义ID")
    private String processDefinitionId;

    @NotEmpty(message = "流程标识不能为空")
    @ApiModelProperty(value = "流程标识")
    private String processKey;

    /**
     * TODO 可能存在多种类型，该业务id无效
     */
    @ApiModelProperty(value = "业务标识")
    private String businessKey;

    @ApiModelProperty(value = "流程模板名称")
    private String processName;

    @ApiModelProperty(value = "流程状态")
    private String processState;

    @ApiModelProperty(value = "审核节点")
    private String assigneeJson;

    @ApiModelProperty(value = "零组件id")
    private List<WorkBaseReq> reqList;

    @ApiModelProperty(value = "零组件id")
    private List<String> uidList;

    @NotEmpty(message = "送审类型不能为空")
    @ApiModelProperty(value = "送审类型")
    private String approvalType;
    @NotEmpty(message = "应用类型不能为空")
    @ApiModelProperty("应用类型")
    private String appName;

    @ApiModelProperty(value="描述",required=false)
    @Length(max = 1024,message ="描述超过了最大长度限制" )
    private String objectDesc;
}
