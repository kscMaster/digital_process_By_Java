package com.nancal.api.model;


import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "Gte4MfgPlantPrRevision 工厂工艺版本 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4MfgPlantPrRevisionResp extends ItemRevisionResp  implements Serializable{
    @ApiModelProperty(value="是否无铅加工",required=false)
    @JsonDict(DictConstant.BOOLEAN_TYPE)
    private Boolean gte4LeadFree;
    @ApiModelProperty(value="静电防护等级",required=false)
    private String gte4ELectrostaticProtection;
    @ApiModelProperty(value="防尘等级",required=false)
    private String gte4DustRating;
    @ApiModelProperty(value="PCB尺寸-长",required=false)
    private Double gte4PCBSizeLength;
    @ApiModelProperty(value="PCB尺寸-宽",required=false)
    private Double gte4PCBSizeWidth;
    @ApiModelProperty(value="PCB尺寸-高",required=false)
    private Double gte4PCBSizeHeigth;

    @Override
    public String getObjectType(){
       return "Gte4MfgPlantPrRevision";
    }

}