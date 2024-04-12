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
@ApiModel(value = "查询列设置")
public class ColumnFindReq {

    @NotBlank(message = "应用Code不能为空")
    @ApiModelProperty(value = "应用Code")
    private String appCode;

//    @NotBlank(message = "对象类型不能为空")
    @ApiModelProperty(value = "对象类型")
    private String objectType;

}
