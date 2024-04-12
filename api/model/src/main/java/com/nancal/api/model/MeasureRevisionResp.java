package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "MeasureRevision 量具版本 的响应")
@Data

@NoArgsConstructor
public class MeasureRevisionResp extends ItemRevisionResp  implements Serializable{

    @ApiModelProperty(value = "规格")
    private String specification;
}