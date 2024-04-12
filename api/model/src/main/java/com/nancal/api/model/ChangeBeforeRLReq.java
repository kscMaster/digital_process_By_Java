package com.nancal.api.model;

import com.nancal.common.base.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDate;


@ApiModel(value = "ChangeBefore 更改前 的请求")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeBeforeRLReq extends RelationReq  implements Serializable{

    @ApiModelProperty(value="在制品处理意见",required=false)
    @Length(max = 1024,message = "在制品处理意见长度超过最大限制",groups = {AddGroup.class})
    private String wipSuggestion;

    @ApiModelProperty(value="已制品处理意见",required=false)
    @Length(max = 1024,message = "已制品处理意见长度超过最大限制",groups = {AddGroup.class})
    private String processedSuggestion;

    @ApiModelProperty(value="更改意见",required=false)
    @Length(max = 1024,message = "更改意见长度超过最大限制",groups = {AddGroup.class})
    private String changeOpinion;

    @ApiModelProperty(value="生效时间",required=false,example="1994-03-07")
    private LocalDate effectiveDate;

    @ApiModelProperty(value="版本号")
    @Length(max = 64,message = "版本号长度超过最大限制",groups = {AddGroup.class})
    private String itemRevId;

    @ApiModelProperty(value="零组件id")
    @Length(max = 64,message = "零组件id长度超过最大限制",groups = {AddGroup.class})
    private String itemUid;
}