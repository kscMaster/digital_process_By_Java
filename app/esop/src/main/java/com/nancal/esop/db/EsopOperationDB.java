package com.nancal.esop.db;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EsopOperationDB {

    @ApiModelProperty("零组件版本id")
    private String lezaoOpRevUid;

    @ApiModelProperty("名称")
    private String lezaoOpName;

    @ApiModelProperty("产品版本id")
    private String esopBopRevUid;

    @ApiModelProperty("排序号")
    private String lezaoOpNum;

    @ApiModelProperty("相册id")
    private String esopVolumeId;

    @ApiModelProperty("是否关键工序")
    private Integer lezaoKeyOperation;

    @ApiModelProperty("分区id")
    private String esopPartitionUid;

    @ApiModelProperty("零组件id")
    private String lezaoOpUid;

    @ApiModelProperty("工艺版本id")
    private String lezaoProcessRevUid;

    @ApiModelProperty("版本号")
    private String lezaoRevisionId;

    @ApiModelProperty("图代号")
    private String lezaoItemId;
}
