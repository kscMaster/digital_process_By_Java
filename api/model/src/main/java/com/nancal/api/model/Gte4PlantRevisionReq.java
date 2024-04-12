package com.nancal.api.model;

import com.nancal.common.annotation.QueryField;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.LikeGroup;
import com.nancal.common.base.UpdateGroup;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


@ApiModel(value = "Gte4PlantRevision 工厂 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4PlantRevisionReq extends ItemRevisionReq  implements Serializable{

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="序号",required=true)
    @Length(max = 128,message = "序号长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String gte4orderNo;

    @QueryField(Ops.LIKE)
    @Length(max = 128,message = "对象名称长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="对象名称",required=true)
    private String objectName;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="对象类型",required=true)
    @Length(max = 64,message = "对象类型长度超过限制",groups = {UpdateGroup.class})
    private String objectType;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="备注",required=true)
    @Length(max = 512,message = "备注长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String remark;

    @ApiModelProperty(value="左对象uid",required=true)
    @Length(max = 64,groups = {UpdateGroup.class})
    @NotEmpty(message = "左对象uid不能为空")
    private String leftObject;

    @ApiModelProperty(value="左对象类型",required=true)
    @Length(max = 128,groups = {UpdateGroup.class})
    @NotEmpty(message = "左对象类型不能为空")
    private String leftObjectType;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="零组件号",required=true)
    @Length(max = 64,message = "零组件号长度超过限制",groups = {LikeGroup.class})
    private String itemId;

    @QueryField(Ops.EQ)
    @NotBlank(message = "工厂类型不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="工厂类型",required=true)
    @Length(max = 128,message = "工厂类型长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String gte4PlantType;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "地址不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="地址",required=true)
    @Length(max = 256,message = "地址长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String gte4Address;

    @QueryField(Ops.EQ)
    @NotBlank(message = "工厂性质不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="工厂性质",required=true)
    @Length(max = 128,message = "工厂性质长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String gte4PlantNature;

    @QueryField(Ops.EQ)
    @NotBlank(message = "MES组织代号不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="MES组织代号",required=true)
    @Length(max = 64,message = "MES组织代号长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String gte4MESOrgCode;


}