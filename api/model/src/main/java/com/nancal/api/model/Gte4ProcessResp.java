package com.nancal.api.model;


import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "Gte4Process 工艺 的响应")
@Data

@NoArgsConstructor
public class Gte4ProcessResp extends ItemResp  implements Serializable{

    @Override
    public String getObjectType(){
       return "Gte4Process";
    }

}