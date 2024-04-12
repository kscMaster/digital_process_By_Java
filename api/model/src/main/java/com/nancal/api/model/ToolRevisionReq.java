package com.nancal.api.model;

import com.nancal.api.model.dataset.FileAttrReq;
import com.nancal.api.utils.FieldAlias;
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
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


@ApiModel(value = "ToolRevision 工装版本 的请求")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolRevisionReq extends MsgbImportReq  implements Serializable{

//    @ApiModelProperty(value="型号",required=false)
//    @Length(max = 64)
//    private String modelNo;
//
//    @ApiModelProperty(value="材料牌号",required=false)
//    @Length(max = 64)
//    private String materialTradeMark;
//
//    @ApiModelProperty(value="资源编号",required=false)
//    @Length(max = 64)
//    private String meterialCode;

    @NotBlank(message = "id不能为空",groups = {UpdateGroup.class})
    @Length(max = 128, message = "id超过了最大长度限制",groups = {UpdateGroup.class})
    @ApiModelProperty(value = "主键id", required = true)
    private String uid;

    @Valid
    @ApiModelProperty(value = "文件数据，数组形式传参")
    private List<FileAttrReq> files;

    @ApiModelProperty(value = "附件路径")
    @FieldAlias(name = "附件")
    private String Gte4Attaches;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="图号")
    @Length(max = 64,message = "图号长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "图号",max = 64)
    private String partNo;

    @QueryField(Ops.LIKE)
    @Length(max = 128,message = "对象名称长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    @ApiModelProperty(value = "对象名称")
    @FieldAlias(name = "名称",max = 128)
    private String objectName;

    @QueryField(Ops.LIKE)
    @Length(max = 64,message = "代号号长度超过了最大限制",groups = {UpdateGroup.class, LikeGroup.class})
    @ApiModelProperty(value = "代号")
    private String itemId;

    @NotBlank(message = "左对象对象类型不能为空",groups = {AddGroup.class})
    @ApiModelProperty(value = "左对象对象类型")
    private String leftObjectType;

    @NotBlank(message = "左对象不能为空",groups = {AddGroup.class})
    @ApiModelProperty(value = "左对象对象id")
    private String leftObject;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ToolRevisionReq that = (ToolRevisionReq) o;
        return uid.equals(that.uid) &&
                files.equals(that.files) &&
                partNo.equals(that.partNo) &&
                objectName.equals(that.objectName) &&
                itemId.equals(that.itemId) &&
                leftObjectType.equals(that.leftObjectType) &&
                leftObject.equals(that.leftObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), uid, files, partNo, objectName, itemId, leftObjectType, leftObject);
    }
}