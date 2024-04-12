package com.nancal.api.model;

import com.nancal.framework.common.annotations.JsonDict;
import com.nancal.common.constants.DictConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "FindResp BOM首页查询结果的响应")
public class PbomNodeViewResp extends BusinessObjectResp {
    /******************************************** BOM节点信息 ********************************************/
    @ApiModelProperty(value = "是否虚拟节点,true是，false否")
    private Boolean bl_isFictitious;

    @ApiModelProperty(value = "当前行BOM节点uid")
    private String bl_uid;

    @ApiModelProperty(value = "当前行零组件UID")
    private String bl_childItem;

    @ApiModelProperty(value = "当前行零组件类型")
    private String bl_childItemType;

    @ApiModelProperty(value = "当前行零组件版本UID")
    private String bl_childItemRevision;

    @ApiModelProperty(value = "当前行零组件版本类型")
    private String bl_childItemTypeRevision;

    @ApiModelProperty(value = "父零组件uid")
    private String bl_parentItem;

    @ApiModelProperty(value = "父零组件类型")
    private String bl_parentItemType;

    @ApiModelProperty(value = "父零组件版本uid")
    private String bl_parentItemRev;

    @ApiModelProperty(value = "查找编号")
    private String bl_foundNo;

    @ApiModelProperty(value = "设计数量")
    private Integer bl_quantity;

    @ApiModelProperty(value = "每产品质量,根据尺寸和单位自动计算")
    private String bl_r006MaterialInProductM;

    @ApiModelProperty(value = "备件数量")
    private Integer bl_r006BatQty;

    @ApiModelProperty(value = "工艺数量")
    private Integer bl_r006ProcessQty;

    @ApiModelProperty(value = "典试数量")
    private Integer bl_r006TestQty;

    @ApiModelProperty(value = "必换数量")
    private Integer bl_r006AltQty;

    @ApiModelProperty(value = "其他数量")
    private Integer bl_r006OtherQty;

    @ApiModelProperty(value = "产品数量")
    private Integer bl_r006ProductQty;

    /******************************************** 组件版本信息 ********************************************/
    @JsonDict(DictConstant.PHASE)
    @ApiModelProperty(value = "阶段")
    private String pt_r006Phase;

    @JsonDict(DictConstant.IMP_KEY)
    @ApiModelProperty(value = "关重件")
    private String pt_r006Key;

    @ApiModelProperty(value = "标准件规格")
    @JsonDict(DictConstant.GB)
    private String pt_r006Specification;

    @ApiModelProperty(value = "工艺文件编号")
    private String pt_r006FileNo;

    @ApiModelProperty(value = "发放单位")
    private String pt_r006Unit;

    @ApiModelProperty(value = "主制单位")
    private String pt_r006ManufacturingUnit;

    @ApiModelProperty(value = "标准件材料")
    @JsonDict(DictConstant.GB_MATERIALS)
    private String pt_r006MaterialStandard;

    @ApiModelProperty(value = "零件类型")
    @JsonDict(DictConstant.PART_TYPE)
    private String pt_r006ItemType;

    @ApiModelProperty(value = "制造类型")
    @JsonDict(DictConstant.MANUFACTURING_TYPE)
    private String pt_r006ManufacturingType;

    @ApiModelProperty(value = "是否顶层")
    private Boolean pt_r006IsTop;

    @JsonDict(DictConstant.MODEL)
    @ApiModelProperty(value = "型号")
    private String pt_r006ModelNo;

    @ApiModelProperty(value = "代(图)号")
    private String pt_itemId;

    @ApiModelProperty(value = "版本号")
    private String pt_revisionId;

    @ApiModelProperty(value = "版次")
    private String pt_sequence;

    @ApiModelProperty(value = "对象名称")
    private String pt_objectName;

    @ApiModelProperty(value = "描述")
    private String pt_objectDesc;

    @ApiModelProperty(value = "版本id")
    private String pt_uid;

    @JsonDict(DictConstant.OBJECT_TYPE_NAME)
    @ApiModelProperty(value = "对象类型")
    private String pt_objectType;

