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
@Entity(name = "gte4material_revision")
@ApiModel(value = "Gte4MaterialRevision 物料版本 的Entity")
public class Gte4MaterialRevisionEntity extends ItemRevisionEntity implements Serializable{

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
    @Column(name = "gte4special", length = 128)
    private String gte4Special;

    @ApiModelProperty(value="负责人",required=false)
    @Column(name = "gte4responsor", length = 128)
    private String gte4Responsor;

    @ApiModelProperty(value="零组件类型",required=false)
    @Column(name = "gte4item_type", length = 128)
    private String gte4ItemType;

    @ApiModelProperty(value="易必件",required=false)
    @Column(name = "gte4delicate_part", length = 128)
    private String gte4DelicatePart;

    @ApiModelProperty(value="标准件类型",required=false)
    @Column(name = "gte4std_type", length = 128)
    private String gte4StdType;

    @ApiModelProperty(value="零组件类型2",required=false)
    @Column(name = "gte4item_type2", length = 128)
    private String gte4ItemType2;

    @ApiModelProperty(value="生产厂家",required=false)
    @Column(name = "gte4maker", length = 128)
    private String gte4Maker;

    @ApiModelProperty(value="批次号",required=false)
    @Column(name = "gte4bn", length = 64)
    private String gte4Bn;

    @ApiModelProperty(value="批次顺序号",required=false)
    @Column(name = "gte4bn_sn", length = 64)
    private String gte4BnSn;

    @ApiModelProperty(value="位置号",required=false)
    @Column(name = "gte4pos_no", length = 64)
    private String gte4PosNo;

    @ApiModelProperty(value="现有材料",required=false)
    @Column(name = "gte4avail_material", length = 128)
    private String gte4AvailMaterial;

    @ApiModelProperty(value="额定寿命时间（成附件）",required=false)
    @Column(name = "gte4rated_life_time", length = 64)
    private String gte4RatedLifeTime;

    @ApiModelProperty(value="当次需运转时间（成附件）",required=false)
    @Column(name = "gte4current_operation_time", length = 64)
    private String gte4CurrentOperationTime;

    @ApiModelProperty(value="型号",required=false)
    @Column(name = "gte4model_no", length = 64)
    private String gte4ModelNo;

    @Override
    public String getObjectType(){
        return "Gte4MaterialRevision";
    }

}