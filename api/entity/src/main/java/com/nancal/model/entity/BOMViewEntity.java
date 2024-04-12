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
@Entity(name = "bomview")
@ApiModel(value = "BOMView BOM视图 的Entity")
public  class BOMViewEntity  extends WorkspaceObjectEntity  implements Serializable{
    @ApiModelProperty(value="是否精确",required=false,example="false")
    @Column(name = "is_precise", length = 0)
    private Boolean isPrecise;
    @ApiModelProperty(value="视图类型",required=false)
    @Column(name = "view_type", length = 64)
    private String viewType;


    public static final String IS_PRECISE="isPrecise";

    public static final String VIEW_TYPE="viewType";

    @Override
    public String getObjectType() {
        return "BOMView";
    }
}