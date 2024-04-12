package com.nancal.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EsopGaProcessStationResp implements Serializable {


    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("revUid")
    private String revUid;

    @ApiModelProperty("线体id")
    private String lineId;

    @ApiModelProperty("工位序号")
    private String stationNo;

    @ApiModelProperty("工位名称")
    private String stationName;

    @ApiModelProperty("工位时间")
    private Double stationTime;


}
