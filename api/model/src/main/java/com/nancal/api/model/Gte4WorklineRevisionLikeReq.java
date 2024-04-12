package com.nancal.api.model;

import com.nancal.common.annotation.QueryField;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.LikeGroup;
import com.nancal.common.base.UpdateGroup;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@ApiModel(value = "Gte4WorklineRevision关联线体模糊查询的请求 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4WorklineRevisionLikeReq {
    @QueryField(Ops.IN)
    @ApiModelProperty(value="uid",required=true)
    private List<String> uid;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="序号",required=true)
    @Length(max = 128,message = "序号长度超过限制",groups = {LikeGroup.class})
    private String gte4orderNo;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="对象名称",required=true)
    @Length(max = 128,message = "对象名称长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String objectName;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="零组件号",required=true)
    @Length(max = 64,message = "零组件号长度超过限制",groups = {LikeGroup.class})
    private String itemId;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="所属车间",required=true)
    @Length(max = 128,message = "所属车间长度超过限制",groups = {LikeGroup.class})
    private String gte4Workshop;

    @QueryField(Ops.LIKE)
    @Length(max = 128,message = "资产编号长度超过限制",groups = {LikeGroup.class})
    private String gte4AssetNum;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="区域",required=true)
    @Length(max = 128,message = "区域长度超过限制",groups = {LikeGroup.class})
    private String gte4Workarea;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="所属部门",required=true)
    @Length(max = 128,message = "所属部门长度超过限制",groups = {LikeGroup.class})
    private String gte4DepartmentName;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="线体能力",required=true)
    @Length(max = 128,message = "线体能力长度超过限制",groups = {LikeGroup.class})
    private String gte4LineCapacity;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="线体类型",required=true)
    @Length(max = 128,message = "线体类型长度超过限制",groups = {LikeGroup.class})
    private String gte4LineType;

    @ApiModelProperty(value="线体工艺uid",required=true)
    @Length(max = 64,message = "线体工艺uid长度超过限制",groups = {LikeGroup.class})
    @NotBlank(message = "线体工艺uid不能为空")
    private String leftObject;

    @ApiModelProperty(value="工厂工艺或线体工艺类型",required=true)
    @Length(max = 128,message = "线体工艺类型长度超过限制",groups = {LikeGroup.class})
    @NotBlank(message = "线体工艺类型不能为空")
    private String leftObjectType;

}
