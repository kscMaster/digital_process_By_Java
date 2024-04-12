package com.nancal.common.constants;

import io.swagger.annotations.ApiModelProperty;

/***
 * BOM相关常量类
 *
 * @author 拓凯
 * @date 2022/7/8
 */
public class BomConstant {
    @ApiModelProperty("工艺资源常量，无字典值配置")
    public static final String PROCESS_RESOURCES="processResources";

    @ApiModelProperty("查找编号起始")
    public static final String FOUNDNO_START="foundNo_start";
    @ApiModelProperty("查找编号步长")
    public static final String FOUNDNO_STEP="foundNo_step";
    @ApiModelProperty("业务号起始")
    public static final String SEQUENCE_START="sequence_start";
    @ApiModelProperty("业务号步长")
    public static final String SEQUENCE_STEP="sequence_step";


}
