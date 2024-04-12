package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "MfgProcessRoute 制造工艺路线 的响应")
@Data

@NoArgsConstructor
public class MfgProcessRouteResp extends ItemResp  implements Serializable{
}