package com.nancal.esop.db;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ResourceDB {

    @ApiModelProperty("图代号")
    private String lezaoItemId;

    @ApiModelProperty("资源名称")
    private String lezaoResourceName;

    @ApiModelProperty("资源版本id")
    private String lezaoResourceRevUid;

    @ApiModelProperty("规格")
    private String lezaoSpec;

    @ApiModelProperty("备注")
    private String lezaoRemark;

    @ApiModelProperty("数据集id")
    private String esopVolumeId;

    @ApiModelProperty("版本号")
    private String lezaoRevisionId;

    @ApiModelProperty("资源类型")
    private Integer esopResourceType;
}
