package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@ApiModel(value = "LibraryFolder 库文件夹 的返回")
@NoArgsConstructor
public class LibraryFolderResp extends FolderResp {

    @ApiModelProperty(value = "类型")
    private String libraryType;

    @ApiModelProperty(value = "显示列Key")
    private String columnKey;

    @ApiModelProperty(value = "前端需要的固定值对象")
    private TreeDataResp scopedSlots = new TreeDataResp("custom");

    @ApiModelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "资源库icon")
    private String icon;

    @ApiModelProperty(value = "LibraryFolder 库文件夹")
    List<LibraryFolderResp> children;
}
