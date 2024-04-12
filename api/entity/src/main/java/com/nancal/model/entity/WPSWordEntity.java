package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.io.Serializable;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "wpsword")
@ApiModel(value = "WPSword WPSword 的Entity")
public  class WPSWordEntity extends DatasetEntity  implements Serializable{
    public String getObjectType() {
        return "WPSWord";
    }
}
