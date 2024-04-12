package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "gte4cnstr_process_revision")
@ApiModel(value = "Gte4CnstrProcessRevision 施工过程卡版本 的Entity")
public class Gte4CnstrProcessRevisionEntity extends Gte4MfgProcessRevisionEntity implements Serializable{

    public static final String GTE4TIME = "gte4Time";

    public static final String GTE4ORDER = "gte4Order";

    public static final String GTE4WORK_ORDER = "gte4WorkOrder";

    public static final String GTE4UNIT_NO = "gte4UnitNo";

    public static final String GTE4BN = "gte4Bn";

    @ApiModelProperty(value="第几次装配/分解",required=false)
    @Column(name = "gte4time", length = 0)
    private Integer gte4Time;

    @ApiModelProperty(value="生产订单号",required=false)
    @Column(name = "gte4order", length = 64)
    private String gte4Order;

    @ApiModelProperty(value="生产工单号",required=false)
    @Column(name = "gte4work_order", length = 64)
    private String gte4WorkOrder;

    @ApiModelProperty(value="台份号",required=false)
    @Column(name = "gte4unit_no", length = 64)
    private String gte4UnitNo;

    @ApiModelProperty(value="批次号",required=false)
    @Column(name = "gte4bn", length = 64)
    private String gte4Bn;

    @ApiModelProperty(value="分区",required=false)
    @Column(name = "gte4partition", length = 128)
    private String gte4partition;

    @Override
    public String getObjectType(){
        return "Gte4CnstrProcessRevision";
    }

}