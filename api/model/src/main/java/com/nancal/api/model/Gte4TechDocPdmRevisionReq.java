package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


@ApiModel(value = "Gte4TechDocPdmRevision 技术文档(PDM)版本 的请求")
@Data

@NoArgsConstructor
@AllArgsConstructor
public class Gte4TechDocPdmRevisionReq extends DocumentRevisionReq  implements Serializable{

    @ApiModelProperty(value="文件号",required=false)
    @Length(max = 64)
    private String gte4FileNo;

    @ApiModelProperty(value="文件类型",required=false)
    @Length(max = 128)
    private String gte4FileType;

    @ApiModelProperty(value="初始型号",required=false)
    @Length(max = 64)
    private String gte4InitModel;

    @ApiModelProperty(value="阶段标识",required=false)
    @Length(max = 64)
    private String gte4Phase;

    @ApiModelProperty(value="设计批次",required=false)
    @Length(max = 64)
    private String gte4DznBn;

    @ApiModelProperty(value="设计者",required=false)
    @Length(max = 128)
    private String gte4Designer;
}