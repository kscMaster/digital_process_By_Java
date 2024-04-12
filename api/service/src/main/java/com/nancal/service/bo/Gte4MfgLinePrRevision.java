package com.nancal.service.bo;


import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nancal.model.entity.Gte4MfgLinePrRevisionEntity;
import com.nancal.api.model.Gte4MfgLinePrRevisionReq;
import com.nancal.api.model.Gte4MfgLinePrRevisionResp;






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
@ApiModel(value = "Gte4MfgLinePrRevision 线体工艺版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4MfgLinePrRevision extends ItemRevision  implements Serializable{

    public static final String GTE4THE_DIRECTION = "gte4TheDirection";
    public static final String GTE4PROCESS_CLASSIFICATION = "gte4ProcessClassification";
    public static final String GTE4PANEL_MODEL = "gte4PanelModel";


    @ApiModelProperty(value="过板方向",required=false)
    private String gte4TheDirection;

    @ApiModelProperty(value="工序分类",required=false)
    private String gte4ProcessClassification;

    @ApiModelProperty(value="工艺拼板模式",required=false)
    private String gte4PanelModel;

    @Override
    public String getObjectType(){
        return "Gte4MfgLinePrRevision";
    }

}