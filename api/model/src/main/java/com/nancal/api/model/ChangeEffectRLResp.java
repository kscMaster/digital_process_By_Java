package com.nancal.api.model;


import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "ChangeEffectRL 受影响的 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class ChangeEffectRLResp extends RelationResp  implements Serializable{

    @Override
    public String getObjectType(){
       return "ChangeEffectRL";
    }

    public String getRelationType(){
       return "ChangeEffectRL";
    }
}