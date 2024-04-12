package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Gte4Inspection 检验项目 的请求")
@Data

@NoArgsConstructor
public class Gte4InspectionReq extends ItemReq  implements Serializable{
}