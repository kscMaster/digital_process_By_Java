package com.nancal.service.bo;


import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nancal.model.entity.Gte4MfgPlantPrRevisionEntity;
import com.nancal.api.model.Gte4MfgPlantPrRevisionReq;
import com.nancal.api.model.Gte4MfgPlantPrRevisionResp;






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
@ApiModel(value = "Gte4MfgPlantPrRevision 工厂工艺版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4MfgPlantPrRevision extends ItemRevision  implements Serializable{

    public static final String GTE4LEAD_FREE = "gte4LeadFree";
    public static final String GTE4ELECTROSTATIC_PROTECTION = "gte4ELectrostaticProtection";
    public static final String GTE4DUST_RATING = "gte4DustRating";
    public static final String GTE4PCBSIZE_LENGTH = "gte4PCBSizeLength";
    public static final String GTE4PCBSIZE_WIDTH = "gte4PCBSizeWidth";
    public static final String GTE4PCBSIZE_HEIGTH = "gte4PCBSizeHeigth";


    @ApiModelProperty(value="是否无铅加工",required=false)
    private Boolean gte4LeadFree;

    @ApiModelProperty(value="静电防护等级",required=false)
    private String gte4ELectrostaticProtection;

    @ApiModelProperty(value="防尘等级",required=false)
    private String gte4DustRating;

    @ApiModelProperty(value="PCB尺寸-长",required=false)
    private Double gte4PCBSizeLength;

    @ApiModelProperty(value="PCB尺寸-宽",required=false)
    private Double gte4PCBSizeWidth;

    @ApiModelProperty(value="PCB尺寸-高",required=false)
    private Double gte4PCBSizeHeigth;

    @Override
    public String getObjectType(){
        return "Gte4MfgPlantPrRevision";
    }

}