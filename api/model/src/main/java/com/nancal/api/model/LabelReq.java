package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName LabelReq
 * @Description TODO
 * @Author tuokai
 * @Date 2022/4/5
 * @Version 1.0
 **/
@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "Label 的请求")
public class LabelReq {
    @ApiModelProperty(value = "主键id",required = true)
    @Length(max = 64, message = "id超过了最大长度限制")
    @NotBlank(message = "主键id不能为空")
    private String uid;

    @ApiModelProperty(value = "对象类型",required = true)
    @Length(max = 64, message = "对象类型超过了最大长度限制")
    @NotBlank(message = "类型不能为空")
    private String objectType;
}
