package com.nancal.api.model;


import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "Gte4DistOrgRL 发放单位关系 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Gte4DistOrgRLResp extends RelationResp  implements Serializable{

    @Override
    public String getObjectType(){
       return "Gte4DistOrgRL";
    }

    public String getRelationType(){
       return "Gte4DistOrgRL";
    }
}