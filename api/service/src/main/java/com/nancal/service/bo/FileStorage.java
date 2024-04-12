package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ApiModel(value = "FileStorage 文件存储 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FileStorage extends WorkspaceObject {

    @ApiModelProperty(value = "原始文件名")
    private String originFileName;

    @ApiModelProperty(value = "文件后缀")
    private String fileExt;

    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @ApiModelProperty(value = "文件大小")
    private String fileSize;

    @ApiModelProperty(value = "mime类型")
    private String mimeType;

    @ApiModelProperty(value = "文件类型")
    private String type;

    @ApiModelProperty(value = "桶名称")
    private String bucketName;

}