package com.nancal.api.model;


import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "ExtraPropertyData 扩展字段数据 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExtraPropertyDataResp extends WorkspaceObjectResp  implements Serializable{
    @ApiModelProperty(value="属性字段",required=false)
    private String property;
    @ApiModelProperty(value="属性值",required=false)
    private String value;
    @ApiModelProperty(value="对应业务id",required=false)
    private String leftObject;
    @ApiModelProperty(value="对应业务类型",required=false)
    private String leftObjectType;

    public String getObjectType(){
       return "ExtraPropertyData";
    }

}