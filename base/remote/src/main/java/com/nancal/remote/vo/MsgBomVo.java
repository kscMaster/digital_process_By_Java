package com.nancal.remote.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MsgBomVo implements Serializable {

    @ApiModelProperty(value="创建日期",required=false,example="1994-03-07")
    private LocalDateTime creationDate;
    @ApiModelProperty(value="最后修改日期",required=false,example="1994-03-07")
    private LocalDateTime lastUpdate;
    @ApiModelProperty(value="所有者用户ID",required=false)
    private String ownerId;
    @ApiModelProperty(value="所有者用户名",required=false)
    private String ownerName;
    @ApiModelProperty(value="对象名称",required=false)
    private String objectName;
    @ApiModelProperty(value="描述",required=false)
    private String objectDesc;
    @ApiModelProperty(value="对象类型",required=false)
    private String objectType;
    @ApiModelProperty(value="生命周期状态",required=false)
    private String lifeCycleState;
    @ApiModelProperty(value="生命周期变更日期",required=false,example="1994-03-07")
    private LocalDateTime stateChangeDate;
    @ApiModelProperty(value="备注",required=false)
    private String remark;
    @ApiModelProperty(value="密级",required=false)
    private String secretLevel;
    @ApiModelProperty(value="保密期限",required=false)
    private String secretTerm;
    @ApiModelProperty("左对象")
    private String leftObject;
    @ApiModelProperty("左对象类型")
    private String leftObjectType;
    private String relationId;
    @ApiModelProperty("右对象id")
    private String rightObject;

    @ApiModelProperty("右对象类型")
    private String rightObjectType;
}
