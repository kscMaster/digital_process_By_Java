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
@Entity(name = "process_instance_r_l")
@ApiModel(value = "ProcessInstanceRL 流程实例和工作对象关系 的Entity,左对象为流程实例ID，右对象为工作对象")
public class ProcessInstanceRLEntity extends RelationEntity {
    @Override
    public String getRelationType() {
        return "ProcessInstanceRL";
    }

    @Override
    public String getObjectType() {
        return "ProcessInstanceRL";
    }
}
