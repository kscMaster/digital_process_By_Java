package com.nancal.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ActionEnum {
    //***************************************** 基础数据服务 *****************************************
    create("根据对象入参创建实例数据"),
    batchCreate("根据对象入参批量创建实例数据"),
    delete("根据对象入参删除实例数据"),
    batchDelete("根据对象ID批量删除实例数据"),
    logicalDelete("根据对象ID将实例数据软删除"),
    batchLogicalDelete("根据对象ID批量将实例数据软删除"),
    deleteByCondition("根据查询条件将查询结果进行删除"),
    logicalDeleteByCondition("根据查询条件将查询结果进行软删除"),
    update("仅更新入参传入的字段信息，当ID不存在时，不处理传入的实例信息"),
    batchUpdate("根据入参批量更新指定属性，当ID不存在时，不处理传入的实例信息"),
    updateByCondition("将查询条件结果按照入参进行更新"),
    find("根据数据实体实例数据入参精确查询实例数据且支持分页"),// {pageSize}/{curPage}
    query("根据对象list属性查询对象且支持分页查询"),// {pageSize}/{curPage}
    statics("支持分组和简单函数统计"),
    count("根据对象入参属性查询版本全量属性返回总数"),
    get("根据对象ID获取对象详细信息"),
    batchGet("根据对象ID批量获取对象详细信息"),
    save("根据对象的唯一键， 更新对象入参传入的字段信息"),
    saveAll("根据对象的唯一键，更新对象所有字段信息，建议为入参为所有字段信息，如果入参没有传入，则更新为空"),
    select("根据查询条件及指定属性分页返回（不支持扩展属性作为选定属性列)"),// {pageSize}/{curPage}
    list("根据查询条件分页返回模型基本属性信息且不级联查询（不支持扩展属性作为查询条件）"),// {pageSize}/{curPage}

    //***************************************** 版本服务 *****************************************
    updateByAdmin("更新主对象+版本对象入参传入的字段信"),
    batchUpdateByAdmin("批量更新主对象+版本对象入参传入的字段信息 API信息"),
    batchUpdateVersion("根据ID批量更新版本属性"),
    getAllVersions("根据主对象ID，获取全量版本以及对应版本对象list属性"),// /{pageSize}/{curPage}
    getVersionByMaster("根据Masterid和版本号和小版本号，返回对应版本属性，小版本号为空则返回最新小版本属性"),
    checkout("根据主对象ID检出版本对象，复制生成一条新的版本记录且状态为已检出"),
    batchCheckout("根据主对象ID批量检出版本对象"),
    undoCheckout("根据主对象ID撤销检出版本对象，删除新的版本记录且状态为已检入"),
    batchUndoCheckout("根据主对象ID批量撤销检出版本对象"),
    checkin("根据主对象ID检入版本对象，按照设置的规则生成新的业务版本"),
    batchCheckin("根据主对象ID批量检入版本对象，小版本升版"),
    revise("根据主对象ID修订对象，按照设置的规则生成新的业务版本"),
    batchRevise("根据主对象ID批量修订对象"),
    deleteLatestVersion("据主对象ID入参，删除最新版本对象"),
    logicalDeleteLatestVersion("根据主对象ID入参，软删除最新版本对"),
    deleteBranch("根据masterid&version删除指定大版本下的所有小版本"),
    logicalDeleteBranch("软删除对应分支版本下的所有小版本"),
    compareBusinessVersion("根据主对象id，输入版本号（大版本+小版本）进行版本属性与关系对比"),
    undoCheckoutByAdmin("管理员根据主对象ID撤销检出版本对象"),
    batchUndoCheckoutByAdmin("管理员根据主对象ID批量撤销检出版本对象"),
    batchDeleteBranch("根据主对象ID&业务版本列表，批量删除指定分支版本下的所有小版本"),
    batchLogicalDeleteBranch("批量软删除对应分支版本下的所有小版本"),
    ;
    String desc;
}