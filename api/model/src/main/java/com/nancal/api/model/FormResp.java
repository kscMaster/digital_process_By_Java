package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Form 表单 的响应")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormResp extends WorkspaceObjectResp implements Serializable{
    @ApiModelProperty(value="表单编号",required=false)
    private String formId;
}