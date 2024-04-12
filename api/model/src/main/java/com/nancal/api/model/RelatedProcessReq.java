package com.nancal.api.model;


import com.nancal.api.model.common.ValidList;
import com.nancal.common.base.IdRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "关联工厂/线体 的请求")
public class RelatedProcessReq {
    @ApiModelProperty(value = "uid")
    @NotBlank(message = "uid不能为空")
    private String uid;
    @ApiModelProperty(value = "类型")
    @NotBlank(message = "类型不能为空")
    private String objectType;
    @ApiModelProperty(value = "关联的数据")
    private ValidList<IdRequest> factoryOrLine;

}
