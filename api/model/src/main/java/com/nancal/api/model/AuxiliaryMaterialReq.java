package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "AuxiliaryMaterial 辅料 的请求")
@Data

@NoArgsConstructor
public class AuxiliaryMaterialReq extends ItemReq  implements Serializable{



}