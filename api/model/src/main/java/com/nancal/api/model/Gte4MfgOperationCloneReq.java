package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel(value = "克隆工序对象 的请求")
@Data
@NoArgsConstructor
public class Gte4MfgOperationCloneReq implements Serializable {

    @ApiModelProperty("克隆的对象的uid")
    @NotBlank(message = "克隆的对象的uid不能为空")
    private String cloneId;

    @ApiModelProperty("克隆的对象的类型")
    @NotBlank(message = "克隆的对象类型不能为空")
    private String cloneType;

    @Valid
    @ApiModelProperty("工序对象属性")
    private Gte4MfgOperationRevisionReq operationRevisionReq;
}
