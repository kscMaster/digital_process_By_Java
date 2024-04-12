package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "删除零组件版本 的请求")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeleteRevisionReq extends ItemRevisionReq{

    @NotNull(message = "对象id不能为空")
    @ApiModelProperty(value="唯一标志符",required=false)
    @Length(max = 64)
    private String uid;
}
