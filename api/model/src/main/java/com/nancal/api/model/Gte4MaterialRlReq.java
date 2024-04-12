package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;


@ApiModel(value = "Gte4MaterialRl 物料关系 的请求")
@Data
@NoArgsConstructor
public class Gte4MaterialRlReq extends RelationReq  implements Serializable{

    @Override
    public String getObjectType(){
        return "Gte4MaterialRl";
    }

    public String getRelationType(){
        return "Gte4MaterialRl";
    }
}