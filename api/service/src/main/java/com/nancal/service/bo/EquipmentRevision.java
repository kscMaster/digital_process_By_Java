package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "EquipmentRevision 设备版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EquipmentRevision extends ItemRevision  implements Serializable{

    @ApiModelProperty(value="型号",required=false)
    private String model;

}