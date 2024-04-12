package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;


@ApiModel(value = "ChangeBefore 更改前 的响应")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeBeforeRLResp extends RelationResp  implements Serializable{
    @ApiModelProperty(value="在制品处理意见",required=false)
    private String wipSuggestion;
    @ApiModelProperty(value="已制品处理意见",required=false)
    private String processedSuggestion;
    @ApiModelProperty(value="更改意见",required=false)
    private String changeOpinion;
    @ApiModelProperty(value="生效时间",required=false,example="1994-03-07")
    private LocalDate effectiveDate;
}