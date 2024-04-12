package com.nancal.api.model;


import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "MsgTemplate 消息模板 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MsgTemplateResp extends WorkspaceObjectResp  implements Serializable{
    @ApiModelProperty(value="标题",required=false)
    private String title;
    @ApiModelProperty(value="主题",required=false)
    private String body;
    @ApiModelProperty(value="消息类型",required=false)
    private Integer msgType;
    @ApiModelProperty(value="消息code码",required=false)
    private String msgCode;

    public String getObjectType(){
       return "MsgTemplate";
    }

}