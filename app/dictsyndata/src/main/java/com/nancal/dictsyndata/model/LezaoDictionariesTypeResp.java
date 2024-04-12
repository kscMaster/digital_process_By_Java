package com.nancal.dictsyndata.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import java.util.List;
import java.util.Objects;

/**
 * @author hewei
 * @date 2022/7/12 17:33
 * @Description
 */
@Data
public class LezaoDictionariesTypeResp {

    private Long id;
    /**
     * 数据字典类型(1，列表 2，树)
     */
    private Integer dictType;

    /**
     * 数据字典名称
     */
    private String dictName;

    /**
     * 数据字典编码
     */
    private String dictCode;

    /**
     * 所属应用编码
     */
    private String appCode;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 子级列表
     */
    private List<LezaoDictionariesResp>  children;

    /**
     * 新增子级列表
     */
    private List<LezaoDictionariesResp> insertChildren;

    /**
     * 修改子级列表
     */
    private List<LezaoDictionariesResp>  deleteChildren;

    /**
     * 删除子级列表
     */
    private List<LezaoDictionariesResp>  updateChildren;




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LezaoDictionariesTypeResp that = (LezaoDictionariesTypeResp) o;
        return dictType.equals(that.dictType) &&
                dictCode.equals(that.dictCode) &&
                appCode.equals(that.appCode);
    }

    @Override
    public String toString() {
        return "LezaoDictionariesTypeResp{" +
                "dictType=" + dictType +
                ", dictCode='" + dictCode + '\'' +
                ", appCode='" + appCode + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(dictType, dictCode, appCode);
    }
}
