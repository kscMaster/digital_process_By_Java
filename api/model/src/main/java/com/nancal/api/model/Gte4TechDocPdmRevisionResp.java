package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Gte4TechDocPdmRevision 技术文档(PDM)版本 的响应")
@Data

@NoArgsConstructor
@AllArgsConstructor
public class Gte4TechDocPdmRevisionResp extends DocumentRevisionResp  implements Serializable{
    @ApiModelProperty(value="文件号",required=false)
    private String gte4FileNo;
    @ApiModelProperty(value="文件类型",required=false)
    private String gte4FileType;
    @ApiModelProperty(value="初始型号",required=false)
    private String gte4InitModel;
    @ApiModelProperty(value="阶段标识",required=false)
    private String gte4Phase;
    @ApiModelProperty(value="设计批次",required=false)
    private String gte4DznBn;
    @ApiModelProperty(value="设计者",required=false)
    private String gte4Designer;
}