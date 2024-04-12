package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "MSWord MSWord 的请求")
@Data

@NoArgsConstructor
public class MSWordReq extends DatasetReq implements Serializable{
}