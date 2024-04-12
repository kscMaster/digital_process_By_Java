package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ApiModel(value = "HomeFolder 个人工作台 的请求")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HomeFolderReq extends FolderReq {

}