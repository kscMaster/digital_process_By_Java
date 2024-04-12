package com.nancal.remote.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName peopleVO
 * @Description TODO
 * @Author fuming
 * @Date 2022/2/23 16:24
 * @Version 1.0
 **/
@Data
public class PeopleVo implements Serializable {

    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("用户名称")
    private String name;

    @ApiModelProperty("用户手机号")
    private String mobile;

    @ApiModelProperty("用户邮箱")
    private String email;

}
