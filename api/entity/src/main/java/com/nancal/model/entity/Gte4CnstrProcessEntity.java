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
@Entity(name = "gte4cnstr_process")
@ApiModel(value = "Gte4CnstrProcess 施工过程卡 的Entity")
public class Gte4CnstrProcessEntity extends Gte4MfgProcessEntity implements Serializable{

    @Override
    public String getObjectType(){
        return "Gte4CnstrProcess";
    }

}