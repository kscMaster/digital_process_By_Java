package com.nancal.api.model;

import cn.hutool.core.util.StrUtil;
import com.nancal.api.model.dataset.FileAttrReq;
import com.nancal.common.annotation.QueryField;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.LikeGroup;
import com.nancal.common.base.UpdateGroup;
import com.nancal.model.entity.Gte4InspectionRevisionEntity;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@ApiModel(value = "Gte4InspectionRevision 检验项目版本 的请求")
@Data

@NoArgsConstructor
@AllArgsConstructor
public class Gte4InspectionRevisionReq extends ItemRevisionReq  implements Serializable{

    @NotBlank(message = "id不能为空",groups = {UpdateGroup.class})
    @Length(max = 128, message = "id超过了最大长度限制",groups = {UpdateGroup.class})
    @ApiModelProperty(value = "主键id", required = true)
    private String uid;

    @NotBlank(message = "左对象id不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "左对象id")
    private String leftObject;

    @NotBlank(message = "左对象类型不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "左对象类型")
    private String leftObjectType;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="代号",required=false)
    @Length(max = 64,message = "代号超过最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String gte4ID;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="名称",required=false)
    @Length(max = 128,message = "代号超过最大长度限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String objectName;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="测量类型",required=false)
    @Length(max = 64,message = "测量类型超过最大长度限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4MeasType;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="所属组件图号",required=false)
    @Length(max = 128,message = "所属组件图号长度超过最大限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4BelongItemId;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="所属组件名称",required=false)
    @Length(max = 128,message = "所属组件名称长度超过最大限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4Name;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="测量方式",required=false)
    @Length(max = 128,message = "测量方式长度超过最大限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4MeasMthd;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="设计许可值",required=false)
    @Length(max = 128,message = "设计许可值长度超过最大限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4LicValue;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="设计许可值上限",required=false)
    @Length(max = 128,message = "设计许可值上限长度超过最大限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4LicValueLimt;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="设计许可值下限",required=false)
    @Length(max = 128,message = "设计许可值下限长度超过最大限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4LicValueLower;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="单位",required=false)
    @Length(max = 128,message = "单位长度超过最大限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4Org;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="所属组件",required=false)
    @Length(max = 128,message = "所属组件长度超过最大限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4Belong;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="许可值文件号",required=false)
    @Length(max = 64,message = "许可值文件号长度超过最大限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4LicFile;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="许可值文件版本",required=false)
    @Length(max = 64,message = "许可值文件版本长度超过最大限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4LicFileVer;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="附件",required=false)
    @Length(max = 128,message = "附件长度超过最大限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4Attaches;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="测量要求",required=false)
    @Length(max = 128,message = "测量要求超过最大限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4MeasReq;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="测量数量",required=false)
    @Length(max = 128,message = "测量数量超过最大限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4MeasValue;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="测量值计算要求",required=false)
    @Length(max = 128,message = "测量值计算要求超过最大限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4MeasCalcReq;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="本次装配是否需要测量",required=false)
    private Boolean gte4NeedMeas;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="设计输入文件号",required=false)
    @Length(max = 64,message = "设计输入文件号超过最大限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4DesignFileNo;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="设计输入文件版本号",required=false)
    @Length(max = 64,message = "设计输入文件版本号超过最大限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4DesignFileVer;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="测量日期",required=false,example="1994-03-07")
    private LocalDate gte4MeasDate;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="测量结果",required=false)
    @Length(max = 128,message = "测量结果超过最大限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4MeasR;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="是否合格",required=false)
    @Length(max = 128,message = "是否合格超过最大限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4Pass;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="施工流程卡编号",required=false)
    @Length(max = 64,message = "施工流程卡编号超过了最大长度限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String gte4CnstrProcessId;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value = "备注")
    @Length(max = 512,message = "备注超过了最大长度限制",groups = {AddGroup.class, UpdateGroup.class,LikeGroup.class})
    private String remark;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "itemId不能为空",groups = {UpdateGroup.class})
    @Length(max = 128, message = "itemId超过了最大长度限制",groups = {UpdateGroup.class,LikeGroup.class})
    @ApiModelProperty(value = "零组件号", required = true)
    private String itemId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Gte4InspectionRevisionReq that = (Gte4InspectionRevisionReq) o;
        return uid.equals(that.uid) &&
                leftObject.equals(that.leftObject) &&
                leftObjectType.equals(that.leftObjectType) &&
                gte4ID.equals(that.gte4ID) &&
                objectName.equals(that.objectName) &&
                gte4MeasType.equals(that.gte4MeasType) &&
                gte4BelongItemId.equals(that.gte4BelongItemId) &&
                gte4Name.equals(that.gte4Name) &&
                gte4MeasMthd.equals(that.gte4MeasMthd) &&
                gte4LicValue.equals(that.gte4LicValue) &&
                gte4LicValueLimt.equals(that.gte4LicValueLimt) &&
                gte4LicValueLower.equals(that.gte4LicValueLower) &&
                gte4Org.equals(that.gte4Org) &&
                gte4Belong.equals(that.gte4Belong) &&
                gte4LicFile.equals(that.gte4LicFile) &&
                gte4LicFileVer.equals(that.gte4LicFileVer) &&
                gte4Attaches.equals(that.gte4Attaches) &&
                gte4MeasReq.equals(that.gte4MeasReq) &&
                gte4MeasValue.equals(that.gte4MeasValue) &&
                gte4MeasCalcReq.equals(that.gte4MeasCalcReq) &&
                gte4NeedMeas.equals(that.gte4NeedMeas) &&
                gte4DesignFileNo.equals(that.gte4DesignFileNo) &&
                gte4DesignFileVer.equals(that.gte4DesignFileVer) &&
                gte4MeasDate.equals(that.gte4MeasDate) &&
                gte4MeasR.equals(that.gte4MeasR) &&
                gte4Pass.equals(that.gte4Pass) &&
                gte4CnstrProcessId.equals(that.gte4CnstrProcessId) &&
                remark.equals(that.remark) &&
                itemId.equals(that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), uid, leftObject, leftObjectType, gte4ID, objectName, gte4MeasType, gte4BelongItemId, gte4Name, gte4MeasMthd, gte4LicValue, gte4LicValueLimt, gte4LicValueLower, gte4Org, gte4Belong, gte4LicFile, gte4LicFileVer, gte4Attaches, gte4MeasReq, gte4MeasValue, gte4MeasCalcReq, gte4NeedMeas, gte4DesignFileNo, gte4DesignFileVer, gte4MeasDate, gte4MeasR, gte4Pass, gte4CnstrProcessId, remark, itemId);
    }
}