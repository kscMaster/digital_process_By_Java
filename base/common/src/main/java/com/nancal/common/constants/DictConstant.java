package com.nancal.common.constants;

import io.swagger.annotations.ApiModelProperty;

/***
 * 字典相关常量类
 *
 * @author 徐鹏军
 * @date 2022/4/11 9:26
 */
public class DictConstant {

    @ApiModelProperty("型号")
    public static final String MODEL = "ModelLOV";
    @ApiModelProperty("密级")
    public static final String SECURITY_CLASS_IFI = "SecurityClassifiLOV";
    @ApiModelProperty("阶段")
    public static final String PHASE = "PhaseLOV";
    @ApiModelProperty("关重件")
    public static final String IMP_KEY = "ImpKeyLOV";
    @ApiModelProperty("标准件规格")
    public static final String GB = "GBLOV";
    @ApiModelProperty("标准件材料")
    public static final String GB_MATERIALS = "GBMaterialsLOV";
    @ApiModelProperty("主材名称")
    public static final String MM_NAME = "MMnameLOV";
    @ApiModelProperty("品种规格")
    public static final String VARIETY_SPEC = "VarietySPECLOV";
    @ApiModelProperty("规格")
    public static final String SPEC = "SPECLOV";
    @ApiModelProperty("计量单位")
    public static final String UNIT = "Unit";
    @ApiModelProperty("辅材牌号 已暂停使用")
    @Deprecated
    public static final String AUX_MATERIAL_NUM = "AuxilMateriNumLOV";
    @ApiModelProperty("辅材技术条件 已暂停使用")
    @Deprecated
    public static final String TECHNICAL_CONDITION = "TechnicalConditionLOV";
    @ApiModelProperty("辅材规格 已暂停使用")
    @Deprecated
    public static final String AM_SPEC = "AMSPECLOV";
    @ApiModelProperty("物料编码")
    public static final String MATERIAL_CODE = "MaterialCodeLOV";

    @ApiModelProperty("生命周期")
    public static final String LIFECYCLE_STATE = "LifeCycleState_lov";
    @ApiModelProperty("对象类型名称")
    public static final String OBJECT_TYPE_NAME = "ObjectTypeName";
    @ApiModelProperty("车间")
    public static final String WORK_SHOP = "WorkshopLOV";
    @ApiModelProperty("型号")
    public static final String R006_MODEL_NO = "ModelLOV";
    @ApiModelProperty("状态")
    public static final String LAUNCH_TYPE = "LaunchTypeLOV";

    @ApiModelProperty("零件类型")
    public static final String PART_TYPE = "PartTypeLOV";
    @ApiModelProperty("制造类型")
    public static final String MANUFACTURING_TYPE = "ManufacturingTypeLOV";

    @ApiModelProperty("主制单位")
    public static final String WORKSHOP_TYPE = "WorkshopLOV";

    @ApiModelProperty("工序类型")
    public static final String OP_TYPE = "opType";

    @ApiModelProperty("工步类型")
    public static final String GTE4_TYPE = "gte4Type";

    @ApiModelProperty("协作单位")
    public static final String GTE4_COOPORG = "gte4CoopOrg";

    @ApiModelProperty("boolean类型")
    public static final String BOOLEAN_TYPE = "booleanType";

    @ApiModelProperty("特性分类")
    public static final String GTE4_CAT = "gte4Cat";

    @ApiModelProperty("保密期限")
    public static final String SECRET_TERM = "secretTerm";

    @ApiModelProperty("初始型号")
    public static final String GTE4_INIT_MODEL = "gte4InitModel";


    @ApiModelProperty("测量方式")
    public static final String GTE4_MEAS_MTHD = "gte4MeasMthd";


    @ApiModelProperty("测量类型")
    public static final String GTE4_MEAS_TYPE = "gte4MeasType";


    @ApiModelProperty("测量值计算要求")
    public static final String GTE4_MEAS_CALC_REQ = "gte4MeasCalcReq";

    @ApiModelProperty("阶段标识")
    public static final String GTE4_PHASE = "gte4Phase";

    @ApiModelProperty("工艺规程结构")
    public static final String GTE4_MFGPROCESS_BOMSTRUCTURE="Gte4MfgProcess_bomStructure";

    @ApiModelProperty("工厂工艺bom结构")
    public static final String GTE4_MFGPLANTPR_BOMSTRUCTURE="Gte4MfgPlantPr_bomStructure";

