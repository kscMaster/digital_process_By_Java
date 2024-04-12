package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(value = "Binary Binary 的响应")
@Data
@NoArgsConstructor
public class BinaryFileResp extends DatasetResp implements Serializable {
}
