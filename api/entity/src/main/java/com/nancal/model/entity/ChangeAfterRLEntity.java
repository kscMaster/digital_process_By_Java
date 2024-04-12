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
@Entity(name = "change_afterrl")
@ApiModel(value = "ChangeAfterRL 更改后 的Entity")
public class ChangeAfterRLEntity extends RelationEntity implements Serializable{

    public static final String CHANGE_TYPE = "changeType";

    public static final String CHANGE_REASON = "changeReason";

    public static final String WIP_SUGGESTION = "wipSuggestion";

    public static final String PROCESSED_SUGGESTION = "processedSuggestion";

    public static final String CHANGE_OPINION = "changeOpinion";

    public static final String EFFECTIVE_DATE = "effectiveDate";

    public static final String CHANGE_AT_ONCE = "changeAtOnce";

    @ApiModelProperty(value="更改类型",required=false)
    @Column(name = "change_type", length = 64)
    private String changeType;

    @ApiModelProperty(value="更改原因",required=false)
    @Column(name = "change_reason", length = 1024)
    private String changeReason;

    @ApiModelProperty(value="在制品处理意见",required=false)
    @Column(name = "wip_suggestion", length = 1024)
    private String wipSuggestion;

    @ApiModelProperty(value="已制品处理意见",required=false)
    @Column(name = "processed_suggestion", length = 1024)
    private String processedSuggestion;

    @ApiModelProperty(value="更改意见",required=false)
    @Column(name = "change_opinion", length = 1024)
    private String changeOpinion;

    @ApiModelProperty(value="生效时间",required=false,example="1994-03-07")
    @Column(name = "effective_date", length = 0)
    private LocalDateTime effectiveDate;

    @ApiModelProperty(value="同时更改的文件和资料",required=false)
    @Column(name = "change_at_once", length = 1024)
    private String changeAtOnce;

    @ApiModelProperty(value="版本号",required=false)
    @Column(name = "item_rev_id", length = 64)
    private String itemRevId;

    @ApiModelProperty(value="零组件id",required=false)
    @Column(name = "item_uid", length = 64)
    private String itemUid;

    @Override
    public String getObjectType(){
        return "ChangeAfterRL";
    }

    public String getRelationType(){
        return "ChangeAfterRL";
    }
}