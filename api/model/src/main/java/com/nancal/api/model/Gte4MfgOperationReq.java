package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@ApiModel(value = "Gte4MfgOperation 工序 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Gte4MfgOperationReq extends MfgOperationReq  implements Serializable{
    public String getObjectType(){
        return "Gte4MfgOperation";
    }

}