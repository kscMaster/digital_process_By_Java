package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "剪切板获取数据请求")
public class ClipboardReq {

    @ApiModelProperty(value = "左对象uid",required = true)
    @NotBlank(message = "左对象id不能为空")
    private String leftObject;

    @ApiModelProperty(value = "左对象类型",required = true)
    @NotBlank(message = "左对象类型不能为空")
    private String leftObjectType;

    @Valid
    @NotNull(message = "右对象集合不能为空")
    @ApiModelProperty(value = "右对象集合",required = true)
    private List<PasteReq> rightObjects;

}
