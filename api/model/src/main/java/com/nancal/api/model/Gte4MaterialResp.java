package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;


@ApiModel(value = "Gte4Material 物料 的响应")
@Data

@NoArgsConstructor
public class Gte4MaterialResp extends ItemResp  implements Serializable{
}