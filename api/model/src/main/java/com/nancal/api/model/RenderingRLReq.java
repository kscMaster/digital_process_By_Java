package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "RenderingRL 渲染关系 的请求")
@Data

@NoArgsConstructor
public class RenderingRLReq extends RelationReq  implements Serializable{
}