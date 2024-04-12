package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "DatasetFile 数据集文件关系 的响应")
@Data
@NoArgsConstructor
public class DatasetFileResp extends RelationResp  implements Serializable{
}