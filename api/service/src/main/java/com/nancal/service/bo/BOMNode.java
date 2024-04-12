package com.nancal.service.bo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.nancal.api.model.BOMNodeResp;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.BOMNodeEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.stream.Collectors;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "BOMNode BOM节点 的BO")
public class BOMNode extends WorkspaceObject {

    @ApiModelProperty(value = "BOM视图")
    private String bomView;

    @ApiModelProperty(value = "父零组件")
    private String parentItem;

    @ApiModelProperty(value = "父零组件类型")
    private String parentItemType;

    @ApiModelProperty(value = "父零组件版本")
    private String parentItemRev;

    @ApiModelProperty(value = "子零组件,只有非精确BOM才使用，精确BOM时为空")
    private String childItem;

    @ApiModelProperty(value = "子零组件类型,只有非精确BOM才使用，精确BOM时为空")
    private String childItemType;

    @ApiModelProperty(value = "子零组件版本,只有是精确BOM才使用，非精确BOM为空")
    private String childItemRevision;

    @ApiModelProperty(value = "子零组件版本类型,只有是精确BOM才使用，非精确BOM为空")
    private String childItemTypeRevision;

    @ApiModelProperty(value = "数量")
    private Double quantity;

    @ApiModelProperty(value = "查找编号")
    private Integer foundNo;

    @ApiModelProperty(value = "备件数量")
    private Double r006BatQty;

    @ApiModelProperty(value = "工艺数量")
    private Double r006ProcessQty;

    @ApiModelProperty(value = "典试数量")
    private Double r006TestQty;

    @ApiModelProperty(value = "必换数量")
    private Double r006AltQty;

    @ApiModelProperty(value = "其他数量")
    private Double r006OtherQty;



    /**
     * 通过parent_item_rev查找BOMNODE信息
     * @param objectType
     * @param uid
     * @author: 薛锦龙
     * @time: 2022/6/21
     * @return: {@link List <  BOMNodeResp >}
     */
    public List<BOMNodeResp> getFirstNode(String objectType,List<String> uid,String type){
        OrderSpecifier order = new OrderSpecifier(Order.ASC, ExpressionUtils.path(BOMNodeEntity.class, BOMNodeEntity.FOUND_NO));
        List<WorkspaceObjectEntity> bomNodeEntity;
        if (StrUtil.isNotBlank(type)){
            bomNodeEntity = EntityUtil.getDynamicQuery(type, Triple.of(BOMNodeEntity.PARENT_ITEM, Ops.IN, uid)).orderBy(order).fetch();
        }else {
           bomNodeEntity = EntityUtil.getDynamicQuery(objectType, Triple.of(BOMNodeEntity.PARENT_ITEM_REV, Ops.IN, uid)).orderBy(order).fetch();
        }
        if (CollUtil.isEmpty(bomNodeEntity)){
            return CollUtil.newArrayList();
        }
        List<BOMNodeResp> resps = bomNodeEntity.stream().map(data -> {
            BOMNodeResp bomNodeResp = new BOMNodeResp();
            BOMNodeEntity bomNode = (BOMNodeEntity) data;
            BeanUtil.copyPropertiesIgnoreNull(bomNode, bomNodeResp);
            return bomNodeResp;
        }).collect(Collectors.toList());
        return resps;
    }
}
