package com.nancal.esop.db;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EsopBopMgmtDB {

    @ApiModelProperty("产品版本id")
    private String bopRevUid;

    @ApiModelProperty("产品组件id")
    private String bopUid;

    @ApiModelProperty("生命周期")
    private Integer lifecycle;

}
