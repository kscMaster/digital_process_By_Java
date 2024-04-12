package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Gte4PartTool 设计工装 的请求")
@Data

@NoArgsConstructor
public class Gte4PartToolReq extends ToolReq  implements Serializable{
}