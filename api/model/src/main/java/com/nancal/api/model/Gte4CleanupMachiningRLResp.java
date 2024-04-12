package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Gte4CleanupMachiningRL 补加工关系 的响应")
@Data

@NoArgsConstructor
public class Gte4CleanupMachiningRLResp extends RelationResp  implements Serializable{
}