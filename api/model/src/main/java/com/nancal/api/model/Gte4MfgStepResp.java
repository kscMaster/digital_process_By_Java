package com.nancal.api.model;


import com.nancal.framework.common.annotations.JsonDict;
import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;


@ApiModel(value = "Gte4MfgStep 工步 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4MfgStepResp extends MfgStepResp  implements Serializable{
    @JsonDict(DictConstant.GTE4_COOPORG)
    @ApiModelProperty(value="协作单位",required=false)
    private String gte4CoopOrg;

    @JsonDict(DictConstant.GTE4_TYPE)
    @ApiModelProperty(value="工序类型",required=false)
    private String gte4Type;

    @ApiModelProperty(value = "代号")
    private String itemId;

    @ApiModelProperty(value="工时(S)",required=false)
    private Integer gte4TaskTime;

    @ApiModelProperty(value="频率",required=false)
    private Double gte4Frequency;

}