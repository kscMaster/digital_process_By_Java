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
@MappedSuperclass
@ApiModel(value = "Relation 关系 的Entity")
public  abstract class RelationEntity  extends WorkspaceObjectEntity  implements Serializable{
    @ApiModelProperty(value="关系类型",required=false)
    @Column(name = "relation_type", length = 64)
    private String relationType;
    @ApiModelProperty(value="左对象ID",required=false)
    @Column(name = "left_object", length = 64)
    private String leftObject;
    @ApiModelProperty(value="左对象类型",required=false)
    @Column(name = "left_object_type", length = 64)
    private String leftObjectType;
    @ApiModelProperty(value="右对象ID",required=false)
    @Column(name = "right_object", length = 64)
    private String rightObject;
    @ApiModelProperty(value="右对象类型",required=false)
    @Column(name = "right_object_type", length = 64)
    private String rightObjectType;

    public static final String LEFT_OBJECT = "leftObject";
    public static final String LEFT_OBJECT_TYPE = "leftObjectType";
    public static final String RIGHT_OBJECT = "rightObject";
    public static final String RIGHT_OBJECT_TYPE = "rightObjectType";
}