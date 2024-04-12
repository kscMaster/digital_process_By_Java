package com.nancal.api.model.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserResp implements Serializable {

    @ApiModelProperty(value = "创建用户ID")
    private String creationUserId;

    @ApiModelProperty(value = "创建用户姓名")
    private String creationUsername;

    @ApiModelProperty(value = "最后更新用户ID")
    private String lastUpdateUserId;

    @ApiModelProperty(value = "最后修改用户名")
    private String lastUpdateUsername;

}
