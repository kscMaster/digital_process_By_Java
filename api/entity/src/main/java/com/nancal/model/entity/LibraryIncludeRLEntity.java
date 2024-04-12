package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "library_include_r_l")
@ApiModel(value = "LibraryIncludeRL 库包含关系 的Entity")
public class LibraryIncludeRLEntity extends RelationEntity {

    @Override
    public String getRelationType() {
        return "LibraryIncludeRL";
    }

    @Override
    public String getObjectType() {
        return "LibraryIncludeRL";
    }
}
