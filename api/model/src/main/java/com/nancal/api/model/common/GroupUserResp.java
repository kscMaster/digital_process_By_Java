package com.nancal.api.model.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class GroupUserResp implements Serializable {

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户姓名")
    private String username;

    @ApiModelProperty(value = "用户组id")
    private String userGroupId;

    @ApiModelProperty(value = "用户组名称")
    private String userGroupName;

}
