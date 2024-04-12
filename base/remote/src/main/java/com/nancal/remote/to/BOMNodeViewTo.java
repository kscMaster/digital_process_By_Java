package com.nancal.remote.to;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.collections.map.HashedMap;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "BOPView BOP视图 的返回")
public class BOMNodeViewTo implements Serializable {

    @ApiModelProperty(value="工序/工步协作单位",required=false)
    private String gte4CoopOrgDesc;

    @ApiModelProperty(value="工序/工步类型",required=false)
    private String opOrGet4TypeDesc;

    @ApiModelProperty(value="工序类型",required=false)
    private String opType;

    @ApiModelProperty(value="是否关键工序",required=false,example="false")
    private Boolean isKey;

    @ApiModelProperty(value="并行工序号",required=false,example="false")
    private String parallelNum;
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
    private List<BOMNodeViewTo> children;

    @ApiModelProperty(value = "是否存在子集(true: 存在子集;  false：不存在子集)")
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
    @ApiModelProperty(value = "创建日期", example = "1994-03-07")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    @ApiModelProperty(value = "上次更新日期", example = "1994-03-07")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdate;

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
    @ApiModelProperty(value = "显示名称")
    private String displayName;

    @ApiModelProperty(value = "主键id")
    private String uid;


    @ApiModelProperty(value = "额外自定义参数", hidden = true)
    protected Map extraMap;

    public BOMNodeViewTo putExtraProperty(Object name, Object value) {
        if (name == null || value == null) {
            return this;
        }
        if (extraMap == null) {
            extraMap = new HashedMap();
        }
        extraMap.put(name, value);
        return this;
    }
    @JsonIgnore
    public <T> T getExtraProperty(Object name, T defaultValue) {
        T value = getExtraProperty(name);
        return value == null ? defaultValue : value;
    }
    @JsonIgnore
    public <T> T getExtraProperty(Object name) {
        return (name == null || extraMap == null) ? null : (T) extraMap.get(name);
    }

    @JsonIgnore
    public <T> T getExtraPropertyDesc(Object name) {
        return (name == null || extraMap == null) ? null : (T) extraMap.get(name+"Desc");
    }

}
