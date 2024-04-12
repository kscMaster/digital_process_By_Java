package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "粘贴右对象的请求")
public class PasteReq {
    @ApiModelProperty(value = "主键id",required = true)
    @Length(max = 64, message = "id超过了最大长度限制")
    @NotBlank(message = "主键id不能为空")
    private String uid;

    @ApiModelProperty(value = "对象类型",required = true)
    @Length(max = 64, message = "对象类型超过了最大长度限制")
    @NotBlank(message = "类型不能为空")
    private String objectType;

    @NotNull(message = "是否有子集不能为空")
    @ApiModelProperty(value = "是否存在子集(true: 存在子集;  false：不存在子集)")
    private boolean hasChildren;

}
