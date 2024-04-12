package com.nancal.api.model;

import com.nancal.api.model.common.ValidList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;


@ApiModel(value = "Gte4ResponsibleRl 负责人关系表 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4ResponsibleRlReq extends WorkspaceObjectReq  implements Serializable{
    @NotNull(message = "对应业务id不能为空")
    @ApiModelProperty(value="对应业务id",required=false)
    @Length(max = 64,message = "对应业务id长度超过了最大限制")
    private String gte4LeftObject;

    @NotNull(message = "对应业务类型不能为空")
    @ApiModelProperty(value="对应业务类型",required=false)
    @Length(max = 64,message = "对应业务类型长度超过了最大限度")
    private String gte4LObjectType;

    @ApiModelProperty(value="责任人",required=false)
    @Length(max = 128)
    private String gte4ResponsibleName;

    @ApiModelProperty(value="责任人ID",required=false)
    @Length(max = 64)
    private String gte4ResponsibleId;

    @ApiModelProperty(value="责任人信息",required=false)
    ValidList<ResponsibleReq> responsibleReqs;
    public String getObjectType(){
        return "Gte4ResponsibleRl";
    }

}