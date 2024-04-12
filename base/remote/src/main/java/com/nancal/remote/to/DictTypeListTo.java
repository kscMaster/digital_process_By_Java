package com.nancal.remote.to;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/***
 * 获取字典列表请求实体
 *
 * @author 徐鹏军
 * @date 2022/4/8 17:02
 */
@Data
@AllArgsConstructor
public class DictTypeListTo implements Serializable {

    @ApiModelProperty("字典类型")
    private String dictTypeCode;

    /**
     * left，right
     * 不传全模糊
     */
    @ApiModelProperty("模糊查询类型")
    private String likeType;

    /**
     * 类型的集合
     */
    private Set<String> dictTypes;

    public DictTypeListTo(String dictTypeCode){
        this.dictTypeCode = dictTypeCode;
    }

    public DictTypeListTo(String dictTypeCode, String likeType) {
        this.dictTypeCode = dictTypeCode;
        this.likeType = likeType;
    }

    public DictTypeListTo(Set<String> dictTypes) {
        this.dictTypes = dictTypes;
    }
}
