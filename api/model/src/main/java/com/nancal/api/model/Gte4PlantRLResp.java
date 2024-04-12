package com.nancal.api.model;


import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "Gte4PlantRL 工厂关系 的响应")
@Data
@NoArgsConstructor
public class Gte4PlantRLResp extends RelationResp  implements Serializable{

    @Override
    public String getObjectType(){
       return "Gte4PlantRL";
    }

    public String getRelationType(){
       return "Gte4PlantRL";
    }
}