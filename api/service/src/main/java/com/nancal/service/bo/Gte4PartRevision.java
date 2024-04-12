package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "Gte4PartRevision 设计零件版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4PartRevision extends ItemRevision  implements Serializable{

    @ApiModelProperty(value="零组件图代号",required=false)
    private String gte4PartNo;

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

}