package com.nancal.model.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Api("用来排产的工序表")
@Entity(name = "ga_operation")
public class EsopGaOperationEntity implements Serializable {

    @ApiModelProperty("主键id")
    @Id
    @Column(name = "id")
    private String id;

    @ApiModelProperty("线体名称")
    @Column(name = "LINENAME")
    private String lineName;

    @ApiModelProperty("工序编号")
    @Column(name = "OPNUM")
    private String opNum;

    @ApiModelProperty("'是否关键工序'")
    @Column(name = "FIXSTATION")
    private Integer fixStation;

    @ApiModelProperty("'工序时间,单位：秒'")
    @Column(name = "OPTIME")
    private Integer opTime;

    @ApiModelProperty("前一个工序的编号,前一个工序的opnum")
    @Column(name = "PREOP")
    private String preOp;

    @ApiModelProperty("任务id")
    @Column(name = "TASKID")
    private String taskId;

    @ApiModelProperty("工序的唯一标记")
    @Column(name = "OPUID")
    private String opUid;

    @ApiModelProperty("线体id")
    @Column(name = "LINEID")
    private String lineId;

    @ApiModelProperty("工序名称")
    @Column(name = "OPNAME")
    private String opName;

    @ApiModelProperty("工序流水号")
    @Column(name = "OPID")
    private String opId;

    @ApiModelProperty("资源id")
    @Column(name = "VOLUMEID")
    private Long volumeId;

    @ApiModelProperty("是否关键工序")
    @Column(name = "KEYOPERATION")
    private Integer keyOperation;

    @ApiModelProperty("注意事项")
    @Column(name = "ATTENTION")
    private String attention;

    @ApiModelProperty("工序描述")
    @Column(name = "OPDESC")
    private String opDesc;

    @Deprecated
    @ApiModelProperty("未用到")
    @Column(name = "PARENTID")
    private String parentId;

    @Deprecated
    @ApiModelProperty("未用到")
    @Column(name = "ISVIRTUAL")
    private Integer isVirtual;

    @Deprecated
    @ApiModelProperty("未用到")
    @Column(name = "MATCH_TYPE")
    private Integer matchType;
}
