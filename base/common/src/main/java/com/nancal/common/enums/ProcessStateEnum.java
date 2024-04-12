package com.nancal.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author： Wang Hui
 * @date： 2022/5/3 10:53
 * @description： 流程状态 0:已挂起; 1:进行中; 2:已完成
 **/
@AllArgsConstructor
@Getter
public enum ProcessStateEnum {
    Prepare("4","编制中"),
    Working("3", "工作中"),
    Suspended("0", "已挂起"),
    InJob("1", "进行中"),
    Released("2", "已完成");
    private String key;
    private String value;

    /***
     *根据key获取value
     *
     * @author: 王辉
     * @date: 2022/5/3 14:49
     * @return:  {@link String}
     */
    public static String getValueByKey(String code) {
        ProcessStateEnum[] processStateEnums = values();
        for (ProcessStateEnum processStateEnum : processStateEnums) {
            if (processStateEnum.getKey().equals(code)) {
                return processStateEnum.name();
            }
        }
        return ProcessStateEnum.Working.name();
    }
}
