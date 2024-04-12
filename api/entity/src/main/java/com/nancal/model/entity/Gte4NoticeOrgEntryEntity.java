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
@Entity(name = "gte4notice_org_entry")
@ApiModel(value = "Gte4NoticeOrgEntry 通知单位信息 的Entity")
public class Gte4NoticeOrgEntryEntity extends WorkspaceObjectEntity implements Serializable{

    public static final String GTE4NOTICE_ORG = "gte4NoticeOrg";

    public static final String GTE4NOTICE_ORG_ID = "gte4NoticeOrgId";

    public static final String GTE4NOTICE_DATE = "gte4NoticeDate";

    public static final String GTE4CHECK = "gte4Check";

    public static final String GTE4CHECK_TIME = "gte4CheckTime";

    @ApiModelProperty(value="通知单位",required=false)
    @Column(name = "gte4notice_org", length = 128)
    private String gte4NoticeOrg;

    @ApiModelProperty(value="通知单位ID",required=false)
    @Column(name = "gte4notice_org_id", length = 64)
    private String gte4NoticeOrgId;

    @ApiModelProperty(value="通知时间",required=false,example="1994-03-07")
    @Column(name = "gte4notice_date", length = 0)
    private LocalDateTime gte4NoticeDate;

    @ApiModelProperty(value="已阅",required=false)
    @Column(name = "gte4check", length = 0)
    private Boolean gte4Check;

    @ApiModelProperty(value="已阅时间",required=false,example="1994-03-07")
    @Column(name = "gte4check_time", length = 0)
    private LocalDateTime gte4CheckTime;

    public String getObjectType(){
        return "Gte4NoticeOrgEntry";
    }

}