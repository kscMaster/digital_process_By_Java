package com.nancal.api.model;


import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;


@ApiModel(value = "Gte4TechDocumentRevision 技术文档版本 的响应")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gte4TechDocumentRevisionResp extends DocumentRevisionResp  implements Serializable{

    @JsonDict(DictConstant.GTE4_FILE_TYPE)
    @ApiModelProperty(value="文件类型",required=false)
    private String gte4FileType;
    @JsonDict(DictConstant.GTE4_INIT_MODEL)
    @ApiModelProperty(value="初始型号",required=false)
    private String gte4InitModel;
    @JsonDict(DictConstant.GTE4_PHASE)
    @ApiModelProperty(value="阶段标识",required=false)
    private String gte4Phase;
    @ApiModelProperty(value="设计批次",required=false)
    private String gte4DznBn;
    @ApiModelProperty(value = "文件号", required = false)
    private String gte4FileNo;
    @ApiModelProperty(value="设计者",required=false)
    private String gte4Designer;
    @ApiModelProperty(value = "是否被其他文档引用", required = true)
    private Boolean isAppoint;

}