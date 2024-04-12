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
@Entity(name = "gte4cleanup_machining")
@ApiModel(value = "Gte4CleanupMachining 补加工记录 的Entity")
public  class Gte4CleanupMachiningEntity  extends FormEntity  implements Serializable{
    @ApiModelProperty(value="图号",required=false)
    @Column(name = "gte4part_no", length = 64)
    private String gte4PartNo;
    @ApiModelProperty(value="批次号",required=false)
    @Column(name = "gte4part_bn", length = 64)
    private String gte4PartBn;
    @ApiModelProperty(value="顺序号",required=false)
    @Column(name = "gte4sn", length = 64)
    private String gte4Sn;
    @ApiModelProperty(value="操作单位",required=false)
    @Column(name = "gte4op_org", length = 128)
    private String gte4OpOrg;
    @ApiModelProperty(value="操作人",required=false)
    @Column(name = "gte4op_person", length = 128)
    private String gte4OpPerson;
    @ApiModelProperty(value="操作日期",required=false,example="1994-03-07")
    @Column(name = "gte4op_date", length = 128)
    private LocalDate gte4OpDate;
    @ApiModelProperty(value="关联技术文件",required=false)
    @Column(name = "gte4tech_file", length = 128)
    private String gte4TechFile;
    @ApiModelProperty(value="技术文件号",required=false)
    @Column(name = "gte4tech_no", length = 64)
    private String gte4TechNo;
    @ApiModelProperty(value="文件类型",required=false)
    @Column(name = "gte4file_type", length = 64)
    private String gte4FileType;
    @ApiModelProperty(value="初始型号",required=false)
    @Column(name = "gte4init_model", length = 64)
    private String gte4InitModel;
    @ApiModelProperty(value="阶段标识",required=false)
    @Column(name = "gte4phase", length = 64)
    private String gte4Phase;
    @ApiModelProperty(value="批次",required=false)
    @Column(name = "gte4bn", length = 64)
    private String gte4Bn;
    @ApiModelProperty(value="设计者",required=false)
    @Column(name = "gte4designer", length = 128)
    private String gte4Designer;
}