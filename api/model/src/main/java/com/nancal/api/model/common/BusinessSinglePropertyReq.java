package com.nancal.api.model.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/***
 * 业务对象单个属性请求实体
 *
 * @author 徐鹏军
 * @date 2022/3/30 13:32
 */
@Data
public class BusinessSinglePropertyReq implements Serializable {

    @NotBlank(message = "业务对象唯一标志符不能为空")
    @ApiModelProperty(value = "业务对象唯一标志符", required = true)
    public String uid;

    @NotBlank(message = "属性名称不能为空")
    @ApiModelProperty(value = "属性名称", required = true)
    public String propertyName;
}
