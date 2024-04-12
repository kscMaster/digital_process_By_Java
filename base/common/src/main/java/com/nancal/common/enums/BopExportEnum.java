package com.nancal.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * @author hewei
 * @date 2022/11/4 16:48
 * @Description
 */
@Getter
@AllArgsConstructor
public enum BopExportEnum{

    GREEN("green", HSSFColor.HSSFColorPredefined.LIGHT_GREEN.getIndex()),
    BLUE("1",HSSFColor.HSSFColorPredefined.BLUE.getIndex()),
    ORANGE("2",HSSFColor.HSSFColorPredefined.ORANGE.getIndex()),
    GREY("3",HSSFColor.HSSFColorPredefined.GREY_40_PERCENT.getIndex()),
    YELLOW("4",HSSFColor.HSSFColorPredefined.YELLOW.getIndex()),
    TAN("5",HSSFColor.HSSFColorPredefined.TAN.getIndex());



    private String code;
    private Short name;



    public static BopExportEnum getBopExportEnum(String code){
        for(BopExportEnum bopExportEnum : BopExportEnum.values()){
            if(StringUtils.equals(code, bopExportEnum.getCode())){
                return bopExportEnum;
            }
        }
        return null;
    }
}
