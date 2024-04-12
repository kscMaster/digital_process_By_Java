package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.io.Serializable;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "item_revision")
@ApiModel(value = "ItemRevision 零组件版本 的Entity")
public  class ItemRevisionEntity  extends WorkspaceObjectEntity  implements Serializable{

    public static final String ITEM_ID = "itemId";

    public static final String REVISION_ID = "revisionId";

    public static final String UOM = "uom";

    public static final String SEQUENCE = "sequence";

    public static final String ITEM_REV_STATE = "itemRevState";

    public static final String ACTIVE = "active";

    public static final String PART_NO = "partNo";


    @ApiModelProperty(value="零组件号",required=false)
    @Column(name = "item_id", length = 64)
    private String itemId;
    @ApiModelProperty(value="版本号",required=false)
    @Column(name = "revision_id", length = 64)
    private String revisionId;
    @ApiModelProperty(value="单位",required=false)
    @Column(name = "uom", length = 64)
    private String uom;
    @ApiModelProperty(value="版次",required=false)
    @Column(name = "sequence", length = 64)
    private String sequence;
    @ApiModelProperty(value="零件状态",required=false)
    @Column(name = "item_rev_state", length = 64)
    private String itemRevState;
    @ApiModelProperty(value="是否激活",required=false)
    @Column(name = "active", length = 0)
    private Boolean active;
//    @ApiModelProperty(value="代图号",required=false)
//    @Column(name = "part_no", length = 64)
//    private String partNo;

    @Override
    public String getObjectType(){
        return "ItemRevision";
    }

}