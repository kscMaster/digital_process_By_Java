package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@ApiModel(value = "ChangeBeforeRL 更改前 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChangeBeforeRL extends Relation  implements Serializable{

    @ApiModelProperty(value="在制品处理意见",required=false)
    private String wipSuggestion;

    @ApiModelProperty(value="已制品处理意见",required=false)
    private String processedSuggestion;

    @ApiModelProperty(value="更改意见",required=false)
    private String changeOpinion;

    @ApiModelProperty(value="生效时间",required=false,example="1994-03-07")
    private LocalDate effectiveDate;

}