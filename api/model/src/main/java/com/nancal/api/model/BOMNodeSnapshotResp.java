package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel(value = "BOMNodeSnapshot bom行快照 的响应")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BOMNodeSnapshotResp extends BOPNodeViewResp{
    @ApiModelProperty(value="快照id",required=false)
    private String snapshotId;
    @ApiModelProperty(value="历史bom行id",required=false)
    private String hisBomId;


    public String getObjectType(){
       return "BOMNodeSnapshot";
    }

}