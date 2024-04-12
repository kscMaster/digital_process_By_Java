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
public enum OperatorEnum {

    Read("读"),
    Write("编辑"),
    Delete("删除"),
    ChangeOwner("更改所有权"),
    SubmitTrial("送审"),
    DownLoad("下载"),
    SaveAs("另存为"),
    Upgrade("升版"),
    ;
    private String desc;
}
