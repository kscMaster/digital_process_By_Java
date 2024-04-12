package com.nancal.remote.to;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName RoleDetailParamDTO
 * @Description 远程调用参数类
 * @Author fuming
 * @Date 2022/2/24 13:13
 * @Version 1.0
 **/
@Data
public class RoleDetailParamTo implements Serializable {

    @ApiModelProperty("应用编码")
    private String appCode;

    @ApiModelProperty("角色id")
    private String roleId;

    @ApiModelProperty("角色名称")
    private String roleName;
}
