package com.nancal.api.model;


import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "Video Video 的响应")
@Data
@NoArgsConstructor
public class VideoResp extends DatasetResp  implements Serializable{

    @Override
    public String getObjectType(){
       return "Video";
    }

}