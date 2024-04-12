package com.nancal.api.model;


import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "Gte4ResponsibleRl 负责人关系表 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4ResponsibleRlResp extends WorkspaceObjectResp  implements Serializable{
    @ApiModelProperty(value="对应业务id",required=false)
    private String gte4LeftObject;
    @ApiModelProperty(value="对应业务类型",required=false)
    private String gte4LObjectType;
    @ApiModelProperty(value="责任人",required=false)
    private String gte4ResponsibleName;
    @ApiModelProperty(value="责任人ID",required=false)
    private String gte4ResponsibleId;

    public String getObjectType(){
       return "Gte4ResponsibleRl";
    }

}