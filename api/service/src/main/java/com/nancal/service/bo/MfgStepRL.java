package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "MfgStepRL 关联工步 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MfgStepRL extends Relation  implements Serializable{

    @ApiModelProperty(value="工步号",required=false)
    private Integer stepNo;

}