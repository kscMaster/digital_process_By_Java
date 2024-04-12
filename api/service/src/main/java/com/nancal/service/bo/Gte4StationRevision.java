package com.nancal.service.bo;


import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nancal.model.entity.Gte4StationRevisionEntity;
import com.nancal.api.model.Gte4StationRevisionReq;
import com.nancal.api.model.Gte4StationRevisionResp;






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
@ApiModel(value = "Gte4StationRevision 工位版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4StationRevision extends ItemRevision  implements Serializable{

    public static final String GTE4ORDER_NO = "gte4orderNo";
    public static final String GTE4STATION_TYPE = "gte4StationType";
    public static final String GTE4LENGTH_OF_STATION = "gte4LengthOfStation";
    public static final String GTE4ESDREQ = "gte4ESDReq";
    public static final String GTE4CLEAN_REQ = "gte4CleanReq";


    @ApiModelProperty(value="序号",required=false)
    private String gte4orderNo;

    @ApiModelProperty(value="工位类型",required=false)
    private String gte4StationType;

    @ApiModelProperty(value="工位长度(m)",required=false)
    private Double gte4LengthOfStation;

    @ApiModelProperty(value="ESD要求",required=false)
    private String gte4ESDReq;

    @ApiModelProperty(value="洁净度要求",required=false)
    private String gte4CleanReq;

    @Override
    public String getObjectType(){
        return "Gte4StationRevision";
    }

}