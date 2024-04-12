package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "JigRevision 夹具版本 的请求")
@Data

@NoArgsConstructor
public class JigRevisionReq extends ItemRevisionReq  implements Serializable{
}