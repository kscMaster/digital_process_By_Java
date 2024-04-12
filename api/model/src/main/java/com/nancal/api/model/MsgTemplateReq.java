package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import org.hibernate.validator.constraints.Length;


@ApiModel(value = "MsgTemplate 消息模板 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MsgTemplateReq extends WorkspaceObjectReq  implements Serializable{

    @ApiModelProperty(value="标题",required=false)
    @Length(max = 128)
    private String title;

    @ApiModelProperty(value="主题",required=false)
    @Length(max = 1024)
    private String body;

    @ApiModelProperty(value="消息类型",required=false)
    private Integer msgType;

    @ApiModelProperty(value="消息code码",required=false)
    @Length(max = 128)
    private String msgCode;

    public String getObjectType(){
        return "MsgTemplate";
    }

}