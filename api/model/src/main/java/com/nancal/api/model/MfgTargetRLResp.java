package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "MfgTargetRL 制造目标 的响应")
@Data

@NoArgsConstructor
public class MfgTargetRLResp extends RelationResp  implements Serializable{
}