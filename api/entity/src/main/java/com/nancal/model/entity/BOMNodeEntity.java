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
@Entity(name = "bomnode")
@ApiModel(value = "BOMNode BOM节点 的Entity")
public  class BOMNodeEntity  extends WorkspaceObjectEntity implements Serializable{

    public static final String BOM_VIEW = "bomView";

    public static final String PARENT_ITEM = "parentItem";

    public static final String PARENT_ITEM_TYPE = "parentItemType";

    public static final String PARENT_ITEM_REV = "parentItemRev";

    public static final String CHILD_ITEM = "childItem";

    public static final String CHILD_ITEM_TYPE = "childItemType";

    public static final String CHILD_ITEM_REVISION = "childItemRevision";

    public static final String CHILD_ITEM_TYPE_REVISION = "childItemTypeRevision";

    public static final String QUANTITY = "quantity";

    public static final String FOUND_NO = "foundNo";

    public static final String NODE_TYPE = "nodeType";

    public static final String SEQUENCE = "sequence";

    public static final String BOMRELATIONVIEW = "bomRelationView";

    public static final String PARENT_ITEM_REV_ID = "parentItemRevId";

    @ApiModelProperty(value="BOM视图",required=false)
    @Column(name = "bom_view", length = 64)
    private String bomView;
    @ApiModelProperty(value="父零组件",required=false)
    @Column(name = "parent_item", length = 64)
    private String parentItem;
    @ApiModelProperty(value="父零组件类型",required=false)
    @Column(name = "parent_item_type", length = 64)
    private String parentItemType;
    @ApiModelProperty(value="父零组件版本",required=false)
    @Column(name = "parent_item_rev", length = 64)
    private String parentItemRev;
    @ApiModelProperty(value="子零组件",required=false)
    @Column(name = "child_item", length = 64)
    private String childItem;
    @ApiModelProperty(value="子零组件类型",required=false)
    @Column(name = "child_item_type", length = 64)
    private String childItemType;
    @ApiModelProperty(value="子零组件版本",required=false)
    @Column(name = "child_item_revision", length = 64)
    private String childItemRevision;
    @ApiModelProperty(value="子零组件版本类型",required=false)
    @Column(name = "child_item_type_revision", length = 64)
    private String childItemTypeRevision;
    @ApiModelProperty(value="数量",required=false)
    @Column(name = "quantity", length = 0)
    private Double quantity;
    @ApiModelProperty(value="查找编号",required=false)
    @Column(name = "found_no", length = 64)
    private Integer foundNo;
    @ApiModelProperty(value="节点类型",required=false)
    @Column(name = "node_type", length = 64)
    private String nodeType;
    @ApiModelProperty(value="业务流水号",required=false)
    @Column(name = "sequence_no", length = 64)
    private Integer sequenceNo;
    @ApiModelProperty(value="标记",required=false)
    @Column(name = "bom_relation_view", length = 64)
    private String bomRelationView;
    @ApiModelProperty(value="父零组件版本号",required=false)
    @Column(name = "parent_item_rev_id", length = 64)
    private String parentItemRevId;
    @ApiModelProperty(value="子零组件版本号",required=false)
    @Column(name = "child_item_rev_id", length = 64)
    private String childItemRevId;

    @ApiModelProperty(value="是否集件",required=false)
    @Column(name = "is_take_item", length = 0)
    private Boolean isTakeItem;

    @ApiModelProperty(value="位号",required=false)
    @Column(name = "tag_no", length = 64)
    private String tagNo;

    @ApiModelProperty(value="手动工时",required=false)
    @Column(name = "manual_task_time", length = 64)
    private Integer manualTaskTime;

    @ApiModelProperty(value="总工时",required=false)
    @Column(name = "task_time", length = 64)
    private Integer taskTime;

    public String getObjectType(){
        return "BOMNode";
    }

}