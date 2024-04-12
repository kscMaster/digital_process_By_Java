package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "Document 文档 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Document extends Item  implements Serializable{

}