package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


@ApiModel(value = "BusinessObject 业务对象 的请求")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class BusinessObjectReq  implements Serializable{

    @ApiModelProperty(value="显示名称",required=false)
    @Length(max = 1024)
    private String displayName;

    @ApiModelProperty(value="唯一标志符",required=false)
    @Length(max = 64)
    private String uid;

    @ApiModelProperty(value="删除标志",required=false)
    private Boolean delFlag;

    @ApiModelProperty(value="对象类型",required=false)
    @Length(max = 64)
    private String objectType;
}