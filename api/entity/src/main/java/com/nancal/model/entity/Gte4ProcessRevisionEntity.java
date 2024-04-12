package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "gte4process_revision")
@ApiModel(value = "Gte4ProcessRevision 工艺版本 的Entity")
public class Gte4ProcessRevisionEntity extends ItemRevisionEntity implements Serializable{

    public static final String GTE4PART_NO = "gte4PartNo";

    public static final String GTE4CAT = "gte4Cat";

    public static final String GTE4MATERIAL_TRADE_MARK = "gte4MaterialTradeMark";

    public static final String GTE4MAT_TECH_STD = "gte4MatTechStd";

    public static final String GTE4MAT_SPEC = "gte4MatSpec";

    public static final String GTE4DZN_BN = "gte4DznBn";

    public static final String GTE4PDMOWNNER = "gte4PDMOwnner";

    public static final String GTE4PDMOWNING_GROUP = "gte4PDMOwningGroup";

    @ApiModelProperty(value="零组件图代号",required=false)
    @Column(name = "gte4part_no", length = 64)
    private String gte4PartNo;

    @ApiModelProperty(value="特性分类",required=false)
    @Column(name = "gte4cat", length = 64)
    private String gte4Cat;

    @ApiModelProperty(value="材料牌号",required=false)
    @Column(name = "gte4material_trade_mark", length = 64)
    private String gte4MaterialTradeMark;

    @ApiModelProperty(value="材料技术标准",required=false)
    @Column(name = "gte4mat_tech_std", length = 128)
    private String gte4MatTechStd;

    @ApiModelProperty(value="材料规格",required=false)
    @Column(name = "gte4mat_spec", length = 128)
    private String gte4MatSpec;

    @ApiModelProperty(value="工艺批次",required=false)
    @Column(name = "gte4dzn_bn", length = 64)
    private String gte4DznBn;

    @ApiModelProperty(value="版本所有权用户(PDM)",required=false)
    @Column(name = "gte4pdmownner", length = 128)
    private String gte4PDMOwnner;

    @ApiModelProperty(value="版本所有权组（PDM）",required=false)
    @Column(name = "gte4pdmowning_group", length = 128)
    private String gte4PDMOwningGroup;

    @ApiModelProperty(value="初始型号",required=false)
    @Column(name = "gte4init_model", length = 64)
    private String gte4InitModel;

    @ApiModelProperty(value="阶段标识",required=false)
    @Column(name = "gte4_phase", length = 64)
    private String gte4Phase;

    @Override
    public String getObjectType(){
        return "Gte4ProcessRevision";
    }

}