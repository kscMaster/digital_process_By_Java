package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Gte4ToolChangeForm 工装实物变更记录 的响应")
@Data

@NoArgsConstructor
public class Gte4ToolChangeFormResp extends FormResp  implements Serializable{
}