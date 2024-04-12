package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;


@ApiModel(value = "Gte4Process 工艺 的请求")
@Data

@NoArgsConstructor
public class Gte4ProcessReq extends ItemReq  implements Serializable{

    @Override
    public String getObjectType(){
        return "Gte4Process";
    }

}