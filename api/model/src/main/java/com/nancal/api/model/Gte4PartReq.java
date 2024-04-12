package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Gte4Part 设计零件 的请求")
@Data

@NoArgsConstructor
public class Gte4PartReq extends ItemReq  implements Serializable{
}