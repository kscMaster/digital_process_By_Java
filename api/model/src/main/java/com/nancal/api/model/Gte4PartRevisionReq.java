package com.nancal.api.model;

import cn.hutool.core.util.StrUtil;
import com.nancal.api.utils.FieldAlias;
import com.nancal.common.annotation.QueryField;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.LikeGroup;
import com.nancal.common.base.UpdateGroup;
import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import com.nancal.model.entity.Gte4PartRevisionEntity;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;


@ApiModel(value = "Gte4PartRevision 设计零件版本 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4PartRevisionReq extends MsgbImportReq  implements Serializable{

    @NotBlank(message = "id不能为空",groups = {UpdateGroup.class})
    @Length(max = 128, message = "长度超过了最大长度限制",groups = {UpdateGroup.class})
    private String uid;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value = "对象名称")
    @NotBlank(message = "名称不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @Length(max = 128,message = "对象名称长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "零组件名称",required = true,max = 128)
    private String objectName;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="零组件图代号",required=true)
    @NotBlank(message = "零组件图代号不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @Length(max = 64,message = "长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "零组件图代号",required = true,max = 64)
    private String gte4PartNo;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="特性分类",required=true)
    @Length(max = 64,message = "长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
//    @NotBlank(message = "特性分类不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @FieldAlias(name = "*特性分类",max = 64)
    @JsonDict(DictConstant.GTE4_CAT)
    private String gte4Cat;

    @QueryField(Ops.LIKE)
//    @NotBlank(message = "材料牌号不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="材料牌号",required=true)
    @Length(max = 64,message = "长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "材料牌号",max = 64)
    private String gte4MaterialTradeMark;

    @QueryField(Ops.LIKE)
//    @NotBlank(message = "材料技术标准不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="材料技术标准",required=true)
    @Length(max = 128,message = "长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "材料技术标准",max = 128)
    private String gte4MatTechStd;

    @QueryField(Ops.LIKE)
//    @NotBlank(message = "材料规格不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="材料规格",required=true)
    @Length(max = 128,message = "长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "材料规格",max = 128)
    private String gte4MatSpec;

    @QueryField(Ops.LIKE)
//    @NotBlank(message = "设计批次不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="设计批次",required=true)
    @Length(max = 64,message = "长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "设计批次",max = 128)
    private String gte4DznBn;

    @QueryField(Ops.EQ)
//    @NotBlank(message = "密级不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="密级",required=true)
    @Length(max = 128,message = "长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "*密级",max = 128)
    @JsonDict(DictConstant.SECURITY_CLASS_IFI)
    private String secretLevel;

    @QueryField(Ops.EQ)
//    @NotBlank(message = "保密期限不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="保密期限",required=true)
    @Length(max = 128,message = "长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "*保密期限",max = 128)
    @JsonDict(DictConstant.SECRET_TERM)
    private String secretTerm;

    @QueryField(Ops.LIKE)
//    @NotBlank(message = "版本所有权用户不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="版本所有权用户(PDM)",required=true)
    @Length(max = 128,message = "长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "版本所有权用户(PDM)",max = 128)
    private String gte4PDMOwnner;

    @QueryField(Ops.LIKE)
//    @NotBlank(message = "版本所有权组不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="版本所有权组（PDM）",required=true)
    @Length(max = 128,message = "长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "版本所有权组(PDM)",max = 128)
    private String gte4PDMOwningGroup;

    @QueryField(Ops.EQ)
//    @NotBlank(message = "初始型号不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="初始型号",required=true)
    @Length(max = 64,message = "长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "*初始型号",max = 64)
    @JsonDict(DictConstant.GTE4_INIT_MODEL)
    private String gte4InitModel;

    @QueryField(Ops.EQ)
//    @NotBlank(message = "阶段标识不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="阶段标识",required=true)
    @Length(max = 64,message = "长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "*阶段标识",max = 64)
    @JsonDict(DictConstant.GTE4_PHASE)
    private String gte4Phase;

    @ApiModelProperty(value = "左对象对象类型")
    @NotBlank(message = "左对象对象类型不能为空",groups = {UpdateGroup.class})
    private String leftObjectType;

    @NotBlank(message = "左对象不能为空",groups = {UpdateGroup.class})
    @ApiModelProperty(value = "左对象对象id")
    private String leftObject;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="代号ID")
    @Length(max = 64,message = "代号ID超过最大长度限制",groups = {LikeGroup.class})
    private String itemId;

    @ApiModelProperty(value="板子的key",required=false)
    private String boardKey;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gte4PartRevisionEntity)) {
            return false;
        }
        Gte4PartRevisionEntity that = (Gte4PartRevisionEntity) o;
        return StrUtil.equals(StrUtil.blankToDefault(uid,StrUtil.EMPTY), that.getUid())
                && StrUtil.equals(StrUtil.blankToDefault(objectName,StrUtil.EMPTY), that.getObjectName())
                && StrUtil.equals(StrUtil.blankToDefault(gte4PartNo,StrUtil.EMPTY), that.getGte4PartNo())
                && StrUtil.equals(StrUtil.blankToDefault(gte4Cat,StrUtil.EMPTY), that.getGte4Cat())
                && StrUtil.equals(StrUtil.blankToDefault(gte4MaterialTradeMark,StrUtil.EMPTY), that.getGte4MaterialTradeMark())
                && StrUtil.equals(StrUtil.blankToDefault(gte4MatTechStd,StrUtil.EMPTY), that.getGte4MatTechStd())
                && StrUtil.equals(StrUtil.blankToDefault(gte4MatSpec,StrUtil.EMPTY), that.getGte4MatSpec())
                && StrUtil.equals(StrUtil.blankToDefault(gte4DznBn,StrUtil.EMPTY), that.getGte4DznBn())
                && StrUtil.equals(StrUtil.blankToDefault(secretLevel,StrUtil.EMPTY), that.getSecretLevel())
                && StrUtil.equals(StrUtil.blankToDefault(secretTerm,StrUtil.EMPTY), that.getSecretTerm())
                && StrUtil.equals(StrUtil.blankToDefault(gte4PDMOwnner,StrUtil.EMPTY), that.getGte4PDMOwnner())
                && StrUtil.equals(StrUtil.blankToDefault(gte4PDMOwningGroup,StrUtil.EMPTY), that.getGte4PDMOwningGroup())
                && StrUtil.equals(StrUtil.blankToDefault(gte4InitModel,StrUtil.EMPTY), that.getGte4InitModel())
                && StrUtil.equals(StrUtil.blankToDefault(gte4Phase,StrUtil.EMPTY), that.getGte4Phase())
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), uid, objectName, gte4PartNo, gte4Cat, gte4MaterialTradeMark, gte4MatTechStd,
                gte4MatSpec, gte4DznBn, secretLevel, secretTerm, gte4PDMOwnner, gte4PDMOwningGroup, gte4InitModel, gte4Phase);
    }

}