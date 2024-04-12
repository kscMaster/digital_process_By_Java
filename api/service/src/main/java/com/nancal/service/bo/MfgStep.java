package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "MfgStep 制造工步 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MfgStep extends WorkspaceObject  implements Serializable{

    @ApiModelProperty(value="工步内容",required=false)
    private String stepContent;

}