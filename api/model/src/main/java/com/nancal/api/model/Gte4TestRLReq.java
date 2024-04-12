package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Gte4TestRL 试验关系 的请求")
@Data

@NoArgsConstructor
public class Gte4TestRLReq extends RelationReq  implements Serializable{
}