package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@ApiModel(value = "MfgProcess 工艺规程 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class MfgProcessReq extends ItemReq  implements Serializable{
}