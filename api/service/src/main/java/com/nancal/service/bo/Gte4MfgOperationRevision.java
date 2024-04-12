package com.nancal.service.bo;


import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nancal.model.entity.Gte4MfgOperationRevisionEntity;
import com.nancal.api.model.Gte4MfgOperationRevisionReq;
import com.nancal.api.model.Gte4MfgOperationRevisionResp;






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
@ApiModel(value = "Gte4MfgOperationRevision 工序版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4MfgOperationRevision extends MfgOperationRevision  implements Serializable{

    public static final String GTE4COOP_ORG = "gte4CoopOrg";
    public static final String GTE4ACTION_DESCRIPTION = "gte4ActionDescription";
    public static final String GTE4EXAMINE_POINTS = "gte4ExaminePoints";
    public static final String GTE4PROCESS_REQ = "gte4ProcessReq";
    public static final String GTE4SAMPLING_RATIO = "gte4SamplingRatio";
    public static final String GTE4TEST_POINTS = "gte4TestPoints";
    public static final String GTE4TASK_TIME = "gte4TaskTime";


    @ApiModelProperty(value="协作单位",required=false)
    private String gte4CoopOrg;

    @ApiModelProperty(value="工艺描述",required=false)
    private String gte4ActionDescription;

    @ApiModelProperty(value="检测要点",required=false)
    private String gte4ExaminePoints;

    @ApiModelProperty(value="工艺要求/作业要领",required=false)
    private String gte4ProcessReq;

    @ApiModelProperty(value="抽检比例",required=false)
    private Double gte4SamplingRatio;

    @ApiModelProperty(value="测试要点",required=false)
    private String gte4TestPoints;

    @ApiModelProperty(value="工时(S)",required=false)
    private Integer gte4TaskTime;

    @Override
    public String getObjectType(){
        return "Gte4MfgOperationRevision";
    }

}