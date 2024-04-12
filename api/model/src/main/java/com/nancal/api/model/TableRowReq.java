package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "TableRow 表行 的请求")
@Data

@NoArgsConstructor
public class TableRowReq extends WorkspaceObjectReq implements Serializable{
}