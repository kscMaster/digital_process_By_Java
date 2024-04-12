package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@ApiModel(value = "MfgProcessRevision 工艺规程版本 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class MfgProcessRevisionResp extends ItemRevisionResp  implements Serializable{
}