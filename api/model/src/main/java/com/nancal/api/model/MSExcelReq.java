package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "MSExcel MSExcel 的请求")
@Data

@NoArgsConstructor
public class MSExcelReq extends DatasetReq implements Serializable{
}