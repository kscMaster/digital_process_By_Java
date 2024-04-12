package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.*;
import java.io.Serializable;

@ApiModel(value = "Gte4MaterialRl 物料关系 的响应")
@Data
@NoArgsConstructor
public class Gte4MaterialRlResp extends RelationResp  implements Serializable{

    @Override
    public String getObjectType(){
       return "Gte4MaterialRl";
    }

    public String getRelationType(){
       return "Gte4MaterialRl";
    }
}