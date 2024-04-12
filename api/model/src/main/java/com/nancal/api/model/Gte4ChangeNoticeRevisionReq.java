package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


@ApiModel(value = "Gte4ChangeNoticeRevision 技术通知版本 的请求")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gte4ChangeNoticeRevisionReq extends ItemRevisionReq  implements Serializable{

    @ApiModelProperty(value="技术通知单号",required=false)
    @Length(max = 64)
    private String gte4TechNoticeNo;

    @ApiModelProperty(value="型号",required=false)
    @Length(max = 64)
    private String gte4ModelNo;

    @ApiModelProperty(value="图样或文件编号",required=false)
    @Length(max = 64)
    private String gte4PartNo;

    @ApiModelProperty(value="图样或文件名称",required=false)
    @Length(max = 128)
    private String gte4PartName;

    @ApiModelProperty(value="采用原因",required=false)
    @Length(max = 128)
    private String gte4Reson;

    @ApiModelProperty(value="生效日期",required=false)
    @Length(max = 128)
    private String gte4EffDate;

    @ApiModelProperty(value="偏离类别",required=false)
    @Length(max = 128)
    private String gte4DevCat;

    @ApiModelProperty(value="使用范围或有效时间",required=false)
    @Length(max = 128)
    private String gte4Scope;

    @ApiModelProperty(value="设计者",required=false)
    @Length(max = 128)
    private String gte4Designer;
}