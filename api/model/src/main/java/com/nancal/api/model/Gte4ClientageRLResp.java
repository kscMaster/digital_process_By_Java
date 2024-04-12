package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Gte4ClientageRL 委托关系 的响应")
@Data

@NoArgsConstructor
public class Gte4ClientageRLResp extends RelationResp  implements Serializable{
}