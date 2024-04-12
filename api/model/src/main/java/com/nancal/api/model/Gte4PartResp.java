package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Gte4Part 设计零件 的响应")
@Data

@NoArgsConstructor
public class Gte4PartResp extends ItemResp  implements Serializable{
}