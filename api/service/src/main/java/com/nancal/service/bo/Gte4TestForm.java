package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@ApiModel(value = "Gte4TestForm 试验表单记录 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4TestForm extends Form  implements Serializable{

    @ApiModelProperty(value="开始时间",required=false,example="1994-03-07")
    private LocalDate gte4StartDate;

    @ApiModelProperty(value="结束时间",required=false,example="1994-03-07")
    private LocalDate gte4EndDate;

    @ApiModelProperty(value="合计运转时间",required=false)
    private String gte4TotOpTime;

    @ApiModelProperty(value="合计燃机时间",required=false)
    private String gte4TotTurbTime;

    @ApiModelProperty(value="文件号",required=false)
    private String gte4FileNo;

}