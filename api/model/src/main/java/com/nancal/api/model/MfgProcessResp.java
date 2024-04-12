package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@ApiModel(value = "MfgProcess 工艺规程 的响应")
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MfgProcessResp extends ItemResp  implements Serializable{
}