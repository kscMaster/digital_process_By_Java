package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Gte4TechDocument 技术文档 的请求")
@Data

@NoArgsConstructor
public class Gte4TechDocumentReq extends DocumentReq  implements Serializable{
}