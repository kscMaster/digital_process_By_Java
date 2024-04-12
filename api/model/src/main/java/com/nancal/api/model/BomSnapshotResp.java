package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "BomSnapshot bom快照 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BomSnapshotResp extends WorkspaceObjectResp  implements Serializable{
    @ApiModelProperty(value="BOM视图",required=false)
    private String bomView;
    @ApiModelProperty(value="查询的版本uid",required=false)
    private String revUid;
    @ApiModelProperty(value="查询的对象类型",required=false)
    private String revObjectType;
    @ApiModelProperty(value="版本号",required=false)
    private String revisionId;
    @ApiModelProperty(value="零件id",required=false)
    private String itemId;
    @ApiModelProperty(value="零件uid",required=false)
    private String itemUid;
    @ApiModelProperty(value="是否快照",required=false)
    private Boolean isSnapshot=true;

    @ApiModelProperty(value="显示时间戳",required=false)
    private String displayDate;

    public String getObjectType(){
       return "BomSnapshot";
    }

}