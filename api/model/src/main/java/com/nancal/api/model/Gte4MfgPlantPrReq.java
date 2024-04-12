package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;


@ApiModel(value = "Gte4MfgPlantPr 工厂工艺 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Gte4MfgPlantPrReq extends ItemReq  implements Serializable{

    @Override
    public String getObjectType(){
        return "Gte4MfgPlantPr";
    }

}