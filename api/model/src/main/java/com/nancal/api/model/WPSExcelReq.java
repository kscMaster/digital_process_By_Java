package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "WPSExcel WPSExcel 的请求")
@Data

@NoArgsConstructor
public class WPSExcelReq extends DatasetReq implements Serializable{
}