package com.nancal.remote.to;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class FlowableTableTo implements Serializable {

    @ApiModelProperty(value = "从零开始的页面索引", example = "0")
    private int pageNo = 0;

    @ApiModelProperty(value = "要返回的页面大小", example = "10")
    private int pageSize = 10;

    @ApiModelProperty(value = "查询条件")
    private FlowableQueryTo query;


}
