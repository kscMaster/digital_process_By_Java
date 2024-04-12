package com.nancal.remote.to;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class DeptMemberTo implements Serializable {

    @ApiModelProperty(value = "分页",required = true)
    private Integer pageNumber = -1;

    @ApiModelProperty(value = "部门id",required = true)
    private String deptId;

    @ApiModelProperty(value = "部门名称",required = true)
    private String deptName;
}
