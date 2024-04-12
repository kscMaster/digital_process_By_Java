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
@Entity(name = "auxiliary_material_revision")
@ApiModel(value = "AuxiliaryMaterial 辅料版本 的Entity")
public  class AuxiliaryMaterialRevisionEntity extends ItemRevisionEntity  implements Serializable{

    @ApiModelProperty(value="规格",required=false)
    @Column(name = "specification", length = 128)
    private String specification;

    @ApiModelProperty(value="标准号",required=false)
    @Column(name = "standard_no", length = 64)
    private String standardNo;

    public String getObjectType() {
        return "AuxiliaryMaterialRevision";
    }

}