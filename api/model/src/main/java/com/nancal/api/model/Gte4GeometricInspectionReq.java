package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


@ApiModel(value = "Gte4GeometricInspection 几何量检测委托单 的请求")
@Data

@NoArgsConstructor
@AllArgsConstructor
public class Gte4GeometricInspectionReq extends FormReq  implements Serializable{

    @ApiModelProperty(value="型别",required=false)
    @Length(max = 64)
    private String gte4ModelNo;

    @ApiModelProperty(value="课题（任务）号",required=false)
    @Length(max = 64)
    private String gte4TaskNo;

    @ApiModelProperty(value="图号",required=false)
    @Length(max = 64)
    private String gte4PartNo;

    @ApiModelProperty(value="数量",required=false)
    @Length(max = 64)
    private String gte4Quantity;

    @ApiModelProperty(value="零件编号（计算机记录号）",required=false)
    @Length(max = 128)
    private String gte4ItemId;

    @ApiModelProperty(value="委托人电话",required=false)
    @Length(max = 32)
    private String gte4TelNo;

    @ApiModelProperty(value="委托单位",required=false)
    @Length(max = 128)
    private String gte4Group;
}