package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.io.Serializable;


@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "bom_history")
@ApiModel(value = "BomHistory bom查询历史 的Entity")
public class BomHistoryEntity extends WorkspaceObjectEntity implements Serializable{

    public static final String BOM_VIEW = "bomView";

    public static final String REV_UID = "revUid";

    public static final String REV_OBJECT_TYPE = "revObjectType";

    public static final String ITEM_ID = "itemId";

    @ApiModelProperty(value="编号",required=false)
    @Column(name = "item_id", length = 64)
    private String itemId;

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
    @Column(name = "revision_id", length = 64)
    private String revisionId;

    public String getObjectType(){
        return "BomHistory";
    }

}