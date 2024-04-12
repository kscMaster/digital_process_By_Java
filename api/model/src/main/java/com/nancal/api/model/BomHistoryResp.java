package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;


@ApiModel(value = "BomHistory bom查询历史 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BomHistoryResp extends ItemRevisionResp  implements Serializable{
    @ApiModelProperty(value="BOM视图",required=false)
    private String bomView;
    @ApiModelProperty(value="查询的版本uid",required=false)
    private String revUid;
    @ApiModelProperty(value="查询的对象类型",required=false)
    private String revObjectType;
    @ApiModelProperty(value="版本号",required=false)
    private String itemRevId;

    public String getObjectType(){
       return "BomHistory";
    }

}