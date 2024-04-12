package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Measure 量具 的请求")
@Data

@NoArgsConstructor
public class MeasureReq extends ItemReq  implements Serializable{
}