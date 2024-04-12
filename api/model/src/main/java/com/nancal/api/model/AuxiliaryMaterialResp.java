package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "AuxiliaryMaterial 辅料 的响应")
@Data

@NoArgsConstructor
public class AuxiliaryMaterialResp extends ItemResp  implements Serializable{


}