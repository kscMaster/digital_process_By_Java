package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Equipment 设备 的响应")
@Data
@NoArgsConstructor
public class EquipmentResp extends ItemResp  implements Serializable{
}