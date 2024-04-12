package com.nancal.api.model;


import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "Gte4ProcessRevision 工艺版本 的响应")
@Data

@NoArgsConstructor
@AllArgsConstructor
public class Gte4ProcessRevisionResp extends ItemRevisionResp  implements Serializable{

    @ApiModelProperty(value="零组件图代号",required=false)
    private String gte4PartNo;
    @JsonDict(DictConstant.GTE4_CAT)
    @ApiModelProperty(value="特性分类",required=false)
    private String gte4Cat;
    @ApiModelProperty(value="材料牌号",required=false)
    private String gte4MaterialTradeMark;
    @ApiModelProperty(value="材料技术标准",required=false)
    private String gte4MatTechStd;
    @ApiModelProperty(value="材料规格",required=false)
    private String gte4MatSpec;
    @ApiModelProperty(value="设计批次",required=false)
    private String gte4DznBn;
    @ApiModelProperty(value="版本所有权用户(PDM)",required=false)
    private String gte4PDMOwnner;
    @ApiModelProperty(value="版本所有权组（PDM）",required=false)
    private String gte4PDMOwningGroup;
    @JsonDict(DictConstant.GTE4_INIT_MODEL)
    @ApiModelProperty(value="初始型号",required=false)
    private String gte4InitModel;
    @JsonDict(DictConstant.GTE4_PHASE)
    @ApiModelProperty(value="阶段标识",required=false)
    private String gte4Phase;
    @Override
    public String getObjectType(){
       return "Gte4ProcessRevision";
    }

}