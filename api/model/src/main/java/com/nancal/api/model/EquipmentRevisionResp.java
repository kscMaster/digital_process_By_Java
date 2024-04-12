package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "EquipmentRevision 设备版本 的响应")
@Data
@NoArgsConstructor
public class EquipmentRevisionResp extends ItemRevisionResp  implements Serializable{
    @ApiModelProperty(value = "型号")
    private String model;
}