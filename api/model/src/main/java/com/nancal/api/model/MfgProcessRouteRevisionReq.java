package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "MfgProcessRouteRevision 制造工艺路线版本 的请求")
@Data

@NoArgsConstructor
public class MfgProcessRouteRevisionReq extends ItemRevisionReq  implements Serializable{
}