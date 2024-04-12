package com.nancal.service.bo;


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
@ApiModel(value = "Gte4ProcessRevision 工艺版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4ProcessRevision extends ItemRevision  implements Serializable{

    public static final String GTE4PART_NO = "gte4PartNo";
    public static final String GTE4CAT = "gte4Cat";
    public static final String GTE4MATERIAL_TRADE_MARK = "gte4MaterialTradeMark";
    public static final String GTE4MAT_TECH_STD = "gte4MatTechStd";
    public static final String GTE4MAT_SPEC = "gte4MatSpec";
    public static final String GTE4DZN_BN = "gte4DznBn";
    public static final String GTE4PDMOWNNER = "gte4PDMOwnner";
    public static final String GTE4PDMOWNING_GROUP = "gte4PDMOwningGroup";


    @ApiModelProperty(value="零组件图代号",required=false)
    private String gte4PartNo;

    @ApiModelProperty(value="特性分类",required=false)
    private String gte4Cat;

    @ApiModelProperty(value="材料牌号",required=false)
    private String gte4MaterialTradeMark;

    @ApiModelProperty(value="材料技术标准",required=false)
    private String gte4MatTechStd;

    @ApiModelProperty(value="材料规格",required=false)
    private String gte4MatSpec;

    @ApiModelProperty(value="设计批次",required=false)
    private String gte4DznBn;

    @ApiModelProperty(value="版本所有权用户(PDM)",required=false)
    private String gte4PDMOwnner;

    @ApiModelProperty(value="版本所有权组（PDM）",required=false)
    private String gte4PDMOwningGroup;

    @Override
    public String getObjectType(){
        return "Gte4ProcessRevision";
    }

}