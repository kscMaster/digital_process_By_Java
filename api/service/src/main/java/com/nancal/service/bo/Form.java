package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "Form 表单 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Form extends WorkspaceObject  implements Serializable{

    @ApiModelProperty(value="表单编号",required=false)
    private String formId;

}