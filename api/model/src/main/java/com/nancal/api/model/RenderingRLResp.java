package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "RenderingRL 渲染关系 的响应")
@Data

@NoArgsConstructor
public class RenderingRLResp extends RelationResp  implements Serializable{
}