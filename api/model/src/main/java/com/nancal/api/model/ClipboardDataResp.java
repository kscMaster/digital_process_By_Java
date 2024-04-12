package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ApiModel(value = "剪切板数据响应")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ClipboardDataResp extends WorkspaceObjectResp {
    @ApiModelProperty(value = "零组件号", required = true)
    private String itemId;

    @ApiModelProperty(value = "零组件版本号")
    private String revisionId;

    @ApiModelProperty(value = "版次")
    private String sequence;
}
