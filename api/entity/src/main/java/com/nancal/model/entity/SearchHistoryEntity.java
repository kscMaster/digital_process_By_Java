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
@Entity(name = "search_history")
@ApiModel(value = "SearchHistory 查询条件历史 的Entity")
public class SearchHistoryEntity extends WorkspaceObjectEntity implements Serializable{

    public static final String APP_CODE = "appCode";

    public static final String CONDITION_NAME = "conditionName";

    public static final String COMBIN_CONDITION = "combinCondition";

    public static final String CONDITION_OBJECT_TYPE = "conditionObjectType";

    @ApiModelProperty(value="应用code",required=false)
    @Column(name = "app_code", length = 128)
    private String appCode;

    @ApiModelProperty(value="条件名称",required=false)
    @Column(name = "condition_name", length = 128)
    private String conditionName;

    @ApiModelProperty(value="组合json条件",required=false)
    @Column(name = "combin_condition", length = 1024)
    private String combinCondition;

    @ApiModelProperty(value="条件对象类型",required=false)
    @Column(name = "condition_object_type", length = 128)
    private String conditionObjectType;


    public String getObjectType(){
        return "SearchHistory";
    }

}