package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "树结构响应体")
public class WorkTreeResp extends WorkspaceObjectResp{
    @ApiModelProperty(value = "是否存在子集(true: 存在子集;  false：不存在子集)")
    private boolean hasChildren;
    @ApiModelProperty(value = "零组件号或代（图）号")
    private String itemId;
    @ApiModelProperty(value = "零组件版本号")
    private String revisionId;
    @ApiModelProperty(value = "版次")
    private String sequence;
    @ApiModelProperty(value = "是否是虚拟节点,true表示虚拟节点")
    private boolean fictitious;

    /**
     * 页签详情的展示字段
     */
    @ApiModelProperty(value = "所有组")
    private String ownerGroupName;
}

