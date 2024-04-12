package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ApiModel(value = "HomeFolder 个人工作台 的响应")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HomeFolderResp extends FolderResp {

    @ApiModelProperty(value = "是否存在子集(true: 存在子集;  false：不存在子集)")
    private boolean hasChildren;
    @ApiModelProperty(value = "是否是虚拟节点")
    private boolean fictitious;
    @ApiModelProperty(value = "子节点集合")
    private List<IncludeOrMasterRLResp> children;
}