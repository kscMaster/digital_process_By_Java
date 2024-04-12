package com.nancal.esop.db;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MoDB {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("top名称")
    private String mtmDesc;

    @ApiModelProperty("图代号")
    private String itemId;

    @ApiModelProperty("线code")
    private String lineCodes;

    @ApiModelProperty("图代号")
    private String productModule;

}
