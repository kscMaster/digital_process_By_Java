package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@ApiModel(value = "负责人 的请求")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsibleReq {

    @NotNull(message = "责任人不能为空")
    @ApiModelProperty(value="责任人",required=false)
    @Length(max = 128,message = "责任人超过了最大长度限制")
    private String gte4ResponsibleName;
    @NotNull(message = "责任人ID不能为空")
    @ApiModelProperty(value="责任人ID",required=false)
    @Length(max = 64,message = "责任人ID超过了最大长度限制")
    private String gte4ResponsibleId;
}
