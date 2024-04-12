package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;


@ApiModel(value = "Gte4Material 物料 的请求")
@Data

@NoArgsConstructor
public class Gte4MaterialReq extends ItemReq  implements Serializable{
}