package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Text Text 的响应")
@Data

@NoArgsConstructor
public class TextResp extends DatasetResp implements Serializable{
}