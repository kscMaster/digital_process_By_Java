package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "MfgProcessRouteRevision 制造工艺路线版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MfgProcessRouteRevision extends ItemRevision  implements Serializable{

}