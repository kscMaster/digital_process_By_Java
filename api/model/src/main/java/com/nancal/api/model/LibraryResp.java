package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@ApiModel(value = "LibraryResp 库文件夹 的返回")
@NoArgsConstructor
public class LibraryResp {

    @ApiModelProperty(value = "类型")
    private String libraryType;

    @ApiModelProperty(value = "显示列Key")
    private String columnKey;

    @ApiModelProperty(value = "前端需要的固定值对象")
    private TreeDataResp scopedSlots = new TreeDataResp("custom");

    @ApiModelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "LibraryFolder 库文件夹")
    List<LibraryResp> children;

    @ApiModelProperty(value = "对象名称")
    private String objectName;

    @ApiModelProperty(value = "主键id")
    private String uid;

    @ApiModelProperty(value = "对象类型")
    private String objectType;

    @ApiModelProperty(value = "父文件夹名称，数据回显是使用")
    private String parentName;

    @ApiModelProperty(value = "文件夹icon")
    private String icon;



}
