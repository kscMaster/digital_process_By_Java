package com.nancal.api.model;

import cn.hutool.core.util.StrUtil;
import com.nancal.common.annotation.QueryField;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.LikeGroup;
import com.nancal.common.base.UpdateGroup;
import com.nancal.model.entity.Gte4TechDocumentRevisionEntity;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;


@ApiModel(value = "Gte4TechDocumentRevision 技术文档版本 的请求")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gte4TechDocumentRevisionReq extends DocumentRevisionReq implements Serializable {
    @ApiModelProperty(value = "对象名称", required = false)
    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class})
    @Length(max = 128, message = "id超过了最大长度限制", groups = {UpdateGroup.class})
    private String uid;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value = "对象名称", required = true)
    @NotBlank(message = "名称不能为空", groups = {AddGroup.class, UpdateGroup.class,FewAddGroup.class,FewUpdateGroup.class})
    @Length(max = 128, message = "对象名称长度超过了最大限制", groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class,FewAddGroup.class,FewUpdateGroup.class})
    private String objectName;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value = "文件类型", required = true)
    @Length(max = 128, message = "文件类型长度超过了最大限制", groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @NotBlank(message = "文件类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String gte4FileType;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value = "初始型号", required = true)
    @Length(max = 64, message = "初始型号长度超过了最大限制", groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @NotBlank(message = "初始型号不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String gte4InitModel;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value = "阶段标识", required = true)
    @Length(max = 64, message = "阶段标识长度超过了最大限制", groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @NotBlank(message = "阶段标识不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String gte4Phase;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value = "设计批次", required = true)
    @Length(max = 64, message = "文件类型长度超过了最大限制", groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @NotBlank(message = "设计批次不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String gte4DznBn;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value = "文件号", required = true)
    @NotBlank(message = "文件号不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 64, message = "文件号长度超过了最大限制", groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String gte4FileNo;

    @QueryField(Ops.EQ)
    @NotBlank(message = "密级不能为空",groups = {AddGroup.class,UpdateGroup.class,FewAddGroup.class,FewUpdateGroup.class})
    @ApiModelProperty(value="密级",required=true)
    @Length(max = 128,message = "密级长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class,FewAddGroup.class,FewUpdateGroup.class})
    private String secretLevel;

    @QueryField(Ops.EQ)
    @NotBlank(message = "保密期限不能为空",groups = {AddGroup.class,UpdateGroup.class,FewAddGroup.class,FewUpdateGroup.class})
    @ApiModelProperty(value="保密期限",required=true)
    @Length(max = 128,message = "保密期限长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class,FewAddGroup.class,FewUpdateGroup.class})
    private String secretTerm;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="设计者",required=false)
    @Length(max = 128,message = "设计者长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    private String gte4Designer;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="备注",required=false)
    @Length(max = 512,message = "备注长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class,FewAddGroup.class,FewUpdateGroup.class})
    private String remark;

    @QueryField(Ops.LIKE)
    @Length(max = 64,message = "代号长度超过了最大限制",groups = {UpdateGroup.class, LikeGroup.class})
    @ApiModelProperty(value = "代号", required = true)
    private String itemId;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value = "版本id")
    @Length(max = 64, message = "版本id长度超过了最大限制", groups = {LikeGroup.class})
    private String revisionId;

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), uid, objectName, gte4FileType, gte4InitModel, gte4Phase,
                gte4DznBn, gte4FileNo, secretLevel, secretTerm, gte4Designer, remark);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gte4TechDocumentRevisionEntity)) {
            return false;
        }
        Gte4TechDocumentRevisionEntity that = (Gte4TechDocumentRevisionEntity) o;
        return StrUtil.equals(StrUtil.blankToDefault(uid,StrUtil.EMPTY), that.getUid())
                && StrUtil.equals(StrUtil.blankToDefault(objectName,StrUtil.EMPTY), that.getObjectName())
                && StrUtil.equals(StrUtil.blankToDefault(gte4FileType,StrUtil.EMPTY), that.getGte4FileType())
                && StrUtil.equals(StrUtil.blankToDefault(gte4InitModel,StrUtil.EMPTY), that.getGte4InitModel())
                && StrUtil.equals(StrUtil.blankToDefault(gte4Phase,StrUtil.EMPTY), that.getGte4Phase())
                && StrUtil.equals(StrUtil.blankToDefault(gte4DznBn,StrUtil.EMPTY), that.getGte4DznBn())
                && StrUtil.equals(StrUtil.blankToDefault(gte4FileNo,StrUtil.EMPTY), that.getGte4FileNo())
                && StrUtil.equals(StrUtil.blankToDefault(secretLevel,StrUtil.EMPTY), that.getSecretLevel())
                && StrUtil.equals(StrUtil.blankToDefault(secretTerm,StrUtil.EMPTY), that.getSecretTerm())
                && StrUtil.equals(StrUtil.blankToDefault(gte4Designer,StrUtil.EMPTY), that.getGte4Designer())
                && StrUtil.equals(StrUtil.blankToDefault(remark,StrUtil.EMPTY), that.getRemark())
                ;
    }

    public interface FewAddGroup{} //少量属性添加文档
    public interface FewUpdateGroup{} //修改少量文档
}