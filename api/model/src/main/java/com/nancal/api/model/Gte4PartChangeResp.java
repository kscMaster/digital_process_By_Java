package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "Gte4PartChange 设计更改单 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Gte4PartChangeResp extends Gte4ChangeResp  implements Serializable{

    public String getObjectType(){
       return "Gte4PartChange";
    }

}