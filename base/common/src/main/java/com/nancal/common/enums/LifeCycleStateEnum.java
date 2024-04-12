package com.nancal.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum LifeCycleStateEnum {

    Working("工作中"),
    InJob("流程中"),
    Released("已发布"),
    Obsolete("已作废"),
    Prepare("编制中");

    private String desc;

    /***
     * 根据状态获取对应的描述
     *
     * @param state 状态
     * @author 徐鹏军
     * @date 2022/4/12 9:21
     * @return {@link String}
     */
    public static String getDescByState(String state) {
        return Arrays.stream(LifeCycleStateEnum.values())
                .filter(item -> item.name().equalsIgnoreCase(state))
                .findFirst()
                .orElse(null)
                .getDesc();
    }
    /***
     * 根据描述获取对应的状态
     *
     * @param desc 描述
     * @author 徐鹏军
     * @date 2022/4/12 9:21
     * @return {@link String}
     */
    public static String getStateByDesc(String desc) {
        return Arrays.stream(LifeCycleStateEnum.values())
                .filter(item -> item.getDesc().equalsIgnoreCase(desc))
                .findFirst()
                .orElse(null)
                .getDesc();
    }
}
