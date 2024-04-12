package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "JigRevision 夹具版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class JigRevision extends ItemRevision  implements Serializable{

}