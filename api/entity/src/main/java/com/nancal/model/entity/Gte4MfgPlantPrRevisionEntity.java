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
@Entity(name = "gte4mfg_plant_pr_revision")
@ApiModel(value = "Gte4MfgPlantPrRevision 工厂工艺版本 的Entity")
public class Gte4MfgPlantPrRevisionEntity extends ItemRevisionEntity implements Serializable{

    public static final String GTE4LEAD_FREE = "gte4LeadFree";

    public static final String GTE4ELECTROSTATIC_PROTECTION = "gte4ELectrostaticProtection";

    public static final String GTE4DUST_RATING = "gte4DustRating";

    public static final String GTE4PCBSIZE_LENGTH = "gte4PCBSizeLength";

    public static final String GTE4PCBSIZE_WIDTH = "gte4PCBSizeWidth";

    public static final String GTE4PCBSIZE_HEIGTH = "gte4PCBSizeHeigth";

    @ApiModelProperty(value="是否无铅加工",required=false)
    @Column(name = "gte4lead_free", length = 0)
    private Boolean gte4LeadFree;

    @ApiModelProperty(value="静电防护等级",required=false)
    @Column(name = "gte4electrostatic_protection", length = 64)
    private String gte4ELectrostaticProtection;

    @ApiModelProperty(value="防尘等级",required=false)
    @Column(name = "gte4dust_rating", length = 64)
    private String gte4DustRating;

    @ApiModelProperty(value="PCB尺寸-长",required=false)
    @Column(name = "gte4pcbsize_length", length = 0)
    private Double gte4PCBSizeLength;

    @ApiModelProperty(value="PCB尺寸-宽",required=false)
    @Column(name = "gte4pcbsize_width", length = 0)
    private Double gte4PCBSizeWidth;

    @ApiModelProperty(value="PCB尺寸-高",required=false)
    @Column(name = "gte4pcbsize_heigth", length = 0)
    private Double gte4PCBSizeHeigth;

    @Override
    public String getObjectType(){
        return "Gte4MfgPlantPrRevision";
    }

}