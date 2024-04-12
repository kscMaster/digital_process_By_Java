package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "JigRevision 夹具版本 的响应")
@Data

@NoArgsConstructor
public class JigRevisionResp extends ItemRevisionResp  implements Serializable{
}