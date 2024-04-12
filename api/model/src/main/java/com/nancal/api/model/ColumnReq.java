package com.nancal.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author hewei
 * @date 2022/7/21 15:04
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnReq {
    /**
     * 列字段
     */
    @NotBlank(message = "列code不能为空")
    private String code;

    /**
     * 列名称
     */
    @NotBlank(message = "列value不能为空")
    private String value;
}
