package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@ApiModel(value = "MfgOperation 制造工序 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class MfgOperationResp extends ItemResp  implements Serializable{
}