package com.nancal.api.model;

import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Gte4RunTimeChangeAttachResp extends RunTimeChangeAttachResp{

    @ApiModelProperty(value = "更改类型")
    @JsonDict(DictConstant.CHANGE_TYPE)
    private String gte4ChangeType;

    @ApiModelProperty(value = "更改内容")
    private String gte4ChangeReason;

    @ApiModelProperty("更改单关联的发放单位中最小的生效时间")
    private String effictivedDate;

}
