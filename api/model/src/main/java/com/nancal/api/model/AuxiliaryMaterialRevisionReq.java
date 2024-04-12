package com.nancal.api.model;

import cn.hutool.core.util.StrUtil;
import com.nancal.api.utils.FieldAlias;
import com.nancal.common.annotation.QueryField;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.LikeGroup;
import com.nancal.common.base.UpdateGroup;
import com.nancal.model.entity.AuxiliaryMaterialRevisionEntity;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


@ApiModel(value = "AuxiliaryMaterial 辅料版本 的请求")
@Data

@NoArgsConstructor
public class AuxiliaryMaterialRevisionReq extends MsgbImportReq  implements Serializable{

    @NotBlank(message = "id不能为空",groups = {UpdateGroup.class})
    @Length(max = 128, message = "id超过了最大长度限制",groups = {UpdateGroup.class})
    @ApiModelProperty(value = "主键id", required = true)
    private String uid;

    @ApiModelProperty(value = "主键id集合", required = true)
    private List<String> uids;

    @QueryField(Ops.LIKE)
    @Length(max = 64,message = "标准号长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    @ApiModelProperty(value="标准号")
    @FieldAlias(name = "标准号",max = 64)
    private String standardNo;

    @QueryField(Ops.LIKE)
    @Length(max = 128,message = "对象名称长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    @ApiModelProperty(value = "对象名称")
    @FieldAlias(name = "名称",max = 128)
    private String objectName;

    @QueryField(Ops.LIKE)
    @Length(max = 128,message = "规格长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    @ApiModelProperty(value = "规格")
    @FieldAlias(name = "规格",max = 128)
    private String specification;

    @QueryField(Ops.LIKE)
    @Length(max = 64,message = "代号长度超过了最大限制",groups = {UpdateGroup.class, LikeGroup.class})
    @ApiModelProperty(value = "代号", required = true)
    private String itemId;

    @NotBlank(message = "左对象对象类型不能为空",groups = {AddGroup.class})
    @ApiModelProperty(value = "左对象对象类型")
    private String leftObjectType;

    @NotBlank(message = "左对象不能为空",groups = {AddGroup.class})
    @ApiModelProperty(value = "左对象对象id")
    private String leftObject;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuxiliaryMaterialRevisionEntity)) {
            return false;
        }
        AuxiliaryMaterialRevisionEntity that = (AuxiliaryMaterialRevisionEntity) o;
        return StrUtil.equals(StrUtil.blankToDefault(standardNo,StrUtil.EMPTY), that.getStandardNo())
                && StrUtil.equals(StrUtil.blankToDefault(objectName,StrUtil.EMPTY), that.getObjectName())
                && StrUtil.equals(StrUtil.blankToDefault(specification,StrUtil.EMPTY), that.getSpecification())
                && StrUtil.equals(StrUtil.blankToDefault(itemId,StrUtil.EMPTY), that.getItemId())
                ;
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), standardNo, objectName,specification,itemId);
    }

}