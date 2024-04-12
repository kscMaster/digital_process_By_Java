package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.io.Serializable;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "gte4part_revision")
@ApiModel(value = "Gte4PartRevision 设计零件版本 的Entity")
public  class Gte4PartRevisionEntity  extends ItemRevisionEntity  implements Serializable{
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
    @ApiModelProperty(value="设计批次",required=false)
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

    @ApiModelProperty(value="代图号",required=false)
    @Column(name = "part_no", length = 64)
    private String partNo;

    @ApiModelProperty(value="板子的key",required=false)
    @Column(name = "board_key", length = 64)
    private String boardKey;


    public String getObjectType(){
        return "Gte4PartRevision";
    }
}