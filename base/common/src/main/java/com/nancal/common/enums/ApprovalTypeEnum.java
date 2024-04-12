package com.nancal.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author： Wang Hui
 * @date： 2022/5/3 14:44
 * @description： 送审类型枚举
 **/
@Getter
@AllArgsConstructor
public enum ApprovalTypeEnum {

    RELEASE("release","发布"),
    RELEASE_ITEMS("releaseItems","发布组件"),
    RECOVERY("recovery","回收"),
    RECOVERY_ITEMS("recoveryItems","回收组件");

    private String key;
    private String value;

}