    @JsonDict(DictConstant.LIFECYCLE_STATE)
    @ApiModelProperty(value = "生命周期状态")
    private String pt_lifeCycleState;

    @ApiModelProperty(value = "备注")
    private String pt_remark;

    @JsonDict(DictConstant.SECURITY_CLASS_IFI)
    @ApiModelProperty(value = "密级")
    private String pt_secretLevel;

    @ApiModelProperty(value = "显示名称")
    private String pt_displayName;

    /******************************************** 标准件信息 ********************************************/
    @JsonDict(DictConstant.PHASE)
    @ApiModelProperty(value = "阶段")
    private String standard_r006Phase;

    @JsonDict(DictConstant.IMP_KEY)
    @ApiModelProperty(value = "关重件")
    private String standard_r006Key;

    @ApiModelProperty(value = "标准件规格")
    @JsonDict(DictConstant.GB)
    private String standard_r006Specification;

    @ApiModelProperty(value = "工艺文件编号")
    private String standard_r006FileNo;

    @ApiModelProperty(value = "发放单位")
    private String standard_r006Unit;

    @ApiModelProperty(value = "主制单位")
    private String standard_r006ManufacturingUnit;

    @ApiModelProperty(value = "标准件材料")
    @JsonDict(DictConstant.GB_MATERIALS)
    private String standard_r006MaterialStandard;

    @ApiModelProperty(value = "零件类型")
    private String standard_r006ItemType;

    @ApiModelProperty(value = "制造类型")
    private String standard_r006ManufacturingType;

    @ApiModelProperty(name = "工艺路线")
    private String standard_r006Route;

    @JsonDict(DictConstant.MODEL)
    @ApiModelProperty(value = "型号")
    private String standard_r006ModelNo;

    @ApiModelProperty(value = "标准号")
    private String standard_itemId;

    @ApiModelProperty(value = "版本号")
    private String standard_revisionId;

    @ApiModelProperty(value = "版次")
    private String standard_sequence;

    @ApiModelProperty(value = "对象名称")
    private String standard_objectName;

    @ApiModelProperty(value = "描述")
    private String standard_objectDesc;

    @ApiModelProperty(value = "版本id")
    private String standard_uid;

    @JsonDict(DictConstant.OBJECT_TYPE_NAME)
    @ApiModelProperty(value = "对象类型")
    private String standard_objectType;

    @JsonDict(DictConstant.LIFECYCLE_STATE)
    @ApiModelProperty(value = "生命周期状态")
    private String standard_lifeCycleState;

    @ApiModelProperty(value = "备注")
    private String standard_remark;

    @JsonDict(DictConstant.SECURITY_CLASS_IFI)
    @ApiModelProperty(value = "密级")
    private String standard_secretLevel;

    @ApiModelProperty(value = "显示名称")
    private String standard_displayName;

    /******************************************** 工艺路线信息 ********************************************/

    @ApiModelProperty(name = "工艺路线")
    private String route_r006Route;

    @ApiModelProperty(value = "版本号")
    private String route_revisionId;

    @ApiModelProperty(value = "版次")
    private String route_sequence;

    @ApiModelProperty(value = "对象名称")
    private String route_objectName;

    @ApiModelProperty(value = "描述")
    private String route_objectDesc;

    @ApiModelProperty(value = "版本id")
    private String route_uid;

    @JsonDict(DictConstant.OBJECT_TYPE_NAME)
    @ApiModelProperty(value = "对象类型")
    private String route_objectType;

    @JsonDict(DictConstant.LIFECYCLE_STATE)
    @ApiModelProperty(value = "生命周期状态")
    private String route_lifeCycleState;

    @ApiModelProperty(value = "备注")
    private String route_remark;

    @JsonDict(DictConstant.SECURITY_CLASS_IFI)
    @ApiModelProperty(value = "密级")
    private String route_secretLevel;

    @ApiModelProperty(value = "显示名称")
    private String route_displayName;

    /******************************************** 主材版本信息 ********************************************/
    @ApiModelProperty(value = "材料牌号")
    private String process_r006MaterialTradeMark;

    @ApiModelProperty(value = "材料状态")
    private String process_r006MaterialStatus;

