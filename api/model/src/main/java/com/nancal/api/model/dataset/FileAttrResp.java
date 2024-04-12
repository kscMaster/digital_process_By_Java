package com.nancal.api.model.dataset;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FileAttrResp implements Serializable {

    @ApiModelProperty(value = "文件uid")
    private String uid;

    @ApiModelProperty(value = "文件大小")
    private String size;

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
    private String fileType;

    @ApiModelProperty(value = "桶名称")
    private String bucketName;

    @ApiModelProperty(value = "创建日期", example = "1994-03-07")
    private LocalDateTime creationDate;
}
