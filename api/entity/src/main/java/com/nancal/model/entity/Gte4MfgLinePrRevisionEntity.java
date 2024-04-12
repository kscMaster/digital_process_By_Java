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
@Entity(name = "gte4mfg_line_pr_revision")
@ApiModel(value = "Gte4MfgLinePrRevision 线体工艺版本 的Entity")
public class Gte4MfgLinePrRevisionEntity extends ItemRevisionEntity implements Serializable{

    public static final String GTE4THE_DIRECTION = "gte4TheDirection";

    public static final String GTE4PROCESS_CLASSIFICATION = "gte4ProcessClassification";

    public static final String GTE4PANEL_MODEL = "gte4PanelModel";

    @ApiModelProperty(value="过板方向",required=false)
    @Column(name = "gte4the_direction", length = 128)
    private String gte4TheDirection;

    @ApiModelProperty(value="工序分类",required=false)
    @Column(name = "gte4process_classification", length = 128)
    private String gte4ProcessClassification;

    @ApiModelProperty(value="工艺拼板模式",required=false)
    @Column(name = "gte4panel_model", length = 128)
    private String gte4PanelModel;

    @Override
    public String getObjectType(){
        return "Gte4MfgLinePrRevision";
    }

}