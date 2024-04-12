package com.nancal.remote.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/***
 * 字典名字响应实体
 *
 * @author 徐鹏军
 * @date 2022/4/8 17:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictItemVo implements Serializable {

    @ApiModelProperty("字典项编码")
    private String code;

    @ApiModelProperty("字典项编码对应的值")
    private String value;

    @ApiModelProperty("字典类型")
    private String dictTypeCode;
}
