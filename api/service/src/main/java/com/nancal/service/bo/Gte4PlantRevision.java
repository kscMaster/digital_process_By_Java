package com.nancal.service.bo;


import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nancal.model.entity.Gte4PlantRevisionEntity;
import com.nancal.api.model.Gte4PlantRevisionReq;
import com.nancal.api.model.Gte4PlantRevisionResp;






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
@ApiModel(value = "Gte4PlantRevision 工厂版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4PlantRevision extends ItemRevision  implements Serializable{

    public static final String GTE4ORDER_NO = "gte4orderNo";
    public static final String GTE4PLANT_TYPE = "gte4PlantType";
    public static final String GTE4ADDRESS = "gte4Address";
    public static final String GTE4PLANT_NATURE = "gte4PlantNature";
    public static final String GTE4MESORG_CODE = "gte4MESOrgCode";


    @ApiModelProperty(value="序号",required=false)
    private String gte4orderNo;

    @ApiModelProperty(value="工厂类型",required=false)
    private String gte4PlantType;

    @ApiModelProperty(value="地址",required=false)
    private String gte4Address;

    @ApiModelProperty(value="工厂性质",required=false)
    private String gte4PlantNature;

    @ApiModelProperty(value="MES组织代号",required=false)
    private String gte4MESOrgCode;

    @Override
    public String getObjectType(){
        return "Gte4PlantRevision";
    }

}