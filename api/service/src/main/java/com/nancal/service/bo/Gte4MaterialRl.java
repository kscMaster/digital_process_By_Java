package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "Gte4MaterialRl 物料关系 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4MaterialRl extends Relation  implements Serializable{



    @Override
    public String getObjectType(){
        return "Gte4MaterialRl";
    }

    public String getRelationType(){
        return "Gte4MaterialRl";
    }
}