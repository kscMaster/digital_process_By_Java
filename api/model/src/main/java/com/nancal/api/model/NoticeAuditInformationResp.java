package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "获取流程节点数据的响应")
public class NoticeAuditInformationResp {
    @ApiModelProperty(value = "变更后中间表的返回值")
    private WorkspaceObjectResp afterChangeResp;
    @ApiModelProperty(value = "变更单数据返回值")
    private WorkspaceObjectResp changeResp;
}
