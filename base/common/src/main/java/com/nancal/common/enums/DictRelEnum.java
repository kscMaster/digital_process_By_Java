package com.nancal.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 关系配置
 */
@Getter
@AllArgsConstructor
public enum DictRelEnum {

    PSEUDO_REL("_pseudo_rel","关系-伪文件夹"),
    PARENT_REL("_parent_rel","关系-左"),
    CHILD_REL("_child_rel","关系-右"),
    PROCESS_LIST("_processList","流程"),
    DELETE_CHECK_REL("_delete_check_rel","删除检查"),
    SYNC_DELETE_REL("_sync_delete_rel","同步删除"),
    TAB("_Tab","标签"),
    RELEASE_REL("_release_rel","送审");

    private String code;
    private String name;

}
