package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ApiModel(value = "HomeFolder 个人工作台 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HomeFolder extends Folder {

}