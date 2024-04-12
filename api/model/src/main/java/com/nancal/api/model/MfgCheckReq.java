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
@ApiModel(value = "msgBom权限检查获取数据请求")
public class MfgCheckReq extends BusinessObjectReq {

    @ApiModelProperty(value = "版本uid",required = true)
    private String id;

    @ApiModelProperty(value = "版本类型",required = true)
    private String type;

    @ApiModelProperty(value = "bom行uid",required = true)
    @NotBlank(message = "bom行uid不能为空")
    private String bomId;

}
