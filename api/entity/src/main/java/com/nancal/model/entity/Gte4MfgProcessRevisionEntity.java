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
@Entity(name = "gte4mfg_process_revision")
@ApiModel(value = "Gte4MfgProcessRevision 工艺版本 的Entity")
public  class Gte4MfgProcessRevisionEntity  extends MfgProcessRevisionEntity  implements Serializable{

    public static final String GTE4PRODUCT_NO = "gte4ProductNo";

    public static final String GTE4PRODUCT_NAME = "gte4ProductName";

    public static final String GTE4PART_NAME = "gte4PartName";

    public static final String GTE4PART_NO = "gte4PartNo";

    public static final String GTE4TYPE = "gte4Type";

    public static final String GTE4PHASE = "gte4Phase";


    @ApiModelProperty(value="产品型代号",required=false)
    @Column(name = "gte4product_no", length = 64)
    private String gte4ProductNo;
    @ApiModelProperty(value="产品名称",required=false)
    @Column(name = "gte4product_name", length = 128)
    private String gte4ProductName;
    @ApiModelProperty(value="部组件名称",required=false)
    @Column(name = "gte4part_name", length = 128)
    private String gte4PartName;
    @ApiModelProperty(value="部组件图号",required=false)
    @Column(name = "gte4part_no", length = 64)
    private String gte4PartNo;
    @ApiModelProperty(value="特性分类",required=false)
    @Column(name = "gte4type", length = 128)
    private String gte4Type;
    @ApiModelProperty(value="阶段",required=false)
    @Column(name = "gte4phase", length = 64)
    private String gte4Phase;
    @ApiModelProperty(value="编制单位",required=false)
    @Column(name = "owner_group_name", length = 128)
    private String ownerGroupName;

    @ApiModelProperty(value="代图号",required=false)
    @Column(name = "part_no", length = 64)
    private String partNo;

    @ApiModelProperty(value="分区",required=false)
    @Column(name = "gte4partition", length = 128)
    private String gte4partition;

    @Override
    public String getObjectType(){
        return "Gte4MfgProcessRevision";
    }

}