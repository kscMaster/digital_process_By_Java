package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@ApiModel(value = "MfgStep 制造工步 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MfgStepResp extends WorkspaceObjectResp implements Serializable{
    @ApiModelProperty(value="工步内容",required=false)
    private String stepContent;
}