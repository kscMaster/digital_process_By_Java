package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "Gte4InspectionRevision 检验项目版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4InspectionRevision extends ItemRevision  implements Serializable{

    @ApiModelProperty(value="代号",required=false)
    private String gte4ID;

    @ApiModelProperty(value="名称",required=false)
    private String gte4Name;

    @ApiModelProperty(value="测量方式",required=false)
    private String gte4MeasMthd;

    @ApiModelProperty(value="设计许可值",required=false)
    private String gte4LicValue;

    @ApiModelProperty(value="设计许可值上限",required=false)
    private String gte4LicValueLimt;

    @ApiModelProperty(value="设计许可值下限",required=false)
    private String gte4LicValueLower;

    @ApiModelProperty(value="单位",required=false)
    private String gte4Org;

    @ApiModelProperty(value="所属组件图号",required=false)
    private String gte4BelongItemId;

    @ApiModelProperty(value="许可值文件号",required=false)
    private String gte4LicFile;

    @ApiModelProperty(value="许可值文件版本",required=false)
    private String gte4LicFileVer;

    @ApiModelProperty(value="附件",required=false)
    private String gte4Attaches;

    @ApiModelProperty(value="测量要求",required=false)
    private String gte4MeasReq;

    @ApiModelProperty(value="测量数量",required=false)
    private String gte4MeasValue;

    @ApiModelProperty(value="测量值计算要求",required=false)
    private String gte4MeasCalcReq;

    @ApiModelProperty(value="测量类型",required=false)
    private String gte4MeasType;

    @ApiModelProperty(value="所属文件图号",required=false)
    private String gte4BelongFileNo;

}