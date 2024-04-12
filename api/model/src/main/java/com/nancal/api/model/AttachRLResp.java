package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "AttachRL 附件 的响应")
@Data
@NoArgsConstructor
public class AttachRLResp extends RelationResp  implements Serializable{
}