package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "BinaryFile BinaryFile 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BinaryFile extends Dataset  implements Serializable{

}