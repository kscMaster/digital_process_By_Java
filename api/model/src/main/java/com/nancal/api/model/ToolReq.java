package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Tool 工装 的请求")
@Data

@NoArgsConstructor
public class ToolReq extends ItemReq  implements Serializable{
}