package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "Gte4GeometricInspection 几何量检测委托单 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4GeometricInspection extends Form  implements Serializable{

    @ApiModelProperty(value="型别",required=false)
    private String gte4ModelNo;

    @ApiModelProperty(value="课题（任务）号",required=false)
    private String gte4TaskNo;

    @ApiModelProperty(value="图号",required=false)
    private String gte4PartNo;

    @ApiModelProperty(value="数量",required=false)
    private String gte4Quantity;

    @ApiModelProperty(value="零件编号（计算机记录号）",required=false)
    private String gte4ItemId;

    @ApiModelProperty(value="委托人电话",required=false)
    private String gte4TelNo;

    @ApiModelProperty(value="委托单位",required=false)
    private String gte4Group;

}