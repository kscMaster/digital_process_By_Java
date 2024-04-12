package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.io.Serializable;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "gte4mfg_operation_revision")
@ApiModel(value = "Gte4MfgOperationRevision 工序版本 的Entity")
public class Gte4MfgOperationRevisionEntity extends MfgOperationRevisionEntity implements Serializable{

    public static final String GTE4COOP_ORG = "gte4CoopOrg";

    public static final String GTE4ACTION_DESCRIPTION = "gte4ActionDescription";

    public static final String GTE4EXAMINE_POINTS = "gte4ExaminePoints";

    public static final String GTE4PROCESS_REQ = "gte4ProcessReq";

    public static final String GTE4SAMPLING_RATIO = "gte4SamplingRatio";

    public static final String GTE4TEST_POINTS = "gte4TestPoints";

    public static final String GTE4TASK_TIME = "gte4TaskTime";

    @ApiModelProperty(value="协作单位",required=false)
    @Column(name = "gte4coop_org", length = 128)
    private String gte4CoopOrg;

    @ApiModelProperty(value="工艺描述",required=false)
    @Column(name = "gte4action_description", length = 128)
    private String gte4ActionDescription;

    @ApiModelProperty(value="检测要点",required=false)
    @Column(name = "gte4examine_points", length = 1024)
    private String gte4ExaminePoints;

    @ApiModelProperty(value="工艺要求/作业要领",required=false)
    @Column(name = "gte4process_req", length = 255)
    private String gte4ProcessReq;

    @ApiModelProperty(value="抽检比例",required=false)
    @Column(name = "gte4sampling_ratio", length = 0)
    private Double gte4SamplingRatio;

    @ApiModelProperty(value="测试要点",required=false)
    @Column(name = "gte4test_points", length = 255)
    private String gte4TestPoints;

    @ApiModelProperty(value="工时(S)",required=false)
    @Column(name = "gte4task_time", length = 0)
    private Integer gte4TaskTime;

    @ApiModelProperty(value="工艺能力组",required=false)
    @Column(name = "gte4action_capability_group", length = 255)
    private String gte4ActionCapabilityGroup;

    @ApiModelProperty(value="工艺能力组名称",required=false)
    @Column(name = "gte4action_capability_group_name", length = 255)
    private String gte4ActionCapabilityGroupName;

    @ApiModelProperty(value="板面",required=false)
    @Column(name = "gte4board_surface", length = 255)
    private String gte4BoardSurface;

    @Override
    public String getObjectType(){
        return "Gte4MfgOperationRevision";
    }

}