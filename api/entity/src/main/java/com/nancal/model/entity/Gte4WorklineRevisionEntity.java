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
@Entity(name = "gte4workline_revision")
@ApiModel(value = "Gte4WorklineRevision 线体版本 的Entity")
public class Gte4WorklineRevisionEntity extends ItemRevisionEntity implements Serializable{

    public static final String GTE4ORDER_NO = "gte4orderNo";

    public static final String GTE4WORKSHOP = "gte4Workshop";

    public static final String GTE4ASSET_NUM = "gte4AssetNum";

    public static final String GTE4WORKAREA = "gte4Workarea";

    public static final String GTE4DEPARTMENT_NAME = "gte4DepartmentName";

    public static final String GTE4LINE_CAPACITY = "gte4LineCapacity";

    @ApiModelProperty(value="序号",required=false)
    @Column(name = "gte4order_no", length = 128)
    private String gte4orderNo;

    @ApiModelProperty(value="所属车间",required=false)
    @Column(name = "gte4workshop", length = 128)
    private String gte4Workshop;

    @ApiModelProperty(value="资产编号",required=false)
    @Column(name = "gte4asset_num", length = 128)
    private String gte4AssetNum;

    @ApiModelProperty(value="区域",required=false)
    @Column(name = "gte4workarea", length = 128)
    private String gte4Workarea;

    @ApiModelProperty(value="所属部门",required=false)
    @Column(name = "gte4department_name", length = 128)
    private String gte4DepartmentName;

    @ApiModelProperty(value="线体能力",required=false)
    @Column(name = "gte4line_capacity", length = 128)
    private String gte4LineCapacity;

    @ApiModelProperty(value="线体类型",required=false)
    @Column(name = "gte4line_type", length = 128)
    private String gte4LineType;

    @Override
    public String getObjectType(){
        return "Gte4WorklineRevision";
    }

}