package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
@Data
@ToString
@ApiModel(value = "剪切请求实体VO")
@NoArgsConstructor
public class ShearReq {
    @NotNull(message = "左对象uid不能为空")
    @ApiModelProperty(value = "左对象uid")
    private String leftObject;
    @NotNull(message = "左对象类型不能为空")
    @ApiModelProperty(value = "左对象类型")
    private String leftObjectType;
    @NotNull(message = "对象uid不能为空")
    @ApiModelProperty(value = "对象uid")
    private String rightObject;
    @NotNull(message = "对象类型不能为空")
    @ApiModelProperty(value = "对象类型")
    private String rightObjectType;
}
