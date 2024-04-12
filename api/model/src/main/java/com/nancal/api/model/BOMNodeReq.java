package com.nancal.api.model;

import cn.hutool.core.util.StrUtil;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.FidHistoryGroup;
import com.nancal.common.base.UpdateGroup;
import com.nancal.model.entity.BOMNodeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


@ApiModel(value = "BOMNode BOM节点 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BOMNodeReq extends WorkspaceObjectReq implements Serializable{

    @ApiModelProperty(value = "BOM行id", required = true)
    @NotBlank(message = "BOM行id不能为空",groups = {UpdateGroup.class})
    @Length(max = 128, message = "长度超过了最大长度限制",groups = {UpdateGroup.class})
    private String uid;

    @ApiModelProperty(value="备注")
    @Length(max = 512,message = "长度超过了最大限制",groups = {AddGroup.class, FidHistoryGroup.class, UpdateGroup.class})
    private String remark;

    @ApiModelProperty(value="BOM视图",required=false)
    @Length(max = 64)
    private String bomView;

    @NotBlank(message = "父节点id不能为空",groups = {AddGroup.class, FidHistoryGroup.class, Paste.class,appoint.class})
    @ApiModelProperty(value="父零组件",required=false)
    @Length(max = 64)
    private String parentItem;

    @NotBlank(message = "父节点类型不能为空",groups = {AddGroup.class, FidHistoryGroup.class, Paste.class,appoint.class})
    @ApiModelProperty(value="父零组件类型",required=false)
    @Length(max = 64)
    private String parentItemType;

    @NotBlank(message = "父节点版本id不能为空",groups = {AddGroup.class, FidHistoryGroup.class, Paste.class,appoint.class})
    @ApiModelProperty(value="父零组件版本",required=false)
    @Length(max = 64)
    private String parentItemRev;

    @NotBlank(message = "子零组件id不能为空",groups = {FidHistoryGroup.class, Paste.class,appoint.class})
    @ApiModelProperty(value="子零组件",required=false)
    @Length(max = 64)
    private String childItem;

    @NotBlank(message = "子零组件类型不能为空",groups = {FidHistoryGroup.class, Paste.class,appoint.class})
    @ApiModelProperty(value="子零组件类型",required=false)
    @Length(max = 64)
    private String childItemType;

    @NotBlank(message = "子零组件版本不能为空",groups = {FidHistoryGroup.class})
    @ApiModelProperty(value="子零组件版本",required=false)
    @Length(max = 64)
    private String childItemRevision;

    @NotBlank(message = "子零组件版本不能为空",groups = {FidHistoryGroup.class})
    @ApiModelProperty(value="子零组件版本类型",required=false)
    @Length(max = 64)
    private String childItemTypeRevision;

    @ApiModelProperty(value="子零组件版本Id",required=false)
    @Length(max = 64)
    private String childItemRevId;

    @ApiModelProperty(value="数量",required=false)
    private Double quantity;

    @NotNull(message = "查找编号不能为空",groups = {UpdateGroup.class})
    @ApiModelProperty(value="查找编号")
    private Integer foundNo;

    @ApiModelProperty(value="节点类型",required=false)
    @Length(max = 64)
    private String nodeType;

    @ApiModelProperty(value="业务流水号")
    private Integer sequenceNo;

    @NotBlank(message = "指派标记不能为空",groups = {appoint.class})
    @ApiModelProperty(value="标记",required=false)
    private String bomRelationView;

    @ApiModelProperty(value="是否集件",required=false)
    private String isTakeItem;

    @ApiModelProperty(value="来源App",required=false)
    private String sourceAppName;

    @ApiModelProperty(value="bom行uid",required=false)
    @Length(max = 64,message = "bom行uid长度超多最大长度限制")
    private String bomId;;

    @ApiModelProperty(value="手动工时",required=false)
    private Integer manualTaskTime;

    @ApiModelProperty(value="总工时",required=false)
    private Integer taskTime;

    @ApiModelProperty(value="位号",required=false)
    @Length(max = 64,message = "位号长度超多最大长度限制")
    private String tagNo;

    private String parentItemRevId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BOMNodeEntity)) {
            return false;
        }
        BOMNodeEntity that = (BOMNodeEntity) o;
        return StrUtil.equals(StrUtil.blankToDefault(uid,StrUtil.EMPTY), that.getUid())
                && StrUtil.equals(StrUtil.blankToDefault(remark,StrUtil.EMPTY), that.getRemark())
                && StrUtil.equals(StrUtil.blankToDefault(bomView,StrUtil.EMPTY), that.getBomView())
                && StrUtil.equals(StrUtil.blankToDefault(parentItem,StrUtil.EMPTY), that.getParentItem())
                && StrUtil.equals(StrUtil.blankToDefault(parentItemType,StrUtil.EMPTY), that.getParentItemType())
                && StrUtil.equals(StrUtil.blankToDefault(parentItemRev,StrUtil.EMPTY), that.getParentItemRev())
                && StrUtil.equals(StrUtil.blankToDefault(childItem,StrUtil.EMPTY), that.getChildItem())
                && StrUtil.equals(StrUtil.blankToDefault(childItemType,StrUtil.EMPTY), that.getChildItemType())
                && StrUtil.equals(StrUtil.blankToDefault(childItemRevision,StrUtil.EMPTY), that.getChildItemRevision())
                && StrUtil.equals(StrUtil.blankToDefault(childItemTypeRevision,StrUtil.EMPTY), that.getChildItemTypeRevision())
                && StrUtil.equals(StrUtil.blankToDefault(String.valueOf(quantity),StrUtil.EMPTY), String.valueOf(that.getQuantity()))
                && StrUtil.equals(StrUtil.blankToDefault(String.valueOf(foundNo),StrUtil.EMPTY), String.valueOf(that.getFoundNo()))
                && StrUtil.equals(StrUtil.blankToDefault(nodeType,StrUtil.EMPTY), that.getNodeType())
                && StrUtil.equals(StrUtil.blankToDefault(String.valueOf(sequenceNo),StrUtil.EMPTY), String.valueOf(that.getSequenceNo()))
                ;
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), uid, remark, bomView, parentItem, parentItemType, parentItemRev, childItem, childItemType, childItemRevision, childItemTypeRevision, quantity, foundNo, nodeType, sequenceNo);
    }

    public interface Paste { }

    public interface appoint { }

}