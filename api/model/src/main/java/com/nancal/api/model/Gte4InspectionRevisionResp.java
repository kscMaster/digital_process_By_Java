package com.nancal.api.model;


import com.nancal.api.utils.FieldAlias;
import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;


@ApiModel(value = "Gte4InspectionRevision 检验项目版本 的响应")
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Gte4InspectionRevisionResp extends ItemRevisionResp  implements Serializable{
//    @ApiModelProperty(value="代号",required=false)
//    private String gte4ID;
//    @ApiModelProperty(value="名称",required=false)
//    private String gte4Name;
//    @ApiModelProperty(value="测量方式",required=false)
//    private String gte4MeasMthd;
//    @ApiModelProperty(value="设计许可值",required=false)
//    private String gte4LicValue;
//    @ApiModelProperty(value="设计许可值上限",required=false)
//    private String gte4LicValueLimt;
//    @ApiModelProperty(value="设计许可值下限",required=false)
//    private String gte4LicValueLower;
//    @ApiModelProperty(value="单位",required=false)
//    private String gte4Org;
//    @ApiModelProperty(value="所属组件",required=false)
//    private String gte4Belong;
//    @ApiModelProperty(value="许可值文件号",required=false)
//    private String gte4LicFile;
//    @ApiModelProperty(value="许可值文件版本",required=false)
//    private String gte4LicFileVer;
//    @ApiModelProperty(value="附件",required=false)
//    private String gte4Attaches;
//    @ApiModelProperty(value="测量要求",required=false)
//    private String gte4MeasReq;
//    @ApiModelProperty(value="测量数量",required=false)
//    private String gte4MeasValue;
//    @ApiModelProperty(value="测量值计算要求",required=false)
//    private String gte4MeasCalcReq;
//    @ApiModelProperty(value="本次装配是否需要测量",required=false)
//    private Boolean gte4NeedMeas;
//    @ApiModelProperty(value="设计输入文件号",required=false)
//    private String gte4DesignFileNo;
//    @ApiModelProperty(value="设计输入文件版本号",required=false)
//    private String gte4DesignFileVer;
//    @ApiModelProperty(value="测量日期",required=false,example="1994-03-07")
//    private LocalDate gte4MeasDate;
//    @ApiModelProperty(value="测量结果",required=false)
//    private String gte4MeasR;
//    @ApiModelProperty(value="是否合格",required=false)
//    private String gte4Pass;
//    @ApiModelProperty(value="施工流程卡编号",required=false)
//    private String gte4CnstrProcessId;
//    @ApiModelProperty(value="附件",required=false)
//    private List<FileAttrResp> files;


    @ApiModelProperty(value = "主键id", required = true)
    private String uid;


    @ApiModelProperty(value = "左对象id")
    private String leftObject;


    @ApiModelProperty(value = "左对象类型")
    private String leftObjectType;

    @ApiModelProperty(value="代号",required=false)
    @FieldAlias(name = "代号",max = 64)
    private String gte4ID;

    @ApiModelProperty(value="名称",required=false)
    @FieldAlias(name = "名称",required = true,max = 128)
    private String objectName;

    @JsonDict(DictConstant.GTE4_MEAS_TYPE)
    @ApiModelProperty(value="测量类型",required=false)
    @FieldAlias(name = "*测试类型",max = 64)
    private String gte4MeasType;

    @ApiModelProperty(value="所属组件图号",required=false)
    @FieldAlias(name = "所属组件图号",max = 128)
    private String gte4BelongItemId;

    @ApiModelProperty(value="所属组件名称",required=false)
    @FieldAlias(name = "所属组件名称",max = 128)
    private String gte4Name;

    @JsonDict(DictConstant.GTE4_MEAS_MTHD)
    @ApiModelProperty(value="测量方式",required=false)
    @FieldAlias(name = "*测试方式",max = 128)
    private String gte4MeasMthd;

    @ApiModelProperty(value="设计许可值",required=false)
    @FieldAlias(name = "设计许可值",max = 128)
    private String gte4LicValue;

    @FieldAlias(name = "设计许可值上限",max = 128)
    @ApiModelProperty(value="设计许可值上限",required=false)

    private String gte4LicValueLimt;

    @ApiModelProperty(value="设计许可值下限",required=false)
    @FieldAlias(name = "设计许可值下限",max = 128)
    private String gte4LicValueLower;

    @ApiModelProperty(value="单位",required=false)
    @FieldAlias(name = "*单位",max = 128)
    private String gte4Org;

    @ApiModelProperty(value="所属组件",required=false)
    @FieldAlias(name = "所属组件",max = 128)
    private String gte4Belong;

    @ApiModelProperty(value="许可值文件号",required=false)
    @FieldAlias(name = "许可值文件号",max = 64)
    private String gte4LicFile;

    @ApiModelProperty(value="许可值文件版本",required=false)
    @FieldAlias(name = "许可值文件版本",max = 64)
    private String gte4LicFileVer;

    @ApiModelProperty(value="附件",required=false)
    @FieldAlias(name = "附件",max = 128)
    private String gte4Attaches;

    @ApiModelProperty(value="测量要求",required=false)
    @FieldAlias(name = "测量要求",max = 128)
    private String gte4MeasReq;

    @ApiModelProperty(value="测量数量",required=false)
    @FieldAlias(name = "测量数量",max = 128)
    private String gte4MeasValue;

    @JsonDict(DictConstant.GTE4_MEAS_CALC_REQ)
    @ApiModelProperty(value="测量值计算要求",required=false)
    @FieldAlias(name = "*测量值计算要求",max = 128)
    private String gte4MeasCalcReq;

    @ApiModelProperty(value="本次装配是否需要测量",required=false)
    private Boolean gte4NeedMeas;

    @ApiModelProperty(value="设计输入文件号",required=false)

    private String gte4DesignFileNo;

    @ApiModelProperty(value="设计输入文件版本号",required=false)

    private String gte4DesignFileVer;

    @ApiModelProperty(value="测量日期",required=false,example="1994-03-07")
    private LocalDate gte4MeasDate;

    @ApiModelProperty(value="测量结果",required=false)

    private String gte4MeasR;

    @ApiModelProperty(value="是否合格",required=false)

    private String gte4Pass;


    @ApiModelProperty(value="施工流程卡编号",required=false)

    private String gte4CnstrProcessId;

    @ApiModelProperty(value = "备注")
    @FieldAlias(name = "备注",max = 512)
    private String remark;



    @ApiModelProperty(value = "零组件号", required = true)
    @FieldAlias(name = "ID",max = 128)
    private String itemId;
}