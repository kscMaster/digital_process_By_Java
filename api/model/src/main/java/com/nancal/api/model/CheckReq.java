package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "权限检查获取数据请求")
public class CheckReq extends BusinessObjectReq {

    @ApiModelProperty(value = "父零组件版本uid",required = true)
    private String pid;

    @ApiModelProperty(value = "子零组件版本uid",required = true)
    @NotBlank(message = "子零组件版本uid不能为空")
    private String sid;

}