    @ApiModelProperty(value = "材料技术条件")
    private String process_r006MaterialTechCondition;

    @JsonDict(DictConstant.VARIETY_SPEC)
    @ApiModelProperty(value = "材料品种规格")
    private String process_r006MaterialVariSpec;

    @JsonDict(DictConstant.SPEC)
    @ApiModelProperty(value = "规格")
    private String process_r006MaterialSpecification;

    @ApiModelProperty(value = "材料定额尺寸")
    private String process_r006MaterialQuotaSize;

    @JsonDict(DictConstant.UNIT)
    @ApiModelProperty(value = "材料计量单位")
    private String process_r006MaterialUom;

    @ApiModelProperty(value = "材料单件毛质量")
    private String process_r006MaterialSingleM;

    @ApiModelProperty(value = "材料工艺余量尺寸")
    private String process_r006MaterialMarginSize;

    @ApiModelProperty(value = "材料工艺余量质量")
    private String process_r006MaterialMarginM;

    @JsonDict(DictConstant.MATERIAL_CODE)
    @ApiModelProperty(value = "物料编码")
    private String process_r006MaterialCode;

    @ApiModelProperty(value = "材料密度")
    private String process_r006MaterialDensity;

    @ApiModelProperty(value = "代(图)号")
    private String process_itemId;

    @ApiModelProperty(value = "版本号")
    private String process_revisionId;

    @ApiModelProperty(value = "版次")
    private String process_sequence;

    @ApiModelProperty(value = "对象名称")
    private String process_objectName;

    @ApiModelProperty(value = "描述")
    private String process_objectDesc;

    @ApiModelProperty(value = "版本id")
    private String process_uid;

    @JsonDict(DictConstant.OBJECT_TYPE_NAME)
    @ApiModelProperty(value = "对象类型")
    private String process_objectType;

    @JsonDict(DictConstant.LIFECYCLE_STATE)
    @ApiModelProperty(value = "生命周期状态")
    private String process_lifeCycleState;

    @ApiModelProperty(value = "备注")
    private String process_remark;

    @JsonDict(DictConstant.SECURITY_CLASS_IFI)
    @ApiModelProperty(value = "密级")
    private String process_secretLevel;

    @ApiModelProperty(value = "显示名称")
    private String process_displayName;

    /******************************************** 辅材版本信息 ********************************************/
    @JsonDict(DictConstant.AUX_MATERIAL_NUM)
    @ApiModelProperty(value = "辅材牌号,目前已itemId为准吧")
    private String aux_r006TradeMark;

    @JsonDict(DictConstant.TECHNICAL_CONDITION)
    @ApiModelProperty(value = "技术条件")
    private String aux_r006TechCondition;

    @JsonDict(DictConstant.AM_SPEC)
    @ApiModelProperty(value = "规格")
    private String aux_r006Specification;

    @ApiModelProperty(value = "单发次用量")
    private String aux_r006SingleShotDose;

    @JsonDict(DictConstant.UNIT)
    @ApiModelProperty(value = "计量单位")
    private String aux_r006Unit;

    @ApiModelProperty(value = "辅材牌号")
    private String aux_itemId;

    @ApiModelProperty(value = "版本号")
    private String aux_revisionId;

    @ApiModelProperty(value = "版次")
    private String aux_sequence;

    @ApiModelProperty(value = "辅材版本id")
    private String aux_uid;

    @ApiModelProperty(value = "对象名称")
    private String aux_objectName;

    @ApiModelProperty(value = "描述")
    private String aux_objectDesc;

    @JsonDict(DictConstant.OBJECT_TYPE_NAME)
    @ApiModelProperty(value = "对象类型")
    private String aux_objectType;

    @JsonDict(DictConstant.LIFECYCLE_STATE)
    @ApiModelProperty(value = "生命周期状态")
    private String aux_lifeCycleState;

    @ApiModelProperty(value = "备注")
    private String aux_remark;

    @JsonDict(DictConstant.SECURITY_CLASS_IFI)
    @ApiModelProperty(value = "密级")
    private String aux_secretLevel;

    @ApiModelProperty(value = "显示名称")
    private String aux_displayName;

}
