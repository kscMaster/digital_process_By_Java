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
@Entity(name = "gte4station_revision")
@ApiModel(value = "Gte4StationRevision 工位版本 的Entity")
public class Gte4StationRevisionEntity extends ItemRevisionEntity implements Serializable{

    public static final String GTE4ORDER_NO = "gte4orderNo";

    public static final String GTE4STATION_TYPE = "gte4StationType";

    public static final String GTE4LENGTH_OF_STATION = "gte4LengthOfStation";

    public static final String GTE4ESDREQ = "gte4ESDReq";

    public static final String GTE4CLEAN_REQ = "gte4CleanReq";

    @ApiModelProperty(value="序号",required=false)
    @Column(name = "gte4order_no", length = 128)
    private String gte4orderNo;

    @ApiModelProperty(value="工位类型",required=false)
    @Column(name = "gte4station_type", length = 128)
    private String gte4StationType;

    @ApiModelProperty(value="工位长度(m)",required=false)
    @Column(name = "gte4length_of_station", length = 0)
    private Double gte4LengthOfStation;

    @ApiModelProperty(value="ESD要求",required=false)
    @Column(name = "gte4esdreq", length = 128)
    private String gte4ESDReq;

    @ApiModelProperty(value="洁净度要求",required=false)
    @Column(name = "gte4clean_req", length = 128)
    private String gte4CleanReq;

    @Override
    public String getObjectType(){
        return "Gte4StationRevision";
    }

}