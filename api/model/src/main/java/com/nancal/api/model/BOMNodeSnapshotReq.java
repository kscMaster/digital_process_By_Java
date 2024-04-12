package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import org.hibernate.validator.constraints.Length;


@ApiModel(value = "BOMNodeSnapshot bom行快照 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BOMNodeSnapshotReq extends BOMNodeReq  implements Serializable{

    @ApiModelProperty(value="快照id",required=false)
    @Length(max = 64)
    private String snapshotId;

    @ApiModelProperty(value="历史bom行id",required=false)
    @Length(max = 64)
    private String hisBomId;

    public String getObjectType(){
        return "BOMNodeSnapshot";
    }

}