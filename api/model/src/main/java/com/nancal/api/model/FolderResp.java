package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ApiModel(value = "Folder 文件夹 的响应")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FolderResp extends WorkspaceObjectResp {

    @ApiModelProperty(value = "排序顺序")
    private String sort;

    @ApiModelProperty(value = "排序准则")
    private String criteria;

}