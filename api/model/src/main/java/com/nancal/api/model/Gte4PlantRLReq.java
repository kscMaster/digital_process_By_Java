package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;


@ApiModel(value = "Gte4PlantRL 工厂关系 的请求")
@Data
@NoArgsConstructor
public class Gte4PlantRLReq extends RelationReq  implements Serializable{

    @Override
    public String getObjectType(){
        return "Gte4PlantRL";
    }

    public String getRelationType(){
        return "Gte4PlantRL";
    }
}