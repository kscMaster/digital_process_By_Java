package com.nancal.dictsyndata.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author hewei
 * @date 2022/7/12 14:49
 * @Description
 */
@Data
@ToString
public class LezaoDictionariesResp implements Serializable {
    private static final long serialVersionUID = 1L;

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




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LezaoDictionariesResp that = (LezaoDictionariesResp) o;
        return dictTypeCode.equals(that.dictTypeCode) &&
                dictionaryCode.equals(that.dictionaryCode);
    }

    @Override
    public String toString() {
        return "LezaoDictionariesResp{" +
                "dictTypeCode='" + dictTypeCode + '\'' +
                ", dictionaryCode='" + dictionaryCode + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(dictTypeId, parentId, dictTypeCode, dictionaryName, dictionaryCode, sort, remark, whetherDefaultValue, status, tenantId);
    }
}
