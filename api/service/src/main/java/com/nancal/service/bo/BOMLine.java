package com.nancal.service.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "BOMLine BOM行 的BO",description = "BOM行对象没有对应的Entity，属性取自BOM节点和零组件版本")
public class BOMLine extends RuntimeBusinessObject {
    @ApiModelProperty(value = "对象名称，取自当前BOM行对应的零组件版本，显示名称请用displayName")
    private String blObjectName;

    @ApiModelProperty(value = "当前行零组件UID")
    private String blItem;

    @ApiModelProperty(value = "当前行零组件类型")
    private String blItemType;

    @ApiModelProperty(value = "当前行零组件版本UID")
    private String blItemRevision;

    @ApiModelProperty(value = "当前行零组件版本类型")
    private String blItemRevisionType;

    @ApiModelProperty(value = "当前行BOM节点")
    private String blBomNode;

    @ApiModelProperty(value = "父零组件版本")
    private String blParentItemRevision;

    @ApiModelProperty(value = "父零组件版本类型")
    private String blParentItemRevisionType;

    @ApiModelProperty(value = "查找编号")
    private String blFoundNo;

    @ApiModelProperty(value = "设计数量")
    private Integer blQuantity;

    @ApiModelProperty(value = "是否借用，当前行ItemRevision与父ItemRevision的都有r006_model_no属性且不相同时为借用")
    private Boolean blIsBorrow;

    @ApiModelProperty(value = "是否有子BOM行")
    private Boolean blHasChildren;

    @ApiModelProperty(value = "子BOM行")
    private List<BOMLine> blChildLines;

    @ApiModelProperty(value = "是否精确BOM")
    private Boolean blIsPrecise;

    @ApiModelProperty(value = "工艺路线")
    private String r006ProcessRoute;

    @ApiModelProperty(value = "阶段")
    private String r006Phase;

    @ApiModelProperty(value = "关重件")
    private String r006Key;

    @ApiModelProperty(value = "标准件规格")
    private String r006Specification;

    @ApiModelProperty(value = "主材名称")
    private String r006PriMaterialName;

    @ApiModelProperty(value = "主材牌号")
    private String r006PriMaterialTradeMark;

    @ApiModelProperty(value = "主材状态")
    private String r006PriMaterialStatus;

    @ApiModelProperty(value = "主材技术条件")
    private String r006PriMaterialTechCondition;

    @ApiModelProperty(value = "主材品种规格")
    private String r006PriMaterialVariSpec;

    @ApiModelProperty(value = "规格")
    private String r006PriMaterialSpecification;

    @ApiModelProperty(value = "材料定额尺寸")
    private String r006PriMaterialQuotaSize;

    @ApiModelProperty(value = "材料计量单位")
    private String r006PriMaterialUom;

    @ApiModelProperty(value = "材料单件毛质量")
    private String r006PriMaterialSingleM;

    @ApiModelProperty(value = "材料工艺余量尺寸")
    private String r006PriMaterialMarginSize;

    @ApiModelProperty(value = "材料工艺余量质量")
    private String r006PriMaterialMarginM;

    @ApiModelProperty(value = "每产品质量,根据尺寸和单位自动计算")
    private String r006PriMaterialInProductM;

    @ApiModelProperty(value = "物料编码")
    private String r006PriMaterialCode;

    @ApiModelProperty(value = "材料密度")
    private String r006MaterialDensity;


    @ApiModelProperty(value = "辅材名称")
    private String r006AuxMaterialName;

    @ApiModelProperty(value = "辅材牌号")
    private String r006AuxMaterialTradeMark;

    @ApiModelProperty(value = "辅材状态")
    private String r006AuxMaterialStatus;

    @ApiModelProperty(value = "辅材技术条件")
    private String r006AuxMaterialTechCondition;

    @ApiModelProperty(value = "辅材品种规格")
    private String r006AuxMaterialVariSpec;


    @ApiModelProperty(value = "工艺文件编号")
    private String r006FileNo;

    @ApiModelProperty(value = "发放单位")
    private String r006Unit;

    @ApiModelProperty(value = "主制单位")
    private String r006ManufacturingUnit;

    @ApiModelProperty(value = "标准件材料")
    private String r006MaterialStandard;

    @ApiModelProperty(value = "零件类型")
    private String r006ItemType;

    @ApiModelProperty(value = "制造类型")
    private String r006ManufacturingType;

    @ApiModelProperty(value = "备件数量")
    private Double r006BatQty;

    @ApiModelProperty(value = "工艺数量")
    private Double r006ProcessQty;

    @ApiModelProperty(value = "典试数量")
    private Double r006TestQty;

    @ApiModelProperty(value = "必换件数量")
    private Double r006AltQty;

    @ApiModelProperty(value = "其他数量")
    private Double r006OtherQty;
}
