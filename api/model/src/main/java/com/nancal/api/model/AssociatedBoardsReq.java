package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "关联板子的请求")
@Data
@NoArgsConstructor
public class AssociatedBoardsReq {
    @ApiModelProperty(value = "板子的key",required = true)
    @Length(max = 128,message = "板子的key超过了最大长度限制")
    private String boardKey;
    @ApiModelProperty(value = "当前数据的版本uid",required = true)
    @NotBlank(message = "数据uid不能为空")
    @Length(max = 64,message = "数据uid超过了最大长度限制")
    private String uid;
    @ApiModelProperty(value = "当前数据的版本类型",required = true)
    @NotBlank(message = "数据类型不能为空")
    @Length(max = 128,message = "数据类型超过了最大长度限制")
    private String objectType;


}
