package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
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
@Entity(name = "gte4worklinerl")
@ApiModel(value = "Gte4WorklineRL 线体关系 的Entity")
public class Gte4WorklineRLEntity extends RelationEntity implements Serializable{

    @Override
    public String getObjectType(){
        return "Gte4WorklineRL";
    }

    public String getRelationType(){
        return "Gte4WorklineRL";
    }
}