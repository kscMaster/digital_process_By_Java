package com.nancal.api.model;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class RunTimeChangeAttachItemResp implements Serializable {


//    @ApiModelProperty("结构的左数据")
//    private String leftPartNo = StrUtil.EMPTY;
//
//    @ApiModelProperty("结构的右数据")
//    private String rightPartNo = StrUtil.EMPTY;

    @ApiModelProperty("itemId")
    private String itemId=StrUtil.EMPTY;

    @ApiModelProperty("更改内容")
    private String content;

    @ApiModelProperty("更改前值")
    private String before;

    @ApiModelProperty("更改后值")
    private String after;
}
