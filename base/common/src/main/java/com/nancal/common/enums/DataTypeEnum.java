package com.nancal.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * 操作动作枚举
 *
 * @author 徐鹏军
 * @date 2022/4/14 16:17
 */
@Getter
@AllArgsConstructor
public enum DataTypeEnum {

    CURRENT_DATA("currentData","当前数据"),
    PARENT_DATA("parentData","父级"),
    CURRENT_DATA_ITEM("currentDataItem","当前数据零件"),
    CURRENT_DATA_ITEMREVISION("currentDataItemRevision","当前数据零件版本"),
    ;
    private String code;
    private String name;
}
