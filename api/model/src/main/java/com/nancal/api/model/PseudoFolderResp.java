package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "伪文件夹 的响应")
public class PseudoFolderResp extends RuntimeBusinessObjectResp{
    @ApiModelProperty(value = "关系名称")
    private String relationName;
    @ApiModelProperty(value = "左对象UID")
    private String leftObject;
    @ApiModelProperty(value = "左对象类型")
    private String leftObjectType;
}
