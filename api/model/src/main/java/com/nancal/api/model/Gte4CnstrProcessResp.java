package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@ApiModel(value = "Gte4CnstrProcess 施工过程卡 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Gte4CnstrProcessResp extends Gte4MfgProcessResp implements Serializable{
    public String getObjectType(){
       return "Gte4CnstrProcess";
    }

}