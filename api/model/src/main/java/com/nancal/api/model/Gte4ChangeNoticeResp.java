package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "Gte4ChangeNotice 技术通知 的响应")
@Data
@NoArgsConstructor
public class Gte4ChangeNoticeResp extends ItemResp  implements Serializable{
}