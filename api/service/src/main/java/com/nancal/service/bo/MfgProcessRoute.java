package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "MfgProcessRoute 制造工艺路线 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MfgProcessRoute extends Item  implements Serializable{

}