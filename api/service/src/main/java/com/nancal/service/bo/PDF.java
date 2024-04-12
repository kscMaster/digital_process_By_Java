package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ApiModel(value = "PDF PDF数据集 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PDF extends Dataset {


}