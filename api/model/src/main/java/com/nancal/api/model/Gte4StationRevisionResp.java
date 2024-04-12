package com.nancal.api.model;


import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "Gte4StationRevision 工位版本 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4StationRevisionResp extends ItemRevisionResp  implements Serializable{
    @ApiModelProperty(value="序号",required=false)
    private String gte4orderNo;
    @JsonDict(DictConstant.GTE4_STATION_TYPE)
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