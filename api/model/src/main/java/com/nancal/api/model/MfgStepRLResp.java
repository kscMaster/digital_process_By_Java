package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "MfgStepRL 关联工步 的响应")
@Data

@NoArgsConstructor
@AllArgsConstructor
public class MfgStepRLResp extends RelationResp  implements Serializable{
    @ApiModelProperty(value="工步号",required=false)
    private Integer stepNo;
}