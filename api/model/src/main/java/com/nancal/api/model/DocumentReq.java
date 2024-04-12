package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Document 文档 的请求")
@Data
@NoArgsConstructor
public class DocumentReq extends ItemReq  implements Serializable{
}