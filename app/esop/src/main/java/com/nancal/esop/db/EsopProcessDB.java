package com.nancal.esop.db;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EsopProcessDB {

    @ApiModelProperty("工艺零组件id")
    private String lezaoProcessUid;

    @ApiModelProperty("工艺零组件版本id")
    private String lezaoProcessRevUid;

    @ApiModelProperty("排序号")
    private String lezaoProcessNum;

    @ApiModelProperty("工艺名称")
    private String lezaoProcessName;

    @ApiModelProperty("工艺版本号")
    private String lezaoRevisionId;

    @ApiModelProperty("分区零组件id")
    private String esopPartitionUid;

    @ApiModelProperty("产品零组件版本id")
    private String esopBopRevId;

    @ApiModelProperty("图代号")
    private String lezaoItemId;
}
