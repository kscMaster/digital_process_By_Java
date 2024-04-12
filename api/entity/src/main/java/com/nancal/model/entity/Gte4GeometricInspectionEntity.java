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
@Entity(name = "gte4geometric_inspection")
@ApiModel(value = "Gte4GeometricInspection 几何量检测委托单 的Entity")
public  class Gte4GeometricInspectionEntity  extends FormEntity  implements Serializable{
    @ApiModelProperty(value="型别",required=false)
    @Column(name = "gte4model_no", length = 64)
    private String gte4ModelNo;
    @ApiModelProperty(value="课题（任务）号",required=false)
    @Column(name = "gte4task_no", length = 64)
    private String gte4TaskNo;
    @ApiModelProperty(value="图号",required=false)
    @Column(name = "gte4part_no", length = 64)
    private String gte4PartNo;
    @ApiModelProperty(value="数量",required=false)
    @Column(name = "gte4quantity", length = 64)
    private String gte4Quantity;
    @ApiModelProperty(value="零件编号（计算机记录号）",required=false)
    @Column(name = "gte4item_id", length = 128)
    private String gte4ItemId;
    @ApiModelProperty(value="委托人电话",required=false)
    @Column(name = "gte4tel_no", length = 32)
    private String gte4TelNo;
    @ApiModelProperty(value="委托单位",required=false)
    @Column(name = "gte4group", length = 128)
    private String gte4Group;
}