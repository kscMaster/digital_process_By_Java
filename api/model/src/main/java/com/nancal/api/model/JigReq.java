package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Jig 夹具 的请求")
@Data

@NoArgsConstructor
public class JigReq extends ItemReq  implements Serializable{
}