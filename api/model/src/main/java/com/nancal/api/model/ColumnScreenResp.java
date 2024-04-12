package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "列设置 的响应")
public class ColumnScreenResp extends WorkspaceObjectResp {

    @ApiModelProperty(value = "应用名称")
    private String appName;
    /**
     * 列筛选针对的是哪个对象存储在ObjectName上，如对BOMLine的列筛选,在ObjectName上存BOMLine
     * 不同用户使用OwnerId和OwnerName区分
     */
    @ApiModelProperty(value = "要显示的列名称,用逗号分割")
    private String displayProperties;


}
