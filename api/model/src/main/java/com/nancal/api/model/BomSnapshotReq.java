package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import org.hibernate.validator.constraints.Length;


@ApiModel(value = "BomSnapshot bom快照 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BomSnapshotReq extends WorkspaceObjectReq  implements Serializable{

    @ApiModelProperty(value="BOM视图",required=false)
    @Length(max = 128)
    private String bomView;

    @ApiModelProperty(value="查询的版本uid",required=false)
    @Length(max = 64)
    private String revUid;

    @ApiModelProperty(value="查询的对象类型",required=false)
    @Length(max = 64)
    private String revObjectType;

    @ApiModelProperty(value="版本号",required=false)
    @Length(max = 64)
    private String revisionId;

    @ApiModelProperty(value="零件id",required=false)
    @Length(max = 64)
    private String itemId;

    @ApiModelProperty(value="零件uid",required=false)
    @Length(max = 64)
    private String itemUid;

    public String getObjectType(){
        return "BomSnapshot";
    }

}