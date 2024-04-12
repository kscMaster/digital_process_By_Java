package com.nancal.service.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ApiModel(value = "Change 更改单 的BO")
@ToString(callSuper = true)
public abstract class BusinessObject {

    @ApiModelProperty(value = "显示名称")
    private String displayName;

    @ApiModelProperty(value = "唯一标志符")
    private String uid;
}
