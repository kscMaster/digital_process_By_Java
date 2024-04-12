package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "Gte4TechDocPdm 技术文档(PDM) 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4TechDocPdm extends Document  implements Serializable{

}