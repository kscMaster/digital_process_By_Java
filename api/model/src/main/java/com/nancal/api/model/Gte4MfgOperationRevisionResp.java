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


@ApiModel(value = "Gte4MfgOperationRevision 工序版本 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4MfgOperationRevisionResp extends MfgOperationRevisionResp  implements Serializable{
    @JsonDict(DictConstant.GTE4_COOPORG)
    @ApiModelProperty(value="协作单位",required=false)
    private String gte4CoopOrg;

    @ApiModelProperty(value="工艺描述",required=false)
    private String gte4ActionDescription;

    @ApiModelProperty(value="检测要点",required=false)
    private String gte4ExaminePoints;

    @ApiModelProperty(value="工艺要求/作业要领",required=false)
    private String gte4ProcessReq;

    @ApiModelProperty(value="抽检比例",required=false)
    private Double gte4SamplingRatio;

    @ApiModelProperty(value="测试要点",required=false)
    private String gte4TestPoints;

    @ApiModelProperty(value="工时(S)",required=false)
    private Integer gte4TaskTime;

    @ApiModelProperty(value="工艺能力组",required=false)
    private String gte4ActionCapabilityGroup;

    @ApiModelProperty(value="工艺能力组名称",required=false)
    private String gte4ActionCapabilityGroupName;

    @ApiModelProperty(value="板面",required=false)
    private String gte4BoardSurface;

}