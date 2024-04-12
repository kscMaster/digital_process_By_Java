package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "MSPowerPoint MSPowerPoint 的响应")
@Data

@NoArgsConstructor
public class MSPowerPointResp extends DatasetResp implements Serializable{
}