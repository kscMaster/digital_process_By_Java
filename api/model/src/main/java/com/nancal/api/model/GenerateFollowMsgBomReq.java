package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(value = "生成随工工艺规程的请求")
@Data
@NoArgsConstructor
public class GenerateFollowMsgBomReq {
    @ApiModelProperty(value = "主键id",required = true)
    @Length(max = 64, message = "id超过了最大长度限制")
    @NotBlank(message = "主键id不能为空")
    private String uid;

    @ApiModelProperty(value = "对象类型",required = true)
    @Length(max = 64, message = "对象类型超过了最大长度限制")
    @NotBlank(message = "类型不能为空")
    private String objectType;
    @NotEmpty(message = "bom行id不能为空")
    private List<String> bomNodeId;

    @NotNull
    private Gte4CnstrProcessRevisionReq gte4CnstrProcessRevisionReq;
}
