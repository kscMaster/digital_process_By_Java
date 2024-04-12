package com.nancal.remote.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Nancal.com Inc.
 * Copyright (c) 2021- All Rights Reserved.
 *
 * @Author wangyb
 * @Date 2021/12/10 17:36
 * @Description
 */
@Data
public class RoleResourceVo implements Serializable {

//    @ApiModelProperty("id 可以是组织，部门，应用，租户的id")
//    private String id;
//
//    @ApiModelProperty("可以是组织，部门，应用，租户的name")
//    private String name;
//
//    @ApiModelProperty("标识符")
//    private String resource;
//
//    @ApiModelProperty("资源id")
//    private String resourceId;
//
//    @ApiModelProperty("备注")
//    private String remark;

    /**
     * {
     *             "code": "lz624-library:LibraryResouceRevision:release",
     *             "action": [
     *                 "show"
     *             ],
     *             "type": "Ui",
     *             "dataAccessLevel": null,
     *             "children": [],
     *             "simpleCode": "release"
     *         },
     */

    @ApiModelProperty("资源类型")
    private String type;

    @ApiModelProperty("资源操作")
    private List<String> action;

//    @ApiModelProperty("是否所有操作")
//    private Boolean actionIsAll;

    private String code;
    private String dataAccessLevel;
    private List<String> children;
    private String simpleCode;

}
