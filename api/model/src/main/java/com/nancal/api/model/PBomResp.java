package com.nancal.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PBomResp<T extends WorkspaceObjectResp> extends BusinessObjectResp {

    @ApiModelProperty("bom行对象属性")
    private BOMNodeResp bomNode;

    @ApiModelProperty("工艺基本信息对象")
    private T revisionPBom;

    @ApiModelProperty("标准件")
    private WorkspaceObjectResp standard;

    @ApiModelProperty("工艺路线")
    private WorkspaceObjectResp route;

    @ApiModelProperty("主材关系")
    private WorkspaceObjectResp r006Process;

    @ApiModelProperty("辅材关系")
    private WorkspaceObjectResp r006ProcessAux;


}
