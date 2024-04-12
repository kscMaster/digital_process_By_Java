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
@ApiModel(value = "BusinessObject 业务对象 的Entity")
public  abstract class BusinessObjectEntity   implements Serializable{
    @ApiModelProperty(value="唯一标志符",required=false)
    @Column(name = "uid", length = 64)
    @Id
    private String uid;
    @ApiModelProperty(value="删除标志",required=false)
    @Column(name = "del_flag", length = 0)
    private Boolean delFlag;

    public static final String UID = "uid";
    public static final String DEL_FLAG = "delFlag";
}