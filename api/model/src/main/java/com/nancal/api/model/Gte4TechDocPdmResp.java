package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Gte4TechDocPdm 技术文档(PDM) 的响应")
@Data

@NoArgsConstructor
public class Gte4TechDocPdmResp extends DocumentResp  implements Serializable{
}