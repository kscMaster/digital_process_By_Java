package com.nancal.api.model;

import com.nancal.api.utils.FieldAlias;
import com.nancal.common.annotation.OrderField;
import com.nancal.common.annotation.QueryField;
import com.querydsl.core.types.Order;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "WorkspaceObject 工作对象 的请求VO")
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public abstract class WorkspaceObjectReq extends BusinessObjectReq {

    @ApiModelProperty(value="创建日期",required=false,example="1994-03-07")
    private LocalDateTime creationDate;

    @ApiModelProperty(value="创建用户ID",required=false)
    @Length(max = 64)
    private String creationUserId;

    @ApiModelProperty(value="创建用户姓名",required=false)
    @Length(max = 128)
    private String creationUsername;

    @OrderField(Order.DESC)
    @ApiModelProperty(value="最后修改日期",required=false,example="1994-03-07")
    private LocalDateTime lastUpdate;

    @ApiModelProperty(value="最后修改用户ID",required=false)
    @Length(max = 64)
    private String lastUpdateUserId;

    @ApiModelProperty(value="最后修改用户名",required=false)
    @Length(max = 128)
    private String lastUpdateUsername;

    @ApiModelProperty(value="所有者用户ID",required=false)
    @Length(max = 64)
    private String ownerId;

    @ApiModelProperty(value="所有者用户名",required=false)
    @Length(max = 128)
    private String ownerName;

    @ApiModelProperty(value="对象名称",required=false)
    @Length(max = 128)
    private String objectName;

    @ApiModelProperty(value="描述",required=false)
    @Length(max = 1024)
    private String objectDesc;

    @ApiModelProperty(value="对象类型",required=false)
    @Length(max = 64)
    @QueryField
    private String objectType;

    @ApiModelProperty(value="生命周期状态",required=false)
    @Length(max = 64)
    private String lifeCycleState;

    @ApiModelProperty(value="生命周期变更日期",required=false,example="1994-03-07")
    private LocalDateTime stateChangeDate;

    @ApiModelProperty(value="备注",required=false)
    @Length(max = 512)
    @FieldAlias(name = "备注",max = 512)
    private String remark;

    @ApiModelProperty(value="密级",required=false)
    @Length(max = 128)
    private String secretLevel;

    @ApiModelProperty(value="保密期限",required=false)
    @Length(max = 128)
    private String secretTerm;

    @ApiModelProperty(value = "左对象对象类型")
    private String leftObjectType;

    @ApiModelProperty(value = "左对象对象id")
    private String leftObject;

    private String relationId;

}