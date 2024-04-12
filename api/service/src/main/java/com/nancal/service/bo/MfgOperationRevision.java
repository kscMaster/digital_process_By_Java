package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "MfgOperationRevision 制造工序版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MfgOperationRevision extends ItemRevision  implements Serializable{

    @ApiModelProperty(value="工序类型",required=false)
    private String opType;

    @ApiModelProperty(value="是否关键工序",required=false,example="false")
    private Boolean isKey;

}