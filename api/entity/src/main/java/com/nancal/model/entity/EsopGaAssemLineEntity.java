package com.nancal.model.entity;

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
@Api("线体表")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "ga_assemline")
public class EsopGaAssemLineEntity implements Serializable {

    @ApiModelProperty("主键id")
    @Id
    @Column(name = "id")
    private String id;

    @ApiModelProperty("任务id")
    @Column(name = "TASKID")
    private String taskId;

    @ApiModelProperty("线体名称")
    @Column(name = "LINENAME")
    private String lineName;

    @ApiModelProperty("线上的瓶颈时间")
    @Column(name = "TACTTIME")
    private Double tactTime;

    @ApiModelProperty("工位数量")
    @Column(name = "STATIONNUM")
    private Integer stationNum;

    @ApiModelProperty("最小工位数")
    @Column(name = "MINSTATIONNUM")
    private Integer minStationNum;

    @ApiModelProperty("最大工位数")
    @Column(name = "MAXSTATIONNUM")
    private Integer maxStationNum;

    @ApiModelProperty("线体唯一标记")
    @Column(name = "LINEUID")
    private String lineUid;


    public static EsopGaAssemLineEntity create(String taskId, String lineName, Integer stationNum) {
        EsopGaAssemLineEntity assemLineEntity = new EsopGaAssemLineEntity();
        assemLineEntity.setId(IdGeneratorUtil.generate());
        assemLineEntity.setTaskId(taskId);
        assemLineEntity.setLineName(lineName);
        assemLineEntity.setStationNum(stationNum);
        return assemLineEntity;
    }

}
