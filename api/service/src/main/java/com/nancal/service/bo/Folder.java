package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ApiModel(value = "Folder 文件夹 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Folder extends WorkspaceObject {

    @ApiModelProperty(value = "排序顺序")
    private String sort;

    @ApiModelProperty(value = "排序准则")
    private String criteria;


}