package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@ApiModel(value = "工夹具版本对象与BOM关系 的请求")
@Data
@NoArgsConstructor
public class LibraryAttributeToolReq extends BusinessObjectReq {

    @Valid
    @ApiModelProperty("工夹具对象")
    private ToolRevisionReq toolRevisionReq;

    @Valid
    @ApiModelProperty("自定义属性对象")
    private List<ExtraPropertyDataReq> extraPropertyDataReq;

}
