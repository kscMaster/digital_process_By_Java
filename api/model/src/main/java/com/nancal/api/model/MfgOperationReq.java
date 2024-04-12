package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@ApiModel(value = "MfgOperation 制造工序 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class MfgOperationReq extends ItemReq  implements Serializable{
}