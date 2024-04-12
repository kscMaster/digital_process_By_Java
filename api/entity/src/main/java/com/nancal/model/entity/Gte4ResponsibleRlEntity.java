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
@Entity(name = "gte4responsible_rl")
@ApiModel(value = "Gte4ResponsibleRl 负责人关系表 的Entity")
public class Gte4ResponsibleRlEntity extends WorkspaceObjectEntity implements Serializable{

    public static final String GTE4LEFT_OBJECT = "gte4LeftObject";

    public static final String GTE4LOBJECT_TYPE = "gte4LObjectType";

    public static final String GTE4RESPONSIBLE_NAME = "gte4ResponsibleName";

    public static final String GTE4RESPONSIBLE_ID = "gte4ResponsibleId";

    @ApiModelProperty(value="对应业务id",required=false)
    @Column(name = "gte4left_object", length = 64)
    private String gte4LeftObject;

    @ApiModelProperty(value="对应业务类型",required=false)
    @Column(name = "gte4lobject_type", length = 64)
    private String gte4LObjectType;

    @ApiModelProperty(value="责任人",required=false)
    @Column(name = "gte4responsible_name", length = 128)
    private String gte4ResponsibleName;

    @ApiModelProperty(value="责任人ID",required=false)
    @Column(name = "gte4responsible_id", length = 64)
    private String gte4ResponsibleId;

    public String getObjectType(){
        return "Gte4ResponsibleRl";
    }

}