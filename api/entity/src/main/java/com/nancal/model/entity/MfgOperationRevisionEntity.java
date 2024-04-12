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
@Entity(name = "mfg_operation_revision")
@ApiModel(value = "MfgOperationRevision 制造工序版本 的Entity")
public  class MfgOperationRevisionEntity  extends ItemRevisionEntity  implements Serializable{
    @ApiModelProperty(value="工序类型",required=false)
    @Column(name = "op_type", length = 64)
    private String opType;
    @ApiModelProperty(value="是否关键工序",required=false,example="false")
    @Column(name = "is_key", length = 0)
    private Boolean isKey;
}