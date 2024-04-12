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
@Entity(name = "tool_revision")
@ApiModel(value = "ToolRevision 工具版本 的Entity")
public  class ToolRevisionEntity  extends ItemRevisionEntity  implements Serializable{
    @ApiModelProperty(value="型号",required=false)
    @Column(name = "model_no", length = 64)
    private String modelNo;
    @ApiModelProperty(value="材料牌号",required=false)
    @Column(name = "material_trade_mark", length = 64)
    private String materialTradeMark;
    @ApiModelProperty(value="资源编号",required=false)
    @Column(name = "meterial_code", length = 64)
    private String meterialCode;
    @ApiModelProperty(value="图号",required=false)
    @Column(name = "part_no", length = 64)
    private String partNo;

    public String getObjectType() {
        return "ToolRevision";
    }
}