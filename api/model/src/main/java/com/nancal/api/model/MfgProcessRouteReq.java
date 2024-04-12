package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "MfgProcessRoute 制造工艺路线 的请求")
@Data

@NoArgsConstructor
public class MfgProcessRouteReq extends ItemReq  implements Serializable{
}