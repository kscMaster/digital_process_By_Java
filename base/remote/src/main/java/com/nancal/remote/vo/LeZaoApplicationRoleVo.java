package com.nancal.remote.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName LeZaoApplicationRoleVO
 * @Description TODO
 * @Author fuming
 * @Date 2022/2/23 16:20
 * @Version 1.0
 **/
@Data
public class LeZaoApplicationRoleVo {

    @ApiModelProperty("应用编码")
    private String applicationCode;

    @ApiModelProperty("应用名称")
    private String applicationName;

    @ApiModelProperty("角色id")
    private String roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("用户基本信息")
    private List<PeopleVo> peoples;

}
