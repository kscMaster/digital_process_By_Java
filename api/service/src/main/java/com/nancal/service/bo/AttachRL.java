package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "AttachRL 附件 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AttachRL extends Relation  implements Serializable{

}