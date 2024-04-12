package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "Gte4TechDocPdmRevision 技术文档(PDM)版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4TechDocPdmRevision extends DocumentRevision  implements Serializable{

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