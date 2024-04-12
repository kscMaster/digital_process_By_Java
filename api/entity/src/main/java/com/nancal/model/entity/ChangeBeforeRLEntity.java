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
@Entity(name = "change_beforerl")
@ApiModel(value = "ChangeBeforeRL 更改前 的Entity")
public  class ChangeBeforeRLEntity  extends RelationEntity  implements Serializable{
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
    private LocalDate effectiveDate;
    @ApiModelProperty(value="版本号",required=false)
    @Column(name = "item_rev_id", length = 64)
    private String itemRevId;
    @ApiModelProperty(value="零组件id",required=false)
    @Column(name = "item_uid", length = 64)
    private String itemUid;

    public static final String ITEM_UID = "itemUid";

    @Override
    public String getObjectType() {
        return "ChangeBeforeRL";
    }

}