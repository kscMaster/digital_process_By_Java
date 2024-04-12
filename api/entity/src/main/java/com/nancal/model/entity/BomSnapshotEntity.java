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
@Entity(name = "bom_snapshot")
@ApiModel(value = "BomSnapshot bom快照 的Entity")
public class BomSnapshotEntity extends WorkspaceObjectEntity implements Serializable{

    public static final String BOM_VIEW = "bomView";

    public static final String REV_UID = "revUid";

    public static final String REV_OBJECT_TYPE = "revObjectType";

    public static final String REVISION_ID = "revisionId";

    public static final String ITEM_ID = "itemId";

    public static final String ITEM_UID = "itemUid";

    @ApiModelProperty(value="BOM视图",required=false)
    @Column(name = "bom_view", length = 128)
    private String bomView;

    @ApiModelProperty(value="查询的版本uid",required=false)
    @Column(name = "rev_uid", length = 64)
    private String revUid;

    @ApiModelProperty(value="查询的对象类型",required=false)
    @Column(name = "rev_object_type", length = 64)
    private String revObjectType;

    @ApiModelProperty(value="版本号",required=false)
    @Column(name = "revision_id", length = 65)
    private String revisionId;

    @ApiModelProperty(value="零件id",required=false)
    @Column(name = "item_id", length = 66)
    private String itemId;

    @ApiModelProperty(value="零件uid",required=false)
    @Column(name = "item_uid", length = 64)
    private String itemUid;

    public String getObjectType(){
        return "BomSnapshot";
    }

}