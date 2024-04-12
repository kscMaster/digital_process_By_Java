package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;


@ApiModel(value = "Gte4WorklineRL 线体关系 的请求")
@Data
@NoArgsConstructor
public class Gte4WorklineRLReq extends RelationReq  implements Serializable{

    @Override
    public String getObjectType(){
        return "Gte4WorklineRL";
    }

    public String getRelationType(){
        return "Gte4WorklineRL";
    }
}