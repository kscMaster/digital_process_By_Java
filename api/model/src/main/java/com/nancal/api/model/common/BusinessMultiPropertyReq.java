package com.nancal.api.model.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/***
 * 业务对象多属性请求实体
 *
 * @author 徐鹏军
 * @date 2022/3/30 13:32
 */
@Data
public class BusinessMultiPropertyReq implements Serializable {

    @NotBlank(message = "业务对象唯一标志符不能为空")
    @ApiModelProperty(value = "业务对象唯一标志符", required = true)
    public String uid;

    @ApiModelProperty(value = "属性名称", required = true)
    @Valid
    @NotNull(message = "属性名称不能为空")
    public List<String> propertyNames = new ArrayList<String>();
}
