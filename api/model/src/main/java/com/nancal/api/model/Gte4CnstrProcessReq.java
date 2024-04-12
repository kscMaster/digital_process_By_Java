package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@ApiModel(value = "Gte4CnstrProcess 施工过程卡 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Gte4CnstrProcessReq extends Gte4MfgProcessReq  implements Serializable{
    public String getObjectType(){
        return "Gte4CnstrProcess";
    }

}