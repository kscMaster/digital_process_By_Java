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

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;


@ApiModel(value = "Gte4MfgProcessRevision 工艺版本 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4MfgProcessRevisionReq extends MfgProcessRevisionReq  implements Serializable{

    @QueryField(Ops.LIKE)
    @NotBlank(message = "id不能为空",groups = {UpdateGroup.class})
    @Length(max = 128, message = "id超过了最大长度限制",groups = {UpdateGroup.class})
    @ApiModelProperty(value = "主键id", required = true)
    private String uid;

    @ApiModelProperty(value = "左对象id")
    private String leftObject;

    @ApiModelProperty(value = "左对象类型")
    private String leftObjectType;


    @QueryField(Ops.LIKE)
    @NotBlank(message = "名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="名称",required=true)
    @Length(max = 128,message = "名称超过最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String objectName;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "产品型代号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="产品型代号",required=true)
    @Length(max = 64,message = "产品型代号超过最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String gte4ProductNo;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "产品名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="产品名称",required=true)
    @Length(max = 128,message = "产品名称超过最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String gte4ProductName;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "部组件名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="部组件名称",required=true)
    @Length(max = 128,message = "部组件名称超过最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String gte4PartName;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "部组件图号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="部组件图号",required=true)
    @Length(max = 64,message = "部组件图号超过最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String gte4PartNo;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "特性分类不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="特性分类",required=true)
    @Length(max = 128,message = "特性分类超过最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String gte4Type;

    @QueryField(Ops.EQ)
    @NotBlank(message = "阶段不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="阶段",required=true)
    @Length(max = 64,message = "阶段超过最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String gte4Phase;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "编号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="编号",required=true)
    @Length(max = 64,message = "编号超过最大长度限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    private String partNo;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="代号ID",required=true)
    @Length(max = 64,message = "代号ID超过最大长度限制",groups = {UpdateGroup.class, LikeGroup.class})
    private String itemId;

    @QueryField(Ops.EQ)
    @NotBlank(message = "密级不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="密级",required=true)
    @Length(max = 128,message = "密级超过最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String secretLevel;

    @QueryField(Ops.EQ)
    @NotBlank(message = "保密期限不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="保密期限",required=true)
    @Length(max = 128,message = "保密期限超过最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String secretTerm;

    @QueryField(Ops.LIKE)
//    @NotBlank(message = "编制单位不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="编制单位",required=true)
    @Length(max = 128,message = "编制单位超过最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String ownerGroupName;


    @QueryField(Ops.LIKE)
    @NotBlank(message = "分区不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="分区",required=true)
    @Length(max = 128,message = "分区超过最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String gte4partition;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Gte4MfgProcessRevisionReq that = (Gte4MfgProcessRevisionReq) o;
        return objectName.equals(that.objectName) &&
                gte4ProductNo.equals(that.gte4ProductNo) &&
                gte4ProductName.equals(that.gte4ProductName) &&
                gte4PartName.equals(that.gte4PartName) &&
                gte4PartNo.equals(that.gte4PartNo) &&
                gte4Type.equals(that.gte4Type) &&
                gte4Phase.equals(that.gte4Phase) &&
                partNo.equals(that.partNo) &&
                itemId.equals(that.itemId) &&
                secretLevel.equals(that.secretLevel) &&
                gte4partition.equals(that.gte4partition) &&
                secretTerm.equals(that.secretTerm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), objectName, gte4ProductNo, gte4ProductName, gte4PartName, gte4PartNo, gte4Type, gte4Phase, partNo, itemId, secretLevel, secretTerm,gte4partition);
    }
}