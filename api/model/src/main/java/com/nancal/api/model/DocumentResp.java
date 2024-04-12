package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Document 文档 的响应")
@Data
@NoArgsConstructor
public class DocumentResp extends ItemResp  implements Serializable{
}