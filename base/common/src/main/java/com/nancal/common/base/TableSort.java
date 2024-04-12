package com.nancal.common.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TableSort implements Serializable {

    @ApiModelProperty(value = "排序字段")
    private List<TableOrder> order = new ArrayList<>();
}
