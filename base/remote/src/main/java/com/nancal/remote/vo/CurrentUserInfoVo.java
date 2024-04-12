package com.nancal.remote.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Nancal.com Inc.
 * Copyright (c) 2021- All Rights Reserved.
 *
 * @Author yangtz
 * @Date 2021/11/30 14:58
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserInfoVo implements Serializable {
    private static final long serialVersionUID = 1075425172045337876L;

    @ApiModelProperty("用户ID")
    private String id;

    @ApiModelProperty("用户登录账号/登录名")
    private String loginName;

    @ApiModelProperty("用户名称")
    private String name;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("描述")
    private String remark;

    @ApiModelProperty("岗位，仅在租户用户有")
    private Duty duty;

    @ApiModelProperty("上级领导，仅租户有")
    private Leader leader;

    @ApiModelProperty("头像")
    private Logo logo;

    @ApiModelProperty("租户")
    private Tenant tenant;

    @ApiModelProperty("部门")
    private List<Department> depts;

    @ApiModelProperty("用户组")
    private List<Group> groups;

    @ApiModelProperty("角色")
    private List<Role> roles;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Duty implements Serializable {
        private String id;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Leader implements Serializable {
        private String id;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Logo implements Serializable {
        private static final long serialVersionUID = -517709612134146167L;

        @ApiModelProperty("文件Id")
        private String id;

        @ApiModelProperty("文件地址")
        private String url;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tenant implements Serializable {
        private static final long serialVersionUID = 6559008622498504237L;

        @ApiModelProperty("租户ID")
        private String id;

        @ApiModelProperty("租户名称")
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Department implements Serializable {
        private static final long serialVersionUID = 212020811085971091L;

        @ApiModelProperty("部门ID")
        private String id;

        @ApiModelProperty("部门名称")
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Group implements Serializable {
        private static final long serialVersionUID = 4784714249406367336L;

        @ApiModelProperty("用户组ID")
        private String id;

        @ApiModelProperty("用户组名称")
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Role implements Serializable {
        private static final long serialVersionUID = -9062112029946745979L;

        @ApiModelProperty("角色ID")
        private String id;

        @ApiModelProperty("角色名称")
        private String name;
    }
}
