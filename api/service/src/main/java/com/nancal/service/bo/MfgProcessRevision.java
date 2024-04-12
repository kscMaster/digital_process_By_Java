package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "MfgProcessRevision 工艺规程版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MfgProcessRevision extends ItemRevision  implements Serializable{

}