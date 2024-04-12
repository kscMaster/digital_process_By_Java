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


@ApiModel(value = "Gte4WorklineRevision 线体 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4WorklineRevisionReq extends ItemRevisionReq  implements Serializable{

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="序号",required=true)
    @Length(max = 128,message = "序号长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String gte4orderNo;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="对象名称",required=true)
    @Length(max = 128,message = "对象名称长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String objectName;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="对象类型",required=true)
    @Length(max = 64,message = "对象类型长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String objectType;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="备注",required=true)
    @Length(max = 512,message = "备注长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String remark;

    @ApiModelProperty(value="左对象uid",required=true)
    @Length(max = 64)
    @NotEmpty(message = "左对象uid不能为空")
    private String leftObject;

    @ApiModelProperty(value="左对象类型",required=true)
    @Length(max = 128)
    @NotEmpty(message = "左对象类型不能为空")
    private String leftObjectType;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="零组件号",required=true)
    @Length(max = 64,message = "零组件号长度超过限制",groups = {LikeGroup.class})
    private String itemId;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "所属车间不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="所属车间",required=true)
    @Length(max = 128,message = "所属车间长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String gte4Workshop;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "资产编号不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="资产编号",required=true)
    @Length(max = 128,message = "资产编号长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String gte4AssetNum;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "区域不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="区域",required=true)
    @Length(max = 128,message = "区域长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String gte4Workarea;

    @QueryField(Ops.EQ)
    @NotBlank(message = "所属部门不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="所属部门",required=true)
    @Length(max = 128,message = "所属部门长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String gte4DepartmentName;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "线体能力不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="线体能力",required=true)
    @Length(max = 128,message = "线体能力长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String gte4LineCapacity;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="线体类型",required=true)
    @NotBlank(message = "线体类型不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @Length(max = 128,message = "线体类型长度超过限制",groups = {LikeGroup.class})
    private String gte4LineType;

}