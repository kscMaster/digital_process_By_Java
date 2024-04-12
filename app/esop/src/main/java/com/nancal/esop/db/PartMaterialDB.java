package com.nancal.esop.db;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PartMaterialDB {

    @ApiModelProperty("图代号")
    private String itemId;

    @ApiModelProperty("名称")
    private String partName;

    @ApiModelProperty("版本号")
    private String revisionId;

    @ApiModelProperty("版本id")
    private String partRevUid;

    @ApiModelProperty("数模id")
    private String linkDesignUid;

    @ApiModelProperty("数据集id")
    private String volumeId;


}
