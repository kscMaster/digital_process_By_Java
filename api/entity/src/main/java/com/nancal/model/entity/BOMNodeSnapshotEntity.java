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
@Entity(name = "bomnode_snapshot")
@ApiModel(value = "BOMNodeSnapshot bom行快照 的Entity")
public class BOMNodeSnapshotEntity extends BOMNodeEntity implements Serializable{

    public static final String SNAPSHOT_ID = "snapshotId";

    public static final String HIS_BOM_ID = "hisBomId";

    @ApiModelProperty(value="快照id",required=false)
    @Column(name = "snapshot_id", length = 64)
    private String snapshotId;

    @ApiModelProperty(value="历史bom行id",required=false)
    @Column(name = "his_bom_id", length = 64)
    private String hisBomId;

    public String getObjectType(){
        return "BOMNodeSnapshot";
    }

}