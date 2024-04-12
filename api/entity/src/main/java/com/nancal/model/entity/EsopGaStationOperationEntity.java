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
@Api("工位工序表")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "ga_stationoperation")
public class EsopGaStationOperationEntity implements Serializable {

    @ApiModelProperty("主键id")
    @Id
    @Column(name = "id")
    private Long id;

    @ApiModelProperty("工位id")
    @Column(name = "STATIONID")
    private Long stationId;

    @ApiModelProperty("ga_operation的id")
    @Column(name = "OPID")
    private String opId;

    @ApiModelProperty("工位时间")
    @Column(name = "PROCTIME")
    private Double procTime;


}
