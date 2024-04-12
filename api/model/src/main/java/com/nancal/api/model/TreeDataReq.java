package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@ToString
@ApiModel(value = "树型对象的请求体")
@NoArgsConstructor
public class TreeDataReq extends WorkspaceObjectReq {

    @Valid
    @NotNull(message = "对象uid不能为空")
    @ApiModelProperty(value = "对象uid",required = true)
    private String uid;

}
