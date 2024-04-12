package com.nancal.common.base;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TableOrder implements Serializable {
    @ApiModelProperty(value = "属性名")
    private String properties;

    @ApiModelProperty(value = "是否正序")
    private boolean asc;
}
