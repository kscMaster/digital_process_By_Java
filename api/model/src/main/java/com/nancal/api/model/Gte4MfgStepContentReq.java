package com.nancal.api.model;


import com.nancal.common.base.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@ApiModel(value = "Gte4MfgStep 工步内容编辑的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4MfgStepContentReq extends WorkspaceObjectReq implements Serializable{
    @ApiModelProperty(value="对象uid",required=true)
    @NotBlank(message = "id不能为空",groups = {UpdateGroup.class})
    @Length(max = 64, message = "长度超过了最大长度限制",groups = {UpdateGroup.class})
    private String uid;
    @ApiModelProperty(value="类型",required=true)
    @NotBlank(message = "类型",groups = {UpdateGroup.class})
    @Length(max = 128, message = "类型超过了最大长度限制",groups = {UpdateGroup.class})
    private String objectType;
    @ApiModelProperty(value="工步内容",required=true)
    private String stepContent;

}