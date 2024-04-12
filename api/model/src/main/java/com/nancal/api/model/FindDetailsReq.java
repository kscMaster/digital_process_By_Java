package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "EBOM首页查询BOM详情的请求")
public class FindDetailsReq implements Serializable {

    @ApiModelProperty(value = "BOM行id",required = true)
    @Length(max = 64, message = "id超过了最大长度限制")
    private String bl_uid;

    @ApiModelProperty(value = "零组件版本id",required = true)
    @Length(max = 64, message = "id超过了最大长度限制")
    @NotBlank(message = "零组件版本id不能为空")
    private String pt_uid;

    @ApiModelProperty(value = "零组件版本的对象类型")
    private String pt_objectType;
}
