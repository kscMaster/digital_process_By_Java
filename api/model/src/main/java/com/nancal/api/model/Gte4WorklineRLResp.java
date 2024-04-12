package com.nancal.api.model;


import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "Gte4WorklineRL 线体关系 的响应")
@Data
@NoArgsConstructor
public class Gte4WorklineRLResp extends RelationResp  implements Serializable{

    @Override
    public String getObjectType(){
       return "Gte4WorklineRL";
    }

    public String getRelationType(){
       return "Gte4WorklineRL";
    }
}