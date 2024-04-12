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
@ApiModel(value = "Gte4MaterialRevision 物料版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4MaterialRevision extends ItemRevision  implements Serializable{

    public static final String GTE4SPECIAL = "gte4Special";
    public static final String GTE4RESPONSOR = "gte4Responsor";
    public static final String GTE4ITEM_TYPE = "gte4ItemType";
    public static final String GTE4DELICATE_PART = "gte4DelicatePart";
    public static final String GTE4STD_TYPE = "gte4StdType";
    public static final String GTE4ITEM_TYPE2 = "gte4ItemType2";
    public static final String GTE4MAKER = "gte4Maker";
    public static final String GTE4BN = "gte4Bn";
    public static final String GTE4BN_SN = "gte4BnSn";
    public static final String GTE4POS_NO = "gte4PosNo";
    public static final String GTE4AVAIL_MATERIAL = "gte4AvailMaterial";
    public static final String GTE4RATED_LIFE_TIME = "gte4RatedLifeTime";
    public static final String GTE4CURRENT_OPERATION_TIME = "gte4CurrentOperationTime";
    public static final String GTE4MODEL_NO = "gte4ModelNo";


    @ApiModelProperty(value="专业",required=false)
    private String gte4Special;

    @ApiModelProperty(value="负责人",required=false)
    private String gte4Responsor;

    @ApiModelProperty(value="零组件类型",required=false)
    private String gte4ItemType;

    @ApiModelProperty(value="易必件",required=false)
    private String gte4DelicatePart;

    @ApiModelProperty(value="标准件类型",required=false)
    private String gte4StdType;

    @ApiModelProperty(value="零组件类型2",required=false)
    private String gte4ItemType2;

    @ApiModelProperty(value="生产厂家",required=false)
    private String gte4Maker;

    @ApiModelProperty(value="批次号",required=false)
    private String gte4Bn;

    @ApiModelProperty(value="批次顺序号",required=false)
    private String gte4BnSn;

    @ApiModelProperty(value="位置号",required=false)
    private String gte4PosNo;

    @ApiModelProperty(value="现有材料",required=false)
    private String gte4AvailMaterial;

    @ApiModelProperty(value="额定寿命时间（成附件）",required=false)
    private String gte4RatedLifeTime;

    @ApiModelProperty(value="当次需运转时间（成附件）",required=false)
    private String gte4CurrentOperationTime;

    @ApiModelProperty(value="型号",required=false)
    private String gte4ModelNo;

    @Override
    public String getObjectType(){
        return "Gte4MaterialRevision";
    }

}