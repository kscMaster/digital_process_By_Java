package com.nancal.esop.consts;

import io.swagger.annotations.ApiModelProperty;

/***
 * 同步ESOP数据需要的常量
 *
 * @author 徐鹏军
 * @date 2022/8/9 16:52
 */
public class EsopConst {

    @ApiModelProperty("sopb_product_bop表中的产品类型")
    public static final String sopb_product_bop$product_type = "QrU5VSwNZAawWD";
    @ApiModelProperty("sopb_product_bop表中的用户id")
    public static final String sopb_product_bop$user_id = "94638581984808960";
    @ApiModelProperty("sopb_product_bop表中的分片数量")
    public static final Integer sopb_product_bop$partition_num = 1;

    @ApiModelProperty("sopb_bop_mgmt表中的站名称")
    public static final String sopb_bop_mgmt$sit_name = "IPP";
    @ApiModelProperty("sopb_bop_mgmt表中的状态")
    public static final Integer sopb_bop_mgmt$bop_state = 0;

    @ApiModelProperty("sopb_operation表中的is_precise")
    public static final Integer sopb_operation$is_precise = 0;
    @ApiModelProperty("sopb_operation表中的is_copy")
    public static final Integer sopb_operation$is_copy = 0;
    @ApiModelProperty("sopb_operation表中的op_qty")
    public static final Integer sopb_operation$op_qty = 1;

    /*************************SOPB_OPERATION_RELATION********************************/
    @ApiModelProperty("前区工序")
    public static final Integer PREDECESSOR = 1;
    @ApiModelProperty("后区工序")
    public static final Integer SUCCESSOR = 2;
    @ApiModelProperty("设计零组件")
    public static final Integer DESIGN = 3;
    @ApiModelProperty("资源，工夹具，量具")
    public static final Integer RESOURCE = 4;
    @ApiModelProperty("辅材")
    public static final Integer ACCESSORY = 5;
    @ApiModelProperty("设备")
    public static final Integer EQUIPMENT = 6;

    /*************************SOPB_RESOURCE********************************/
    @ApiModelProperty("资源，工夹具，量具")
    public static final Integer RE_RESOURCE = 0;
    @ApiModelProperty("辅材")
    public static final Integer RE_ACCESSORY = 1;
    @ApiModelProperty("设备")
    public static final Integer RE_EQUIPMENT = 2;

    @ApiModelProperty("线体code")
    public static final String LINE_CODE = "A";
    public static final String PASSWORD_KEY = "@#$%^&*()OPG#$%^&*(HG";
    public static final String BASIC_NAME = "L1 WLAN";
}
