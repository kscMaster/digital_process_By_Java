package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Gte4InspectionRL 检验关系 的响应")
@Data

@NoArgsConstructor
public class Gte4InspectionRLResp extends RelationResp  implements Serializable{
}