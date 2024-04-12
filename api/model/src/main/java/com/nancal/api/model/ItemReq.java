package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


@ApiModel(value = "Item 零组件 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemReq extends WorkspaceObjectReq  implements Serializable{

    @ApiModelProperty(value="零组件号",required=false)
    @Length(max = 64)
    private String itemId;

    @ApiModelProperty(value="代图号",required=false)
    @Length(max = 64)
    private String partNo;
//    public String getObjectType(){
//        return "Item";
//    }

}