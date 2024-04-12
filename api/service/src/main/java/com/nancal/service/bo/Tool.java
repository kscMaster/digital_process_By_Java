package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "Tool 工具 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Tool extends Item  implements Serializable{

    @ApiModelProperty(value="图号",required=false)
    private String partNo;

}