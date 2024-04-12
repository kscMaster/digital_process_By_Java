package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "WPSWord WPSWord 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WPSWord extends Dataset  implements Serializable{

}