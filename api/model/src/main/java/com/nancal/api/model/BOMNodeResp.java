package com.nancal.api.model;


import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;


@ApiModel(value = "BOMNode BOM节点 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BOMNodeResp extends WorkspaceObjectResp{
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
    @JsonDict(DictConstant.OBJECT_TYPE_NAME)
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
    private List<BOMNodeResp> children;

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

    @ApiModelProperty(value="位号",required=false)
    private String tagNo;

    @ApiModelProperty(value="手动工时",required=false)
    private Integer manualTaskTime;

    @ApiModelProperty(value="总工时",required=false)
    private Integer taskTime;

    @Override
    public String getObjectType(){
       return "BOMNode";
    }


    public void add(BOMNodeResp resp) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(resp);
    }

}