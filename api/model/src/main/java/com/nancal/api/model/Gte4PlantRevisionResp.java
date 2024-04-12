package com.nancal.api.model;


import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "Gte4PlantRevision 工厂版本 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4PlantRevisionResp extends ItemRevisionResp  implements Serializable{
    @ApiModelProperty(value="序号",required=false)
    private String gte4orderNo;
    @JsonDict(DictConstant.GTE4_PLANT_TYPE)
    @ApiModelProperty(value="工厂类型",required=false)
    private String gte4PlantType;
    @ApiModelProperty(value="地址",required=false)
    private String gte4Address;
    @JsonDict(DictConstant.GTE4_PLANT_NATURE)
    @ApiModelProperty(value="工厂性质",required=false)
    private String gte4PlantNature;
    @JsonDict(DictConstant.MES_CODE)
    @ApiModelProperty(value="MES组织代号",required=false)
    private String gte4MESOrgCode;


}