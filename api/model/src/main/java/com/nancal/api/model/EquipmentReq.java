package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Equipment 设备 的请求")
@Data
@NoArgsConstructor
public class EquipmentReq extends ItemReq  implements Serializable{
}