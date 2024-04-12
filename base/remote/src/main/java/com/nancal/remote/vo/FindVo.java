package com.nancal.remote.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FindVo implements Serializable {
    @ApiModelProperty(value="BOM视图",required=false)
    private String bomView;
    @ApiModelProperty(value="父零组件",required=false)
    private String parentItem;
    @ApiModelProperty(value="父零组件类型",required=false)
    private String parentItemType;
    @ApiModelProperty(value="父零组件版本",required=false)
    private String parentItemRev;
    @ApiModelProperty(value="子零组件",required=false)
    private String childItem;
    @ApiModelProperty(value="子零组件类型",required=false)
    private String childItemType;
    @ApiModelProperty(value="子零组件版本",required=false)
    private String childItemRevision;
    @ApiModelProperty(value="子零组件版本类型",required=false)
    private String childItemTypeRevision;
    @ApiModelProperty(value="数量",required=false)
    private Double quantity;
    @ApiModelProperty(value="查找编号",required=false)
    private Integer foundNo;
    @ApiModelProperty(value="节点类型",required=false)
    private String nodeType;
    @ApiModelProperty(value="业务流水号",required=false)
    private Integer sequenceNo;

    @ApiModelProperty(value="版本uid",required=false,example="false")
    private String revUid;

    @ApiModelProperty(value="版本类型",required=false,example="false")
    private String revObjectType;

    @ApiModelProperty(value="随机Uid，用于前端显示树",required=false,example="false")
    private String randomUid;

    @ApiModelProperty(value="子集列表",required=false,example="false")
    private List<FindVo> children;

    @ApiModelProperty(value = "是否存在子集")
    private boolean hasChildren;

    @ApiModelProperty(value="标记",required=false)
    private String bomRelationView;

    @ApiModelProperty(value = "层级，导出时使用")
    private String level;

    @ApiModelProperty(value="父零组件版本号",required=false)
    private String parentItemRevId="0";

    @ApiModelProperty(value="子零组件版本号",required=false)
    private String childItemRevId="0";

    @ApiModelProperty(value="是否集件",required=false)
    private String isTakeItem;

    @ApiModelProperty(value = "所有者用户ID", required = true)
    private String ownerId;

    @ApiModelProperty(value = "所有者用户名")
    private String ownerName;

    @ApiModelProperty(value = "对象名称")
    private String objectName;

    @ApiModelProperty(value = "描述")
    private String objectDesc;

    @ApiModelProperty(value = "对象类型")
    private String objectType;

    @ApiModelProperty(value = "生命周期状态")
    private String lifeCycleState;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "密级")
    private String secretLevel;

    @ApiModelProperty("左对象id")
    private String leftObject;

    @ApiModelProperty("左对象类型")
    private String leftObjectType;

    @ApiModelProperty("右对象id")
    private String rightObject;

    @ApiModelProperty("右对象类型")
    private String rightObjectType;

    @ApiModelProperty("保密期限")
    private String secretTerm;

    @ApiModelProperty("关联id")
    private String relationId;

    private Boolean resp_active;
    private String resp_creationDate;
    private String resp_creationUserId;
    private String resp_creationUsername;
    private String resp_displayName;
    private String resp_gte4Cat;
    private String resp_gte4CatDesc;
    private String resp_gte4DznBn;
    private String resp_gte4InitModel;
    private String resp_gte4InitModelDesc;
    private String resp_gte4MatSpec;
    private String resp_gte4MatTechStd;
    private String resp_gte4MaterialTradeMark;
    private String resp_gte4PDMOwningGroup;
    private String resp_gte4PDMOwnner;
    private String resp_gte4PartNo;
    private String resp_gte4Phase;
    private String resp_gte4PhaseDesc;
    private Boolean resp_hasDrawingCode;
    private String resp_itemId;
    private String resp_lastUpdate;
    private String resp_lastUpdateUserId;
    private String resp_lastUpdateUsername;
    private String resp_lifeCycleState;
    private String resp_lifeCycleStateDesc;
    private String resp_objectName;
    private String resp_objectType;
    private String resp_objectTypeDesc;
    private String resp_ownerId;
    private String resp_ownerName;
    private String resp_revisionId;
    private String resp_secretLevel;
    private String resp_secretLevelDesc;
    private String resp_secretTerm;
    private String resp_secretTermDesc;
    private String resp_sequence;
    private String resp_uid;
}
