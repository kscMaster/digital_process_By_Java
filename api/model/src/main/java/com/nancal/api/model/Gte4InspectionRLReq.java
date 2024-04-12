package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Gte4InspectionRL 检验关系 的请求")
@Data

@NoArgsConstructor
public class Gte4InspectionRLReq extends RelationReq  implements Serializable{
}