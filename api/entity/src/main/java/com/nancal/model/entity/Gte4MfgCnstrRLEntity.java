package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
@Entity(name = "gte4mfg_cnstrrl")
@ApiModel(value = "Gte4MfgCnstrRL 工艺规程关系 的Entity")
public  class Gte4MfgCnstrRLEntity extends RelationEntity  implements Serializable{
    public String getObjectType() {
        return "Gte4MfgCnstrRL";
    }
}