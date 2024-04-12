package com.nancal.esop.db;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PartTopDB {


    @ApiModelProperty("图代号")
    private String itemId;

    @ApiModelProperty("图代号")
    private String productModule;

    @ApiModelProperty("名称")
    private String partName;

    @ApiModelProperty("零组件id")
    private String partUid;

    @ApiModelProperty("版本号")
    private String revisionId;

    @ApiModelProperty("版本id")
    private String partRevUid;

    @ApiModelProperty("版本id")
    private String linkDesign;

    @ApiModelProperty("数据集id")
    private String volumeId;

}
