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
@Entity(name = "gte4inspection_revision")
@ApiModel(value = "Gte4InspectionRevision 检验项目版本 的Entity")
public  class Gte4InspectionRevisionEntity  extends ItemRevisionEntity  implements Serializable{
    @ApiModelProperty(value="代号",required=false)
    @Column(name = "gte4id", length = 64)
    private String gte4ID;
    @ApiModelProperty(value="名称",required=false)
    @Column(name = "gte4name", length = 128)
    private String gte4Name;
    @ApiModelProperty(value="测量方式",required=false)
    @Column(name = "gte4meas_mthd", length = 128)
    private String gte4MeasMthd;
    @ApiModelProperty(value="设计许可值",required=false)
    @Column(name = "gte4lic_value", length = 128)
    private String gte4LicValue;
    @ApiModelProperty(value="设计许可值上限",required=false)
    @Column(name = "gte4lic_value_limt", length = 128)
    private String gte4LicValueLimt;
    @ApiModelProperty(value="设计许可值下限",required=false)
    @Column(name = "gte4lic_value_lower", length = 128)
    private String gte4LicValueLower;
    @ApiModelProperty(value="单位",required=false)
    @Column(name = "gte4org", length = 128)
    private String gte4Org;
    @ApiModelProperty(value="所属组件图号",required=false)
    @Column(name = "gte4belong_item_id", length = 128)
    private String gte4BelongItemId;
    @ApiModelProperty(value="许可值文件号",required=false)
    @Column(name = "gte4lic_file", length = 64)
    private String gte4LicFile;
    @ApiModelProperty(value="许可值文件版本",required=false)
    @Column(name = "gte4lic_file_ver", length = 64)
    private String gte4LicFileVer;
    @ApiModelProperty(value="附件",required=false)
    @Column(name = "gte4attaches", length = 128)
    private String gte4Attaches;
    @ApiModelProperty(value="测量要求",required=false)
    @Column(name = "gte4meas_req", length = 128)
    private String gte4MeasReq;
    @ApiModelProperty(value="测量数量",required=false)
    @Column(name = "gte4meas_value", length = 128)
    private String gte4MeasValue;
    @ApiModelProperty(value="测量值计算要求",required=false)
    @Column(name = "gte4meas_calc_req", length = 128)
    private String gte4MeasCalcReq;
    @ApiModelProperty(value="测量类型",required=false)
    @Column(name = "gte4meas_type", length = 64)
    private String gte4MeasType;
    @ApiModelProperty(value="所属文件图号",required=false)
    @Column(name = "gte4belong_file_no", length = 64)
    private String gte4BelongFileNo;

    public String getObjectType() {
        return "Gte4InspectionRevision";
    }


}