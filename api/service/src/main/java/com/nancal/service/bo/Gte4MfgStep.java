package com.nancal.service.bo;


import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nancal.model.entity.Gte4MfgStepEntity;
import com.nancal.api.model.Gte4MfgStepReq;
import com.nancal.api.model.Gte4MfgStepResp;






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
@ApiModel(value = "Gte4MfgStep 工步 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4MfgStep extends MfgStep  implements Serializable{

    public static final String GTE4COOP_ORG = "gte4CoopOrg";
    public static final String GTE4TASK_TIME = "gte4TaskTime";
    public static final String GTE4FREQUENCY = "gte4Frequency";


    @ApiModelProperty(value="协作单位",required=false)
    private String gte4CoopOrg;

    @ApiModelProperty(value="工时(S)",required=false)
    private Integer gte4TaskTime;

    @ApiModelProperty(value="频率",required=false)
    private Double gte4Frequency;

}