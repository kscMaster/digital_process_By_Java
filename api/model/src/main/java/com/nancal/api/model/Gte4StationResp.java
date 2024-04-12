package com.nancal.api.model;


import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "Gte4Station 工位 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4StationResp extends ItemResp  implements Serializable{
    @ApiModelProperty(value="序号",required=false)
    private String gte4orderNo;

    @Override
    public String getObjectType(){
       return "Gte4Station";
    }

}