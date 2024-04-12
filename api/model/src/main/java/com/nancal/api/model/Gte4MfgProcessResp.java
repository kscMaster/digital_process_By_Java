package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@ApiModel(value = "Gte4MfgProcess 工艺 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Gte4MfgProcessResp extends MfgProcessResp  implements Serializable{
//    public String getObjectType(){
//       return "Gte4MfgProcess";
//    }

}