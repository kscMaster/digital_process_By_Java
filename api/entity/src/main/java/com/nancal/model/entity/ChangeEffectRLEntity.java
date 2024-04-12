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
@Entity(name = "change_effectrl")
@ApiModel(value = "ChangeEffectRL 受影响的 的Entity")
public class ChangeEffectRLEntity extends RelationEntity implements Serializable{

    @ApiModelProperty(value="版本号",required=false)
    @Column(name = "item_rev_id", length = 64)
    private String itemRevId;

    @ApiModelProperty(value="零组件id",required=false)
    @Column(name = "item_uid", length = 64)
    private String itemUid;

    @Override
    public String getObjectType(){
        return "ChangeEffectRL";
    }
//
//    public String getRelationType(){
//        return "ChangeEffectRL";
//    }
}