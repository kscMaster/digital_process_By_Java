package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


@ApiModel(value = "Form 表单 的请求")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormReq extends WorkspaceObjectReq implements Serializable{

    @ApiModelProperty(value="表单编号",required=false)
    @Length(max = 64)
    private String formId;
}