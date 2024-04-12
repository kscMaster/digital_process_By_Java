package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ApiModel(value = "PDF PDF数据集 的响应")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PDFResp extends DatasetResp {

}