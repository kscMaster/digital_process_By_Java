package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "MSWord MSWord 的响应")
@Data

@NoArgsConstructor
public class MSWordResp extends DatasetResp implements Serializable{
}