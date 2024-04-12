package com.nancal.api.model;


import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "Gte4MfgLinePrRevision 线体工艺版本 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4MfgLinePrRevisionResp extends ItemRevisionResp  implements Serializable{
    @ApiModelProperty(value="过板方向",required=false)
    private String gte4TheDirection;
    @ApiModelProperty(value="工序分类",required=false)
    @JsonDict(DictConstant.GTE4_PROCESS_CLASSIFICATION)
    private String gte4ProcessClassification;
    @ApiModelProperty(value="工艺拼板模式",required=false)
    private String gte4PanelModel;

    @Override
    public String getObjectType(){
       return "Gte4MfgLinePrRevision";
    }

}