package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Gte4TestRL 试验关系 的响应")
@Data

@NoArgsConstructor
public class Gte4TestRLResp extends RelationResp  implements Serializable{
}