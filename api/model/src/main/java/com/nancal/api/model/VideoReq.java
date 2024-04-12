package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;


@ApiModel(value = "Video Video 的请求")
@Data
@NoArgsConstructor
public class VideoReq extends DatasetReq  implements Serializable{

    @Override
    public String getObjectType(){
        return "Video";
    }

}