package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@ApiModel(value = "Gte4NonDestructiveInspection 无损检测委托单 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4NonDestructiveInspection extends Form  implements Serializable{

    @ApiModelProperty(value="零件名称",required=false)
    private String gte4PartName;

    @ApiModelProperty(value="零件图号",required=false)
    private String gte4PartNo;

    @ApiModelProperty(value="零件批次号",required=false)
    private String gte4PartBn;

    @ApiModelProperty(value="零件数量",required=false)
    private String gte4PartQuantity;

    @ApiModelProperty(value="零件材料",required=false)
    private String gte4PartMaterial;

    @ApiModelProperty(value="加工工艺",required=false)
    private String gte4Process;

    @ApiModelProperty(value="表面处理工艺",required=false)
    private String gte4StProcess;

    @ApiModelProperty(value="委托单位",required=false)
    private String gte4Group;

    @ApiModelProperty(value="任务课题号",required=false)
    private String gte4TaskNo;

    @ApiModelProperty(value="检测项目",required=false)
    private String gte4TestItem;

    @ApiModelProperty(value="检测标准",required=false)
    private String gte4TestStd;

    @ApiModelProperty(value="关键工序",required=false)
    private Boolean gte4KeyOp;

    @ApiModelProperty(value="验收标准",required=false)
    private String gte4AcceptStd;

    @ApiModelProperty(value="接收人",required=false)
    private String gte4Receiver;

    @ApiModelProperty(value="接收日期",required=false,example="1994-03-07")
    private LocalDate gte4ReceiveDate;

    @ApiModelProperty(value="委托须知",required=false)
    private String gte4Statement;

    @ApiModelProperty(value="取件人",required=false)
    private String gte4ReceiverId;

    @ApiModelProperty(value="取件日期",required=false)
    private Boolean gte4ReceivedDate;

}