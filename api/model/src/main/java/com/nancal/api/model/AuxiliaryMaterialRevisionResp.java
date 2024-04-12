package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@ApiModel(value = "AuxiliaryMaterialRevision 辅料版本 的响应")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuxiliaryMaterialRevisionResp extends ItemRevisionResp  implements Serializable{

    @ApiModelProperty(value="规格")
    private String standardNo;

    @ApiModelProperty(value="标准号")
    private String specification;

}