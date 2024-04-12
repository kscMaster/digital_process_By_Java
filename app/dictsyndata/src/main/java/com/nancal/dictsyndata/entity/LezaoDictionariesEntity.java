package com.nancal.dictsyndata.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author hewei
 * @date 2022/7/12 14:49
 * @Description
 */
@Data
@Entity(name = "lezao_dictionaries_copy1")
public class LezaoDictionariesEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    private Long id;

    /**
     * 数据字典类型id
     */
    private Long dictTypeId;

    /**
     * 所属数据字典项目父id
     */
    private Long parentId;

    /**
     * 所属数组字典编码
     */
    private String dictTypeCode;

    /**
     * 数据字典项目名称
     */
    private String dictionaryName;

    /**
     * dictionary_code
     */
    private String dictionaryCode;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否是字典项默认值 （1(true)-是 0(false)-否(默认)
     */
    private Integer whetherDefaultValue;

    /**
     * 0 启用（默认）、1 禁用
     */
    private Integer status;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建者id
     */
    private String creationUserId;

    /**
     * 创建者名称
     */
    private String creationUsername;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新者id
     */
    private String lastUpdateUserId;

    /**
     * 更新者名称
     */
    private String lastUpdateUsername;

    /**
     * 是否删除 0未删除，1删除(默认0)
     */
    private Integer delFlag;

}
