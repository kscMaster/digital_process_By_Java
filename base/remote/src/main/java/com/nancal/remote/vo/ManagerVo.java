package com.nancal.remote.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerVo implements Serializable {

    @ApiModelProperty("责任人id")
    private String id;

    @ApiModelProperty("责任人名称")
    private String name;
}
