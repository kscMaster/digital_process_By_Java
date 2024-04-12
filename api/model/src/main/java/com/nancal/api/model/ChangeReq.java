package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import org.hibernate.validator.constraints.Length;


@ApiModel(value = "Change 更改单 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChangeReq extends WorkspaceObjectReq  implements Serializable{

    @ApiModelProperty(value="更改单号",required=false)
    @Length(max = 64)
    private String changeId;

    @ApiModelProperty(value="更改意见",required=false)
    @Length(max = 1024)
    private String changeComment;


}