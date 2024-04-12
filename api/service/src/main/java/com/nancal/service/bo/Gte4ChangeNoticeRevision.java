package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "Gte4ChangeNoticeRevision 技术通知版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4ChangeNoticeRevision extends ItemRevision  implements Serializable{

    @ApiModelProperty(value="技术通知单号",required=false)
    private String gte4TechNoticeNo;

    @ApiModelProperty(value="型号",required=false)
    private String gte4ModelNo;

    @ApiModelProperty(value="图样或文件编号",required=false)
    private String gte4PartNo;

    @ApiModelProperty(value="图样或文件名称",required=false)
    private String gte4PartName;

    @ApiModelProperty(value="采用原因",required=false)
    private String gte4Reson;

    @ApiModelProperty(value="生效日期",required=false)
    private String gte4EffDate;

    @ApiModelProperty(value="偏离类别",required=false)
    private String gte4DevCat;

    @ApiModelProperty(value="使用范围或有效时间",required=false)
    private String gte4Scope;

    @ApiModelProperty(value="设计者",required=false)
    private String gte4Designer;

}