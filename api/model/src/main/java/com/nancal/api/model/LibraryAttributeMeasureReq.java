package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@ApiModel(value = "量具版本对象与BOM关系 的请求")
@Data
@NoArgsConstructor
public class LibraryAttributeMeasureReq extends BusinessObjectReq {

    @Valid
    @ApiModelProperty("量具对象")
    private MeasureRevisionReq measureRevisionReq;

    @Valid
    @ApiModelProperty("自定义属性对象")
    private List<ExtraPropertyDataReq> extraPropertyDataReq;

}
