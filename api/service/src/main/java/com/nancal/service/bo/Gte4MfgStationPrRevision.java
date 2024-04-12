package com.nancal.service.bo;


import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nancal.model.entity.Gte4MfgStationPrRevisionEntity;
import com.nancal.api.model.Gte4MfgStationPrRevisionReq;
import com.nancal.api.model.Gte4MfgStationPrRevisionResp;






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
@ApiModel(value = "Gte4MfgStationPrRevision 工位工艺版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4MfgStationPrRevision extends ItemRevision  implements Serializable{

    public static final String GTE4CHANGE_RECORD = "gte4ChangeRecord";
    public static final String GTE4SPECIAL_CHARACTERISTICS = "gte4SpecialCharacteristics";
    public static final String GTE4EXCEPTION_CAUTIONS = "gte4ExceptionCautions";
    public static final String GTE4KEY_INFO_PROGRAM = "gte4KeyInfoProgram";
    public static final String GTE4KEY_PROCESS = "gte4KeyProcess";


    @ApiModelProperty(value="变更记录",required=false)
    private String gte4ChangeRecord;

    @ApiModelProperty(value="特殊特性",required=false)
    private String gte4SpecialCharacteristics;

    @ApiModelProperty(value="异常处理、注意事项、其他",required=false)
    private String gte4ExceptionCautions;

    @ApiModelProperty(value="关键信息",required=false)
    private String gte4KeyInfoProgram;

    @ApiModelProperty(value="关键工序",required=false)
    private Boolean gte4KeyProcess;

    @Override
    public String getObjectType(){
        return "Gte4MfgStationPrRevision";
    }

}