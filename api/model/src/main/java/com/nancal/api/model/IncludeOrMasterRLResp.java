package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName IncludeOrMasterRLResp
 * @Description TODO
 * @Author fuming
 * @Date 2022/4/2 16:10
 * @Version 1.0
 **/
@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "IncludeRL 或者Master 包含关系 的响应")
public class IncludeOrMasterRLResp extends WorkspaceObjectResp {

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
    @ApiModelProperty(value = "代号,设计零组件使用")
    private String partNo ;
    @ApiModelProperty(value = "代号,设计零组件版本使用")
    private String gte4PartNo ;

    /**
     * 页签详情的展示字段
     */
    @ApiModelProperty(value = "所有组")
    private String ownerGroupName;

    @ApiModelProperty(value = "查询结果时需要该属性零组件版本信息")
    List<WorkTreeResp> children;
}
