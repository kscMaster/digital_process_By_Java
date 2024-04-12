package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "AuxiliaryMaterialRevision 辅料 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AuxiliaryMaterialRevision extends ItemRevision  implements Serializable{


    @ApiModelProperty(value="标准号",required=false)
    private String standardNo;

    @ApiModelProperty(value="规格",required=false)
    private String specification;


}