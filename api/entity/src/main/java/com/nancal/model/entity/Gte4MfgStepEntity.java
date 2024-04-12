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
@Entity(name = "gte4mfg_step")
@ApiModel(value = "Gte4MfgStep 工步 的Entity")
public class Gte4MfgStepEntity extends MfgStepEntity implements Serializable{

    public static final String GTE4COOP_ORG = "gte4CoopOrg";

    public static final String GTE4TASK_TIME = "gte4TaskTime";

    public static final String GTE4FREQUENCY = "gte4Frequency";

    @ApiModelProperty(value="编号",required=false)
    @Column(name = "item_id", length = 64)
    private String itemId;

    @ApiModelProperty(value="协作单位",required=false)
    @Column(name = "gte4coop_org", length = 128)
    private String gte4CoopOrg;


    @ApiModelProperty(value="工步类型",required=true)
    @Column(name = "gte4_type", length = 64)
    private String gte4Type;

    @ApiModelProperty(value="执行标志",required=false)
    @Column(name = "is_execute", length = 0)
    private Boolean isExecute;

    @ApiModelProperty(value="工时(S)",required=false)
    @Column(name = "gte4task_time", length = 0)
    private Integer gte4TaskTime;

    @ApiModelProperty(value="频率",required=false)
    @Column(name = "gte4frequency", length = 0)
    private Double gte4Frequency;

    public String getObjectType(){
        return "Gte4MfgStep";
    }

}