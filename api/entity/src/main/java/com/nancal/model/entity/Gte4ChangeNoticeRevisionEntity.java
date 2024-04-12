package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "gte4change_notice_revision")
@ApiModel(value = "Gte4ChangeNoticeRevision 技术通知版本 的Entity")
public  class Gte4ChangeNoticeRevisionEntity  extends ItemRevisionEntity  implements Serializable{
    @ApiModelProperty(value="技术通知单号",required=false)
    @Column(name = "gte4tech_notice_no", length = 64)
    private String gte4TechNoticeNo;
    @ApiModelProperty(value="型号",required=false)
    @Column(name = "gte4model_no", length = 64)
    private String gte4ModelNo;
    @ApiModelProperty(value="图样或文件编号",required=false)
    @Column(name = "gte4part_no", length = 64)
    private String gte4PartNo;
    @ApiModelProperty(value="图样或文件名称",required=false)
    @Column(name = "gte4part_name", length = 128)
    private String gte4PartName;
    @ApiModelProperty(value="采用原因",required=false)
    @Column(name = "gte4reson", length = 128)
    private String gte4Reson;
    @ApiModelProperty(value="生效日期",required=false)
    @Column(name = "gte4eff_date", length = 128)
    private String gte4EffDate;
    @ApiModelProperty(value="偏离类别",required=false)
    @Column(name = "gte4dev_cat", length = 128)
    private String gte4DevCat;
    @ApiModelProperty(value="使用范围或有效时间",required=false)
    @Column(name = "gte4scope", length = 128)
    private String gte4Scope;
    @ApiModelProperty(value="设计者",required=false)
    @Column(name = "gte4designer", length = 128)
    private String gte4Designer;
}