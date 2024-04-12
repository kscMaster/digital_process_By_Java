package com.nancal.model.entity;

import cn.hutool.core.util.StrUtil;
import com.nancal.common.enums.TaskStatusEnum;
import com.nancal.common.utils.IdGeneratorUtil;
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
@Api("任务表")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "ga_task")
public class EsopGaTaskEntity implements Serializable {

    @ApiModelProperty("主键id")
    @Id
    @Column(name = "id")
    private String id;

    @ApiModelProperty("任务状态 0:NotStart(默认), 1:Start, 2:Error, 3:Success")
    @Column(name = "TASKSTATUS")
    private Integer taskStatus;

    @ApiModelProperty("每小时产量 = 3600 / 节拍(线上的瓶颈时间)")
    @Column(name = "UPH")
    private Double uph;

    @ApiModelProperty("线平衡率")
    @Column(name = "LINEBALANCERATE")
    private Double lineBalanceRate;

    @ApiModelProperty("节拍")
    @Column(name = "LINETACTTIME")
    private Double lineTactTime;

    @ApiModelProperty("任务日志")
    @Column(name = "TASKLOG")
    private String taskLog;

    @Deprecated
    @ApiModelProperty("tc用户id")
    @Column(name = "TCUSERID")
    private String tcUserId;

    @Deprecated
    @ApiModelProperty("tc密码")
    @Column(name = "TCPASSWORD")
    private String tcPassword;

    @Deprecated
    @ApiModelProperty("没用到")
    @Column(name = "FORMUID")
    private String formUid;

    @Deprecated
    @ApiModelProperty("期望节拍，未用到")
    @Column(name = "EXPECTEDTACTTIME")
    private Double expectedTactTime;

    @ApiModelProperty("0：非固定工位(默认)，1：固定工位")
    @Column(name = "FIXSTATION")
    private Integer fixstation;

    @ApiModelProperty("排产版本id")
    @Column(name = "REV_ID")
    private String revId;

    @ApiModelProperty("0:fromSOP(默认), 1:fromTC")
    @Column(name = "FROMTC")
    private Integer fromtc;

    @ApiModelProperty("空工位数量，默认：9999")
    @Column(name = "EMPTYSTATION")
    private String emptyStation;

    public static EsopGaTaskEntity create(String revId,String emptyStation) {
        EsopGaTaskEntity taskEntity = new EsopGaTaskEntity();
        taskEntity.setId(IdGeneratorUtil.generate());
        taskEntity.setTaskStatus(TaskStatusEnum.NOT_START.ordinal());
        taskEntity.setTcUserId(null);
        taskEntity.setTcPassword(null);
        taskEntity.setFormUid(null);
        taskEntity.setExpectedTactTime(null);
        taskEntity.setFixstation(0);
        taskEntity.setRevId(revId);
        taskEntity.setFromtc(0);
        taskEntity.setEmptyStation(StrUtil.blankToDefault(emptyStation,"9999"));
        return taskEntity;
    }

}