    @ApiModelProperty("624EBOM")
    public static final String GTE4_PART_BOMSTRUCTURE="Gte4Part_bomStructure";

    @ApiModelProperty("MBOM")
    public static final String GTE4PROCESS_BOMSTRUCTURE="Gte4Process_bomStructure";

    @ApiModelProperty("工序bom信息")
    public static final String GTE4_MFGOPERATION_BOM="Gte4MfgOperation_bom";

    @ApiModelProperty("工步bom信息")
    public static final String GTE4_MFGSTEP_BOM="Gte4MfgStep_bom";

    @ApiModelProperty("设计零件bom信息")
    public static final String GTE4_PART_BOM="Gte4Part_bom";

    @ApiModelProperty("工艺规程克隆信息")
    public static final String GTE4_MFGPROCESS_CLONESTRUCTURE="Gte4MfgProcess_cloneStructure";

    @ApiModelProperty("工序克隆信息")
    public static final String GTE4_MFGOPERATION_CLONESTRUCTURE="Gte4MfgOperation_cloneStructure";

    @ApiModelProperty("工步克隆信息")
    public static final String GTE4_MFGSTEP_CLONESTRUCTURE="Gte4MfgStep_cloneStructure";

    @ApiModelProperty("技术文档-文件类型")
    public static final String GTE4_FILE_TYPE="gte4FileType";

    @ApiModelProperty("技术文档bom信息")
    public static final String GTE4_TECH_DOCUMENT_BOM="Gte4TechDocument_bom";

    @ApiModelProperty("技术文档结构")
    public static final String GTE4_DOCUMENT_BOMSTRUCTURE="Gte4Document_bomStructure";

    @ApiModelProperty("随工工艺规程结构")
    public static final String GTE4_CNSTRPROCESS_BOMSTRUCTURE="Gte4CnstrProcess_bomStructure";

    @ApiModelProperty("分区")
    public static final String GTE4_PARTITION="partitionCode";

    @ApiModelProperty("产品名称")
    public static final String GTE4_PRODUCTNAME ="productCode";

    @ApiModelProperty("工厂结构")
    public static final String GTE4_PLANT_BOMSTRUCTURE="Gte4Plant_bomStructure";

    @ApiModelProperty("工序类型")
    public static final String GTE4_PROCESS_CLASSIFICATION="gte4ProcessClassification";

    @ApiModelProperty("工厂类型")
    public static final String GTE4_PLANT_TYPE="gte4PlantType";

    @ApiModelProperty("工厂性质")
    public static final String GTE4_PLANT_NATURE="gte4PlantNature";

    @ApiModelProperty("MES组织代号")
    public static final String MES_CODE="MESCode";

    @ApiModelProperty("所属部门")
    public static final String GTE4_DEPARTMENT_NAME="gte4DepartmentName";

    @ApiModelProperty("所属部门")
    public static final String GTE4_STATION_TYPE="gte4StationType";

    @ApiModelProperty("bom快捷栏历史记录展示条数")
    public static final String BOM_HISTORY_NUMBER="bomHistoryNumber";

    @ApiModelProperty("线体性质")
    public static final String GTE4_LINE_TYPE="gte4LineType";

    @ApiModelProperty("更改类型")
    public static final String CHANGE_TYPE="change_type";

    @ApiModelProperty("设计更改比对")
    public static final String PART_CHANGE_COMPARE="Gte4PartChange_compare";


    @ApiModelProperty("辅料-补充字段")
    public static final String AUXILIARY_MATERIAL_EXTRADATA="AuxiliaryMaterial_ExtraData";

    @ApiModelProperty("设备-补充字段")
    public static final String EQUIPMENT_EXTRADATA="Equipment_ExtraData";

    @ApiModelProperty("量具-补充字段")
    public static final String MEASURE_EXTRADATA="Measure_ExtraData";

    @ApiModelProperty("工夹具-补充字段")
    public static final String TOOL_EXTRADATA="Tool_ExtraData";


    @ApiModelProperty("导入类型")
    public static final String IMEXPORTTASK_OPERATIONTYPE="ImexportTask_operationType";

    @ApiModelProperty("状态")
    public static final String IMEXPORTTASK_OPERATIONSTATUS="ImexportTask_operationStatus";

    @ApiModelProperty("应用名称")
    public static final String IMEXPORTTASK_APPCODE="ImexportTask_appCode";

    @ApiModelProperty("工艺规划导出列设置")
    public static final String IMEXPORTTASK_BOP_EXPORT="imexporttask_bop_export";
}
