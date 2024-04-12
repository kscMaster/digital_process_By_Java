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
@ApiModel(value = "列设置 的请求")
public class ColumnPropReq{

    @NotBlank(message = "应用Code不能为空")
    @ApiModelProperty(value = "应用Code")
    private String appCode;

//    @NotBlank(message = "对象类型不能为空")
    @ApiModelProperty(value = "对象类型")
    private String objectType;

    /**
     * 列筛选针对的是哪个对象存储在ObjectName上，如对BOMLine的列筛选,在ObjectName上存BOMLine
     * 不同用户使用OwnerId和OwnerName区分
     */
   @NotBlank(message = "列设置不能为空")
    @ApiModelProperty(value = "要显示的列名称,用逗号分割")
    private String displayProperties;
}
