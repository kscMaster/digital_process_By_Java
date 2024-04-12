package com.nancal.api.model;


import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.api.model.dataset.FileAttrResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@ApiModel(value = "Dataset 数据集 的响应")
@Data
@NoArgsConstructor
public class DatasetResp extends WorkspaceObjectResp implements Serializable{
    @ApiModelProperty("文件列表")
    private List<FileAttrResp> files;
}