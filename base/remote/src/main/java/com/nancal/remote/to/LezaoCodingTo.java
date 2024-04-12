package com.nancal.remote.to;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/***
 * @Description: 规则编码入参
 * @Author: zuxh
 * @Date: 2022/3/28 11:33
 **/
@Data
@Builder
@ApiModel(value = "获取规则编码入参")
public class LezaoCodingTo {
    /** 规则编码 **/
    @NotBlank(message = "规则编码不能为空")
    private String ruleCoding;

    /** 参考值 **/
    private String referenceValue;

    /** 条数 **/
    private Integer counts;

    /**变量量集合 key必须为1-5数字 **/
    private Map<Integer,String> variableValueMap;
}
