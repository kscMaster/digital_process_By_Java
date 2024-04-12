package com.nancal.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.codec.binary.StringUtils;

/**
 * @author： Wang Hui
 * @date： 2022/4/26 20:27
 * @description：应用程序的枚举
 **/
@Getter
@AllArgsConstructor
public enum AppNameEnum {
    MY_WORKSPACE("lz624-myworkspace", "乐造-数研-个人工作台"),
    INSPECTION_LIBRARY("lz624-inspection-library", "乐造-数研-检测项目库"),
    LIBRARY("lz624-library", "乐造-数研-基础库管理"),
    EBOM("lz624-ebom", "乐造-数研-EBOM管理"),
    MSGBOM("lz624-msgbom", "乐造-数研-工艺规程管理"),
    DOCUMENTBOM("lz624-document", "乐造-数研-技术文档管理"),
    HOME("lz624-home", "乐造-数研-HOME管理"),
    FOLLOW_MSGBOM("lz624-follow-msgbom","乐造-数研-随工工艺规程管理"),
    MIDBOM("lz624-midbom","乐造-数研-中间BOM管理"),
//    PBOM("lz624-mbom","乐造-数研-PBOM管理"),
    MBOM("lz624-mbom","乐造-数研-MBOM管理"),
    PLANT("lz624-plant","乐造-数研-工厂管理"),
    CHANGE_ORDER("lz624-changeorder","乐造-数研-更改单管理"),
    IMEXPORT("lz624-imexport","乐造-数研-导入导出管理");

    private String code;
    private String name;

    public static AppNameEnum getAppNameEnum(String code){
        for(AppNameEnum appNameEnum : AppNameEnum.values()){
            if(StringUtils.equals(code, appNameEnum.getCode())){
                return appNameEnum;
            }
        }
        return null;
    }
}
