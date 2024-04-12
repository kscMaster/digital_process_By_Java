package com.nancal.remote.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeptVo implements Serializable {

    @ApiModelProperty("责任人")
    private List<ManagerVo> manager;
}
