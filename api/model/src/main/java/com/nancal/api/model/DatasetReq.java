package com.nancal.api.model;

import com.nancal.api.model.dataset.FileAttrReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;


@ApiModel(value = "Dataset 数据集 的请求")
@Data
@NoArgsConstructor
public class DatasetReq extends WorkspaceObjectReq implements Serializable{
    @ApiModelProperty("文件列表")
    private List<FileAttrReq> files;
}