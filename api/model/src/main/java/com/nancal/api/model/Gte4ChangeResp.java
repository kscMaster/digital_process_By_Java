package com.nancal.api.model;


import com.nancal.api.model.dataset.FileAttrResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "Gte4Change 624更改单 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4ChangeResp extends ChangeResp  implements Serializable{
    @ApiModelProperty(value="更改类型",required=false)
    private String gte4ChangeType;
    @ApiModelProperty(value="更改原因",required=false)
    private String gte4ChangeReason;
    @ApiModelProperty(value="附件",required=false)
    private List<FileAttrResp> files;

    public String getObjectType(){
       return "Gte4Change";
    }

}