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
@Entity(name = "change")
@ApiModel(value = "Change 更改单 的Entity")
public class ChangeEntity extends WorkspaceObjectEntity implements Serializable{

    public static final String CHANGE_ID = "changeId";

    public static final String CHANGE_COMMENT = "changeComment";

    @ApiModelProperty(value="更改单号",required=false)
    @Column(name = "change_id", length = 64)
    private String changeId;

    @ApiModelProperty(value="更改意见",required=false)
    @Column(name = "change_comment", length = 1024)
    private String changeComment;

    public String getObjectType(){
        return "Change";
    }

}