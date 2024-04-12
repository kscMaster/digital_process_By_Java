package com.nancal.common.base;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TableRequest<T> implements Serializable {

    @ApiModelProperty(value = "从零开始的页面索引", example = "0")
    private int skip = 0;

    @ApiModelProperty(value = "要返回的页面大小", example = "10")
    private int take = 10;

    @ApiModelProperty(value = "查询条件")
    @Valid
    private T data;

    @ApiModelProperty(value = "排序")
    private TableSort sort;

    @ApiModelProperty(value = "分组")
    private List<String> groupByList;

}
