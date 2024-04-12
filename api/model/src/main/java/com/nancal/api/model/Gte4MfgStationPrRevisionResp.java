package com.nancal.api.model;


import com.nancal.common.constants.Constant;
import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "Gte4MfgStationPrRevision 工位工艺版本 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4MfgStationPrRevisionResp extends ItemRevisionResp  implements Serializable{
    @ApiModelProperty(value="变更记录",required=false)
    private String gte4ChangeRecord;
    @ApiModelProperty(value="特殊特性",required=false)
    private String gte4SpecialCharacteristics;
    @ApiModelProperty(value="异常处理、注意事项、其他",required=false)
    private String gte4ExceptionCautions;
    @ApiModelProperty(value="关键信息",required=false)
    private String gte4KeyInfoProgram;
    @ApiModelProperty(value="关键工序",required=false)
    @JsonDict(DictConstant.BOOLEAN_TYPE)
    private Boolean gte4KeyProcess;

    @Override
    public String getObjectType(){
       return "Gte4MfgStationPrRevision";
    }

}