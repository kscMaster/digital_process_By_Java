package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import org.hibernate.validator.constraints.Length;


@ApiModel(value = "Gte4Workline 线体 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4WorklineReq extends ItemReq  implements Serializable{

    @ApiModelProperty(value="序号",required=false)
    @Length(max = 128)
    private String gte4orderNo;

    @Override
    public String getObjectType(){
        return "Gte4Workline";
    }

}