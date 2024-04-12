package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@Entity(name = "gte4part")
@ApiModel(value = "Gte4Part 设计零件 的Entity")
public  class Gte4PartEntity  extends ItemEntity  implements Serializable{

    @ApiModelProperty(value="代图号",required=false)
    @Column(name = "part_no", length = 64)
    private String partNo;

    @Override
    public String getObjectType(){
        return "Gte4Part";
    }
}