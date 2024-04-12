package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@ApiModel(value = "辅材版本对象与BOM关系 的请求")
@Data
@NoArgsConstructor
public class LibraryAttributeAuxiliaryReq extends BusinessObjectReq {

    @Valid
    @ApiModelProperty("辅材对象")
    private AuxiliaryMaterialRevisionReq auxiliaryMaterialRevisionReq;

    @Valid
    @ApiModelProperty("自定义属性对象")
    private List<ExtraPropertyDataReq> extraPropertyDataReq;

}
