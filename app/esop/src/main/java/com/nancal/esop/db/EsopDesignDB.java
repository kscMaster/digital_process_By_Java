package com.nancal.esop.db;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EsopDesignDB {

    @ApiModelProperty("零组件版本id")
    private String lezaoDegignRevUid;

    @ApiModelProperty("零组件id")
    private String lezaoDesignUid;

    @ApiModelProperty("零组件名称")
    private String lezaoDesignName;

    @ApiModelProperty("版本号")
    private String lezaoRevisionId;

    @ApiModelProperty("资源id")
    private String esopVolumeId;

    @ApiModelProperty("图代号")
    private String lezaoItemId;
}
