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
@Api("工位工艺表")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "ga_processstation")
public class EsopGaProcessStationEntity implements Serializable {

    @ApiModelProperty("主键id")
    @Id
    @Column(name = "id")
    private Long id;

    @ApiModelProperty("线体id")
    @Column(name = "LINEID")
    private String lineId;

    @ApiModelProperty("工位id")
    @Column(name = "STATIONNO")
    private Long stationNo;

    @ApiModelProperty("工位时间")
    @Column(name = "STATIONTIME")
    private Double stationTime;

    @ApiModelProperty("是否关键工位")
    @Column(name = "KEYSTATION")
    private Integer keyStation;

    @ApiModelProperty("是否瓶颈工位")
    @Column(name = "BOTTLENECK")
    private Integer bottLeneck;

    @ApiModelProperty("关键工序的名称")
    @Column(name = "KEYSTATIONCONTENT")
    private String keyStationContent;


}
