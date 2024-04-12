package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;


@ApiModel(value = "Gte4TestForm 试验表单记录 的响应")
@Data

@NoArgsConstructor
@AllArgsConstructor
public class Gte4TestFormResp extends FormResp  implements Serializable{
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