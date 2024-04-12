package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import org.hibernate.validator.constraints.Length;


@ApiModel(value = "Gte4Change 624更改单 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4ChangeReq extends ChangeReq  implements Serializable{

    @ApiModelProperty(value="更改类型",required=false)
    @Length(max = 128)
    private String gte4ChangeType;

    @ApiModelProperty(value="更改原因",required=false)
    @Length(max = 1024)
    private String gte4ChangeReason;


}