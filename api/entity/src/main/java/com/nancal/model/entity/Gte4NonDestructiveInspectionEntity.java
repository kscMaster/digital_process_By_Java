package com.nancal.model.entity;

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
@Entity(name = "gte4non_destructive_inspection")
@ApiModel(value = "Gte4NonDestructiveInspection 无损检测委托单 的Entity")
public  class Gte4NonDestructiveInspectionEntity  extends FormEntity  implements Serializable{
    @ApiModelProperty(value="零件名称",required=false)
    @Column(name = "gte4part_name", length = 128)
    private String gte4PartName;
    @ApiModelProperty(value="零件图号",required=false)
    @Column(name = "gte4part_no", length = 64)
    private String gte4PartNo;
    @ApiModelProperty(value="零件批次号",required=false)
    @Column(name = "gte4part_bn", length = 64)
    private String gte4PartBn;
    @ApiModelProperty(value="零件数量",required=false)
    @Column(name = "gte4part_quantity", length = 128)
    private String gte4PartQuantity;
    @ApiModelProperty(value="零件材料",required=false)
    @Column(name = "gte4part_material", length = 128)
    private String gte4PartMaterial;
    @ApiModelProperty(value="加工工艺",required=false)
    @Column(name = "gte4process", length = 128)
    private String gte4Process;
    @ApiModelProperty(value="表面处理工艺",required=false)
    @Column(name = "gte4st_process", length = 128)
    private String gte4StProcess;
    @ApiModelProperty(value="委托单位",required=false)
    @Column(name = "gte4group", length = 128)
    private String gte4Group;
    @ApiModelProperty(value="任务课题号",required=false)
    @Column(name = "gte4task_no", length = 64)
    private String gte4TaskNo;
    @ApiModelProperty(value="检测项目",required=false)
    @Column(name = "gte4test_item", length = 128)
    private String gte4TestItem;
    @ApiModelProperty(value="检测标准",required=false)
    @Column(name = "gte4test_std", length = 128)
    private String gte4TestStd;
    @ApiModelProperty(value="关键工序",required=false)
    @Column(name = "gte4key_op", length = 128)
    private Boolean gte4KeyOp;
    @ApiModelProperty(value="验收标准",required=false)
    @Column(name = "gte4accept_std", length = 128)
    private String gte4AcceptStd;
    @ApiModelProperty(value="接收人",required=false)
    @Column(name = "gte4receiver", length = 128)
    private String gte4Receiver;
    @ApiModelProperty(value="接收日期",required=false,example="1994-03-07")
    @Column(name = "gte4receive_date", length = 128)
    private LocalDate gte4ReceiveDate;
    @ApiModelProperty(value="委托须知",required=false)
    @Column(name = "gte4statement", length = 128)
    private String gte4Statement;
    @ApiModelProperty(value="取件人",required=false)
    @Column(name = "gte4receiver_id", length = 128)
    private String gte4ReceiverId;
    @ApiModelProperty(value="取件日期",required=false)
    @Column(name = "gte4received_date", length = 128)
    private Boolean gte4ReceivedDate;
}