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
@Entity(name = "gte4plant_revision")
@ApiModel(value = "Gte4PlantRevision 工厂版本 的Entity")
public class Gte4PlantRevisionEntity extends ItemRevisionEntity implements Serializable{

    public static final String GTE4ORDER_NO = "gte4orderNo";

    public static final String GTE4PLANT_TYPE = "gte4PlantType";

    public static final String GTE4ADDRESS = "gte4Address";

    public static final String GTE4PLANT_NATURE = "gte4PlantNature";

    public static final String GTE4MESORG_CODE = "gte4MESOrgCode";

    @ApiModelProperty(value="序号",required=false)
    @Column(name = "gte4order_no", length = 128)
    private String gte4orderNo;

    @ApiModelProperty(value="工厂类型",required=false)
    @Column(name = "gte4plant_type", length = 128)
    private String gte4PlantType;

    @ApiModelProperty(value="地址",required=false)
    @Column(name = "gte4address", length = 256)
    private String gte4Address;

    @ApiModelProperty(value="工厂性质",required=false)
    @Column(name = "gte4plant_nature", length = 128)
    private String gte4PlantNature;

    @ApiModelProperty(value="MES组织代号",required=false)
    @Column(name = "gte4mesorg_code", length = 64)
    private String gte4MESOrgCode;

    @Override
    public String getObjectType(){
        return "Gte4PlantRevision";
    }

}