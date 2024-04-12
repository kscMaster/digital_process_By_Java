package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "ToolRevision 工具版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ToolRevision extends ItemRevision  implements Serializable{

    @ApiModelProperty(value="型号",required=false)
    private String modelNo;

    @ApiModelProperty(value="材料牌号",required=false)
    private String materialTradeMark;

    @ApiModelProperty(value="资源编号",required=false)
    private String meterialCode;

    @ApiModelProperty(value="图号",required=false)
    private String partNo;

}