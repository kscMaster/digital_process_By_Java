package com.nancal.api.model;


import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "BOPView BOP视图 的返回")
public class BOPNodeViewResp extends BOMNodeResp{

    @ApiModelProperty(value="工序/工步协作单位",required=false)
    private String gte4CoopOrgDesc;

    @ApiModelProperty(value="工序/工步类型",required=false)
    private String opOrGet4TypeDesc;

//
//    @ApiModelProperty(value="工序/工步类型",required=false)
//    private String opOrGet4TypeDesc;
//
////    @JsonDict(DictConstant.GTE4_TYPE)
//    @ApiModelProperty(value="工步类型",required=false)
//    private String gte4Type;
//
//    @JsonDict(DictConstant.OP_TYPE)
    @JsonDict(DictConstant.OP_TYPE)
    @ApiModelProperty(value="工序类型",required=false)
    private String opType;
//
//    @ApiModelProperty(value="是否关键工序",required=false,example="false")
//    private Boolean isKey;

    @ApiModelProperty(value="并行工序号",required=false,example="false")
    private String parallelNum;

    @ApiModelProperty(value="总工时",required=false,example="false")
    private Integer taskTime=0;

    public static final String GTE4_TYPE = "gte4Type";
    public static final String OP_TYPE = "opType";
    public static final String GTE4_COOP_ORG = "gte4CoopOrg";

}
