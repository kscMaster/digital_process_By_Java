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
@Entity(name = "gte4part_tool_revision")
@ApiModel(value = "Gte4PartToolRevision 设计工装版本 的Entity")
public  class Gte4PartToolRevisionEntity  extends ToolRevisionEntity  implements Serializable{
    @ApiModelProperty(value="阶段标识",required=false)
    @Column(name = "gte4phase", length = 64)
    private String gte4Phase;
    @ApiModelProperty(value="设计者",required=false)
    @Column(name = "gte4designer", length = 128)
    private String gte4Designer;
}