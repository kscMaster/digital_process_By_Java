package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "process_instance")
@ApiModel(value = "ProcessInstance 流程 的Entity")
public class ProcessInstanceEntity extends WorkspaceObjectEntity{

    @ApiModelProperty(value = "流程标题")
    @Column(name = "process_title", length = 128)
    private String processTitle;
    @ApiModelProperty(value = "流程定义ID")
    @Column(name = "process_definition_id", length = 64)
    private String processDefinitionId;
    @ApiModelProperty(value = "流程标识")
    @Column(name = "process_key", length = 255)
    private String processKey;
    /*
    @ApiModelProperty(value = "流程定义名称processName，直接使用继承属性objectName")
    @Column(name = "object_name", length = 64)
    private String objectName;
    */
    @ApiModelProperty(value = "流程定义ID")
    @Column(name = "business_key", length = 255)
    private String businessKey;

    @Override
    public String getObjectType() {
        return "ProcessInstance";
    }
}
