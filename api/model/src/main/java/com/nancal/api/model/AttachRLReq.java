package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "AttachRL 附件 的请求")
@Data
@NoArgsConstructor
public class AttachRLReq extends RelationReq  implements Serializable{
}