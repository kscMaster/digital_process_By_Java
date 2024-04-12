package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;


@ApiModel(value = "Gte4MfgStationPr 工位工艺 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Gte4MfgStationPrReq extends ItemReq  implements Serializable{

    @Override
    public String getObjectType(){
        return "Gte4MfgStationPr";
    }

}