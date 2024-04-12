package com.nancal.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RunTimeChangeAttachOuterResp extends BusinessObjectResp {


    @ApiModelProperty("更改单id")
    private String partChangeId;

    @ApiModelProperty("更改单名称")
    private String partChangeName;

    @ApiModelProperty("总页数")
    private Integer partChangePage;

    @ApiModelProperty("数据")
    private List<RunTimeChangeAttachResp> list;
}
