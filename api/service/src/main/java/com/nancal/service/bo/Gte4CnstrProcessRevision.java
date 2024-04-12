package com.nancal.service.bo;


import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

@Data
@ApiModel(value = "Gte4CnstrProcessRevision 施工过程卡版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4CnstrProcessRevision extends Gte4MfgProcessRevision  implements Serializable{

    public static final String GTE4TIME = "gte4Time";
    public static final String GTE4ORDER = "gte4Order";
    public static final String GTE4WORK_ORDER = "gte4WorkOrder";
    public static final String GTE4UNIT_NO = "gte4UnitNo";
    public static final String GTE4BN = "gte4Bn";


    @ApiModelProperty(value="第几次装配/分解",required=false)
    private Integer gte4Time;

    @ApiModelProperty(value="生产订单号",required=false)
    private String gte4Order;

    @ApiModelProperty(value="生产工单号",required=false)
    private String gte4WorkOrder;

    @ApiModelProperty(value="台份号",required=false)
    private String gte4UnitNo;

    @ApiModelProperty(value="批次号",required=false)
    private String gte4Bn;

    @Override
    public String getObjectType(){
        return "Gte4CnstrProcessRevision";
    }

}