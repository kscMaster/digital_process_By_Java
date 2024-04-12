package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Gte4EntrustRL 关联委托项 的响应")
@Data

@NoArgsConstructor
public class Gte4EntrustRLResp extends RelationResp  implements Serializable{
}