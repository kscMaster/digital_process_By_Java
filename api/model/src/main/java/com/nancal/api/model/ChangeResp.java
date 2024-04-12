package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "Change 更改单 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChangeResp extends WorkspaceObjectResp  implements Serializable{
    @ApiModelProperty(value="更改单号",required=false)
    private String changeId;
    @ApiModelProperty(value="更改意见",required=false)
    private String changeComment;

    public String getObjectType(){
       return "Change";
    }

}