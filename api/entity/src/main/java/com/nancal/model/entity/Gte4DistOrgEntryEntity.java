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
@Entity(name = "gte4dist_org_entry")
@ApiModel(value = "Gte4DistOrgEntry 发放单位信息 的Entity")
public class Gte4DistOrgEntryEntity extends WorkspaceObjectEntity implements Serializable{

    public static final String GTE4DISTRI_ORG = "gte4DistriOrg";

    public static final String GTE4DISTRI_ORG_ID = "gte4DistriOrgId";

    public static final String GTE4ORG_RESPONSIBLE_NAME = "gte4OrgResponsibleName";

    public static final String GTE4ORG_RESPONSIBLE_ID = "gte4OrgResponsibleId";

    public static final String GTE4RESPONSIBLE_NAME = "gte4ResponsibleName";

    public static final String GTE4RESPONSIBLE_ID = "gte4ResponsibleId";

    public static final String GTE4WIP_SUGGESTION = "gte4WipSuggestion";

    public static final String GTE4PROCESSED_SUGGESTION = "gte4ProcessedSuggestion";

    public static final String GTE4CHANGE_OPINION = "gte4ChangeOpinion";

    public static final String GTE4EFFECTIVE_DATE = "gte4EffectiveDate";

    public static final String GTE4NOTICE_DATE = "gte4NoticeDate";

    @ApiModelProperty(value="发放单位",required=false)
    @Column(name = "gte4distri_org", length = 128)
    private String gte4DistriOrg;

    @ApiModelProperty(value="发放单位ID",required=false)
    @Column(name = "gte4distri_org_id", length = 64)
    private String gte4DistriOrgId;

    @ApiModelProperty(value="单位责任人",required=false)
    @Column(name = "gte4org_responsible_name", length = 128)
    private String gte4OrgResponsibleName;

    @ApiModelProperty(value="单位责任人ID",required=false)
    @Column(name = "gte4org_responsible_id", length = 64)
    private String gte4OrgResponsibleId;

    @ApiModelProperty(value="责任人",required=false)
    @Column(name = "gte4responsible_name", length = 128)
    private String gte4ResponsibleName;

    @ApiModelProperty(value="责任人ID",required=false)
    @Column(name = "gte4responsible_id", length = 64)
    private String gte4ResponsibleId;

    @ApiModelProperty(value="在制品处理意见",required=false)
    @Column(name = "gte4wip_suggestion", length = 1024)
    private String gte4WipSuggestion;

    @ApiModelProperty(value="已制品处理意见",required=false)
    @Column(name = "gte4processed_suggestion", length = 1024)
    private String gte4ProcessedSuggestion;

    @ApiModelProperty(value="更改意见",required=false)
    @Column(name = "gte4change_opinion", length = 1024)
    private String gte4ChangeOpinion;

    @ApiModelProperty(value="生效时间",required=false,example="1994-03-07")
    @Column(name = "gte4effective_date", length = 0)
    private LocalDateTime gte4EffectiveDate;

    @ApiModelProperty(value="通知时间",required=false,example="1994-03-07")
    @Column(name = "gte4notice_date", length = 0)
    private LocalDateTime gte4NoticeDate;

    public String getObjectType(){
        return "Gte4DistOrgEntry";
    }

}