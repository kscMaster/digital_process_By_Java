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
@AllArgsConstructor
@Entity(name = "extra_property_data")
@ApiModel(value = "ExtraPropertyData 扩展字段数据 的Entity")
public class ExtraPropertyDataEntity extends WorkspaceObjectEntity implements Serializable{

    public static final String PROPERTY = "property";

    public static final String VALUE = "value";

    public static final String LEFT_OBJECT = "leftObject";

    public static final String LEFT_OBJECT_TYPE = "leftObjectType";

    @ApiModelProperty(value="属性字段",required=false)
    @Column(name = "property", length = 64)
    private String property;

    @ApiModelProperty(value="属性值",required=false)
    @Column(name = "value", length = 1024)
    private String value;

    @ApiModelProperty(value="对应业务id",required=false)
    @Column(name = "left_object", length = 64)
    private String leftObject;

    @ApiModelProperty(value="对应业务类型",required=false)
    @Column(name = "left_object_type", length = 64)
    private String leftObjectType;

    public String getObjectType(){
        return "ExtraPropertyData";
    }

}