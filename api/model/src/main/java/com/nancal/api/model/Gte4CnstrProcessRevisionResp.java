package com.nancal.api.model;


import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "Gte4CnstrProcessRevision 施工过程卡版本 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4CnstrProcessRevisionResp extends Gte4MfgProcessRevisionResp  implements Serializable{
    @ApiModelProperty(value="第几次装配/分解",required=false)
    private Integer gte4Time;
    @ApiModelProperty(value="生产订单号",required=false)
    private String gte4Order;
    @ApiModelProperty(value="生产工单号",required=false)
    private String gte4WorkOrder;
    @ApiModelProperty(value="台份号",required=false)
    private String gte4UnitNo;
    @ApiModelProperty(value="批次号",required=false)
    private String gte4Bn;

    @Override
    public String getObjectType(){
       return "Gte4CnstrProcessRevision";
    }

}