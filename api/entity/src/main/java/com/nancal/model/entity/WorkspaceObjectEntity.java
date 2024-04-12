package com.nancal.model.entity;

import com.nancal.model.entity.base.User;
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
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@ApiModel(value = "WorkspaceObject 工作对象 的Entity")
public  abstract class WorkspaceObjectEntity  extends BusinessObjectEntity  implements Serializable{
    @CreatedDate
    @ApiModelProperty(value="创建日期",required=false,example="1994-03-07")
    @Column(name = "creation_date", length = 64)
    private LocalDateTime creationDate;
    @LastModifiedDate
    @ApiModelProperty(value="最后修改日期",required=false,example="1994-03-07")
    @Column(name = "last_update", length = 0)
    private LocalDateTime lastUpdate;

    @CreatedBy
    @LastModifiedBy
    @Embedded
    protected User user;

    @ApiModelProperty(value="所有者用户ID",required=false)
    @Column(name = "owner_id", length = 64)
    private String ownerId;
    @ApiModelProperty(value="所有者用户名",required=false)
    @Column(name = "owner_name", length = 128)
    private String ownerName;
    @ApiModelProperty(value="对象名称",required=false)
    @Column(name = "object_name", length = 128)
    private String objectName;
    @ApiModelProperty(value="描述",required=false)
    @Column(name = "object_desc", length = 1024)
    private String objectDesc;
    @ApiModelProperty(value="对象类型",required=false)
    @Column(name = "object_type", length = 64)
    private String objectType;
    @ApiModelProperty(value="生命周期状态",required=false)
    @Column(name = "life_cycle_state", length = 64)
    private String lifeCycleState;
    @ApiModelProperty(value="生命周期变更日期",required=false,example="1994-03-07")
    @Column(name = "state_change_date", length = 0)
    private LocalDateTime stateChangeDate;
    @ApiModelProperty(value="备注",required=false)
    @Column(name = "remark", length = 512)
    private String remark;
    @ApiModelProperty(value="密级",required=false)
    @Column(name = "secret_level", length = 128)
    private String secretLevel;
    @ApiModelProperty(value="保密期限",required=false)
    @Column(name = "secret_term", length = 128)
    private String secretTerm;

    @ApiModelProperty("左对象")
    @Transient
    private String leftObject;


    @ApiModelProperty("左对象类型")
    @Transient
    private String leftObjectType;

    //TODO 临时解决方案
    @Transient
    private String relationId;


    public static final String OWNER_ID = "ownerId";
    public static final String CREATION_DATE = "creationDate";
    public static final String CREATION_USER_ID = "creationUserId";
    public static final String LIFE_CYCLE_STATE = "lifeCycleState";
    public static final String OBJECT_NAME = "objectName";

    public static final String OBJECT_TYPE = "objectType";
    public static final String REMARK = "remark";

}