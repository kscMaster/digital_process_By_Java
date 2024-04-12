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
@Entity(name = "gte4mfg_station_pr_revision")
@ApiModel(value = "Gte4MfgStationPrRevision 工位工艺版本 的Entity")
public class Gte4MfgStationPrRevisionEntity extends ItemRevisionEntity implements Serializable{

    public static final String GTE4CHANGE_RECORD = "gte4ChangeRecord";

    public static final String GTE4SPECIAL_CHARACTERISTICS = "gte4SpecialCharacteristics";

    public static final String GTE4EXCEPTION_CAUTIONS = "gte4ExceptionCautions";

    public static final String GTE4KEY_INFO_PROGRAM = "gte4KeyInfoProgram";

    public static final String GTE4KEY_PROCESS = "gte4KeyProcess";

    @ApiModelProperty(value="变更记录",required=false)
    @Column(name = "gte4change_record", length = 1024)
    private String gte4ChangeRecord;

    @ApiModelProperty(value="特殊特性",required=false)
    @Column(name = "gte4special_characteristics", length = 128)
    private String gte4SpecialCharacteristics;

    @ApiModelProperty(value="异常处理、注意事项、其他",required=false)
    @Column(name = "gte4exception_cautions", length = 1024)
    private String gte4ExceptionCautions;

    @ApiModelProperty(value="关键信息",required=false)
    @Column(name = "gte4key_info_program", length = 1024)
    private String gte4KeyInfoProgram;

    @ApiModelProperty(value="关键工序",required=false)
    @Column(name = "gte4key_process", length = 0)
    private Boolean gte4KeyProcess;

    @Override
    public String getObjectType(){
        return "Gte4MfgStationPrRevision";
    }

}