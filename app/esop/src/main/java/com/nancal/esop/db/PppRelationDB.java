package com.nancal.esop.db;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PppRelationDB {

    @ApiModelProperty("主键id")
    private String pppUid;

    @ApiModelProperty("产品组件id")
    private String bopUid;

    @ApiModelProperty("产品模型code，取itemId即可")
    private String productModel;

    @ApiModelProperty("线code")
    private String lineCode;

    @ApiModelProperty("产品版本id")
    private String bopRevUid;

}
