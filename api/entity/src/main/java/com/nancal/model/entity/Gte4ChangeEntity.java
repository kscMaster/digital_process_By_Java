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
@MappedSuperclass
@ApiModel(value = "Gte4Change 624更改单 的Entity")
public abstract class Gte4ChangeEntity extends ChangeEntity implements Serializable{

    public static final String GTE4CHANGE_TYPE = "gte4ChangeType";

    public static final String GTE4CHANGE_REASON = "gte4ChangeReason";

    @ApiModelProperty(value="更改类型",required=false)
    @Column(name = "gte4change_type", length = 128)
    private String gte4ChangeType;

    @ApiModelProperty(value="更改原因",required=false)
    @Column(name = "gte4change_reason", length = 1024)
    private String gte4ChangeReason;

    public String getObjectType(){
        return "Gte4Change";
    }

}